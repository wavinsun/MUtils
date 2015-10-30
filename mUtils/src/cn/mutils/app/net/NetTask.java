package cn.mutils.app.net;

import java.lang.reflect.Type;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import cn.mutils.app.AppUtil;
import cn.mutils.app.BuildConfig;
import cn.mutils.app.core.log.Logs;
import cn.mutils.app.core.net.NetClient;
import cn.mutils.app.core.net.NetClient.ConnectNotFoundException;
import cn.mutils.app.core.net.NetClient.NetClientListener;
import cn.mutils.app.core.reflect.ReflectUtil;
import cn.mutils.app.queue.QueueItem;

/**
 * Template of request and response for net API at Android application level
 * 
 * @see NetClient
 */
@SuppressWarnings("unchecked")
public class NetTask<REQUEST, RESPONSE> extends QueueItem<INetTask<REQUEST, RESPONSE>>
		implements INetTask<REQUEST, RESPONSE> {

	protected NetAsyncTask mTask;

	protected NetClient<REQUEST, RESPONSE> mClient = new NetClient<REQUEST, RESPONSE>();

	public NetTask() {
		mClient.setListener(new NetClientListener<REQUEST, RESPONSE>() {

			@Override
			public Class<?> requestRawType() {
				return NetTask.this.requestRawType();
			}

			@Override
			public Type requestGenericType() {
				return NetTask.this.requestGenericType();
			}

			@Override
			public Class<?> responseRawType() {
				return NetTask.this.responseRawType();
			}

			@Override
			public Type responseGenericType() {
				return NetTask.this.responseGenericType();
			}

			@Override
			public String requestCookie(URL url) {
				return NetCookieCache.getCookie(mContext, url);
			}

			@Override
			public void responseCookie(URL url, String cookie) {
				NetCookieCache.setCookie(mContext, url, cookie);
			}

			@Override
			public void debugging(String event, String message) {
				NetTask.this.debugging(event, message);
			}

			@Override
			public REQUEST convertToRequest() {
				return NetTask.this.convertToRequest();
			}

			@Override
			public Object convertFromResponse(RESPONSE response) {
				return NetTask.this.convertFromResponse(response);
			}

			@Override
			public Object signPostJson(REQUEST request) throws Exception {
				return NetTask.this.signPostJson(request);
			}

			@Override
			public void errorCodeVerify(RESPONSE response) throws Exception {
				super.errorCodeVerify(response);
				NetTask.this.errorCodeVerify(response);
			}

		});
	}

	protected Class<?> requestRawType() {
		return ReflectUtil.getParameterizedRawType(this.getClass(), 0);
	}

	protected Type requestGenericType() {
		return null;
	}

	protected Class<?> responseRawType() {
		return ReflectUtil.getParameterizedRawType(this.getClass(), 1);
	}

	protected Type responseGenericType() {
		return null;
	}

	public void addListener(NetTaskListener<REQUEST, RESPONSE> listener) {
		super.addListener(listener);
	}

	@Override
	public long getResponseTime() {
		return mClient.getResponseTime();
	}

	public boolean isSplitArrayParams() {
		return mClient.isSplitArrayParams();
	}

	public void setSplitArrayParams(boolean splitArrayParams) {
		if (mStarted || mStoped) {
			return;
		}
		mClient.setSplitArrayParams(splitArrayParams);
	}

	public boolean isRestUrl() {
		return mClient.isRestUrl();
	}

	public void setRestUrl(boolean restUrl) {
		if (mStarted || mStoped) {
			return;
		}
		mClient.setRestUrl(restUrl);
	}

	public boolean isPostParams() {
		return mClient.isPostParams();
	}

	public void setPostParams(boolean postParams) {
		if (mStarted || mStoped) {
			return;
		}
		mClient.setPostParams(postParams);
	}

	public boolean isPostJson() {
		return mClient.isPostJson();
	}

	public void setPostJson(boolean postJson) {
		if (mStarted || mStoped) {
			return;
		}
		mClient.setPostJson(postJson);
	}

	public void setPostJsonSigned(boolean postJsonSigned) {
		if (mStarted || mStoped) {
			return;
		}
		mClient.setPostJsonSigned(postJsonSigned);
	}

	public boolean isRequestConvertible() {
		return mClient.isRequestConvertible();
	}

	public void setRequestConvertible(boolean requestConvertible) {
		if (mStarted || mStoped) {
			return;
		}
		mClient.setRequestConvertible(requestConvertible);
	}

	public boolean isResponseConvertible() {
		return mClient.isResponseConvertible();
	}

	public void setResponseConvertible(boolean responseConvertible) {
		if (mStarted || mStoped) {
			return;
		}
		mClient.setResponseConvertible(responseConvertible);
	}

	public Object getResponseConverted() {
		return mClient.getResponseConverted();
	}

	@Override
	public String getUrl() {
		return mClient.getUrl();
	}

	@Override
	public void setUrl(String url) {
		if (this.mStarted || this.mStoped) {
			return;
		}
		mClient.setUrl(url);
	}

	@Override
	public String getRequestMethod() {
		return mClient.getRequestMethod();
	}

	@Override
	public void setRequestMethod(String requestMethod) {
		if (this.mStarted || this.mStoped) {
			return;
		}
		mClient.setRequestMethod(requestMethod);
	}

	@Override
	public REQUEST getRequest() {
		return mClient.getRequest();
	}

	@Override
	public void setRequest(REQUEST request) {
		if (this.mStarted || this.mStoped) {
			return;
		}
		mClient.setRequest(request);
	}

	@Override
	public RESPONSE getResponse() {
		return mClient.getResponse();
	}

	@Override
	public void setResponse(RESPONSE response) {
		if (this.mStoped) {
			return;
		}
		mClient.setResponse(response);
	}

	public void setCookieId(String cookieId) {
		if (this.mStarted || this.mStoped) {
			return;
		}
		mClient.setCookieCachedId(cookieId);
	}

	public void setCookieWithRequest(boolean cookieWithRequest) {
		if (this.mStarted || this.mStoped) {
			return;
		}
		mClient.setCookieWithRequest(cookieWithRequest);
	}

	public void setCookieWithResponse(boolean cookieWithResponse) {
		if (this.mStarted || this.mStoped) {
			return;
		}
		mClient.setCookieWithResponse(cookieWithResponse);
	}

	protected REQUEST convertToRequest() {
		return null;
	}

	protected Object convertFromResponse(RESPONSE response) {
		return null;
	}

	protected void debugging(String event, String message) {
		if (!BuildConfig.DEBUG) {
			return;
		}
		if (NetClient.EVENT_EXCEPTION.equals(event)) {
			Logs.e(this.getClass().getSimpleName(), NetClient.getLog(event, message).toString());
		} else {
			Logs.i(this.getClass().getSimpleName(), NetClient.getLog(event, message).toString());
		}
	}

	protected Object signPostJson(REQUEST request) throws Exception {
		return request;
	}

	protected void errorCodeVerify(RESPONSE response) throws Exception {

	}

	@Override
	public boolean start() {
		boolean result = super.start();
		if (result) {
			this.mTask = new NetAsyncTask();
			this.mTask.execute();
		}
		return result;
	}

	@Override
	public boolean stop() {
		boolean result = super.stop();
		if (result) {
			if (mClient.getResponse() == null) {
				if (this.mTask != null) {
					this.mTask.cancel(true);
				}
			}
		}
		mContext = null;
		mClient.setRequest(null);
		mClient.setResponse(null);
		mClient.setResponseConverted(null);
		mTask = null;
		return result;
	}

	class NetAsyncTask extends AsyncTask<String, Integer, Object> {

		protected Object doInBackground(String... paramters) {
			try {
				ConnectivityManager connMgr = (ConnectivityManager) mContext
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
				if (networkInfo == null || !networkInfo.isConnected()) {
					return new ConnectNotFoundException();
				}
				return mClient.execute();
			} catch (Exception e) {
				if (BuildConfig.DEBUG) {
					debugging(NetClient.EVENT_EXCEPTION, AppUtil.printStackTrace(e));
				}
				return e;
			}
		}

		protected void onPostExecute(Object object) {
			for (INetTaskListener<REQUEST, RESPONSE> listener : getListeners(INetTaskListener.class)) {
				if (object instanceof Exception) {
					listener.onException(NetTask.this, (Exception) object);
				} else {
					listener.onComplete(NetTask.this, NetTask.this.getResponse());
				}
			}
			stop();
		}
	}

}
