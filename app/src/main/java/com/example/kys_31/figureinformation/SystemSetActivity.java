package com.example.kys_31.figureinformation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Observable;

import util.AutoLoginUtil;
import util.CacheUtil;
import util.ViewUtil;
import variable.SystemSetVariable;
import variable.UserMessageVariable;
import view.DrawHookViewView;

/**
 * Created by 张同心 on 2017/9/13.
 * @function 系统设置
 */

public class SystemSetActivity extends BaseActivity {

    private LinearLayout mLlBack;
    private TextView mTvTitleName;
    private LinearLayout mLlHandMoveMent;
    private TextView mTvDataSize;
    private Switch mSNightModel;
    private TextView mTvSwitch;
    private LinearLayout mLlCheckUpdate;
    private LinearLayout mLlDoGrade;
    private LinearLayout mLlAbout;
    private Button mBtExitLogin;
    private Button mBtReplaceLogin;

    @Override
    protected int getLayoutID() {
        return R.layout.systemset_activity;
    }

    @Override
    protected void initControl() {
        mLlBack = (LinearLayout)findViewById(R.id.titleBack_persionMessage_ll);
        mLlHandMoveMent =(LinearLayout)findViewById(R.id.handMoveMentClear_systemSet_ll);
        mTvDataSize = (TextView)findViewById(R.id.dataSize_systemSet_tv);
        mSNightModel = (Switch)findViewById(R.id.nightModel_systemSet_s);
        mTvSwitch = (TextView)findViewById(R.id.nightModel_systemSet_tv);
        mLlCheckUpdate = (LinearLayout) findViewById(R.id.checkUpdata_systemSet_ll);
        mLlDoGrade = (LinearLayout)findViewById(R.id.DoGrade_systemSet_ll);
        mLlAbout = (LinearLayout)findViewById(R.id.about_systemSet_ll);
        mBtExitLogin = (Button) findViewById(R.id.exitLogin_systemSet_bt);
        mBtReplaceLogin = (Button)findViewById(R.id.replaceLogin_systemSet_bt);
        mTvTitleName = (TextView)findViewById(R.id.titleName_tv);
        mTvTitleName.setText("系统设置");
        int cacheSize = CacheUtil.initCache().getCount();
        mTvDataSize.setText(""+cacheSize+"KB");
    }

    @Override
    public void setControlListener() {
        mLlBack.setOnClickListener(this);
        mLlHandMoveMent.setOnClickListener(this);
        mLlCheckUpdate.setOnClickListener(this);
        mLlDoGrade.setOnClickListener(this);
        mLlAbout.setOnClickListener(this);
        mBtExitLogin.setOnClickListener(this);
        mBtReplaceLogin.setOnClickListener(this);
        mSNightModel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences nightModelSp = getSharedPreferences("nightModel", 0);
                SharedPreferences.Editor nightEditor = nightModelSp.edit();
                if (b){
                    SystemSetVariable.osNightModel = true;
                    ViewUtil.setScreenBrightness(SystemSetActivity.this, 10);
                    mTvSwitch.setText("开");
                    nightEditor.putBoolean("nightModel", true);
                }else {
                    SystemSetVariable.osNightModel = false;
                    ViewUtil.setScreenBrightness(SystemSetActivity.this,SystemSetVariable.osScreenBrightValue);
                    mTvSwitch.setText("关");
                    nightEditor.putBoolean("nightModel", false);
                }
                nightEditor.commit();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.titleBack_persionMessage_ll:
                finish();
                break;
            case R.id.handMoveMentClear_systemSet_ll:
                handMoveMentClear();
                break;
            case R.id.checkUpdata_systemSet_ll:
                showToast("已是最新版本",true);
                break;
            case R.id.DoGrade_systemSet_ll:
                doGradeAboutApp();
                break;
            case R.id.about_systemSet_ll:
                startActivity(new Intent(this,AboutActivity.class));
                break;
            case R.id.exitLogin_systemSet_bt:
                showExitLoginDialog();
                break;
            case R.id.replaceLogin_systemSet_bt:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;

        }
    }

    private void showExitLoginDialog() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确认退出登录吗？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AutoLoginUtil.cancleAutoLogin(SystemSetActivity.this);
                        dialog.dismiss();
                        System.exit(0);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    /**
     * 为App打分
     */
    private void doGradeAboutApp() {
        if (!UserMessageVariable.mBrShowDoGrade ){
            final Dialog dialogDoGrade = new AlertDialog.Builder(this).create();
            View viewDoGrade = LayoutInflater.from(this).inflate(R.layout.dograde_systemset_view,null);
            dialogDoGrade.show();
            dialogDoGrade.getWindow().setContentView(viewDoGrade);
            dialogDoGrade.setCanceledOnTouchOutside(false);
            ViewUtil.setDialogWindowAttr(dialogDoGrade,1000,800);
            ImageView ivClose = (ImageView)viewDoGrade.findViewById(R.id.close_systemSet_iv);
            ImageView ivSure = (ImageView)viewDoGrade.findViewById(R.id.sure_systemSet_iv);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogDoGrade.dismiss();
                }
            });
            ivSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showToast("感谢您的评价，愿你每天快乐！", true);
                    UserMessageVariable.mBrShowDoGrade = true;
                    dialogDoGrade.dismiss();
                }
            });

        }else {
            showToast("您已经对本次使用打过分了，诚挚感谢！", false);
        }

    }

    /**
     * 手动清理缓存
     */
    private void handMoveMentClear() {
        final Dialog dialogHandMoveMentClear = new AlertDialog.Builder(this).create();
        View viewHandMoveMentClear = LayoutInflater.from(this).inflate(R.layout.handmovement_systemset_view,null);
        dialogHandMoveMentClear.show();
        dialogHandMoveMentClear.getWindow().setContentView(viewHandMoveMentClear);
        dialogHandMoveMentClear.setCanceledOnTouchOutside(false);
        ViewUtil.setDialogWindowAttr(dialogHandMoveMentClear,700,550);
        TextView tvClearSuccess = (TextView)viewHandMoveMentClear.findViewById(R.id.clearSuccess_handMoveMent_tv);
        Button btSure = (Button)viewHandMoveMentClear.findViewById(R.id.sure_systemSet_bt);
        DrawHookViewView dhvcDrawHook = (DrawHookViewView)viewHandMoveMentClear.findViewById(R.id.drawHook_handMoveMent_dhvc);
        dhvcDrawHook.setTextView(tvClearSuccess);
        mTvDataSize.setText("0");
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogHandMoveMentClear.dismiss();
                CacheUtil.initCache().clearCache();
                mTvDataSize.setText("0KB");
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        /*夜间模式*/
        if (SystemSetVariable.osNightModel){
            mSNightModel.setChecked(true);
        }else {
            mSNightModel.setChecked(false);
        }
        /*计算缓存大小*/

    }

    @Override
    public void update(Observable observable, Object o) {
        mLoginState = (Boolean)o;
    }
}
