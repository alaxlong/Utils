package util;

//import org.apache.poi.hssf.usermodel.HSSFDateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期公共类
 * Created by saifenghuang on 2018/6/10.
 */
public class DateUtil {

    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYY_MM_DDHHMISS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public DateUtil() {
    }

    public static String getCurrentDate() {
        return format(new Date(), "yyyyMMdd");
    }

    public static String getCurrentTime() {
        return format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String format(Date date, String patten) {
        SimpleDateFormat format2 = new SimpleDateFormat(patten);
        return format2.format(date);
    }

    public static Date parser(String dateStr, String patten) {
        try {
            SimpleDateFormat e = new SimpleDateFormat(patten);
            Date date = e.parse(dateStr);
            return date;
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static String getNormalDate(Date date) {
        return format(date, "yyyy-MM-dd");
    }

    public static String getNormalYYYYMMDDDate(Date date) {
        return format(date, "yyyyMMdd");
    }

    /*public static Date getDate(int time) {
        Date dt = HSSFDateUtil.getJavaDate((double)time);
        return dt;
    }*/

}
