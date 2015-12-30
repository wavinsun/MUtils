package cn.mutils.app.core.err;

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
