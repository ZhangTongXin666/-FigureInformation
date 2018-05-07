package com.example.kys_31.figureinformation;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import adapter.FirstPageAdapter;
import data.ClassDataMessage;
import data.HandleClassDataMessage;
import database.DBUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import util.CacheUtil;
import util.SVProgressHUDUtil;
import util.StringUtil;
import variable.ImageUrlVariable;
import variable.LoginStateVariable;
import variable.RemeberRandomIntVariable;
import variable.UserMessageVariable;

import static variable.ImageUrlVariable.mStrUrl;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class HotSpecialActivity extends BaseActivity {
    private ListView mListView;
    private TextView mTvUpdataTime;
    private TextView mTvSpecialName;
    private String mStrSpecialUrl;
    private List<HashMap<String, String>> listItemContnet = new ArrayList<>();
    private FirstPageAdapter adapter;
    private static final String SPECIAL_URL = "spcialurl";
    private TextView mTvTitleName;
    private LinearLayout mLlBack;
    private ImageView mSpecialBg;
    private ImageView mIvAttentionState;
    private String mStrUpdateTime;
    private String mStrShowTitle;
    private List<HashMap<String, String>> cacheList;
    private boolean mBrAttention = false;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    SVProgressHUDUtil.dismiss(HotSpecialActivity.this);
                    showData();
                    break;
                default:break;
            }
            return true;
        }
    });

    @Override
    protected int getLayoutID() {
        return R.layout.hotspecial_activity;
    }

    @Override
    protected void initControl() {
        mListView = (ListView)findViewById(R.id.lv_hotspecialEssay);
        mTvSpecialName = (TextView)findViewById(R.id.tv_specialName);
        mTvUpdataTime = (TextView)findViewById(R.id.tv_updataTime);
        mTvTitleName = (TextView)findViewById(R.id.titleName_tv);
        mLlBack = (LinearLayout) findViewById(R.id.titleBack_persionMessage_ll);
        mSpecialBg = (ImageView)findViewById(R.id.iv_specialBg);
        mIvAttentionState = (ImageView)findViewById(R.id.iv_attentionState);
        mStrSpecialUrl = getIntent().getStringExtra(SPECIAL_URL);
        mTvTitleName.setText("专题文章");
        if (!RemeberRandomIntVariable.osPicture.equals("0")){
            Glide.with(this).load(RemeberRandomIntVariable.osPicture).asBitmap()
                    .placeholder(R.drawable.picture_load)
                    .error(R.drawable.picture_error)
                    .into(mSpecialBg);
        }else {
            RemeberRandomIntVariable.osPicture = mStrUrl[new Random().nextInt(mStrUrl.length-1)];
            Glide.with(this).load(RemeberRandomIntVariable.osPicture).asBitmap()
                    .placeholder(R.drawable.picture_load)
                    .error(R.drawable.picture_error)
                    .into(mSpecialBg);
        }
        loadData();
        showData();
    }

    /*加载数据*/
    private void loadData() {
        cacheList = CacheUtil.initCache().getFromCache(mStrSpecialUrl);
        if (cacheList == null || cacheList.size() < 1){
            SVProgressHUDUtil.showWithStatus(this, "搜素中");

            cacheList = new ArrayList<>();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10,TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .build();
                    final Request request = new Request.Builder().url(mStrSpecialUrl).build();
                    Response response = null;
                    try {
                        response = client.newCall(request).execute();
                        parseHtml(response.body().string(), "编译报道");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else {
            mStrShowTitle = cacheList.get(0).get("title");
            mStrUpdateTime = cacheList.get(0).get("time");
            listItemContnet.clear();
            for (int i = 1; i < cacheList.size(); i++) {
                listItemContnet.add(cacheList.get(i));
            }
        }
    }

    /*解析数据*/
    private void parseHtml(String html, String kind){
        listItemContnet.clear();
        Document mDocument = Jsoup.parse(html);
        Elements elements = mDocument.select("item");
        int contentSize = elements.size();
        String classname =  mDocument.title();
        mStrShowTitle = classname.substring(0, classname.length()-3);
        mStrUpdateTime = mDocument.select("pubDate").first().text();
        HashMap<String, String> firstMap = new HashMap<>();
        firstMap.put("title", mStrShowTitle);
        firstMap.put("time", mStrUpdateTime);
        cacheList.add(firstMap);
        for (int i = 0; i < contentSize; i++) {
            HashMap<String, String> mMap = new HashMap<>();
            mMap.put("title", elements.get(i).select("title").first().text());
            // Log.d("TAG","标题："+elements.get(i).select("title").first().text());
            if (StringUtil.subcribtionHtml(elements.get(i).html()) == null){
                return;
            }
            mMap.put("kind", kind);
            mMap.put("link", StringUtil.subcribtionHtml(elements.get(i).html()));
            mMap.put("description", elements.get(i).select("description").first().text());
            String className = mDocument.title();
            mMap.put("classname", className.substring(0, className.length()-7));
            mMap.put("timeandauthor", elements.get(i).select("pubDate").first().text());
            mMap.put("pubdate", StringUtil.subcribtionPubDate(mDocument.select("pubDate").first().text()));
            listItemContnet.add(mMap);
            cacheList.add(mMap);
        }
        handler.sendEmptyMessage(0);
        DBUtil.getInstance(HotSpecialActivity.this).addCardContent(listItemContnet);
    }

    private void showData() {
        mTvSpecialName.setText(mStrShowTitle);
        mTvUpdataTime.setText(mStrUpdateTime);
        Collections.sort(listItemContnet, new FirstPageAdapter.CustomComparator());
        adapter = new FirstPageAdapter(listItemContnet, "hotspecila");
        mListView.setAdapter(adapter);
        if (LoginStateVariable.osLoginState){
            if (HandleClassDataMessage.classExist(mStrShowTitle)){
                mBrAttention = true;
                mIvAttentionState.setImageResource(R.drawable.attentionstate_yes_icon);
            }else {
                mBrAttention = false;
                mIvAttentionState.setImageResource(R.drawable.attentionstate_no_icon);
            }
        }
    }

    @Override
    public void setControlListener() {
        mLlBack.setOnClickListener(this);
        mIvAttentionState.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleBack_persionMessage_ll:
                finish();
                break;
            case R.id.iv_attentionState:
                handleAttentionState();
                break;
            default:break;
        }
    }

    private void handleAttentionState() {
        if (LoginStateVariable.osLoginState){
            if (mBrAttention){
                mBrAttention = false;
                mIvAttentionState.setImageResource(R.drawable.attentionstate_no_icon);
                SVProgressHUDUtil.showErrorWithStatus(this, "取消关注", SVProgressHUDUtil.SVProgressHUDMaskType.GradientCancel);
            }else {
                mBrAttention = true;
                mIvAttentionState.setImageResource(R.drawable.attentionstate_yes_icon);
                showToast("关注成功", false);
            }
        }else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public static Intent startIntnet(Context content , String strExtra){
        Intent intent = new Intent(content, HotSpecialActivity.class);
        intent.putExtra(SPECIAL_URL, strExtra);
        return intent;
    }

    @Override
    public void onPause(){
        super.onPause();
        if (LoginStateVariable.osLoginState){

            if (mBrAttention){
                if (!HandleClassDataMessage.classExist(mStrShowTitle)){
                    ClassDataMessage classDataMessage = new ClassDataMessage(mStrShowTitle, UserMessageVariable.osUserMessage.oStrPhoneNumber,
                            RemeberRandomIntVariable.osPicture);
                    HandleClassDataMessage.saveData(classDataMessage);
                }

            }else {
                if (HandleClassDataMessage.classExist(mStrShowTitle)){
                    HandleClassDataMessage.deleteSingleClass(mStrShowTitle);
                }
            }
        }
        RemeberRandomIntVariable.osPicture = "0";
        if (CacheUtil.initCache().getFromCache(mStrSpecialUrl) == null){
            CacheUtil.initCache().saveToCache(mStrSpecialUrl, cacheList);
        }
    }
}
