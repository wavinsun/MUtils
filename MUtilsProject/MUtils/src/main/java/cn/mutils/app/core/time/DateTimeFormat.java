package cn.mutils.app.core.time;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date format of framework
 */
@SuppressWarnings("ConstantConditions")
@SuppressLint("SimpleDateFormat")
public class DateTimeFormat {

    /**
     * Parse text to {@link Date} by pattern
     */
    public static Date parse(String text, String pattern) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat();
        Date d = null;
        while (!pattern.isEmpty()) {
            try {
                df.applyPattern(pattern);
                return df.parse(text);
            } catch (Exception e) {
                try {
                    String s = df.format(d != null ? d : new Date());
                    if (s.length() <= text.length()) {// parse matching length
                        return df.parse(text.substring(0, s.length()));
                    }
                } catch (Exception ex) {
                    // ParseException
                }
                pattern = getSuggestPattern(pattern);// short pattern string
            }
        }
        throw new Exception();
    }

    public static String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * Compatible pattern: yyyy-MM-dd'T'HH:mm:ss.SSS'Z' ->
     * yyyy-MM-dd'T'HH:mm:ss.SSS
     */
    protected static String getSuggestPattern(String pattern) {
        int suggestIndex = 0;
        char lastChar = 0;
        boolean hasQuotes = false;
        for (int i = pattern.length() - 1; i >= 0; i--) {
            char c = pattern.charAt(i);
            if (i == pattern.length() - 1) {
                if (c == '\'') {
                    hasQuotes = true;
                }
            } else {
                suggestIndex = i;
                if (hasQuotes) {
                    if (c == '\'') {
                        break;
                    }
                } else {
                    if (c != lastChar) {
                        suggestIndex++;
                        break;
                    }
                }
            }
            lastChar = c;
        }
        return pattern.substring(0, suggestIndex);
    }
}
