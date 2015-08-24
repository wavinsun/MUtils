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

}
