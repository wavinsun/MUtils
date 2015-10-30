package cn.mutils.app.core.io;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * IO of framework
 */
public class IOUtil {

	public static void copy(InputStream in, OutputStream out) throws Exception {
		byte[] buffer = new byte[4096];// 4K
		int bufferIndex = -1;
		while ((bufferIndex = in.read(buffer)) != -1) {
			out.write(buffer, 0, bufferIndex);
		}
	}

	public static byte[] getBytes(InputStream in) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			copy(in, bos);
			return bos.toByteArray();
		} catch (Exception e) {
			return null;
		} finally {
			close(bos);
		}
	}

	public static byte[] getBytes(File f) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			return getBytes(fis);
		} catch (Exception e) {
			return null;
		} finally {
			close(fis);
		}
	}

	public static boolean putBytes(File f, byte[] bytes) {
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(f, "rw");
			raf.setLength(bytes.length);
			raf.write(bytes);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			IOUtil.close(raf);
		}
	}

	public static boolean equals(File f, byte[] bytes) {
		if (bytes == null || !f.isFile() || bytes.length != f.length()) {
			return false;
		}
		return Arrays.equals(getBytes(f), bytes);
	}

	public static void close(Closeable c) {
		if (c == null) {
			return;
		}
		try {
			c.close();
		} catch (Exception e) {

		}
	}

}
