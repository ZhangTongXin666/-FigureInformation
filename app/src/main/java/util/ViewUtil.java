package util;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.kys_31.figureinformation.R;

/**
 * Created by 张同心 on 2017/9/26.
 * @function 视图工具类
 */

public class ViewUtil {

    private static AlphaAnimation mHideAnimation= null;
    private static AlphaAnimation mShowAnimation= null;

    /**
     * 改变Dialog大小
     * @param dialog
     * @param context
     * @param width
     * @param height
     */
    public static void setDialogWindowAttr(Dialog dialog, int width, int height){
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = width;
        lp.height = height;
        dialog.getWindow().setAttributes(lp);
    }

    /**
     * 设置屏幕亮度
     * @param activity 环境
     * @param value 亮度值
     */
    public static void setScreenBrightness(Activity activity, int value){
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.screenBrightness = value / 255f;
        activity.getWindow().setAttributes(params);
    }

    /**
     * 获取屏幕亮度
     * @param activity
     * @return
     */
  public static int getScreenBrightness(Activity activity){
      int value = 0;
      ContentResolver cr = activity.getContentResolver();
      try {
          value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
      }catch (Exception e){
          e.printStackTrace();
      }
      return value;
  }
    /**
     * 获取屏幕亮度
     * @param content
     * @return 屏幕宽度
     */
    public static int getScreenWith(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }


    //渐渐隐藏动画
    public static void setHideAnimation(View view, int duration ){
        if( null == view || duration < 0 ){
            return;
        }
        if( null != mHideAnimation ){

            mHideAnimation.cancel( );
        }
        mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration( duration );
        view.startAnimation( mHideAnimation );
    }

    //渐渐显示动画
    public static void setShowAnimation( View view, int duration ){
        if( null == view || duration < 0 ){
            return;
        }
        if( null != mShowAnimation ){
            mShowAnimation.cancel( );
        }
        mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration( duration );
        view.startAnimation( mShowAnimation );

    }

}
