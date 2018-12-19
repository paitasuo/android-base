package com.zmguanjia.commlib.util;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: paitasuo
 * Datae: 2016/8/30
 * Description:字符串相关的工具类
 */
public class StringUtils {
    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public final static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
    public final static SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    public final static SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
    public final static SimpleDateFormat getDateFormater3 = new SimpleDateFormat("yyyy年MM月dd日", Locale.CANADA);
    private final static SimpleDateFormat dateFormater4 = new SimpleDateFormat("MM-dd", Locale.CHINA);
    public final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);//输入日期的格式
    public final static SimpleDateFormat simpleDateFormat5 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);//输入日期的格式
    public final static SimpleDateFormat simpleDateFormat6 = new SimpleDateFormat("yyyy年MM月", Locale.CANADA);

    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回唯一行标识（主键）
     *
     * @return
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        str = str.replaceAll("-", "");
        return str;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String nowDateTime(SimpleDateFormat dateFormater) {
        Date curDate = new Date(System.currentTimeMillis());
        return dateFormater.format(curDate);
    }

    /**
     * 使用delimiter来分割str，生成数组
     *
     * @param str
     * @param delimiter
     * @return
     */
    public static String[] delimitedListToStringArray(String str, String delimiter) {
        if (str == null) {
            return new String[0];
        }
        if (delimiter == null) {
            return new String[]{str};
        }

        List<String> result = new ArrayList<String>();
        if ("".equals(delimiter)) {
            result.add(str);
        } else {
            int pos = 0;
            int delPos = 0;
            while ((delPos = str.indexOf(delimiter, pos)) != -1) {
                result.add(str.substring(pos, delPos));
                pos = delPos + delimiter.length();
            }
            if (str.length() > 0 && pos <= str.length()) {
                // Add rest of String, but not in case of empty input.
                result.add(str.substring(pos));
            }
        }
        return result.toArray(new String[result.size()]);
    }

    // 判断str不是（null或者""）
    public static boolean hasLength(String str) {
        return (str != null && str.length() > 0);
    }

    @SuppressLint("SimpleDateFormat")
    public static boolean compareTime(String date1, String date2) {
        boolean isBefore = false;
        try {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(simpleDateFormat6.parse(date1));
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(simpleDateFormat6.parse(date2));
            if (calendar1.after(calendar2)) {
                isBefore = true;               // date1 > date2
            } else {
                isBefore = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isBefore;
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.parse(sdate);

        } catch (ParseException e) {
            return null;
        }
    }

    public static Date toDate2(String sdate) {
        try {
            return dateFormater2.parse(sdate);

        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 以友好的方式显示时间。先按照时常判断 刚刚(2分钟内) n分钟前(1小时内),超出一小时按照日历判断 昨天 今天 月-日
     *
     * @param sdate
     * @return
     */
    public static String friendly_time(String sdate) {
        Date time = toDate(sdate);// 传入时间
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();// 当前时间
        Calendar yescal = Calendar.getInstance();// 昨天时间
        yescal.add(Calendar.DAY_OF_MONTH, -1);

        // 判断是否是同一天
        String curDate = dateFormater2.format(cal.getTime());// 当前时间日期
        String yescurDate = dateFormater2.format(yescal.getTime());// 昨天日期
        String paramDate = dateFormater2.format(time);// 传入时间的日期
        int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);// 和当前相差小时数
        int minuts = (int) ((cal.getTimeInMillis() - time.getTime()) / 60000);// 和当前相差分钟数

        if (minuts < 2) {// 两分钟之内为“刚刚”
            ftime = "刚刚";
            return ftime;
        } else if (hour == 0) {// 2分钟~1小时为“..分钟前”
            ftime = Math.max(minuts, 2) + "分钟前";
            return ftime;
        }

        if (curDate.equals(paramDate)) {// 如果同一天
            ftime = "今天";
        } else if (yescurDate.equals(paramDate)) {// 如果是当前时间的昨天等于传入时间
            ftime = "昨天";
        } else {
            ftime = dateFormater4.format(time);
        }
        return ftime;
    }

    /**
     * 字符串格式化为时间为 12:00 2014/09/15
     *
     * @param date 日期：2014-09-15 14:02:58
     * @return
     */
    public static String formatDate(String date) {
        StringBuffer strBuff = new StringBuffer();
        // 获取ymd
        String[] ymd_hs = date.split(" ");
        if (2 == ymd_hs.length) {
            String ymd = ymd_hs[0];
            String hms = ymd_hs[1];
            String[] hour_second = hms.split(":");
            if (hour_second.length == 3) {
                String hour = hour_second[0];
                String minute = hour_second[1];
                String second = hour_second[2];
                if (Integer.valueOf(hour) < 10 && hour.length() == 1) {
                    hour = "0" + hour;
                }
                if (Integer.valueOf(minute) < 10 && hour.length() == 1) {
                    minute = "0" + minute;
                }
                if (Integer.valueOf(second) < 10 && hour.length() == 1) {
                    second = "0" + second;
                }
                hms = hour + ":" + minute;
            }
            strBuff.append(hms);
            strBuff.append("  ");

            String[] year_month_day = ymd.split("-");
            if (3 == year_month_day.length) {
                // yeay
                String year = year_month_day[0];
                // month
                String month = year_month_day[1];
                // day
                String day = year_month_day[2];

                strBuff.append(year);
                strBuff.append("/");
                strBuff.append(month);
                strBuff.append("/");
                strBuff.append(day);
            }
        }
        return strBuff.toString();

    }

    /**
     * 字符串格式化为时间为 12:00 2014/09/15
     *
     * @param date 日期：2014-09-15 14:02:58
     * @return
     */
    public static String formatNoticeDate(String date) {
        StringBuffer strBuff = new StringBuffer();
        // 获取ymd
        String[] ymd_hs = date.split(" ");
        if (2 == ymd_hs.length) {
            String ymd = ymd_hs[0];
            String hms = ymd_hs[1];
            String[] year_month_day = ymd.split("-");
            if (3 == year_month_day.length) {
                // yeay
                String year = year_month_day[0];
                if (4 == year.length()) {
                    year = year.substring(2, 4);
                }

                // month
                String month = year_month_day[1];
                // day
                String day = year_month_day[2];

                strBuff.append(year);
                strBuff.append("-");
                strBuff.append(month);
                strBuff.append("-");
                strBuff.append(day);
            }

            strBuff.append(" ");
            String[] hour_second = hms.split(":");
            if (hour_second.length == 3) {
                String hour = hour_second[0];
                String minute = hour_second[1];
                String second = hour_second[2];
                if (Integer.valueOf(hour) < 10 && hour.length() == 1) {
                    hour = "0" + hour;
                }
                if (Integer.valueOf(minute) < 10 && hour.length() == 1) {
                    minute = "0" + minute;
                }
                if (Integer.valueOf(second) < 10 && hour.length() == 1) {
                    second = "0" + second;
                }
                hms = hour + ":" + minute;
            }
            strBuff.append(hms);

        }
        return strBuff.toString();

    }

    /**
     * 字符串格式化为时间为 将选出的个位数时间化为10位
     *
     * @param date 日期：2014-09-15 6:4---->2014-09-15 06:04
     * @return
     */
    public static String formatCommonDate(String date) {
        StringBuffer strBuff = new StringBuffer();
        // 获取ymd
        String[] ymd_hs = date.split(" ");
        if (2 == ymd_hs.length) {
            String ymd = ymd_hs[0];
            String[] year_month_day = ymd.split("-");
            if (year_month_day.length == 3) {
                strBuff.append(year_month_day[0]);
                strBuff.append("-");
                String month = year_month_day[1];
                String day = year_month_day[2];
                if (Integer.valueOf(month) < 10) {
                    month = "0" + month;
                }
                strBuff.append(month);
                strBuff.append("-");
                if (Integer.valueOf(day) < 10) {
                    day = "0" + day;
                }
                strBuff.append(day);
            } else {
                strBuff.append(ymd);
            }
            String hs = ymd_hs[1];
            String[] hour_second = hs.split(":");
            if (hour_second.length == 2) {
                String hour = hour_second[0];
                String second = hour_second[1];
                if (Integer.valueOf(hour) < 10) {
                    hour = "0" + hour;
                }
                if (Integer.valueOf(second) < 10) {
                    second = "0" + second;
                }
                hs = "  " + hour + ":" + second;
            }
            strBuff.append(hs);

        }

        return strBuff.toString();

    }

    /**
     * 清除str前面的不可见字符
     *
     * @param str
     * @return
     */
    public static String trimLeadingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuffer buf = new StringBuffer(str);
        while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
            buf.deleteCharAt(0);
        }
        return buf.toString();
    }

    /**
     * emoji 表情过滤
     *
     * @return
     */
    public static InputFilter emojiFilter() {
        return new InputFilter() {

            Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                    Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher emojiMatcher = emoji.matcher(source);
                if (emojiMatcher.find()) {
                    return "";
                }
                return null;
            }
        };
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 将服务器返回时间字符串转为所需格式, 2014-09-26 17:08
     *
     * @param s
     * @param containTime 是否要输出时间
     * @return
     */
    public static String formatDate2(String s, boolean containTime) {
        String[] parts1 = s.split(" ");
        StringBuffer b = new StringBuffer();
        if (parts1 != null && parts1.length > 0) {
            String[] parts2 = parts1[0].split("-");// 日期
            b.append(parts2[0] + "-" + parts2[1] + "-" + parts2[2]);
            if (containTime && parts1 != null && parts1.length > 1) {
                String[] parts3 = parts1[1].split(":");
                if (parts3 != null && parts3.length > 1) {
                    b.append(" " + parts3[0] + ":" + parts3[1]);
                }
            }
        }
        return b.toString();
    }

    public static String getTimeStampDifference(long start_time, long end_time) {
        String seconds;
        Date date1 = new Date(start_time);
        Date date2 = new Date(end_time);
        long date_time1 = date1.getTime();
        long date_time2 = date2.getTime();
        seconds = (date_time2 - date_time1) / 1000 + "";
        return seconds;
    }

    /**
     * 获取星期几
     */
    public static String getWeek(Calendar c) {
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "日";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mWay;
    }

    /**
     * 得到当前日期的下一天
     */
    public static Calendar getNextCanlendar(int count) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());   //设置当前日期
        c.add(Calendar.DATE, count); //日期加1
        return c;
    }

    public static Calendar getNextCanlendar(SimpleDateFormat dateFormater, String date) {
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(dateFormater.parse(date));   //设置当前日期
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 1); //日期加1
        return c;
    }

    public static int getDateCount(String str1, String str2) {

        Date date1 = null;
        try {
            date1 = StringUtils.simpleDateFormat.parse(str1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = StringUtils.simpleDateFormat.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        cal1.setTime(date1);
        cal2.setTime(date2);
        double dayCount = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 3600 * 24);//从间隔毫秒变成间隔天数
        return (int) dayCount;
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be DidiCitySelEntity positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static float round(float v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be DidiCitySelEntity positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Float.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 小数后面自动去0
     *
     * @param v
     * @return
     */
    public static String roundNoZero(double v, int scale) {
        String s = String.valueOf(round(v, scale));
        if (!TextUtils.isEmpty(s)) {
            while (s.endsWith("0")) {// 从后面截掉0
                s = s.substring(0, s.length() - 1);
            }
            if (s.endsWith(".")) {// 截掉小数点
                s = s.substring(0, s.length() - 1);
            }
        }
        return s;
    }

    /**
     * 小数后面自动去0
     *
     * @param v
     * @return
     */
    public static String roundNoZero(double v) {
        String s = String.valueOf(round(v, 2));
        if (!TextUtils.isEmpty(s)) {
            while (s.endsWith("0")) {// 从后面截掉0
                s = s.substring(0, s.length() - 1);
            }
            if (s.endsWith(".")) {// 截掉小数点
                s = s.substring(0, s.length() - 1);
            }
        }
        return s;
    }

    /**
     * 小数后面自动去0
     *
     * @param v
     * @return
     */
    public static String roundNoZero(float v) {
        String s = String.valueOf(round(v, 2));
        if (!TextUtils.isEmpty(s)) {
            while (s.endsWith("0")) {// 从后面截掉0
                s = s.substring(0, s.length() - 1);
            }
            if (s.endsWith(".")) {// 截掉小数点
                s = s.substring(0, s.length() - 1);
            }
        }
        return s;
    }

    /**
     * 将时间戳转换成字符串
     *
     * @param dateFormater
     * @param cc_time
     * @return
     */
    public static String getStrTime(SimpleDateFormat dateFormater, String cc_time) {
//        String re_strTime = null;
//        long lcc_time = Long.valueOf(cc_time);
//        re_strTime = dateFormater.format(new Date(lcc_time * 1000L));
        long lcc = Long.valueOf(cc_time);
        String times = dateFormater.format(new Date(lcc));

        return times;
    }

    /**
     * 将字符串转换成时间戳
     *
     * @param dateFormater
     * @param user_time
     * @return
     */
    public static String getTime(SimpleDateFormat dateFormater, String user_time) {
        String re_time = null;
        Date d;
        try {
            d = dateFormater.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }

    /**
     * 验证身份证号是否符合规则
     *
     * @param text 身份证号
     * @return
     */
    public static boolean personIdValidation(String text) {
        String regx = "[0-9]{17}x";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        return text.matches(regx) || text.matches(reg1) || text.matches(regex);
    }


    public static boolean isZero(@NonNull String s) {
        return s.equals("0") || s.equals("0.0") || s.equals("0.00");
    }

    /**
     * 手机号脱敏
     */
    public static String phoneStr(String phone) {
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }


    public static String[] splitSymbol(String source, String symbol) {
        if (isEmpty(source) || isEmpty(symbol)) return null;
        String[] s = source.split(symbol);
        return s;
    }


    public static String removeHtmlTag(String inputString) {
        if (inputString == null)
            return null;
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;
        java.util.regex.Pattern p_special;
        java.util.regex.Matcher m_special;
        try {
            //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            // 定义HTML标签的正则表达式
            String regEx_html = "<[^>]+>";
            // 定义一些特殊字符的正则表达式 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            String regEx_special = "\\&[a-zA-Z]{1,10};";
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
            m_special = p_special.matcher(htmlStr);
            htmlStr = m_special.replaceAll(""); // 过滤特殊标签
            textStr = htmlStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textStr;// 返回文本字符串
    }

}
