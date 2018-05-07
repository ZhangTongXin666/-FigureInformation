package com.example.kys_31.figureinformation;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.Observable;

import view.RoundImageView;
import data.HandleUserMessage;
import data.UserMessage;
import util.BitmapUtil;
import util.PermissionApplyUtil;
import util.TimeUtil;
import util.ViewUtil;
import variable.UserMessageVariable;

/**
 *@author : 老头儿
 *@email : 527672827@qq.com
 *@org : 河北北方学院 移动开发工程部 C508
 *@function : （功能） 个人信息
 */

public class PersionMessageActivity extends BaseActivity {

    private TextView mTvEMail;
    private LinearLayout mLlHead;
    private RoundImageView mRivHead;
    private LinearLayout mLlName;
    private TextView mTvName;
    private LinearLayout mLlSex;
    private TextView mTvSex;
    private LinearLayout mLlBirsday;
    private TextView mTvBirsday;
    private TextView mTvLookCount;
    private TextView mTvDeviceName;
    private Button mBtSaveMessage;
    private LinearLayout mLlTitleBack;
    private TextView mTvTitleName;
    private Bitmap mHeadBitmep;

    @Override
    protected int getLayoutID() {
        return R.layout.persionmessage_activity;
    }

    @Override
    protected void initControl() {
        mTvEMail = (TextView)findViewById(R.id.email_persionMessage_tv);
        mLlHead = (LinearLayout)findViewById(R.id.head_persionMessage_ll);
        mRivHead = (RoundImageView) findViewById(R.id.head_persionMessage_riv);
        mLlName = (LinearLayout)findViewById(R.id.name_persionMessage_ll);
        mTvName = (TextView)findViewById(R.id.name_persionMessage_tv);
        mLlSex = (LinearLayout)findViewById(R.id.sex_persionMessage_ll);
        mTvSex = (TextView)findViewById(R.id.sex_persionMessage_tv);
        mLlBirsday = (LinearLayout)findViewById(R.id.birsday_persionMessage_ll);
        mTvBirsday = (TextView)findViewById(R.id.birsday_persionMessage_tv);
        mBtSaveMessage = (Button)findViewById(R.id.saveMessage_systemSet_bt);
        mLlTitleBack = (LinearLayout)findViewById(R.id.titleBack_persionMessage_ll);
        mTvTitleName = (TextView)findViewById(R.id.titleName_tv);
        mTvDeviceName = (TextView)findViewById(R.id.deviceName_persionMessage_tv);
        mTvLookCount = (TextView)findViewById(R.id.lookCount_persionMessage_tv);
        mTvTitleName.setText("个人信息");
        mTvDeviceName.setText(Build.MODEL);
        mTvLookCount.setText(String.valueOf(UserMessageVariable.osUserMessage.oIntLookCount));
        mTvName.setText(UserMessageVariable.osUserMessage.oStrName);
        mTvSex.setText(UserMessageVariable.osUserMessage.oIntSex == 0?"男":"女");
        mTvBirsday.setText(UserMessageVariable.osUserMessage.oStrBirsday);
        if (UserMessageVariable.osUserMessage.oStrHead.contains("http")){
            Glide.with(this).load(UserMessageVariable.osUserMessage.oStrHead)
                    .asBitmap()
                    .override(60, 60)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(mRivHead);
        }else {
            mRivHead.setImageBitmap(BitmapUtil.get().stringToBitmap(UserMessageVariable.osUserMessage.oStrHead));
        }

        PermissionApplyUtil.applyPermissionCarmera(this);
    }

    @Override
    public void setControlListener() {
        mLlHead.setOnClickListener(this);
        mLlName.setOnClickListener(this);
        mLlSex.setOnClickListener(this);
        mLlBirsday.setOnClickListener(this);
        mBtSaveMessage.setOnClickListener(this);
        mLlTitleBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.titleBack_persionMessage_ll:
                finish();
                break;
            case R.id.head_persionMessage_ll:
                showSelectWay();
                break;
            case R.id.name_persionMessage_ll:
                setName();
                break;
            case R.id.sex_persionMessage_ll:
                setSex();
                break;
            case R.id.birsday_persionMessage_ll:
                setBirsday();
                break;
            case R.id.saveMessage_systemSet_bt:
                saveUserMessage();
                break;
            default:break;
        }
    }

    /**
     * 保存用户信息
     */
    private void saveUserMessage() {
        UserMessageVariable.osUserMessage = new UserMessage(UserMessageVariable.osUserMessage.oStrPhoneNumber,
                UserMessageVariable.osUserMessage.oStrPassword, mHeadBitmep == null?UserMessageVariable.osUserMessage.oStrHead:BitmapUtil.get().bitmapToString(mHeadBitmep),
                mTvName.getText().toString(), mTvBirsday.getText().toString(), mTvEMail.getText().toString(), mTvSex.getText().toString().equals("男")?0:1,
                UserMessageVariable.osUserMessage.oIntLookCount, UserMessageVariable.osUserMessage.oStrUpLookTime);
        HandleUserMessage.saveData(UserMessageVariable.osUserMessage);
        showToast("保存成功", false);
        finish();
    }

    /**
     * 设置生日
     */
    private void setBirsday() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialogBirsday = new DatePickerDialog(PersionMessageActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                mTvBirsday.setText(TimeUtil.filterTime(i+"-"+(i1+1)+"-"+i2));
            }
        },year,month,day);
        dialogBirsday.show();

    }

    /**
     * 设置性别
     */
    private void setSex() {
        final Dialog dialogSetSex = new AlertDialog.Builder(this).create();
        View viewSetSex = LayoutInflater.from(this).inflate(R.layout.setsex_persionmessage_view,null);
        dialogSetSex.show();
        dialogSetSex.setCanceledOnTouchOutside(true);
        dialogSetSex.setContentView(viewSetSex);
        ViewUtil.setDialogWindowAttr(dialogSetSex, 500, 500);
        final RadioButton rbMan = (RadioButton)viewSetSex.findViewById(R.id.sexMan_persionMessage_rb);
        RadioButton rbWoman = (RadioButton)viewSetSex.findViewById(R.id.sexWoman_persionMessage_rb);
        if (UserMessageVariable.osUserMessage.oIntSex==0){
            rbMan.setChecked(true);
        }else {
            rbWoman.setChecked(true);
        }
        if (mTvSex.getText().equals("女")){rbWoman.isChecked();}
        Button btSure = (Button)viewSetSex.findViewById(R.id.sureSex_persionMessage_bt);
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbMan.isChecked()){
                    mTvSex.setText("男");
                }else {
                    mTvSex.setText("女");
                }
                dialogSetSex.dismiss();
            }
        });
    }

    /**
     * 设置姓名
     */
    private void setName() {
        final Dialog dialogSetName = new AlertDialog.Builder(this).create();
        View viewSetName = LayoutInflater.from(this).inflate(R.layout.setname_persionmessage_view,null);
        dialogSetName.show();
        dialogSetName.setCanceledOnTouchOutside(true);
        ViewUtil.setDialogWindowAttr(dialogSetName, 700, 450);
        dialogSetName.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialogSetName.getWindow().setContentView(viewSetName);
        final EditText etSetName = (EditText)viewSetName.findViewById(R.id.setname_persionmessage_tv);
        etSetName.setText(mTvName.getText().toString());
        Button btSure = (Button)viewSetName.findViewById(R.id.sureName_persionMessage_bt);
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvName.setText(etSetName.getText().toString());
                dialogSetName.dismiss();
            }
        });
    }

    /**
     * 选择图片的方式
     */
    private void showSelectWay() {
        final Dialog dialogSelectWay = new AlertDialog.Builder(this).create();
        View viewSelectWay = LayoutInflater.from(this).inflate(R.layout.pictureselectway_persionmessage_view,null);
        dialogSelectWay.show();
        dialogSelectWay.setContentView(viewSelectWay);
        dialogSelectWay.setCanceledOnTouchOutside(true);
        ViewUtil.setDialogWindowAttr(dialogSelectWay, 700, 350);

        LinearLayout llCamera = (LinearLayout)viewSelectWay.findViewById(R.id.camera_persionMessage_ll);
        LinearLayout llPictureJar = (LinearLayout)viewSelectWay.findViewById(R.id.pictureJar_persionMessage_ll);
        LinearLayout llCancle = (LinearLayout)viewSelectWay.findViewById(R.id.cancle_persionMessage_ll);

        llCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSelectWay.dismiss();
            }
        });
        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,0);
                dialogSelectWay.dismiss();
            }
        });
        llPictureJar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pictureJar = new Intent(Intent.ACTION_GET_CONTENT);
                pictureJar.setType("image/*");
                startActivityForResult(pictureJar,1);
                dialogSelectWay.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != RESULT_OK){
            return;
        }else {
            if (requestCode == 1){
                startPhotoZoom(data.getData());
            }else if (requestCode == 0){
                Bundle extra = data.getExtras();
                mHeadBitmep = extra.getParcelable("data");
                mRivHead.setImageBitmap(mHeadBitmep);
            }else {
                if (data != null){
                    Bundle extra = data.getExtras();
                    mHeadBitmep = extra.getParcelable("data");
                    mRivHead.setImageBitmap(mHeadBitmep);
                }
            }
        }
    }

    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    @Override
    public void update(Observable observable, Object o) {
        mLoginState = (Boolean)o;
    }
}
