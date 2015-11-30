package cn.mutils.app.ui.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import cn.mutils.app.core.log.Logs;
import cn.mutils.app.core.time.DateTime;

/**
 * WebFrame chrome client of framework
 */
public class WebFrameChromeClient extends WebChromeClient {

	public static final int REQUEST_CODE_FILE_CHOOSER_DEFAULT = 10000;

	protected StringBuilder mLogs;

	protected boolean mDebug;

	protected Context mContext;

	protected int mFileChooserRequestCode = REQUEST_CODE_FILE_CHOOSER_DEFAULT;

	protected ValueCallback<Uri> mUploadMessage;

	protected ValueCallback<Uri[]> mUploadMessages;

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context context) {
		mContext = context;
	}

	@Override
	public void onConsoleMessage(String message, int lineNumber, String sourceID) {
		if (!mDebug) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(new DateTime().toString());
		sb.append("][");
		sb.append(sourceID);
		sb.append("][L:");
		sb.append(lineNumber);
		sb.append("] ");
		sb.append(message);
		Logs.i("WebChromeClient", sb.toString());
		if (mLogs == null || mLogs.length() >= 4096) {
			mLogs = new StringBuilder();
		}
		if (mLogs.length() != 0) {
			mLogs.insert(0, "\n");
		}
		mLogs.insert(0, sb);
	}

	public int getFileChooserRequestCode() {
		return mFileChooserRequestCode;
	}

	public void setFileChooserRequestCode(int requestCode) {
		mFileChooserRequestCode = requestCode;
	}

	public ValueCallback<Uri> getUploadMessage() {
		return mUploadMessage;
	}

	public void setUploadMessage(ValueCallback<Uri> uploadMessage) {
		mUploadMessage = uploadMessage;
	}

	public ValueCallback<Uri[]> getUploadMessages() {
		return mUploadMessages;
	}

	public void setUploadMessages(ValueCallback<Uri[]> uploadMessages) {
		mUploadMessages = uploadMessages;
	}

	/**
	 * JS上传文件的<input type="file" name="fileField" id="fileField" />事件捕获<br>
	 * 5.0 + 调用这个方法
	 */
	@Override
	public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg,
			FileChooserParams fileChooserParams) {
		mUploadMessages = uploadMsg;
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		if (mContext instanceof Activity) {
			((Activity) mContext).startActivityForResult(Intent.createChooser(intent, "文件上传"), mFileChooserRequestCode);
		}
		return true;
	}

	/**
	 * JS上传文件的<input type="file" name="fileField" id="fileField" />事件捕获<br>
	 * 4.1.1 + 调用这个方法
	 * 
	 * @param uploadMsg
	 * @param acceptType
	 * @param capture
	 */
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
		mUploadMessage = uploadMsg;
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		if (mContext instanceof Activity) {
			((Activity) mContext).startActivityForResult(Intent.createChooser(intent, "文件上传"), mFileChooserRequestCode);
		}
	}

	/**
	 * JS上传文件的<input type="file" name="fileField" id="fileField" />事件捕获<br>
	 * 3.0 + 调用这个方法
	 * 
	 * @param uploadMsg
	 * @param acceptType
	 */
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
		mUploadMessage = uploadMsg;
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		if (mContext instanceof Activity) {
			((Activity) mContext).startActivityForResult(Intent.createChooser(intent, "文件上传"), mFileChooserRequestCode);
		}
	}

	/**
	 * JS上传文件的<input type="file" name="fileField" id="fileField" />事件捕获<br>
	 * Android < 3.0 调用这个方法
	 * 
	 * @param uploadMsg
	 */
	public void openFileChooser(ValueCallback<Uri> uploadMsg) {
		mUploadMessage = uploadMsg;
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		if (mContext instanceof Activity) {
			((Activity) mContext).startActivityForResult(Intent.createChooser(intent, "文件上传"), mFileChooserRequestCode);
		}
	}

	public String getLogs() {
		return mLogs == null ? "" : mLogs.toString();
	}

	public boolean isDebug() {
		return mDebug;
	}

	public void setDebug(boolean debug) {
		mDebug = debug;
	}

}
