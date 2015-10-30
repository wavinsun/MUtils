package cn.mutils.app.core.math;

/**
 * Number utility of framework
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
		double scale = Math.pow(10, decimals > 0 ? decimals : 0);
		return Math.round(number * scale) / scale;
	}

	/**
	 * Reserve double decimals by ceil way
	 * 
	 * @param number
	 * @param decimals
	 * @return
	 */
	public static double toFixedCeil(double number, int decimals) {
		double scale = Math.pow(10, decimals > 0 ? decimals : 0);
		return Math.ceil(number * scale) / scale;
	}

	/**
	 * Reserve double decimals by floor way
	 * 
	 * @param number
	 * @param decimals
	 * @return
	 */
	public static double toFixedFloor(double number, int decimals) {
		double scale = Math.pow(10, decimals > 0 ? decimals : 0);
		return Math.floor(number * scale) / scale;
	}

	/**
	 * Reserve float decimals by round way
	 * 
	 * @param number
	 * @param decimals
	 * @return
	 */
	public static float toFixedRound(float number, int decimals) {
		float scale = (float) Math.pow(10, decimals > 0 ? decimals : 0);
		return Math.round(number * scale) / scale;
	}

	/**
	 * Reserve float decimals by ceil way
	 * 
	 * @param number
	 * @param decimals
	 * @return
	 */
	public static float toFixedCeil(float number, int decimals) {
		float scale = (float) Math.pow(10, decimals > 0 ? decimals : 0);
		return ((int) Math.ceil(number * scale)) / scale;
	}

	/**
	 * Reserve float decimals by floor way
	 * 
	 * @param number
	 * @param decimals
	 * @return
	 */
	public static float toFixedFloor(float number, int decimals) {
		float scale = (float) Math.pow(10, decimals > 0 ? decimals : 0);
		return ((int) Math.floor(number * scale)) / scale;
	}

}
