package cn.mutils.app.core.codec;

import cn.mutils.app.core.text.StringUtil;

public class ByteUtil {

    public static byte[] toBytes(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((value >> 24) & 0xFF);
        bytes[1] = (byte) ((value >> 16) & 0xFF);
        bytes[2] = (byte) ((value >> 8) & 0xFF);
        bytes[3] = (byte) (value & 0xFF);
        return bytes;
    }

    public static int toInt(byte[] bytes) {
        if (bytes == null) {
            return 0;
        }
        return toInt(bytes, 0);
    }

    public static int toInt(byte[] bytes, int offset) {
        if (bytes == null) {
            return 0;
        }
        if (offset < 0 || (offset + 4) > bytes.length) {
            return 0;
        }
        int value = ((int) (bytes[offset] & 0xFF)) << 24;
        value |= ((int) (bytes[offset + 1] & 0xFF)) << 16;
        value |= ((int) (bytes[offset + 2] & 0xFF)) << 8;
        value |= ((int) (bytes[offset + 3] & 0xFF));
        return value;
    }

    public static String toString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return StringUtil.toBinary(bytes);
    }

    public static String toHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return StringUtil.toHex(bytes);
    }

}
