package cn.o.app.ui.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import cn.o.app.ui.StateView;

/**
 * Implements {@link IWebFrame}
 */
@SuppressLint("SetJavaScriptEnabled")
@SuppressWarnings("serial")
public class WebFrame extends StateView implements IWebFrame {

	protected WebView mWebView;

	protected IWebJSInterface mWebJSInterface;

	protected IWebMessageManager mWebMessageManager;

	protected Handler mHandler = new Handler();

	public WebFrame(Context context) {
		super(context);
		init(context, null);
	}

	public WebFrame(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public WebFrame(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	protected void init(Context context, AttributeSet attrs) {
		mWebView = new WebView(context);
		mWebView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT));
		mWebView.setWebViewClient(new WebFrameClient());
		mWebView.setWebChromeClient(new WebFrameChromeClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		this.addView(mWebView);
		this.setWebJSInterface(new WebJSInterface());
		this.setWebMessageManager(new WebMessageManager());
	}

	@Override
	public void onDestroy() {
		mHandler.removeCallbacksAndMessages(null);
		if (mWebMessageManager != null) {
			mWebMessageManager.clear();
		}
		super.onDestroy();
	}

	public void add(Class<? extends IWebMessageDispatcher<?>> dispatcherClass) {
		if (mWebMessageManager != null) {
			mWebMessageManager.add(dispatcherClass);
		}
	}

	@Override
	public void sendMessage(String json) {
		mHandler.post(new DispatchMessageRunnable(json));
	}

	@Override
	public void postMessage(String json) {
		mHandler.post(new DispatchMessageRunnable(json));
	}

	@Override
	public void onMessage(String json) {
		if (mWebJSInterface == null) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("javascript:");
		sb.append(mWebJSInterface.name());
		sb.append(".");
		sb.append(mWebJSInterface.onMessage());
		sb.append("('");
		sb.append(json);
		sb.append("');");
		mHandler.post(new LoadUrlRunnable(sb.toString()));
	}

	public WebView getWebView() {
		return mWebView;
	}

	@Override
	public IWebMessageManager getWebMessageManager() {
		return mWebMessageManager;
	}

	@Override
	public void setWebMessageManager(IWebMessageManager webMessageManager) {
		if (mWebMessageManager != null) {
			mWebMessageManager.clear();
		}
		mWebMessageManager = webMessageManager;
		if (mWebMessageManager == null) {
			return;
		}
		mWebMessageManager.setWebFrame(this);
	}

	@Override
	public IWebJSInterface getWebJSInterface(IWebJSInterface webJSInterface) {
		return mWebJSInterface;
	}

	@Override
	public void setWebJSInterface(IWebJSInterface webJSInterface) {
		if (mWebJSInterface != null) {
			mWebJSInterface.setWebFrame(null);
		}
		mWebJSInterface = webJSInterface;
		if (mWebJSInterface == null) {
			return;
		}
		mWebJSInterface.setWebFrame(this);
		mWebView.addJavascriptInterface(mWebJSInterface, mWebJSInterface.name());
	}

	class WebJSInterface implements IWebJSInterface {

		@Override
		public String name() {
			return "app";
		}

		@Override
		@JavascriptInterface
		public void sendMessage(String json) {
			WebFrame.this.sendMessage(json);
		}

		@Override
		@JavascriptInterface
		public void postMessage(String json) {
			WebFrame.this.postMessage(json);
		}

		@Override
		public String onMessage() {
			return "onMessage";
		}

		@Override
		public IWebFrame getWebFrame() {
			return WebFrame.this;
		}

		@Override
		public void setWebFrame(IWebFrame webFrame) {

		}

	}

	class LoadUrlRunnable implements Runnable {

		protected String mUrl;

		public LoadUrlRunnable(String url) {
			mUrl = url;
		}

		@Override
		public void run() {
			try {
				mWebView.loadUrl(mUrl);
			} catch (Exception e) {

			}
		}

	}

	class DispatchMessageRunnable implements Runnable {

		protected String mMesssage;

		public DispatchMessageRunnable(String message) {
			mMesssage = message;
		}

		@Override
		public void run() {
			if (mWebMessageManager != null) {
				mWebMessageManager.dispatchMessage(mMesssage);
			}
		}

	}

}
