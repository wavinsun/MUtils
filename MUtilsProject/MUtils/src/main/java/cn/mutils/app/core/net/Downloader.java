package cn.mutils.app.core.net;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.mutils.app.core.err.CodeException;
import cn.mutils.app.core.err.ErrorCodeException;
import cn.mutils.app.core.err.HttpStatusException;
import cn.mutils.app.core.io.IOUtil;

/**
 * Simple synchronized HTTP download implementation
 */
public class Downloader {

    /**
     * Content type of image
     */
    public static final String CONTENT_TYPE_IMAGE = "image";
    /**
     * Content type of text
     */
    public static final String CONTENT_TYPE_TEXT = "text";

    /**
     * Response content type is not that expected
     */
    public static final int CODE_CONTENT_INVALID = 1000;
    /**
     * Response reading error
     */
    public static final int CODE_READ_ERROR = 1001;
    /**
     * Response size read is zero
     */
    public static final int CODE_EMPTY_RESPONSE = 1002;
    /**
     * Error occurred while saving response to file
     */
    public static final int CODE_SAVE_ERROR = 1003;

    /**
     * Tag for download state
     */
    public static final Object DOWNLOD_SUCCESS = new Object();

    /**
     * Download URL
     */
    protected String mUrl;

    /**
     * Download file path
     */
    protected String mFileName;

    /**
     * Download content type for HTTP
     */
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
                throw new HttpStatusException(code);
            }
            if (mContentType != null) {
                if (!http.getContentType().contains(mContentType)) {
                    throw new ErrorCodeException(CODE_CONTENT_INVALID);
                }
            }
            is = http.getInputStream();
            byte[] bytes = IOUtil.getBytes(is);
            if (bytes == null) {
                throw new CodeException(CODE_READ_ERROR);
            }
            if (bytes.length == 0) {
                throw new ErrorCodeException(CODE_EMPTY_RESPONSE);
            }
            File file = new File(mFileName);
            if (!IOUtil.equals(file, bytes)) {
                if (!IOUtil.putBytes(file, bytes)) {
                    throw new ErrorCodeException(CODE_SAVE_ERROR);
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
