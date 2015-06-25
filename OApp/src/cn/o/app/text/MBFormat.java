package cn.o.app.text;

import java.text.DecimalFormat;

import android.annotation.SuppressLint;

//以MB为单位的字符串格式化工具 13.4MB
@SuppressLint("DefaultLocale")
public class MBFormat {

	public static double parse(String str) {
		double value = 0;
		if (str == null) {
			return value;
		}
		String strUp = str.toUpperCase();
		String unit = "M";
		try {
			value = Double.parseDouble(strUp);
		} catch (Exception e) {
			try {
				value = Double.parseDouble(strUp.substring(0,
						strUp.length() - 1));
				unit = strUp.substring(strUp.length() - 1);
			} catch (Exception ex) {
				try {
					value = Double.parseDouble(strUp.substring(0,
							strUp.length() - 2));
					unit = strUp.substring(strUp.length() - 2,
							strUp.length() - 1);
				} catch (Exception exx) {
					// 无效的字符串
				}
			}
		}
		if ("M".equals(unit)) {
			return value;
		} else if ("K".equals(unit)) {
			return value / 1024;
		} else if ("G".equals(unit)) {
			return value * 1024;
		} else if ("T".equals(unit)) {
			return value * 1048576;// 1024*1024=1048576
		} else {
			return value;
		}
	}

	public static String format(double value) {
		return format(value, "#.##");
	}

	public static String format(double value, String format) {
		String unit = "M";
		double valueAbs = value > 0 ? value : (value * -1);
		if (valueAbs >= 1048576) {
			value = value / 1048576;// 1024*1024=1048576
			unit = "T";
		} else if (valueAbs >= 1024) {
			value = value / 1024;
			unit = "G";
		} else if (valueAbs < 1 && valueAbs != 0) {
			value = value * 1024;
			unit = "K";
		} else if (valueAbs == 0) {
			unit = "M";
		}
		DecimalFormat df = new DecimalFormat(format);
		StringBuilder sb = new StringBuilder();
		sb.append(df.format(value));
		sb.append(unit);
		sb.append("B");
		return sb.toString();
	}
}
