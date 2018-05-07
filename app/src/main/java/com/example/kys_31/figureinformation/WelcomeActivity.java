package com.example.kys_31.figureinformation;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import dataBean.WheatherBean;
import dataBean.WheatherBean2;
import fragment.GuildFragment;
import util.GetCityNameByLocation;
import variable.PersionCenterVariable;
import variable.UserMessageVariable;
import view.CircleProgressBar;

public class WelcomeActivity extends BaseActivity {
    private ViewPager vp;
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private List<Fragment> fragments;
    private Button bt_start;
    private String API_KEY="60840102c71a2cb8c0f050fb54d44ca3";//聚合数据KEY
    private String URL="http://v.juhe.cn/weather/index?format=1";//聚合数据天气接口

    @Override
    protected int getLayoutID() {
        return R.layout.activity_welcome;
    }

    private void assignViews() {
        vp = (ViewPager) findViewById(R.id.vp);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        bt_start = (Button) findViewById(R.id.bt_start);
    }

    @Override
    protected void initControl() {
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);

        /*判断是否是首次登录*/
        try{
            if (UserMessageVariable.osFirstLogin != 0){
                Intent intent= new Intent(WelcomeActivity.this,SplashActivity.class);
                startActivity(intent);
                finish();

            }
        }catch (Exception e){
            System.out.print(e);
        }

        assignViews();
        initData();
        initView();
     //  checkPromiss();
    //    getLocation();
        bt_start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent= new Intent(WelcomeActivity.this,SplashActivity.class);
                startActivity(intent);
                finish();
            }
        });

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
        GetCityNameByLocation.startLocation(WelcomeActivity.this, true, new GetCityNameByLocation.CallBack() {
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
        return URL + "&cityname=" + province + "&key=" + API_KEY;//聚合数据
    }

    @Override
    public void setControlListener() {

    }

    /**
     * 初始化数据,添加三个Fragment
     */
    private void initData() {
        fragments = new ArrayList<>();

        Fragment fragment1 = new GuildFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("index", 1);
        fragment1.setArguments(bundle1);
        fragments.add(fragment1);

        Fragment fragment2 = new GuildFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("index", 2);
        fragment2.setArguments(bundle2);
        fragments.add(fragment2);

        Fragment fragment3 = new GuildFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putInt("index", 3);
        fragment3.setArguments(bundle3);
        fragments.add(fragment3);
    }

    /**
     * 设置ViewPager的适配器和滑动监听
     */
    private void initView()
    {
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(new MyPageAdapter(getSupportFragmentManager()));
        vp.addOnPageChangeListener(new MyPageChangeListener());
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void update(Observable o, Object arg) {

    }

    /**
     * ViewPager适配器
     */
    private class MyPageAdapter extends FragmentPagerAdapter
    {
        public MyPageAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            return fragments.get(position);
        }

        @Override
        public int getCount()
        {
            return fragments.size();
        }
    }

    /**
     * ViewPager滑动页面监听器
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {

        }

        /**
         * 根据页面不同动态改变红点和在最后一页显示立即体验按钮
         *
         * @param position
         */
        @Override
        public void onPageSelected(int position)
        {
            bt_start.setVisibility(View.GONE);
            iv1.setImageResource(R.mipmap.dot_normal);
            iv2.setImageResource(R.mipmap.dot_normal);
            iv3.setImageResource(R.mipmap.dot_normal);

            if (position == 0){
                iv1.setImageResource(R.mipmap.dot_focus);
            }else if (position == 1){
                iv2.setImageResource(R.mipmap.dot_focus);
            }else {
                iv3.setImageResource(R.mipmap.dot_focus);
                bt_start.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    }

}
