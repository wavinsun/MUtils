package cn.o.app.text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.UUID;

import cn.o.app.io.IOUtil;

public class StringUtil {

	protected static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	public static byte[] md5(byte[] bytes) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(bytes);
			return md5.digest();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * MD5 of string
	 * 
	 * @param text
	 * @return Small letter of MD5
	 */
	public static String md5(String text) {
		try {
			return toHex(md5(text.getBytes("UTF-8")));
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * MD5 of string
	 * 
	 * @param text
	 * @return Capital letter of MD5
	 */
	public static String MD5(String text) {
		return md5(text).toUpperCase(Locale.getDefault());
	}

	public static String md5(File file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int bufferIndex = -1;
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			while ((bufferIndex = fis.read(buffer)) != -1) {
				md5.update(buffer, 0, bufferIndex);
			}
			return toHex(md5.digest());
		} catch (Exception e) {
			return "";
		} finally {
			IOUtil.close(fis);
		}
	}

	public static String MD5(File file) {
		return md5(file).toUpperCase(Locale.getDefault());
	}

	/**
	 * Convert byte data to hex string
	 * 
	 * @param data
	 * @return
	 */
	public static String toHex(byte[] data) {
		char[] str = new char[data.length + data.length];
		for (int i = data.length - 1, strIndex = str.length - 1; i >= 0; i--) {
			byte byte4i = data[i];
			str[strIndex--] = hexDigits[byte4i & 0xF];
			str[strIndex--] = hexDigits[byte4i >>> 4 & 0xF];
		}
		return new String(str);
	}

	/**
	 * Convert hex string to byte data
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] toBytes(String hex) {
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0, size = bytes.length; i < size; i++) {
			int index = 2 * i;
			bytes[i] = Integer.valueOf(hex.substring(index, index + 2), 16).byteValue();
		}
		return bytes;
	}

	public static String get(File file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			return StringUtil.get(fis);
		} catch (Exception e) {
			return null;
		} finally {
			IOUtil.close(fis);
		}
	}

	public static boolean put(File file, String str) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(str.getBytes("UTF-8"));
			fos.flush();
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			IOUtil.close(fos);
		}
	}

	public static String get(InputStream is) {
		ByteArrayOutputStream bos = null;
		try {
			bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int bufferIndex = -1;
			while ((bufferIndex = is.read(buffer)) != -1) {
				bos.write(buffer, 0, bufferIndex);
			}
			return bos.toString("UTF-8");
		} catch (Exception e) {
			return null;
		} finally {
			IOUtil.close(bos);
		}
	}

	public static boolean put(OutputStream os, String str) {
		try {
			os.write(str.getBytes("UTF-8"));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Whether version is stable
	 * 
	 * @param version
	 * @return Return true if last one is even
	 */
	public static boolean isVersionStable(String version) {
		if (version == null) {
			return false;
		}
		String lastCode = version;
		int dotIndex = version.lastIndexOf('.');
		if (dotIndex != -1) {
			lastCode = version.substring(dotIndex + 1);
		}
		try {
			return Integer.parseInt(lastCode) % 2 == 0;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Compare double versions
	 * 
	 * @param v1
	 * @param v2
	 * @return Zero for v1==v2, positive number for v1>v2, negative number for
	 *         v1<v2
	 */
	public static int compareVersion(String v1, String v2) {
		int diff = 0;
		if (v1 == null || v2 == null) {
			return diff;
		}
		String[] vs1 = v1.split("\\.");
		String[] vs2 = v2.split("\\.");
		int size = vs1.length < vs2.length ? vs1.length : vs2.length;
		for (int i = 0; i < size; i++) {
			String vs1i = vs1[i];
			String vs2i = vs2[i];
			if ((diff = vs1i.length() - vs2i.length()) != 0) {
				break;
			}
			if ((diff = vs1i.compareTo(vs2i)) != 0) {
				break;
			}
		}
		// return if it has result,otherwise who has subversion is bigger.
		return diff != 0 ? diff : (vs1.length - vs2.length);
	}

	public static String printStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.flush();
		pw.close();
		return sw.toString();
	}

	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static Locale getLocale(String locale) {
		if (locale == null) {
			return null;
		}
		String[] parts = locale.split("_");
		if (parts.length == 1) {
			return new Locale(parts[0]);
		} else if (parts.length == 2 || (parts.length == 3 && parts[2].startsWith("#"))) {
			return new Locale(parts[0], parts[1]);
		} else {
			return new Locale(parts[0], parts[1], parts[2]);
		}
	}

	public static String toString(Object object) {
		return object == null ? null : object.toString();
	}

	public static String getTag(StackTraceElement e) {
		StringBuilder sb = new StringBuilder();
		String name = e.getClassName();
		name = name.substring(name.lastIndexOf(".") + 1);
		sb.append(name);
		sb.append(".");
		sb.append(e.getMethodName());
		sb.append("(L:");
		sb.append(e.getLineNumber());
		sb.append(")");
		return sb.toString();
	}

}
