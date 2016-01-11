package cn.mutils.app.core.err;

/**
 * Http status exception of framework
 */
@SuppressWarnings("unused")
public class HttpStatusException extends CodeException {

    public HttpStatusException() {
        mCode = 200;
    }

    public HttpStatusException(int code) {
        super(code);
    }

    public HttpStatusException(int code, String message) {
        super(code, message);
    }

}
