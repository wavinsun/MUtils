package cn.o.app.core.io;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * SystemStream to replace System.out and System.err stream
 */
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
			syserr(new String(buffer, offset, length));
		} else {
			sysout(new String(buffer, offset, length));
		}
	}

	public void sysout(String str) {

	}

	public void syserr(String str) {

	}

}
