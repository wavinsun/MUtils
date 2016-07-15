package cn.mutils.core.codec;

import cn.mutils.core.text.StringUtil;

/**
 * Flag utility of framework
 */
@SuppressWarnings("unused")
public class FlagUtil {

    public static final int FLAG_01 = 0x00000001;
    public static final int FLAG_02 = 0x00000002;
    public static final int FLAG_03 = 0x00000004;
    public static final int FLAG_04 = 0x00000008;
    public static final int FLAG_05 = 0x00000010;
    public static final int FLAG_06 = 0x00000020;
    public static final int FLAG_07 = 0x00000040;
    public static final int FLAG_08 = 0x00000080;
    public static final int FLAG_09 = 0x00000100;
    public static final int FLAG_10 = 0x00000200;
    public static final int FLAG_11 = 0x00000400;
    public static final int FLAG_12 = 0x00000800;
    public static final int FLAG_13 = 0x00001000;
    public static final int FLAG_14 = 0x00002000;
    public static final int FLAG_15 = 0x00004000;
    public static final int FLAG_16 = 0x00008000;
    public static final int FLAG_17 = 0x00010000;
    public static final int FLAG_18 = 0x00020000;
    public static final int FLAG_19 = 0x00040000;
    public static final int FLAG_20 = 0x00080000;
    public static final int FLAG_21 = 0x00100000;
    public static final int FLAG_22 = 0x00200000;
    public static final int FLAG_23 = 0x00400000;
    public static final int FLAG_24 = 0x00800000;
    public static final int FLAG_25 = 0x01000000;
    public static final int FLAG_26 = 0x02000000;
    public static final int FLAG_27 = 0x04000000;
    public static final int FLAG_28 = 0x08000000;
    public static final int FLAG_29 = 0x10000000;
    public static final int FLAG_30 = 0x20000000;
    public static final int FLAG_31 = 0x40000000;
    public static final int FLAG_32 = 0x80000000;

    public static final int FLAGS_TRUE = 0xFFFFFFFF;
    public static final int FLAGS_FALSE = 0x00000000;

    /**
     * Get one bit flag value from flags
     *
     * @param flags Flags
     * @param bit   [1,32]
     * @return Flag value
     */
    public static boolean getValue(int flags, int bit) {
        if (bit <= 0 || bit > 32) {
            return false;
        }
        int mask = flags;
        if (bit > 1) {
            mask = mask >> (bit - 1);
        }
        return (mask & FlagUtil.FLAG_01) == FlagUtil.FLAG_01;
    }

    /**
     * Set one bit flag value to flags
     *
     * @param flags Flags
     * @param bit   [1,32]
     * @param value Flag value
     * @return Flags assigned
     */
    public static int setValue(int flags, int bit, boolean value) {
        if (bit <= 0 || bit > 32) {
            return flags;
        }
        int mask = FlagUtil.FLAG_01;
        if (bit > 1) {
            mask = mask << (bit - 1);
        }
        return value ? (flags | mask) : (flags & (~mask));
    }

    /**
     * Create flag value
     */
    public static int obtainFlag(int bit) {
        return FlagUtil.setValue(FlagUtil.FLAGS_FALSE, bit, true);
    }

    /**
     * Whether has flags
     *
     * @param flags Source flags
     * @param mask  Mask flags
     * @return Result
     */
    public static boolean hasFlags(int flags, int mask) {
        return (flags & mask) == mask;
    }

    /**
     * Add mask flags to source flags
     *
     * @param flags Flags
     * @param mask  Mask
     * @return Flags assigned
     */
    public static int addFlags(int flags, int mask) {
        return flags | mask;
    }

    /**
     * Clear mask flags for source flags
     *
     * @param flags Flags
     * @param mask  Mask
     * @return Flags assigned
     */
    public static int clearFlags(int flags, int mask) {
        return flags & (~mask);
    }

    /**
     * Set mask flags for source flags
     */
    public static int setFlags(int flags, int mask, boolean value) {
        return value ? (flags | mask) : (flags & (~mask));
    }

    /**
     * Format flags to binary string
     */
    public static String toBinaryString(int flags) {
        return StringUtil.zeroPadding(Integer.toBinaryString(flags), 32);
    }

    /**
     * Format flags to hex string
     */
    public static String toHexString(int flags) {
        return StringUtil.zeroPadding(Integer.toHexString(flags), 8);
    }

    /**
     * Format flags to normal string
     */
    public static String toString(int flags) {
        return Integer.toString(flags);
    }

}
