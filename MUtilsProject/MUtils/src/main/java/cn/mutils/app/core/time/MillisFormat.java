package cn.mutils.app.core.time;

/**
 * String format tool for millisecond: 6000 -> 00:00:06
 */
public class MillisFormat {

    public static long parse(String str) {
        try {
            long millis = 0L;
            int index4Second = str.lastIndexOf(':');
            if (index4Second == -1) {
                return millis;
            }
            long seconds = Long.parseLong(str.substring(index4Second + 1));
            millis += seconds * 1000L;
            str = str.substring(0, index4Second);
            int index4Minute = str.lastIndexOf(':');
            if (index4Minute == -1) {
                return millis;
            }
            long minutes = Long.parseLong(str.substring(index4Minute + 1));
            millis += minutes * 60000L;
            str = str.substring(0, index4Minute);
            int index4Hours = str.lastIndexOf(':');
            if (index4Hours == -1) {
                long hours = Long.parseLong(str);
                millis += hours * 3600000L;
                return millis;
            }
            str = str.substring(0, index4Hours);
            long days = Long.parseLong(str);
            millis += days * 86400000L;
            return millis;
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * Format to second
     *
     * @param value
     * @return
     */
    protected static StringBuilder formatInternal(long value) {
        StringBuilder sb = new StringBuilder();
        if (value >= 86400000L) {
            long day = value / 86400000L;
            if (day < 10L) {
                sb.append("0");
            }
            sb.append(day);
            sb.append(":");
            value %= 86400000L;
        }
        if (value >= 3600000L) {
            long hour = value / 3600000L;
            if (hour < 10L) {
                sb.append("0");
            }
            sb.append(hour);
            sb.append(":");
            value %= 3600000L;
        } else {
            sb.append("00:");
        }
        if (value >= 60000L) {
            long minute = value / 60000L;
            if (minute < 10L) {
                sb.append("0");
            }
            sb.append(minute);
            sb.append(":");
            value %= 60000L;
        } else {
            sb.append("00:");
        }
        if (value >= 1000L) {
            long second = value / 1000L;
            if (second < 10L) {
                sb.append("0");
            }
            sb.append(second);
        } else {
            sb.append("00");
        }
        return sb;
    }

    /**
     * Format to second
     *
     * @param value
     * @return
     */
    public static String format(long value) {
        return formatInternal(value).toString();
    }

    /**
     * Format to millisecond
     *
     * @param value
     * @return
     */
    public static String formatAll(long value) {
        StringBuilder sb = formatInternal(value);
        sb.append(".");
        value = value % 1000L;
        if (value < 100L) {
            sb.append("0");
        }
        if (value < 10L) {
            sb.append("0");
        }
        sb.append(value);
        return sb.toString();
    }
}
