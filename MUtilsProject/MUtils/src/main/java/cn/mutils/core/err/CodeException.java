package cn.mutils.core.err;

/**
 * Code exception of framework
 */
public class CodeException extends Exception {

    protected int mCode;

    public CodeException() {

    }

    public CodeException(int code) {
        mCode = code;
    }

    public CodeException(int code, String message) {
        super(message);
        mCode = code;
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        mCode = code;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(" ");
        sb.append(mCode);
        String msg = getMessage();
        if (msg != null) {
            sb.append("\n{\n");
            sb.append(msg);
            sb.append("\n}");
        }
        return sb.toString();
    }

}
