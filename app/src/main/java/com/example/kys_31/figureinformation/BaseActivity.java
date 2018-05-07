package com.example.kys_31.figureinformation;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.android.debug.hv.ViewServer;

import java.util.Observer;

import util.AutoLoginUtil;
import util.ViewUtil;
import variable.SystemSetVariable;


/**
 *@author : 老头儿
 *@email : 527672827@qq.com
 *@org : 河北北方学院 移动开发工程部 C508
 *@function : （功能） 基类
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, Observer{

    public boolean mLoginState = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*隐藏标题栏*/
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        /*设置状态栏颜色*/
        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#d5a175"));

        int layoutID = getLayoutID();
        if (layoutID != 0){
            setContentView(layoutID);
        }
        ViewServer.get(this).addWindow(this);
        initControl();
        setControlListener();
    }

    /**
     * Toast提示
     * @param content 提示内容
     * @param which true:是长提示 ；false:是短提示
     */
    public void showToast(String content, boolean which){
        Toast.makeText(this,content,which?Toast.LENGTH_LONG:Toast.LENGTH_SHORT).show();
    }

    /**
     * 获得布局ID
     * @return 布局ID
     */
     protected abstract int getLayoutID();

    /**
     * 初始化控件
     */
     protected abstract void initControl();

    /**
     * 设置控件的监听
     */
     public abstract void setControlListener();

    @Override
    public void onStart(){
        super.onStart();
        /*屏幕亮度*/
        SystemSetVariable.osScreenBrightValue = ViewUtil.getScreenBrightness(this);
        if (SystemSetVariable.osNightModel){
            ViewUtil.setScreenBrightness(this, 10);
        }else {
            ViewUtil.setScreenBrightness(this, SystemSetVariable.osScreenBrightValue);
        }
        /*自动登录*/
        AutoLoginUtil.startAutoLogin(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

}
