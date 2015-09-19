package cn.o.app.ui.web;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import cn.o.app.core.json.JsonUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class WebMessageManager implements IWebMessageManager {

	protected List<Class<? extends IWebMessageDispatcher<?>>> mDispatchers;

	protected IWebFrame mWebFrame;

	@Override
	public Context getContext() {
		return mWebFrame == null ? null : mWebFrame.getContext();
	}

	@Override
	public void add(Class<? extends IWebMessageDispatcher<?>> dispatcherClass) {
		if (mDispatchers == null) {
			mDispatchers = new ArrayList<Class<? extends IWebMessageDispatcher<?>>>();
		}
		mDispatchers.add(dispatcherClass);
	}

	@Override
	public void dispatchMessage(String message) {
		if (mDispatchers == null) {
			return;
		}
		Object json = null;
		try {
			json = JsonUtil.toJSON(message);
		} catch (Exception e) {
			return;
		}
		for (Class<? extends IWebMessageDispatcher<?>> dispatcherClass : mDispatchers) {
			IWebMessageDispatcher dispatcher = null;
			try {
				dispatcher = JsonUtil.convertFromJson(json, dispatcherClass);
				dispatcher.setManager(this);
				if (!dispatcher.preTranslateMessage()) {
					Object msg = dispatcher.translateMessage();
					if (msg != null) {
						dispatcher.onMessage(msg);
					}
				}
			} catch (Exception e) {

			} finally {
				if (dispatcher != null) {
					dispatcher.setManager(null);
				}
			}
		}
	}

	@Override
	public void onMessage(String message) {
		if (mWebFrame == null) {
			return;
		}
		mWebFrame.onMessage(message);
	}

	@Override
	public IWebFrame getWebFrame() {
		return mWebFrame;
	}

	@Override
	public void setWebFrame(IWebFrame webFrame) {
		mWebFrame = webFrame;
	}

	@Override
	public void clear() {
		mWebFrame = null;
		if (mDispatchers != null) {
			mDispatchers.clear();
			mDispatchers = null;
		}
	}

}
