package cn.mutils.app.core.codec;

public class BooleanBitsUtil {

	public static int BITS_TRUE = 0xFFFFFFFF;
	public static int BITS_FALSE = 0x00000000;

	public static boolean get(int number, int bit) {
		if (bit <= 0 || bit > 32) {
			return false;
		}
		int mask = number;
		if (bit > 1) {
			mask = mask >> (bit - 1);
		}
		return (mask & 0x00000001) == 0x00000001;
	}

	public static int set(int number, int bit, boolean value) {
		if (bit <= 0 || bit > 32) {
			return number;
		}
		int mask = 0x00000001;
		if (bit > 1) {
			mask = mask << (bit - 1);
		}
		return value ? (number | mask) : (number & (~mask));
	}

	public static String toBinaryString(int number) {
		return Integer.toBinaryString(number);
	}

	public static String toHexString(int number) {
		return Integer.toHexString(number);
	}

	public static String toString(int number) {
		return Integer.toString(number);
	}

}
