package util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.kys_31.figureinformation.R;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CountDownLatch;

/**
 * Created by 张同心 on 2017/9/25.
 * @function 图片工具类
 */

public class BitmapUtil {

    private static class InitBitmapUtil{
        final static BitmapUtil BITMAPUTL = new BitmapUtil();
    }

    public static BitmapUtil get(){
        return InitBitmapUtil.BITMAPUTL;
    }
    /**
     * 图片的URL转Bitmap
     */
    public String URLToBitmapToString(Context context, String url){
        final String[] strBitmap = {null};
        final CountDownLatch countLatch = new CountDownLatch(1);
        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                strBitmap[0] = bitmapToString(resource);
                countLatch.countDown();
            }
        };
        Glide.with(context).load(url).asBitmap().into(target);
        try {
            countLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return strBitmap[0];
    }

    /**
     * Bitmap转字符串
     * @param bitmap
     * @return 字符串
     */
    public String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, arrayOutputStream);
        byte[] picture = arrayOutputStream.toByteArray();
        return Base64.encodeToString(picture, Base64.DEFAULT);
    }

    /**
     * 字符串转Bitmap
     * @param strBitmap
     * @return Bitmap
     */
    public Bitmap stringToBitmap(String strBitmap){
        Bitmap bitmap = null;
        byte[] bitmapArray;
        bitmapArray = Base64.decode(strBitmap, Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        return bitmap;
    }

    /**
     * 设置网络加载图片
     * @return 是否成功
     */
    public boolean setImageView(ImageView imageView, Context context, String url, String gifAndBitmap){
        if (imageView != null){
            if ( gifAndBitmap.equals("gif")){
                Glide.with(context)
                        .load(url)
                        .asGif()
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.close)
                        .into(imageView);
            }else {
                Glide.with(context)
                        .load(url)
                        .asBitmap()
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.close)
                        .into(imageView);
            }
            return true;
        }else {
            return false;
        }
    }


    public int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                        int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public Bitmap small(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(0.0000000000000001f,0.0000000000000000001f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }
    public Bitmap big(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(100000000000f,1000000000000f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }
}
