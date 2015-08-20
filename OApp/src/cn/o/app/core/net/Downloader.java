package cn.o.app.core.net;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.o.app.core.io.IOUtil;

public class Downloader {

	public static final String CONTENT_TYPE_IMAGE = "image";
	public static final String CONTENT_TYPE_TEXT = "text";

	public static final int CODE_CONTENT_INVALID = 1000;
	public static final int CODE_READ_ERROR = 1001;
	public static final int CODE_EMPTY_RESPONSE = 1002;
	public static final int CODE_SAVE_ERROR = 1003;

	public static final Object DOWNLOD_SUCCESS = new Object();

	protected String mUrl;

	protected String mFileName;

	protected String mContentType;

	public Downloader(String url, String fileName) {
		mUrl = url;
		mFileName = fileName;
	}

	public Downloader(String url, String fileName, String contentType) {
		mUrl = url;
		mFileName = fileName;
		mContentType = contentType;
	}

	public Object download() {
		HttpURLConnection http = null;
		InputStream is = null;
		try {
			http = (HttpURLConnection) new URL(mUrl).openConnection();
			http.setConnectTimeout(5000);
			http.setReadTimeout(5000);
			http.setRequestProperty("User-Agent", "");
			http.setRequestMethod("GET");
			http.connect();
			int code = http.getResponseCode();
			if (code != 200 && code != 301 && code != 302) {
				throw new NetClient.HttpStatusException(code);
			}
			if (mContentType != null) {
				if (!http.getContentType().contains(mContentType)) {
					throw new NetClient.ErrorCodeException(CODE_CONTENT_INVALID);
				}
			}
			is = http.getInputStream();
			byte[] bytes = IOUtil.getBytes(is);
			if (bytes == null) {
				throw new NetClient.CodeException(CODE_READ_ERROR);
			}
			if (bytes.length == 0) {
				throw new NetClient.ErrorCodeException(CODE_EMPTY_RESPONSE);
			}
			File file = new File(mFileName);
			if (!IOUtil.equals(file, bytes)) {
				if (!IOUtil.putBytes(file, bytes)) {
					throw new NetClient.ErrorCodeException(CODE_SAVE_ERROR);
				}
			}
			return DOWNLOD_SUCCESS;
		} catch (Exception e) {
			return e;
		} finally {
			IOUtil.close(is);
			try {
				if (http != null) {
					http.disconnect();
				}
			} catch (Exception e) {

			}
		}
	}

}
