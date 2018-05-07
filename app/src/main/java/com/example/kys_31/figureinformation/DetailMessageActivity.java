package com.example.kys_31.figureinformation;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import data.CollectEssayMessage;
import data.HandleCollectEssayMessage;
import database.DBUtil;
import interfaces.TimePrompt;
import util.CacheUtil;
import util.SVProgressHUDUtil;
import util.SharedUtil;
import util.StringUtil;
import util.TimeUtil;
import util.ViewUtil;
import variable.ImageUrlVariable;
import variable.LoginStateVariable;
import variable.UserMessageVariable;

import static util.CacheUtil.initCache;

/**
 *@author : 老头儿
 *@email : 527672827@qq.com
 *@org : 河北北方学院 移动开发工程部 C508
 *@function : （功能） 详细信息
 */
public class DetailMessageActivity extends BaseActivity implements View.OnClickListener , TimePrompt {

    //控件
    private TextView tv_title;
    private TextView tv_author;
    private TextView tv_time;
    private TextView tv_clickNumber;
    private LinearLayout ll_back;
    private LinearLayout llContent;
    private ImageView iv_share;
    private ImageView mIvCollection;
    private ImageView iv_voice;
    private ImageView networkImageView;

    private String author;
    private String clickNumber;
    private String content;
    private SpeechSynthesizer mySynthesizer;
    private boolean playing=false;
    private boolean played=false;
    private String URL;
    private String intentTitle;
    private String intentTime;
    private String pictureURL;
    private TextView tv_content;
    private boolean isCollection=false;
    private float dpToPx;
    private TextView newTextView;
    private ScrollView mScView;
    private boolean mBrNightModel = false;
    private Snackbar mSnackbar;
    private List<Integer> mList = new ArrayList<>();
    private List<HashMap<String, String>> cacheList;
    private static final String TAG = "DetailMessageActivity";
    private TextView text;

    private Handler viewHandler=new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    SVProgressHUDUtil.dismiss(DetailMessageActivity.this);
                    showTitle();
                    break;
                case 1:
                    SVProgressHUDUtil.showErrorWithStatus(DetailMessageActivity.this, "连接超时", SVProgressHUDUtil.SVProgressHUDMaskType.ClearCancel);
                    break;
                default:break;
            }
        }
    };

    @Override
    protected int getLayoutID() {
        return R.layout.main;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void initControl() {
        mScView = (ScrollView)findViewById(R.id.sv_scrollView);
        mIvCollection = (ImageView)findViewById(R.id.collection_DetailMessage_iv);
        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_author=(TextView)findViewById(R.id.tv_author);
        tv_time=(TextView)findViewById(R.id.tv_time);
        tv_clickNumber=(TextView)findViewById(R.id.tv_clickNumber);
        iv_share=(ImageView)findViewById(R.id.iv_share);
        ll_back=(LinearLayout)findViewById(R.id.ll_back);
        iv_voice=(ImageView)findViewById(R.id.iv_voice);
        networkImageView=(ImageView)findViewById(R.id.network_img);
        text=(TextView)findViewById(R.id.text);
        tv_content=(TextView)findViewById(R.id.tv_content);
        llContent = (LinearLayout)findViewById(R.id.ll_content);
        Intent intent = getIntent();
        URL = intent.getStringExtra("URL");
        intentTitle = intent.getStringExtra("title");
        intentTime = intent.getStringExtra("timeandauthor");
        dpToPx = getResources().getDisplayMetrics().density;

        mScView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY - oldScrollY > 50 ){
                    if (mSnackbar == null){
                        mSnackbar =  Snackbar.make(llContent,  "夜间模式", 2500);
                        mSnackbar.setAction("启动", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mBrNightModel = !mBrNightModel;
                                if (mBrNightModel){
                                    ViewUtil.setScreenBrightness(DetailMessageActivity.this, 10);
                                    mSnackbar.setText("日常模式");
                                    mSnackbar.setActionTextColor(Color.WHITE);
                                }
                                else{
                                    ViewUtil.setScreenBrightness(DetailMessageActivity.this, 150);
                                    mSnackbar.setText("夜间模式");
                                    mSnackbar.setActionTextColor(Color.BLUE);
                                }
                            }
                        });
                        mSnackbar.setActionTextColor(Color.BLUE);
                    }
                    mSnackbar.show();
                }
            }
        });

        if (LoginStateVariable.osLoginState){
            if (HandleCollectEssayMessage.essayExist(intentTitle)){
                isCollection = true;
                mIvCollection.setImageResource(R.drawable.collection);
            }else {
                isCollection = false;
                mIvCollection.setImageResource(R.drawable.nocollection);
            }
        }

       cacheList = CacheUtil.initCache().getFromCache(URL);
        if (cacheList == null || cacheList.size() < 1){
            cacheList = new ArrayList<>();
            CreateThread();
        }else {
            author = cacheList.get(0).get("author");
            clickNumber = cacheList.get(0).get("clickNumber");
            content = cacheList.get(0).get("content");
            pictureURL = cacheList.get(0).get("pictureURL");
            showTitle();
        }
    }


    private void showPicture(){
        if (pictureURL.length()<10){
            networkImageView.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
        }else {
            try {
                Glide.with(this).load(pictureURL)
                        .asBitmap()
                        .error(R.drawable.picture_error)
                        .placeholder(R.drawable.picture_load)
                        .into(networkImageView);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public void setControlListener() {
        iv_share.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        iv_voice.setOnClickListener(this);
        mIvCollection.setOnClickListener(this);
    }

    private void showTitle(){
        showPicture();
        tv_title.setText(intentTitle);
        tv_author.setText("作者："+author);
        tv_time.setText("发表时间："+ TimeUtil.formatTimeOne(intentTime).substring(0,10));
        tv_clickNumber.setText("点击量："+clickNumber);
        String[] newStringArray = StringUtil.splitStringContent(content);
        if (newStringArray != null && newStringArray.length > 5){
            for (int i = 0; i < newStringArray.length; i++) {
                if (i >= 0 && i < 5){
                    tv_content.append(newStringArray[i]);
                }else {
                    if (i % 5 == 0){
                        createPicture(createImageView());
                        newTextView = createTextView();
                    }
                    newTextView.append(newStringArray[i]);
                }
            }
        }else {
            tv_content.setText(content);
        }
    }

    /*创建ImageView*/
    private ImageView createImageView(){
        final ImageView newImageView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (200 * dpToPx));
        params.setMargins(0, (int) (10 * dpToPx), 0, 0);
        newImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        newImageView.setLayoutParams(params);
        llContent.addView(newImageView);
        TextView newTextView = new TextView(this);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params1.setMargins(0, 0, 0, (int) (10 * dpToPx));
        newTextView.setLayoutParams(params1);
        newTextView.setGravity(Gravity.CENTER);
        newTextView.setText("图片来源于图片资讯");
        newTextView.setTextColor(getResources().getColor(R.color.gray));
        llContent.addView(newTextView);
        return newImageView;
    }

    private void createPicture(ImageView imageView) {
        int random = new Random().nextInt(ImageUrlVariable.mStrUrl.length-1);
        if (!mList.contains(random)){
            mList.add(random);
            try {
                Glide.with(DetailMessageActivity.this).load(ImageUrlVariable.mStrUrl[random])
                        .asBitmap()
                        .placeholder(R.drawable.picture_load)
                        .error(R.drawable.picture_error)
                        .into(imageView);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            createPicture(imageView);
        }
    }

    /*创建TextView*/
    private TextView createTextView(){
        TextView newTextView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins((int) (10 * dpToPx), 0, (int) (10 * dpToPx), 0);
        newTextView.setLayoutParams(params);
        newTextView.setTextSize(20);
        newTextView.setTextColor(Color.BLACK);
        llContent.addView(newTextView);
        return newTextView;
    }


    private void CreateThread() {
        SVProgressHUDUtil.showWithStatus(this, "加载中。。。");
        final HashMap<String, String> firstMap = new HashMap<>();
        Thread thread=new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Document doc = Jsoup.connect(URL).timeout(5000).get();
                    Elements authorElement = doc.body().select("table").select("td");
                    Elements clickNumberElement = doc.body().select("table").select("td");

                    if (authorElement.size() == 0){
                        author = "无作者";
                    }else {
                        author=doc.body().select("table").select("td").get(3).text();
                    }

                    if (clickNumberElement.size() == 0){
                        clickNumber =  "暂无点击量";
                    }else {
                        clickNumber=doc.body().select("table").select("td").get(7).text();
                    }
                    content=doc.body().select("p").text();
                    if (content.length() < 25){
                        Log.w("TAG", "titile:"+intentTitle);
                        content = DBUtil.getInstance(DetailMessageActivity.this).getCardContentByTitle(intentTitle);
                    }
                    try {
                        pictureURL="http://portal.nstl.gov.cn"+doc.select("div.zoom.mt-15").select("img").attr("src").substring(0,doc.select("div.zoom.mt-15").select("img").attr("src").indexOf(".jpg")+4);
                    }catch (Exception e){
                        pictureURL = "0";
                        Log.e("TAG", "图片URL获取失败："+e.getMessage());
                    }
                    firstMap.put("author", author);
                    firstMap.put("clickNumber", clickNumber);
                    firstMap.put("pictureURL", pictureURL);
                    firstMap.put("content", content);
                    cacheList.add(firstMap);
                    viewHandler.sendEmptyMessage(0);
                } catch (IOException e) {
                    viewHandler.sendEmptyMessage(1);
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void speakTextMethod(String str) {
        SpeechUtility.createUtility(DetailMessageActivity.this, "appid=5760ba33");
        mySynthesizer = SpeechSynthesizer.createSynthesizer(this, myInitListener);
        mySynthesizer.setParameter(SpeechConstant.VOICE_NAME,"xiaoyan");
        mySynthesizer.setParameter(SpeechConstant.PITCH,"50");
        mySynthesizer.setParameter(SpeechConstant.VOLUME,"50");
        mySynthesizer.startSpeaking(str,mTtsListener);
    }


    private com.iflytek.cloud.SynthesizerListener mTtsListener = new com.iflytek.cloud.SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
        }
        @Override
        public void onSpeakPaused() {
        }
        @Override
        public void onSpeakResumed() {
        }
        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
        }
        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        @Override
        public void onCompleted(SpeechError error) {
            if(error!=null) {
                Log.d("mySynthesiezer complete code:", error.getErrorCode()+"");
            }
            else {
                Log.d("mySynthesiezer complete code:", "0");
            }
        }
        @Override
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
            // TODO Auto-generated method stub

        }
    };


    private InitListener myInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d("mySynthesiezer:", "InitListener init() code = " + code);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.iv_voice:
                HandleVoice();
                break;
            case R.id.iv_share:
                setAnim();
                SharedUtil.showShare(DetailMessageActivity.this, pictureURL, intentTitle, URL, content, R.drawable.logo);
                break;
            case R.id.collection_DetailMessage_iv:
                handleCollection();
                break;
        }
    }

    private void setAnim(){
        Animation shareAnim = AnimationUtils.loadAnimation(this, R.anim.share_degrees);
        shareAnim.setInterpolator(new LinearInterpolator());
        iv_share.startAnimation(shareAnim);
    }

    /*处理语音播报*/
    private void HandleVoice() {
        if (playing){
            iv_voice.setBackgroundResource(R.drawable.play);
            mySynthesizer.pauseSpeaking();
            playing=false;
        }else {
            if (!played){
                speakTextMethod(content);
                played=true;
            }else {
                mySynthesizer.resumeSpeaking();
            }
            iv_voice.setBackgroundResource(R.drawable.pause);
            playing=true;
        }
    }

    /*处理收藏*/
    private void handleCollection() {
        if (LoginStateVariable.osLoginState){
            if (isCollection){
                SVProgressHUDUtil.showErrorWithStatus(this, "取消收藏", SVProgressHUDUtil.SVProgressHUDMaskType.GradientCancel);
                mIvCollection.setImageResource(R.drawable.nocollection);
                isCollection = false;
            }else {
                mIvCollection.setImageResource(R.drawable.collection);
                isCollection = true;
                showToast("收藏成功", false);
            }
        }else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void update(Observable observable, Object o) {

    }

    @Override
    public void onPause(){
        super.onPause();
        if (LoginStateVariable.osLoginState){
            if (isCollection){
                if (!HandleCollectEssayMessage.essayExist(intentTitle)){
                    CollectEssayMessage essayContentMessage = new CollectEssayMessage(intentTitle, author, intentTime, clickNumber, content, URL, pictureURL, UserMessageVariable.osUserMessage.oStrPhoneNumber);
                    HandleCollectEssayMessage.saveData(essayContentMessage);
                }
            }else {
                if (HandleCollectEssayMessage.essayExist(intentTitle)){
                    HandleCollectEssayMessage.deleteSingleEssay(intentTitle);
                }
            }
        }
        if (SVProgressHUDUtil.isShowing(this)){
            SVProgressHUDUtil.dismiss(this);
        }
        if (CacheUtil.initCache().getFromCache(URL) == null){
            CacheUtil.initCache().saveToCache(URL, cacheList);
        }
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void showTimeDialog() {
        Dialog dialog = new AlertDialog.Builder(DetailMessageActivity.this)
                .setTitle("护眼提示")
                .setMessage("您已连续浏览新闻半个小时了\n图情资讯提示您保护眼睛")
                .setPositiveButton("继续浏览", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
