package cn.mutils.app.core.text;

import android.annotation.SuppressLint;

import java.text.DecimalFormat;

/**
 * String format tool for MB:13.4MB
 */
@SuppressWarnings("StringBufferReplaceableByString")
@SuppressLint("DefaultLocale")
public class MBFormat {

    public static final double MILLION_SIZE = 1048576;// 1024*1024

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
                value = Double.parseDouble(strUp.substring(0, strUp.length() - 1));
                unit = strUp.substring(strUp.length() - 1);
            } catch (Exception ex) {
                try {
                    value = Double.parseDouble(strUp.substring(0, strUp.length() - 2));
                    unit = strUp.substring(strUp.length() - 2, strUp.length() - 1);
                } catch (Exception exx) {
                    // invalid string
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
            return value * MILLION_SIZE;
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
        if (valueAbs >= MILLION_SIZE) {
            value = value / MILLION_SIZE;
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
