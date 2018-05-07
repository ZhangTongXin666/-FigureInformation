package util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.kys_31.figureinformation.R;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

/**
 * Created by 张同心 on 2017/9/21.
 * @function 分享功能
 */

public class SharedUtil {

    /**
     * 分享
     * @param context 上下文环境
     */
    public static void showShare(final Context context, final String pictureURL, final String title, final String contentURL, final String content, final int drawableID) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setSite(context.getString(R.string.app_name));
        oks.setSilent(true);

        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
                if("QQ".equals(platform.getName())){
                    paramsToShare.setImageUrl(pictureURL);//图片的URL
                    paramsToShare.setTitle(title); //标题
                    paramsToShare.setTitleUrl(contentURL); //链接地址
                    paramsToShare.setText(content);//内容
                }
                else if ("QZone".equals(platform.getName())) {
                    Bitmap imageData = BitmapFactory.decodeResource(context.getResources(), drawableID);
                    paramsToShare.setImageData(imageData);
                    paramsToShare.setTitle(title); //标题
                    paramsToShare.setTitleUrl(contentURL); //链接地址
                    paramsToShare.setText(content);//内容
                }
                else  if ("SinaWeibo".equals(platform.getName())) {
                    paramsToShare.setImageUrl(pictureURL);
                    paramsToShare.setTitle(title);
                    paramsToShare.setUrl(contentURL);
                    paramsToShare.setShareType(0);
                }
                else if ("Wechat".equals(platform.getName())) {
                    paramsToShare.setUrl(contentURL);
                    paramsToShare.setTitle(title);
                    paramsToShare.setText(content);
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                }
                else if ("WechatMoments".equals(platform.getName())) {
                    paramsToShare.setUrl(contentURL);
                    paramsToShare.setTitle(title);
                    paramsToShare.setText(content);
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                }else if ("WechatFavorite".equals(platform.getName())){
                    paramsToShare.setUrl(contentURL);
                    paramsToShare.setTitle(title);
                    paramsToShare.setText(content);
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                }
            }
        });
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                platform.isClientValid();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                throwable.getMessage();
                throwable.printStackTrace();
                Log.w("TAG", "错误信息："+throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        // 启动分享GUI
        oks.show(context);
    }
}
