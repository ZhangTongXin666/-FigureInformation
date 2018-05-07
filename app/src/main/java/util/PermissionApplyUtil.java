package util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class PermissionApplyUtil {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    private static String[] PERMESSIONS_CARMERA = {
            "android.permission.CAMERA"
    };
    private static String[] PERMISSIONS_SMS = {
            "android.permission.SEND_SMS"
    };

    /*申请读取SD卡权限*/
    public static void verifyStoragePermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23){
            try {
                //检测是否有写的权限
                int permission = ActivityCompat.checkSelfPermission(activity,
                        "android.permission.WRITE_EXTERNAL_STORAGE");
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // 没有写的权限，去申请写的权限，会弹出对话框
                    ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*申请相机权限*/
    public static void applyPermissionCarmera(Activity activity){
        if (Build.VERSION.SDK_INT >= 23){
            try {
                int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.CAMERA");
                if (permission != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(activity, PERMESSIONS_CARMERA, REQUEST_EXTERNAL_STORAGE);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 请求短信权限
     */
    public static void requestPermission(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.SEND_SMS");
            if (permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity, PERMISSIONS_SMS, REQUEST_EXTERNAL_STORAGE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
