package com.example.kys_31.figureinformation;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.client.android.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import database.DBUtil;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;
import util.SVProgressHUDUtil;
import variable.PreShowDataVariable;
import view.FlowLayoutView;

public class SearchActivity extends BaseActivity {
    private List<String> list = new ArrayList<>();
    private TextView textView_search;
    private AutoCompleteTextView mActvSearchContent;
    private List<String> mListHintContent = new ArrayList<>();
    //删除
    private LinearLayout clear;
    private FlowLayoutView flKeyword;
    private ImageView mIvClear;
    private LinearLayout ll_scanning;

    private boolean showDanmaku;
    private DanmakuView danmakuView;
    private DanmakuContext danmakuContext;

    private HashMap<String, String> mSearchMap;
    private String mStrUrl;
    private boolean mBrSearchResult = false;
    private LinearLayout mLlHomeSearch;

    private ImageView iv_back;

    private BaseDanmakuParser parser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };

    @Override
    protected int getLayoutID() {
        return R.layout.activity_search;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initControl() {
        /*设置状态栏颜色*/
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.木色));

        mLlHomeSearch = (LinearLayout)findViewById(R.id.ll_homeSearch);
        mIvClear = (ImageView)findViewById(R.id.iv_clear);
        mActvSearchContent = (AutoCompleteTextView) findViewById(R.id.actv_searchContent);
        textView_search = (TextView) findViewById(R.id.tv_search);
        clear = (LinearLayout) findViewById(R.id.clear);
        flKeyword = (FlowLayoutView) findViewById(R.id.fl_keyword);
        danmakuView = (DanmakuView) findViewById(R.id.danmaku_view);
        danmakuView.enableDanmakuDrawingCache(true);
        mSearchMap = PreShowDataVariable.osPreShowData.get(PreShowDataVariable.osPreShowData.size()-1);
        for (String key : mSearchMap.keySet()){
            mListHintContent.add(key);
        }

        ArrayAdapter adapter = new ArrayAdapter(this,  R.layout.autocomplete_adapter_item, R.id.tv_content, mListHintContent);
        mActvSearchContent.setAdapter(adapter);

        mActvSearchContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SVProgressHUDUtil.showWithStatus(SearchActivity.this, "搜索中...", SVProgressHUDUtil.SVProgressHUDMaskType.None);
                String content = (String)parent.getItemAtPosition(position);
                mStrUrl = mSearchMap.get(content);
                setLayout(content);
                startActivity(HotSpecialActivity.startIntnet(SearchActivity.this, mStrUrl));
                finish();
            }
        });

        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (ContextCompat.checkSelfPermission(SearchActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(SearchActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }

        ll_scanning=(LinearLayout)findViewById(R.id.ll_scanning);
        ll_scanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SearchActivity.this, CaptureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    /*设置动画*/
    private void setAnimation() {
        Animation clearAnim = AnimationUtils.loadAnimation(this, R.anim.clear_anim);
        clearAnim.setInterpolator(new LinearInterpolator());
        mIvClear.startAnimation(clearAnim);
        Animation clearSuccessAnim = AnimationUtils.loadAnimation(this, R.anim.clearsuccess_anin);
        clearSuccessAnim.setInterpolator(new LinearInterpolator());
        mIvClear.startAnimation(clearSuccessAnim);
    }

    private void showSearchHistroy() {
        list.clear();
        SharedPreferences spSaveList = getSharedPreferences("searchhistory", 0);
        String json = spSaveList.getString("searchhistory", "1");
        if (!json.equals("1")){
            list = new Gson().fromJson(json, new TypeToken<List<String>>(){}.getType());
            flKeyword.setFlowlayout(list, new FlowLayoutView.OnItemClickListener() {
                @Override
                public void onItemClick(String content) {
                    mActvSearchContent.setText(content);
                    SVProgressHUDUtil.showWithStatus(SearchActivity.this, "搜索中...", SVProgressHUDUtil.SVProgressHUDMaskType.None);
                    mStrUrl = mSearchMap.get(content);
                    startActivity(HotSpecialActivity.startIntnet(SearchActivity.this, mStrUrl));
                    finish();
                }
            },R.drawable.bg_frame_qing);
        }
    }

    @Override
    public void setControlListener() {
        mLlHomeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this,CollectonSearchActivity.class));
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                setAnimation();
                flKeyword.removeAllViews();
                list.clear();
                saveSearchHistory();
            }
        });
        textView_search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String content = mActvSearchContent.getText().toString();
                startSearch(content);
            }
        });

        danmakuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                showDanmaku = true;
                danmakuView.start();
                generateSomeDanmaku();
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {
            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {
            }

            @Override
            public void drawingFinished() {
            }
        });
        danmakuContext = DanmakuContext.create();
        danmakuView.prepare(parser, danmakuContext);
    }

    /*开始搜素*/
    private void startSearch(String content) {
        if (content.isEmpty()){
            showToast("请输入正确搜素的内容",false);
        }else {
            for (String key : mSearchMap.keySet()){
                if (key.contains(content)){
                    mBrSearchResult = true;
                    mStrUrl = mSearchMap.get(key);
                    setLayout(content);
                    startActivity(HotSpecialActivity.startIntnet(SearchActivity.this, mStrUrl));
                    finish();
                    return;
                }
            }
            if (!mBrSearchResult){
                SVProgressHUDUtil.showErrorWithStatus(SearchActivity.this, "没有找到", SVProgressHUDUtil.SVProgressHUDMaskType.BlackCancel);
            }
        }
    }

    /**
     * 加载流布局
     * @param content
     */
    private void setLayout(String content) {
        if (!list.contains(content)){
            list.add(content);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void update(Observable o, Object arg) {

    }

    /**
     * 向弹幕View中添加一条弹幕
     * @param content
     *          弹幕的具体内容
     * @param  withBorder
     *          弹幕是否有边框
     */
    private void addDanmaku(final String content, boolean withBorder) {
        if (showDanmaku){
            final BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);//控制出现的方向
            danmaku.text = content; //弹幕内容
            danmaku.padding = 5;//弹幕内边距
            danmaku.textSize = sp2px(20); //弹幕大小
            danmaku.textColor = Color.WHITE;//弹幕颜色
            danmaku.setTime(danmakuView.getCurrentTime());
            if (true) {
                danmaku.borderColor = Color.GREEN;
            }
            danmakuView.addDanmaku(danmaku);
        }
    }

    /**
     * 随机生成一些弹幕内容以供测试
     */
    private void generateSomeDanmaku() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(showDanmaku) {
                    List<String> className = DBUtil.getInstance(SearchActivity.this).getAllClassName();
                    for (String s : className) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        addDanmaku(s, false);
                    }
                }
            }
        }).start();
    }

    /**
     * sp转px的方法。
     */
    public int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    protected void onPause() {
        super.onPause();
        showDanmaku = false;
        if (danmakuView != null && danmakuView.isPrepared()) {
            danmakuView.pause();
        }
    saveSearchHistory();
    }

    private void saveSearchHistory(){
        SharedPreferences spSaveList = getSharedPreferences("searchhistory", 0);
        SharedPreferences.Editor editor = spSaveList.edit();
        editor.putString("searchhistory", new Gson().toJson(list));
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (danmakuView != null && danmakuView.isPrepared() && danmakuView.isPaused()) {
            danmakuView.resume();
        }
        flKeyword.removeAllViews();
        showSearchHistroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showDanmaku = false;
        if (danmakuView != null) {
            danmakuView.release();
            danmakuView = null;
        }
    }


}