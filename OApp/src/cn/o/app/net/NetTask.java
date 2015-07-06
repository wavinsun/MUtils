package cn.o.app.net;

import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import cn.o.app.BuildConfig;
import cn.o.app.LogCat;
import cn.o.app.OUtil;
import cn.o.app.net.NetClient.ConnectNotFoundException;
import cn.o.app.net.NetClient.NetClientListener;
import cn.o.app.queue.QueueItem;
import cn.o.app.runtime.ReflectUtil;

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
			public Class<REQUEST> requestClass() {
				return (Class<REQUEST>) ReflectUtil.getParameterizedClass(NetTask.this.getClass(), 0);
			}

			@Override
			public Class<RESPONSE> responseClass() {
				return (Class<RESPONSE>) ReflectUtil.getParameterizedClass(NetTask.this.getClass(), 1);
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

	public void addListener(NetTaskListener<REQUEST, RESPONSE> listener) {
		super.addListener(listener);
	}

	@Override
	public long getResponseTime() {
		return mClient.mResponseTime;
	}

	public boolean isSplitArrayParams() {
		return mClient.mSplitArrayParams;
	}

	public void setSplitArrayParams(boolean splitArrayParams) {
		if (mStarted || mStoped) {
			return;
		}
		mClient.mSplitArrayParams = splitArrayParams;
	}

	public boolean isRestUrl() {
		return mClient.mRestUrl;
	}

	public void setRestUrl(boolean restUrl) {
		if (mStarted || mStoped) {
			return;
		}
		mClient.mRestUrl = restUrl;
	}

	public boolean isPostParams() {
		return mClient.mPostParams;
	}

	public void setPostParams(boolean postParams) {
		if (mStarted || mStoped) {
			return;
		}
		mClient.mPostParams = postParams;
	}

	public boolean isPostJson() {
		return mClient.mPostJson;
	}

	public void setPostJson(boolean postJson) {
		if (mStarted || mStoped) {
			return;
		}
		mClient.mPostJson = postJson;
	}

	public void setPostJsonSigned(boolean postJsonSigned) {
		if (mStarted || mStoped) {
			return;
		}
		mClient.mPostJsonSigned = postJsonSigned;
	}

	public boolean isRequestConvertible() {
		return mClient.mRequestConvertible;
	}

	public void setRequestConvertible(boolean requestConvertible) {
		if (mStarted || mStoped) {
			return;
		}
		mClient.mRequestConvertible = requestConvertible;
	}

	public boolean isResponseConvertible() {
		return mClient.mResponseConvertible;
	}

	public void setResponseConvertible(boolean responseConvertible) {
		if (mStarted || mStoped) {
			return;
		}
		mClient.mResponseConvertible = responseConvertible;
	}

	public Object getResponseConverted() {
		return mClient.mResponseConverted;
	}

	@Override
	public String getUrl() {
		return mClient.mUrl;
	}

	@Override
	public void setUrl(String url) {
		if (this.mStarted || this.mStoped) {
			return;
		}
		mClient.mUrl = url;
	}

	@Override
	public String getRequestMethod() {
		return mClient.mRequestMethod;
	}

	@Override
	public void setRequestMethod(String requestMethod) {
		if (this.mStarted || this.mStoped) {
			return;
		}
		mClient.mRequestMethod = requestMethod;
	}

	@Override
	public REQUEST getRequest() {
		return mClient.mRequest;
	}

	@Override
	public void setRequest(REQUEST request) {
		if (this.mStarted || this.mStoped) {
			return;
		}
		mClient.mRequest = request;
	}

	@Override
	public RESPONSE getResponse() {
		return mClient.mResponse;
	}

	@Override
	public void setResponse(RESPONSE response) {
		if (this.mStoped) {
			return;
		}
		mClient.mResponse = response;
	}

	public void setCookieId(String cookieId) {
		if (this.mStarted || this.mStoped) {
			return;
		}
		mClient.mCookieCacheId = cookieId;
	}

	public void setCookieWithRequest(boolean cookieWithRequest) {
		if (this.mStarted || this.mStoped) {
			return;
		}
		mClient.mCookieWithRequest = cookieWithRequest;
	}

	public void setCookieWithResponse(boolean cookieWithResponse) {
		if (this.mStarted || this.mStoped) {
			return;
		}
		mClient.mCookieWithResponse = cookieWithResponse;
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
			LogCat.e(this.getClass().getSimpleName(), NetClient.getLog(event, message).toString());
		} else {
			LogCat.i(this.getClass().getSimpleName(), NetClient.getLog(event, message).toString());
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
			if (mClient.mResponse == null) {
				if (this.mTask != null) {
					this.mTask.cancel(true);
				}
			}
		}
		mContext = null;
		mClient.mRequest = null;
		mClient.mResponse = null;
		mClient.mResponseConverted = null;
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
					debugging(NetClient.EVENT_EXCEPTION, OUtil.printStackTrace(e));
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
