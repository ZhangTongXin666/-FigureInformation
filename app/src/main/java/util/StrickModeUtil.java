package util;

import android.os.StrictMode;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class StrickModeUtil {
    public static void strickMode(){
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectCustomSlowCalls() //API等级大于等于11，可以使用StrictMode.noteSlowCode
                .detectDiskReads() //用于检查UI线程中是否有磁盘的读写
                .detectDiskWrites()
                //  .detectNetwork()   // or .detectAll() for all detectable problems   用于检查UI线程中是否有网络请求
                .penaltyDialog() //弹出违规提示对话框
                .penaltyLog() //在Logcat 中打印违规异常信息
                .penaltyFlashScreen() //API等级大于等于11，会造成屏幕闪烁，一般的设备没有这个功能
                .build());

/*        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectActivityLeaks()//用于检查Activity内存泄露情况
                .detectLeakedRegistrationObjects()//用来检查BroadcastReceiver和ServiceConnection注册类对象是否被正确关闭
                .setClassInstanceLimit(MainActivity.class, 2)//用于设置某个类的实例上限
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects() //API等级大于等于11，用于检查资源有没有关闭
                .penaltyLog()
                .penaltyDeath()//当触发违规行为就Crash掉程序
                .penaltyDeathOnCleartextNetwork()//当触发网络违规时，就Crach掉程序
                .build());*/
/*
        if ( 1 > 2) {
            StrictMode.noteSlowCall(" 触发自定义严格模式 ");
        }*/
    }

}
