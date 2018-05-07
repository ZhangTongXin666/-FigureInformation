package util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import data.HandleUserMessage;
import data.UserMessage;
import variable.LoginStateVariable;
import variable.UserMessageVariable;

/**
 * Created by 张同心 on 2017/9/21.
 * @function 第三方登录
 */

public class ThirdPartyLoginUtil {

    /**
     * 新浪授权
     */
    public static void authPlatform_XinLang(final Activity activity) {
        //获取具体的平台手动授权
        final Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);

        //设置回调监听
        weibo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, final HashMap<String, Object> hashMap) {
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = activity;
                handler.sendMessage(msg);
                userLogin(platform, activity);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                activity.finish();
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = activity;
                handler.sendMessage(msg);
                throwable.printStackTrace();
                Log.e("TAG", "VB第三方登录："+throwable.getMessage()+"  当前线程名字："+Thread.currentThread().getName());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                weibo.removeAccount(true);
            }
        });
        //授权并获取用户信息
        weibo.showUser(null);
    }

    /**
     * QQ 授权
     */
    public static void authPlatform_QQ(final Activity activity) {
        //获取QQ平台手动授权
        final Platform qq = ShareSDK.getPlatform(QQ.NAME);

        //设置回调监听
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, final HashMap<String, Object> hashMap) {
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = activity;
                handler.sendMessage(msg);
                userLogin(platform, activity);
            }
            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                activity.finish();
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = activity;
                handler.sendMessage(msg);
                throwable.printStackTrace();
                Log.e("TAG", "QQ第三方登录："+throwable.getMessage()+"   当前线程的名字："+Thread.currentThread().getName());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                qq.removeAccount(true);
            }
        });
        qq.showUser(null);
    }
    /**
     * 微信授权
     */
    public static void authPlatform_Weixin(final Activity activity) {
        //设置平台手动授权
        final Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
        weixin.removeAccount(true);
        //设置监听回调
        weixin.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, final HashMap<String, Object> hashMap) {
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = activity;
                handler.sendMessage(msg);
                userLogin(platform, activity);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                activity.finish();
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = activity;
                handler.sendMessage(msg);
                throwable.printStackTrace();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                weixin.removeAccount(true);
            }
        });
        //单独授权,进入输入用户名和密码界面
        /*weixin.authorize();*/
        //授权并获取用户信息
        weixin.showUser(null);

    }

    /**
     * 用户登录
     * @param platform
     */
    private static void userLogin(Platform platform, Activity activity){
        if (!HandleUserMessage.userExist(platform.getDb().getUserId())){
            UserMessageVariable.osUserMessage = new UserMessage(platform.getDb().getUserId(), platform.getDb().getUserId(),
                    platform.getDb().getUserIcon(), platform.getDb().getUserName(), "0", platform.getDb().getUserId(),
                    platform.getDb().getUserGender().equals("m")?0:1, 0, "首次使用");
        }else {
            UserMessageVariable.osUserMessage = HandleUserMessage.readUserMessage(platform.getDb().getUserId());
        }
        HandleUserMessage.saveData( UserMessageVariable.osUserMessage);
        LoginStateVariable.osLoginState = true;
        AutoLoginUtil.autoLogin(activity);
        activity.finish();
    }

    private static Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    Toast.makeText((Context) msg.obj, "获取信息失败,请重新登陆！", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText((Context) msg.obj, "登录成功", Toast.LENGTH_SHORT).show();
                    break;
                default:break;
            }
        }
    };
}
