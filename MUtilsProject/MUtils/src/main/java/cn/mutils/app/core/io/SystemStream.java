package cn.mutils.app.core.io;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * SystemStream to replace System.out and System.err stream
 */
@SuppressWarnings({"NullableProblems", "unused", "UnnecessaryEnumModifier"})
public class SystemStream extends PrintStream implements ISystemPrinter {

    public static enum STREAM_TYPE {
        OUT, ERR
    }

    protected STREAM_TYPE mStreamType = STREAM_TYPE.OUT;

    public SystemStream(OutputStream out) {
        super(out);
    }

    public SystemStream(OutputStream out, STREAM_TYPE type) {
        super(out);
        mStreamType = type;
    }

    @Override
    public void write(byte[] buffer, int offset, int length) {
        if (mStreamType == STREAM_TYPE.ERR) {
            systemErr(new String(buffer, offset, length));
        } else {
            systemOut(new String(buffer, offset, length));
        }
    }

    public void systemOut(String str) {

    }

    public void systemErr(String str) {

    }

}
