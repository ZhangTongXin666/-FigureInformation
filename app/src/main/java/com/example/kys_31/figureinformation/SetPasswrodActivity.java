package com.example.kys_31.figureinformation;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Observable;

import data.HandleUserMessage;
import data.UserMessage;
import util.AutoLoginUtil;
import util.BitmapUtil;
import variable.LoginStateVariable;
import variable.UserMessageVariable;

/**
 *@author : 老头儿
 *@email : 527672827@qq.com
 *@org : 河北北方学院 移动开发工程部 C508
 *@function : （功能） 设置密码
 */

public class SetPasswrodActivity extends BaseActivity {

    private TextView mTvBackRegister;
    private EditText mEtSetPassword;
    private EditText mEtSurePassword;
    private Button mBtSuccess;

    private String mStrPhoneNumber;

    @Override
    protected int getLayoutID() {
        return R.layout.setpassword_activity;
    }

    @Override
    protected void initControl() {
        mTvBackRegister = (TextView)findViewById(R.id.backRegister_setPassword_tv);
        mEtSetPassword = (EditText)findViewById(R.id.setPassword_setPassword_et);
        mEtSurePassword = (EditText)findViewById(R.id.surePassword_setPassword_et);
        mBtSuccess = (Button)findViewById(R.id.success_setPassword_bt);
        mStrPhoneNumber = getIntent().getStringExtra("phoneNumber");
    }

    @Override
    public void setControlListener() {
        mTvBackRegister.setOnClickListener(this);
        mBtSuccess.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backRegister_setPassword_tv:
                startActivity(new Intent(this,RegisterActivity.class));
                finish();
                break;
            case R.id.success_setPassword_bt:
                String password = mEtSetPassword.getText().toString();
                if (password.equals(mEtSurePassword.getText().toString())){
                    UserMessageVariable.osUserMessage = new UserMessage(mStrPhoneNumber, password, BitmapUtil.get().bitmapToString(BitmapFactory.decodeResource(getResources(), R.drawable.logo)), "未设置姓名", "0", "qwertyuioasdfghjklzxcvbnmqweadg", 0, 0,"首次使用");
                    HandleUserMessage.saveData(UserMessageVariable.osUserMessage);
                    LoginStateVariable.osLoginState = true;
                    AutoLoginUtil.autoLogin(this);
                    finish();//注册完毕
                }else {
                    showToast("两次输入密码不同", false);
                    mEtSetPassword.setText("");
                    mEtSurePassword.setText("");
                }
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void update(Observable observable, Object o) {
        mLoginState = (Boolean)o;
    }
}
