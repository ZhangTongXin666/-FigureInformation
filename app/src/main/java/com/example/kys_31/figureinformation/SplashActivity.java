package com.example.kys_31.figureinformation;

import android.Manifest;
import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import dataBean.WheatherBean2;
import util.GetCityNameByLocation;
import variable.PersionCenterVariable;
import view.CircleProgressBar;

/**
 * Created by kys_7 on 2017/11/23.
 */

public class SplashActivity extends AppCompatActivity{
    private ImageView imageView_gif;
    private CircleProgressBar bar1;
    private Timer timer;
    private int mIntProgress = 100;
    private ImageView mIvLogo;
    private ImageView mIvBottom;
    private String API_KEY="60840102c71a2cb8c0f050fb54d44ca3";//聚合数据KEY
    private String URL="http://v.juhe.cn/weather/index?format=1";//聚合数据天气接口

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        ActionBar bar=getSupportActionBar();
        bar.hide();
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        bar1 = (CircleProgressBar)findViewById(R.id.bar1);
        mIvLogo = (ImageView)findViewById(R.id.iv_logo);
        mIvBottom = (ImageView)findViewById(R.id.iv_bottom);
        startAnim();
        try{
           // checkPromiss();
           // getLocation();
        }catch (Exception e){
            System.out.print(e);
        }

        bar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                timer.cancel();
            }
        });

        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mIntProgress -=10;
                bar1.setProgress(mIntProgress);
                if (mIntProgress<=0){
                    Intent intent= new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask, 1000, 500);

    }

    private void startAnim() {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(3000);
        mIvBottom.startAnimation(anim);
        mIvLogo.startAnimation(anim);
    }


    private void checkPromiss(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String permission1 = Manifest.permission.ACCESS_COARSE_LOCATION;
            String permission2 = Manifest.permission.ACCESS_FINE_LOCATION;
            requestPermissions(new String[]{permission1,permission2}, 123);
        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    String cityName = (String) msg.obj;
                    PersionCenterVariable.location=cityName;
                    Log.e("所在城市",cityName);
                    QueryWheather();
                    break;
                case 500:
                    PersionCenterVariable.location="张家口";
                    QueryWheather();
                    break;
            }
        }
    };

    private void QueryWheather() {

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                String url = setParams(PersionCenterVariable.location);
                String res = null;
                try {
                    res = doGet(url);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                //聚合数据
                WheatherBean2 wheatherBean=new Gson().fromJson(res,WheatherBean2.class);
                WheatherBean2.ResultBean resultBean=wheatherBean.getResult();
                WheatherBean2.ResultBean.TodayBean today=resultBean.getToday();
                PersionCenterVariable.weather=today.getWeather();
                PersionCenterVariable.temperature=today.getTemperature();


            }
        });
        thread.start();

    }

    private void getLocation() {
        GetCityNameByLocation.startLocation(SplashActivity.this, true, new GetCityNameByLocation.CallBack() {
            @Override
            public void onGetLocaltionSuccess(String cityName) {
                Message msg = new Message();
                msg.what = 200;
                msg.obj = cityName;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onGetLocaltionFail(GetCityNameByLocation.LocErrorType type) {
                mHandler.sendEmptyMessage(500);
            }
        });
    }


    private String doGet(String urlStr)throws IOException {
        java.net.URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        url = new URL(urlStr);//根据地址创建URL对象
        conn = (HttpURLConnection) url.openConnection();//打开网络链接
        conn.setReadTimeout(5 * 1000);//设置读取超时时间
        conn.setConnectTimeout(5 * 1000);//设置连接超时时间
        conn.setRequestMethod("GET");//设置请求方式为GET
        String jsonString;
        if (conn.getResponseCode() == 200) {//结果码为200，表示连接成功
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));//获取响应的输入流对象
            String line;
            final StringBuffer responseResult = new StringBuffer();
            while ((line = reader.readLine()) != null) {//读取信息
                responseResult.append("\n").append(line);
            }
            jsonString = responseResult.toString();
        } else {
            jsonString = "null";
        }
        return jsonString;
    }

    private String setParams(String province) {
        // return URL + "?cityname=" + province + "&key=" + API_KEY;//阿凡达数据
        return URL + "&cityname=" + province + "&key=" + API_KEY;//聚合数据
    }

}
