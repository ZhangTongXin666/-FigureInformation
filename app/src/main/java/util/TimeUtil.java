package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 张同心 on 2017/9/13.
 * @function 时间工具
 */

public class TimeUtil {

    private static final String TAG = "timeUtil";

    /**
     * 格式化时间
     * @param time
     * @return
     */
    public static String filterTime(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(time);
            String newTime =sdf.format(date);
            return newTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 获取系统时间
     * @return 系统时间
     */
    public static String getSystemTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        return time;
    }
    /**
     * 获取系统日期
     * @return 系统日期
     */
    public static String getSystemDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        return time.substring(0,10);
    }

    public static String formatTimeOne(String strTime){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (strTime.contains("GMT")){
            strTime = strTime.substring(0, strTime.indexOf("GMT"));
        }
        return format.format(new Date(strTime));
    }

    public static boolean compatatorTime(String time1,String time2){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(time1);
            date2 = format.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1.getTime()-date2.getTime()>0;
    }
}
