package cn.mutils.app.core.math;

import java.math.BigDecimal;

/**
 * Number utility of framework<br>
 * High precision feature
 */
public class NumberUtil {

	/** Transform for degress to radian */
	public static final double TO_RADIAN = Math.PI / 180;
	/** Transform for radian to degress */
	public static final double TO_DEGRESS = 180 / Math.PI;

	/**
	 * Reserve double decimals by round way
	 * 
	 * @param number
	 * @param decimals
	 * @return
	 */
	public static double toFixedRound(double number, int decimals) {
		String str = String.valueOf(number);
		BigDecimal n = toBigDecimal(str, decimals);
		n = n.setScale(decimals, BigDecimal.ROUND_HALF_UP);
		return n.doubleValue();
	}

	/**
	 * Reserve double decimals by ceil way
	 * 
	 * @param number
	 * @param decimals
	 * @return
	 */
	public static double toFixedCeil(double number, int decimals) {
		String str = String.valueOf(number);
		BigDecimal n = toBigDecimal(str, decimals);
		n = n.setScale(decimals, BigDecimal.ROUND_CEILING);
		return n.doubleValue();
	}

	/**
	 * Reserve double decimals by floor way
	 * 
	 * @param number
	 * @param decimals
	 * @return
	 */
	public static double toFixedFloor(double number, int decimals) {
		String str = String.valueOf(number);
		BigDecimal n = toBigDecimal(str, decimals);
		n = n.setScale(decimals, BigDecimal.ROUND_FLOOR);
		return n.doubleValue();
	}

	/**
	 * Reserve float decimals by round way
	 * 
	 * @param number
	 * @param decimals
	 * @return
	 */
	public static float toFixedRound(float number, int decimals) {
		String str = String.valueOf(number);
		BigDecimal n = toBigDecimal(str, decimals);
		n = n.setScale(decimals, BigDecimal.ROUND_HALF_UP);
		return n.floatValue();
	}

	/**
	 * Reserve float decimals by ceil way
	 * 
	 * @param number
	 * @param decimals
	 * @return
	 */
	public static float toFixedCeil(float number, int decimals) {
		String str = String.valueOf(number);
		BigDecimal n = toBigDecimal(str, decimals);
		n = n.setScale(decimals, BigDecimal.ROUND_CEILING);
		return n.floatValue();
	}

	/**
	 * Reserve float decimals by floor way
	 * 
	 * @param number
	 * @param decimals
	 * @return
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
	 * 
	 * @param number
	 * @param minScale
	 * @return
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
