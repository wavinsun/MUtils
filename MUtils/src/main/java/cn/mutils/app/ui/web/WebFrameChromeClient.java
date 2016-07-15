package cn.mutils.app.ui.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

import cn.mutils.core.log.Logs;
import cn.mutils.core.text.StringUtil;
import cn.mutils.core.time.DateTime;

/**
 * WebFrame chrome client of framework
 */
@SuppressLint("NewApi")
@SuppressWarnings({"serial", "unused", "deprecation"})
@Keep
@KeepClassMembers
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
     * Support <input type="file" accept="image/*"/><br>
     * 5.0 +
     */
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg,
                                     FileChooserParams fileChooserParams) {
        mUploadMessages = uploadMsg;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] acceptTypes = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            acceptTypes = fileChooserParams.getAcceptTypes();
        }
        if (acceptTypes == null || acceptTypes.length == 0) {
            intent.setType("*/*");
        } else {
            String acceptType = acceptTypes[0];
            if (StringUtil.isEmpty(acceptType)) {
                intent.setType("*/*");
            } else {
                intent.setType(acceptType);
            }
        }
        if (mContext instanceof Activity) {
            ((Activity) mContext).startActivityForResult(Intent.createChooser(intent, "文件上传"), mFileChooserRequestCode);
        }
        return true;
    }

    /**
     * Support <input type="file" accept="image/*"/><br>
     * 4.1.1 +
     */
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        mUploadMessage = uploadMsg;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (StringUtil.isEmpty(acceptType)) {
            intent.setType("*/*");
        } else {
            intent.setType(acceptType);
        }
        if (mContext instanceof Activity) {
            ((Activity) mContext).startActivityForResult(Intent.createChooser(intent, "文件上传"), mFileChooserRequestCode);
        }
    }

    /**
     * Support <input type="file" accept="image/*"/><br>
     * 3.0 +
     */
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        mUploadMessage = uploadMsg;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (StringUtil.isEmpty(acceptType)) {
            intent.setType("*/*");
        } else {
            intent.setType(acceptType);
        }
        if (mContext instanceof Activity) {
            ((Activity) mContext).startActivityForResult(Intent.createChooser(intent, "文件上传"), mFileChooserRequestCode);
        }
    }

    /**
     * Support <input type="file" accept="image/*"/><br>
     * 3.0 -
     */
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
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
