package com.example.kys_31.figureinformation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import data.ClassDataMessage;
import data.HandleClassDataMessage;
import database.DBUtil;
import nokonw.RatioLayout;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import util.BitmapUtil;
import util.SVProgressHUDUtil;
import util.StringUtil;
import variable.ImageUrlVariable;
import variable.PreShowDataVariable;
import variable.UserMessageVariable;
import view.DynamicAvatarView;

public class DynamicAvatarViewActivity extends BaseActivity {

    private DynamicAvatarView mDynamicAvatarView;
    private RatioLayout mRatio;
    private List<String> listClassName = new ArrayList<>();
    private Button mBtReplace;
    private Button mBtSuccess;
    private TextView mTvTitleName;
    private LinearLayout mLlBack;
    private HashMap<String, String> mMapTheme;
    private List<HashMap<String, String>> listItemContnet = new ArrayList<>();
    private static final String TAG = "DynamicAvatarViewActivity--------------";

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    SVProgressHUDUtil.dismiss(DynamicAvatarViewActivity.this);
                    break;
                case 1:
                    mBtReplace.setEnabled(true);
                    break;
                default:break;
            }
            return true;
        }
    });

    @Override
    protected int getLayoutID() {
        return R.layout.activity_dynamic_avatar_view;
    }

    @Override
    protected void initControl() {
        mRatio = (RatioLayout) findViewById(R.id.ratio);
        mTvTitleName = (TextView)findViewById(R.id.titleName_tv);
        mDynamicAvatarView = (DynamicAvatarView) findViewById(R.id.dynamic);
        mBtReplace = (Button)findViewById(R.id.bt_replace);
        mBtSuccess = (Button)findViewById(R.id.bt_success);
        mLlBack = (LinearLayout)findViewById(R.id.titleBack_persionMessage_ll);
        mTvTitleName.setText("私人定制");
        mMapTheme = PreShowDataVariable.osPreShowData.get(PreShowDataVariable.osPreShowData.size()-1);
        for (String key : mMapTheme.keySet()){
            listClassName.add(key.substring(0, key.length()-3));
        }
        mRatio.addText(listClassName);
        mRatio.enterBubble();
        RatioLayout.setControl(new RatioLayout.SetControl() {
            @Override
            public void setControl() {
                handler.sendEmptyMessageDelayed(1, 1500);

            }
        });
        SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(resource);
                mDynamicAvatarView.setImageDrawable(drawable);
            }
        };
        if (UserMessageVariable.osUserMessage.oStrHead.contains("http")){
            Glide.with(this).load(UserMessageVariable.osUserMessage.oStrHead)
                    .asBitmap()
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.load)
                    .override(300, 300)
                    .into(simpleTarget);
        }else {
            mDynamicAvatarView.setImageBitmap(BitmapUtil.get().stringToBitmap(UserMessageVariable.osUserMessage.oStrHead));
        }

    }

    @Override
    public void setControlListener() {
        mRatio.setInnerCenterListener(new RatioLayout.InnerCenterListener() {
            @Override
            public void innerCenterHominged(int position, String text) {
                Log.d(TAG, "innerCenterHominged："+position+"   文本："+text);
            }
            @Override
            public void innerCenter(int position, String text) {
                if (HandleClassDataMessage.classExist(text)){
                    HandleClassDataMessage.deleteSingleClass(text);
                    mRatio.changeTextBackground(position);
                    mRatio.setPlayLoveXin(false);
                }else {
                    ClassDataMessage classDataMessage = new ClassDataMessage(text, UserMessageVariable.osUserMessage.oStrPhoneNumber,
                            ImageUrlVariable.mStrUrl[new Random().nextInt(ImageUrlVariable.mStrUrl.length-1)]);
                    HandleClassDataMessage.saveData(classDataMessage);
                    mRatio.setPlayLoveXin(true);
                    if (DBUtil.getInstance(DynamicAvatarViewActivity.this).getCardContentByClassName(text).size() == 0){
                        SVProgressHUDUtil.showWithStatus(DynamicAvatarViewActivity.this, "加载中。。。");
                        loadData(text);
                    }
                }
            }
        });
        mBtReplace.setOnClickListener(this);
        mBtSuccess.setOnClickListener(this);
        mLlBack.setOnClickListener(this);
        mDynamicAvatarView.setOnClickListener(this);
    }

    /*加载数据*/
    private void                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                loadData(final String key) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10,TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .build();
                final Request request = new Request.Builder().url(mMapTheme.get(key+"RSS")).build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    parseHtml(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /*解析数据*/
    private void parseHtml(String html){
        listItemContnet.clear();
        Document mDocument = Jsoup.parse(html);
        Elements elements = mDocument.select("item");
        int contentSize = elements.size();
        String classname =  mDocument.title();

        for (int i = 0; i < contentSize; i++) {
            HashMap<String, String> mMap = new HashMap<>();
            mMap.put("title", elements.get(i).select("title").first().text());
            if (StringUtil.subcribtionHtml(elements.get(i).html()) == null){
                return;
            }
            mMap.put("kind", classname.substring(classname.length()-7, classname.length()-3));
            mMap.put("link", StringUtil.subcribtionHtml(elements.get(i).html()));
            mMap.put("description", elements.get(i).select("description").first().text());
            mMap.put("classname", classname.substring(0, classname.length()-3));
            mMap.put("timeandauthor", elements.get(i).select("pubDate").first().text());
            mMap.put("pubdate", StringUtil.subcribtionPubDate(mDocument.select("pubDate").first().text()));
            listItemContnet.add(mMap);
        }
        DBUtil.getInstance(DynamicAvatarViewActivity.this).addCardContent(listItemContnet);
        handler.sendEmptyMessage(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_replace:
                replaceClassName();
                break;
            case R.id.bt_success:
                finish();
                break;
            case R.id.titleBack_persionMessage_ll:
                finish();
                break;
            case R.id.dynamic:
                startActivity(new Intent(DynamicAvatarViewActivity.this, PersionMessageActivity.class));
                finish();
                break;
            default:break;
        }
    }

    /*换一批主题*/
    private void replaceClassName() {
        mBtReplace.setEnabled(false);
        mRatio.exitBubble();
        mRatio.addText(listClassName);
        mRatio.enterBubble();
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRatio.destry();
    }

}
