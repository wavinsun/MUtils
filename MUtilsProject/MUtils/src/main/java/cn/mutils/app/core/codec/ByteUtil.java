package cn.mutils.app.core.codec;

import cn.mutils.app.core.text.StringUtil;

/**
 * Byte utility of framework
 */
public class ByteUtil {

    /**
     * Convert integer to bytes
     *
     * @param value
     * @return
     */
    public static byte[] toBytes(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((value >> 24) & 0xFF);
        bytes[1] = (byte) ((value >> 16) & 0xFF);
        bytes[2] = (byte) ((value >> 8) & 0xFF);
        bytes[3] = (byte) (value & 0xFF);
        return bytes;
    }

    /**
     * Convert bytes to integer
     *
     * @param bytes
     * @return
     */
    public static int toInt(byte[] bytes) {
        if (bytes == null) {
            return 0;
        }
        return toInt(bytes, 0);
    }

    /**
     * Convert bytes to integer from offset position
     *
     * @param bytes
     * @param offset
     * @return
     */
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

    /**
     * Convert bytes to binary string
     *
     * @param bytes
     * @return
     */
    public static String toString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return StringUtil.toBinary(bytes);
    }

    /**
     * Convert bytes to hex string
     *
     * @param bytes
     * @return
     */
    public static String toHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return StringUtil.toHex(bytes);
    }

}
