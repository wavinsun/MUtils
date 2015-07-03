package cn.o.app.socket;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import cn.o.app.json.JsonUtil;
import cn.o.app.queue.IQueueItem;
import cn.o.app.queue.Queue;
import cn.o.app.runtime.ReflectUtil;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketConnectionHandler;
import de.tavendo.autobahn.WebSocketOptions;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SocketQueue extends Queue implements ISocketQueue {

	public static final String SOCKET_EVENT = "event";

	public static final String SOCKET_CHANNEL = "channel";

	public static final String SOCKET_CHANNEL_MESSAGE = "data";

	public static final String SOCKET_ADD_CHANNEL = "addChannel";

	public static final String SOCKET_REMOVE_CHANNEL = "removeChannel";

	protected Map<String, SocketMapItem> mScoketMap;

	public SocketQueue() {
		super();
		mMaxRunningCount = Integer.MAX_VALUE;
		mScoketMap = new HashMap<String, SocketMapItem>();
	}

	@Override
	protected void onItemStart(IQueueItem item) {
		if (!(item instanceof ISocketTask)) {
			return;
		}
		ISocketTask<?, ?> task = (ISocketTask<?, ?>) item;
		String url = task.getUrl();
		if (url == null) {
			return;
		}
		SocketMapItem mapItem = mScoketMap.get(url);
		if (mapItem == null) {
			mapItem = new SocketMapItem(url);
			mScoketMap.put(url, mapItem);
		}
		mapItem.onStart(task);
	}

	@Override
	protected void onItemStop(IQueueItem item) {
		if (!(item instanceof ISocketTask)) {
			return;
		}
		ISocketTask<?, ?> task = (ISocketTask<?, ?>) item;
		String url = task.getUrl();
		if (url == null) {
			return;
		}
		SocketMapItem mapItem = mScoketMap.get(url);
		if (mapItem == null) {
			return;
		}
		mapItem.onStop(task);
	}

	protected class SocketMapItem {

		protected String mUrl;
		protected WebSocketConnection mConn;
		protected WebSocketConnectionHandler mHandler;
		protected WebSocketOptions mConnOptions;
		protected Map<String, List<ISocketTask<?, ?>>> mChannelsMap;

		public SocketMapItem(String url) {
			this.mUrl = url;
			this.mChannelsMap = new HashMap<String, List<ISocketTask<?, ?>>>();
			this.mHandler = new WebSocketConnectionHandler() {

				@Override
				public void onTextMessage(String payload) {
					onMessage(payload);
				}

				@Override
				public void onOpen() {
					sendMessage(getAddAllChannelMessage());
				}

			};
			this.mConnOptions = new WebSocketOptions();
			this.mConnOptions.setSocketReceiveTimeout(20000);
			this.mConnOptions.setSocketConnectTimeout(10000);
			this.mConnOptions.setReconnectInterval(2000);
			this.mConn = new WebSocketConnection();
			try {
				this.mConn.connect(new URI(url), this.mHandler, this.mConnOptions);
			} catch (Exception e) {

			}
		}

		protected void onMessage(String message) {
			try {
				JSONTokener jsonTokener = new JSONTokener(message);
				Object v = jsonTokener.nextValue();
				if (v instanceof JSONObject) {
					JSONObject jsonObject = (JSONObject) v;
					onMessage(jsonObject.getString(SOCKET_CHANNEL), jsonObject.getString(SOCKET_CHANNEL_MESSAGE));
				} else if (v instanceof JSONArray) {
					JSONArray jsonArray = (JSONArray) v;
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						onMessage(jsonObject.getString(SOCKET_CHANNEL), jsonObject.getString(SOCKET_CHANNEL_MESSAGE));
					}
				}
			} catch (Exception e) {

			}

		}

		protected void onMessage(String channel, String message) {
			List<ISocketTask<?, ?>> channelsItem = mChannelsMap.get(channel);
			if (channelsItem == null) {
				return;
			}
			for (ISocketTask task : channelsItem) {
				Class<?> responseClass = ReflectUtil.getParameterizedClass(task.getClass(), 1);
				try {
					task.setResponse(JsonUtil.convert(message, responseClass));
				} catch (Exception e) {

				}

			}
		}

		public void sendMessage(String message) {
			if (message == null) {
				return;
			}
			if (message.isEmpty()) {
				return;
			}
			if (mConn.isConnected()) {
				mConn.sendTextMessage(message);
			}
		}

		public void onStart(ISocketTask<?, ?> task) {
			String channel = task.getChannel();
			if (channel == null) {
				return;
			}
			List<ISocketTask<?, ?>> channelsItem = mChannelsMap.get(channel);
			if (channelsItem == null) {
				channelsItem = new LinkedList<ISocketTask<?, ?>>();
				mChannelsMap.put(channel, channelsItem);
			}
			if (channelsItem.size() == 0) {
				sendMessage(getAddChannelMessage(task));
			}
			if (!channelsItem.contains(task)) {
				channelsItem.add(task);
			}
		}

		public void onStop(ISocketTask<?, ?> task) {
			String channel = task.getChannel();
			if (channel == null) {
				return;
			}
			List<ISocketTask<?, ?>> channelsItem = mChannelsMap.get(channel);
			if (channelsItem == null) {
				return;
			}
			channelsItem.remove(task);
			if (channelsItem.size() == 0) {
				sendMessage(getRemoveChannelMessage(task));
			}
		}

		public String getAddChannelMessage(ISocketTask<?, ?> task) {
			try {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(SOCKET_EVENT, SOCKET_ADD_CHANNEL);
				jsonObject.put(SOCKET_CHANNEL, task.getChannel());
				return jsonObject.toString();
			} catch (Exception e) {
				return null;
			}
		}

		public String getRemoveChannelMessage(ISocketTask<?, ?> task) {
			try {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(SOCKET_EVENT, SOCKET_REMOVE_CHANNEL);
				jsonObject.put(SOCKET_CHANNEL, task.getChannel());
				return jsonObject.toString();
			} catch (Exception e) {
				return null;
			}
		}

		protected String getAddAllChannelMessage() {
			try {
				JSONArray jsonArray = new JSONArray();
				for (Entry<String, List<ISocketTask<?, ?>>> entry : mChannelsMap.entrySet()) {
					List<ISocketTask<?, ?>> value = entry.getValue();
					if (value.size() == 0) {
						continue;
					}
					jsonArray.put(jsonArray.length(), getAddChannelMessage(value.get(0)));
				}
				if (jsonArray.length() == 0) {
					return null;
				}
				if (jsonArray.length() == 1) {
					return jsonArray.getString(0);
				}
				return jsonArray.toString();
			} catch (Exception e) {
				return null;
			}
		}
	}

}
