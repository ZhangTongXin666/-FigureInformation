package com.example.kys_31.figureinformation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Observable;

/**
 *@author : 老头儿
 *@email : 527672827@qq.com
 *@org : 河北北方学院 移动开发工程部 C508
 *@function : （功能） 关于
 */

public class AboutActivity extends BaseActivity {

    private LinearLayout mLlBack;
    private TextView mTvTitleName;
    private Context context = AboutActivity.this;

    @Override
    protected int getLayoutID() {
        return R.layout.aboutapp_view;
    }

    @Override
    protected void initControl() {
        mLlBack = (LinearLayout) findViewById(R.id.titleBack_persionMessage_ll);
        mTvTitleName = (TextView) findViewById(R.id.titleName_tv);
        mTvTitleName.setText("关于图情资讯");

    }



    @Override
    public void setControlListener() {
        mLlBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    @Override
    public void update(Observable observable, Object o) {
        mLoginState = (Boolean)o;
    }
}
