package util;

import android.app.Activity;
import android.content.SharedPreferences;

import data.HandleUserMessage;
import variable.LoginStateVariable;
import variable.UserMessageVariable;

/**
 * Created by 张同心 on 2017/9/29.
 * @function 自动登录
 */

public class AutoLoginUtil {

    /**
     * 自动登录
     * @param activity
     */
    public static void autoLogin(Activity activity){
          /*记录自动登录*/
        SharedPreferences autoLoginSp = activity.getSharedPreferences("autoLogin", 0);
        SharedPreferences.Editor autoLogin = autoLoginSp.edit();
        autoLogin.putString("phoneNumber", UserMessageVariable.osUserMessage.oStrPhoneNumber);
        autoLogin.commit();
    }

    /**
     * 取消自动登录
      * @param activity
     */
    public static void cancleAutoLogin(Activity activity){
          /*记录自动登录*/
        SharedPreferences autoLoginSp = activity.getSharedPreferences("autoLogin", 0);
        SharedPreferences.Editor autoLogin = autoLoginSp.edit();
        autoLogin.putString("phoneNumber", "0");
        autoLogin.commit();
    }

    /**
     * 开始自动登录
     * @param activity
     */
    public static void startAutoLogin(Activity activity){
        SharedPreferences autoLoginSp = activity.getSharedPreferences("autoLogin", 0);
        String strPhoneNumber = autoLoginSp.getString("phoneNumber", "0");
        if (HandleUserMessage.userExist(strPhoneNumber)){
            UserMessageVariable.osUserMessage = HandleUserMessage.readUserMessage(strPhoneNumber);
            LoginStateVariable.osLoginState = true;
        }
    }
}
