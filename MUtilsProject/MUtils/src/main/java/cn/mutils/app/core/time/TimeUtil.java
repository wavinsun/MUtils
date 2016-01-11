package cn.mutils.app.core.time;

import java.util.Date;

/**
 * Milliseconds utility of framework
 */
@SuppressWarnings({"deprecation", "unused"})
public class TimeUtil {

    public static long getDayTime(long time) {
        Date d = new Date(time);
        return new Date(d.getYear(), d.getMonth(), d.getDate()).getTime();
    }

    public static boolean isSameDay(long one, long another) {
        return getDayTime(one) == getDayTime(another);
    }

    public static long getDayTime(Date date) {
        return new Date(date.getYear(), date.getMonth(), date.getDate()).getTime();
    }

    public static int getYear(Date date) {
        return date.getYear() + 1900;
    }

    public static int getMonth(Date date) {
        return date.getMonth() + 1;
    }

    public static int getDay(Date date) {
        return date.getDate();
    }

    /**
     * Whether is leap year 闰年
     */
    public static boolean isLeap(Date date) {
        int year = date.getYear() + 1900;
        return isLeap(year);
    }

    /**
     * Whether is leap year 闰年
     */
    public static boolean isLeap(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    public static int getDaysOfMonth(Date date) {
        return getDaysOfMonth(TimeUtil.getYear(date), TimeUtil.getMonth(date));
    }

    public static Date getDate(int year, int month, int day) {
        return new Date(year - 1900, month - 1, day);
    }

    public static Date getDate(int year, int month, int day, int hour, int minute) {
        return new Date(year - 1900, month - 1, day, hour, minute);
    }

    public static int getDaysOfMonth(int year, int month) {
        if (month == 2) {
            return isLeap(year) ? 29 : 28;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else {
            return 31;
        }
    }

    public static boolean isSameDay(Date d1, Date d2) {
        return (d1 != null && d2 != null)
                ? (d1.getYear() == d2.getYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate())
                : (d1 == d2);
    }

    public static boolean isSameMonth(Date d1, Date d2) {
        return (d1 != null && d2 != null) ? (d1.getYear() == d2.getYear() && d1.getMonth() == d2.getMonth())
                : (d1 == d2);
    }

}
