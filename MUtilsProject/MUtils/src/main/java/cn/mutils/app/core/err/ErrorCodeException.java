package cn.mutils.app.core.err;

@SuppressWarnings("unused")
public class ErrorCodeException extends CodeException {

    public ErrorCodeException() {

    }

    public ErrorCodeException(int code) {
        super(code);
    }

    public ErrorCodeException(int code, String message) {
        super(code, message);
    }

}
