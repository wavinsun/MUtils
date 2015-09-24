package cn.o.app.core.time;

import java.util.Date;

/**
 * Milliseconds utility of framework
 */
@SuppressWarnings("deprecation")
public class MillisUtil {

	public static long getDayTime(long time) {
		Date d = new Date(time);
		return new Date(d.getYear(), d.getMonth(), d.getDate()).getTime();
	}

	public static long getDayTime(Date date) {
		return new Date(date.getYear(), date.getMonth(), date.getDate()).getTime();
	}

	public static boolean isSameDay(long one, long another) {
		return getDayTime(one) == getDayTime(another);
	}

	public static boolean isSameDay(Date one, Date another) {
		return getDayTime(one) == getDayTime(another);
	}

}
