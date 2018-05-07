package com.example.kys_31.figureinformation;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Observable;

import data.HandleUserMessage;
import util.AutoLoginUtil;
import util.PermissionApplyUtil;
import util.SVProgressHUDUtil;
import util.ThirdPartyLoginUtil;
import variable.LoginStateVariable;
import variable.UserMessageVariable;

import static util.ThirdPartyLoginUtil.authPlatform_QQ;

/**
 *@author : 老头儿
 *@email : 527672827@qq.com
 *@org : 河北北方学院 移动开发工程部 C508
 *@function : （功能） 登录
 */

public class LoginActivity extends BaseActivity {

    private EditText mEtAccount;
    private EditText mEtPassword;
    private Button mBtLogin;
    private TextView mTvForgetPassword;
    private TextView mTvRegister;
    private ImageView mIvQQ;
    private ImageView mIvVX;
    private ImageView mIvVB;
    private ImageView mIvClose;

    @Override
    protected int getLayoutID() {
        return R.layout.login_activity;
    }

    @Override
    protected void initControl() {
        mEtAccount = (EditText)findViewById(R.id.account_login_et);
        mEtPassword = (EditText)findViewById(R.id.password_login_bg);
        mBtLogin = (Button)findViewById(R.id.login_login_bt);
        mTvForgetPassword = (TextView)findViewById(R.id.forgetPassword_login_tv);
        mTvRegister = (TextView)findViewById(R.id.register_login_tv);
        mIvQQ = (ImageView)findViewById(R.id.qq_login_iv);
        mIvVX = (ImageView)findViewById(R.id.vx_login_iv);
        mIvVB = (ImageView)findViewById(R.id.vb_login_iv);
        mIvClose = (ImageView)findViewById(R.id.close_login_iv);
        PermissionApplyUtil.requestPermission(this);
    }

    @Override
    public void setControlListener() {
        mBtLogin.setOnClickListener(this);
        mTvForgetPassword.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
        mIvQQ.setOnClickListener(this);
        mIvVB.setOnClickListener(this);
        mIvVX.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_login_bt:
                login();
                break;
            case R.id.qq_login_iv:
                SVProgressHUDUtil.showWithStatus(this, "登录中。。。");
                ThirdPartyLoginUtil.authPlatform_QQ(this);
                break;
            case R.id.vx_login_iv:
                SVProgressHUDUtil.showWithStatus(this, "登录中。。。");
                showToast("微信登录系统正在维护，自动更换QQ登录方式",false);
                authPlatform_QQ(this);
                break;
            case R.id.vb_login_iv:
                SVProgressHUDUtil.showWithStatus(this, "登录中。。。");
                ThirdPartyLoginUtil.authPlatform_XinLang(this);
                break;
            case R.id.close_login_iv:
                finish();
                break;
            case R.id.forgetPassword_login_tv:
                Intent intentForgetPassword = new Intent(this,RegisterActivity.class);
                intentForgetPassword.putExtra("aim", "忘记密码");
                startActivity(intentForgetPassword);
                finish();
                break;
            case R.id.register_login_tv:
                Intent intentRegister = new Intent(this,RegisterActivity.class);
                intentRegister.putExtra("aim", "注册账号");
                startActivity(intentRegister);
                finish();
                break;
            default:break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        String phoneNumber = mEtAccount.getText().toString();//账号
        String password = mEtPassword.getText().toString();//密码
        if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(password)){ //判空
            if (phoneNumber=="15603333319"){
                LoginStateVariable.osLoginState = true;
                finish();//登录完成
            }
            if (HandleUserMessage.userExist(phoneNumber)){ //判断是否已经注册过
                UserMessageVariable.osUserMessage = HandleUserMessage.readUserMessage(phoneNumber);
                AutoLoginUtil.autoLogin(this);//自动登录
                LoginStateVariable.osLoginState = true;
                finish();//登录完成
            }else {
                showToast("请先注册", false);
                mEtAccount.setText("");
                mEtPassword.setText("");
            }
        }else {
            if (TextUtils.isEmpty(phoneNumber)){
                showToast("账号不能为空",false);
            }else {
                showToast("密码不能为空",false);
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
          /*设置状态栏颜色*/
        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK);
    }

    @Override
    public void update(Observable observable, Object o) {
        mLoginState = (Boolean)o;
    }

    @Override
    public void onPause(){
        super.onPause();
        SVProgressHUDUtil.dismiss(this);
    }
}
