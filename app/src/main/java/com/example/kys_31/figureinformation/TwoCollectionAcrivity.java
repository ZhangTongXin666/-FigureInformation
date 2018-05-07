package com.example.kys_31.figureinformation;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import adapter.MessageAdapter;
import data.CollectEssayMessage;
import data.HandleCollectEssayMessage;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class TwoCollectionAcrivity extends BaseActivity {

    private ListView mLvCollectionContent;
    private List<CollectEssayMessage> mListCollectionMessage=new ArrayList<>();
    private LinearLayout mLlBack;
    private TextView mTvWarn;
    private ImageView mIvCollectionTop;
    private ImageView mIvCollectionBottom;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    setImageViewAnim(0);
                    setImageViewAnim(1);
                    handler.sendEmptyMessageDelayed(0, 15000);
                    break;
            }
            return false;
        }
    });

    @Override
    protected int getLayoutID() {
        return R.layout.main_layout;
    }

    @Override
    protected void initControl() {
        mIvCollectionTop = (ImageView)findViewById(R.id.iv_collectionTop);
        mIvCollectionBottom = (ImageView)findViewById(R.id.iv_collectionBottom);
        mTvWarn = (TextView)findViewById(R.id.tv_warn);
        mLvCollectionContent = (ListView)findViewById(R.id.showCollection_lv);
        mLlBack = (LinearLayout)findViewById(R.id.titleBack_persionMessage_ll);
        mListCollectionMessage = HandleCollectEssayMessage.readAllEssayMessage();
        MessageAdapter adapter = new MessageAdapter(this ,mListCollectionMessage);
        mLvCollectionContent.setAdapter(adapter);
        mTvWarn.setVisibility(View.VISIBLE);
        checkVersion();
        handler.sendEmptyMessageDelayed(0, 1500);
    }

    private void setImageViewAnim(int i){
        Animation anim;
        if (i == 0){
            anim = AnimationUtils.loadAnimation(this, R.anim.translate_out_anim);
            anim.setInterpolator(new LinearInterpolator());
            mIvCollectionTop.startAnimation(anim);
        }else {
            anim = AnimationUtils.loadAnimation(this, R.anim.translate_in);
            anim.setInterpolator(new LinearInterpolator());
            mIvCollectionBottom.startAnimation(anim);
        }
    }

    /*检查版本号*/
    private void checkVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
    @Override
    public void setControlListener() {
        mLlBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleBack_persionMessage_ll:
                finish();
                break;
            default:break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
