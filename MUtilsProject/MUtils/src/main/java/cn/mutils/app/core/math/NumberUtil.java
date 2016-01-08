package cn.mutils.app.core.math;

import java.math.BigDecimal;

/**
 * Number utility of framework<br>
 * High precision feature
 */
public class NumberUtil {

    /**
     * Transform for degree to radian
     */
    public static final double TO_RADIAN = Math.PI / 180;

    /**
     * Transform for radian to degree
     */
    public static final double TO_DEGREE = 180 / Math.PI;

    /**
     * Reserve double decimals by round way
     */
    public static double toFixedRound(double number, int decimals) {
        String str = String.valueOf(number);
        BigDecimal n = toBigDecimal(str, decimals);
        n = n.setScale(decimals, BigDecimal.ROUND_HALF_UP);
        return n.doubleValue();
    }

    /**
     * Reserve double decimals by ceil way
     */
    public static double toFixedCeil(double number, int decimals) {
        String str = String.valueOf(number);
        BigDecimal n = toBigDecimal(str, decimals);
        n = n.setScale(decimals, BigDecimal.ROUND_CEILING);
        return n.doubleValue();
    }

    /**
     * Reserve double decimals by floor way
     */
    public static double toFixedFloor(double number, int decimals) {
        String str = String.valueOf(number);
        BigDecimal n = toBigDecimal(str, decimals);
        n = n.setScale(decimals, BigDecimal.ROUND_FLOOR);
        return n.doubleValue();
    }

    /**
     * Reserve float decimals by round way
     */
    public static float toFixedRound(float number, int decimals) {
        String str = String.valueOf(number);
        BigDecimal n = toBigDecimal(str, decimals);
        n = n.setScale(decimals, BigDecimal.ROUND_HALF_UP);
        return n.floatValue();
    }

    /**
     * Reserve float decimals by ceil way
     */
    public static float toFixedCeil(float number, int decimals) {
        String str = String.valueOf(number);
        BigDecimal n = toBigDecimal(str, decimals);
        n = n.setScale(decimals, BigDecimal.ROUND_CEILING);
        return n.floatValue();
    }

    /**
     * Reserve float decimals by floor way
     */
    public static float toFixedFloor(float number, int decimals) {
        String str = String.valueOf(number);
        BigDecimal n = toBigDecimal(str, decimals);
        n = n.setScale(decimals, BigDecimal.ROUND_FLOOR);
        return n.floatValue();
    }

    /**
     * Make number to BigDecimal by minimum scale<br>
     * Parameter maybe to be used to round
     */
    public static BigDecimal toBigDecimal(String number, int minScale) {
        BigDecimal n = new BigDecimal(number);
        int s = n.scale() - 2;
        if (s > minScale) {
            n = n.setScale(s, BigDecimal.ROUND_HALF_UP);
        }
        return n;
    }

}
