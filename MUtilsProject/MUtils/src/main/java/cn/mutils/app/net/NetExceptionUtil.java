package cn.mutils.app.net;

import org.apache.http.NoHttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import cn.mutils.app.R;
import cn.mutils.app.ui.core.IToastOwner;
import cn.mutils.core.err.CookieExpiredException;
import cn.mutils.core.err.ErrorCodeException;
import cn.mutils.core.err.HttpStatusException;
import cn.mutils.core.err.NoConnectionException;

/**
 * Net exception handler
 */
@SuppressWarnings("deprecation")
public class NetExceptionUtil {

    public static Exception handle(Exception e, IToastOwner handler) {
        if (e == null) {
            return null;
        }
        if (handler == null) {
            return e;
        }
        if (e instanceof CookieExpiredException) {
            return e;
        }
        int resId = resOf(e);
        if (resId == 0) {
            return e;
        }
        if (e instanceof HttpStatusException) {
            handler.toast(resId, ((HttpStatusException) e).getCode());
        } else {
            handler.toast(resId);
        }
        return null;
    }

    protected static int resOf(Exception e) {
        if (e instanceof NoConnectionException || e instanceof ConnectException) {
            return R.string.exception_no_connection;
        }
        if (e instanceof ConnectTimeoutException || e instanceof SocketException
                || e instanceof SocketTimeoutException) {
            return R.string.exception_timeout;
        }
        if (e instanceof NoHttpResponseException || e instanceof FileNotFoundException || e instanceof EOFException
                || e instanceof UnknownHostException || e instanceof SSLException) {
            return R.string.exception_no_response;
        }
        if (e instanceof HttpStatusException) {
            return R.string.exception_http_status;
        }
        if (e instanceof ErrorCodeException) {
            try {
                String name = "exception_" + ((ErrorCodeException) e).getCode();
                return R.string.class.getField(name).getInt(null);
            } catch (Exception ex) {
                return 0;
            }
        }
        return 0;
    }
}
