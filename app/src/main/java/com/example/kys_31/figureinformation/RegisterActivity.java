package com.example.kys_31.figureinformation;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.myfirstjar.Utils.NoteVerify;
import com.example.myfirstjar.intefaces.NoteVerifyInterface;

import java.util.Observable;

import data.HandleUserMessage;

/**
 *@author : 老头儿
 *@email : 527672827@qq.com
 *@org : 河北北方学院 移动开发工程部 C508
 *@function : （功能） 注册
 */

public class RegisterActivity extends BaseActivity {

    private EditText mEtPhoneNumber;
    private EditText mEtVerificationCode;
    private Button mBtGetVerificationCode;
    private Button mBtNext;
    private int mNoteCode;
    private NoteVerifyInterface mNoteVerify;
    private ImageView mIvClose;
    private String mStrAim = "";

    @Override
    protected int getLayoutID() {
        return R.layout.register_activity;
    }

    @Override
    protected void initControl() {
        mEtPhoneNumber = (EditText)findViewById(R.id.phoneNumber_register_et);
        mBtGetVerificationCode = (Button)findViewById(R.id.getVerificationCode_register_bt);
        mEtVerificationCode = (EditText)findViewById(R.id.verificationCode_register_et);
        mBtNext = (Button)findViewById(R.id.next_register_bt);
        mIvClose = (ImageView)findViewById(R.id.close_register_iv);
        mStrAim = getIntent().getStringExtra("aim");
    }

    @Override
    public void setControlListener() {
        mIvClose.setOnClickListener(this);
        mBtNext.setOnClickListener(this);
        mBtGetVerificationCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.close_register_iv:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.next_register_bt:
                nextStep();
                break;
            case R.id.getVerificationCode_register_bt:
                getVerificationCode();
                break;
            default:break;
        }
    }

    /**
     * 下一步
     */
    private void nextStep() {
        String phoneNumber = mEtPhoneNumber.getText().toString();
        if (!TextUtils.isEmpty(phoneNumber)){
            if (mStrAim.equals("忘记密码")){
                if (HandleUserMessage.userExist(phoneNumber)){
                    Intent intent = new Intent(this, SetPasswrodActivity.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("aim", mStrAim);
                    startActivity(intent);
                    finish();
                }else {
                    showToast("该手机号从未注册过！", false);
                }
            }else {
                if (!HandleUserMessage.userExist(phoneNumber)){
                    if (mNoteVerify !=null && mNoteVerify.MatePhoneNumberAndVerifyNumber(phoneNumber, mNoteCode)){
                        Intent intent = new Intent(this, SetPasswrodActivity.class);
                        intent.putExtra("phoneNumber", phoneNumber);
                        intent.putExtra("aim", mStrAim);
                        startActivity(intent);
                        finish();
                    }else {
                        showToast("请输入正确的验证码",false);
                    }
                }else {
                    showToast("该手机号已经注册", false);
                }
            }
        }else {
            showToast("手机号不能为空", false);
        }

    }

    /**
     * 获取验证码
     */
    public void getVerificationCode() {
        String phoneNumber = mEtPhoneNumber.getText().toString();
        mNoteVerify =  NoteVerify.getInitialize(RegisterActivity.this);
        mNoteCode = mNoteVerify.createVerifyNumber(phoneNumber, "图情资讯");
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
