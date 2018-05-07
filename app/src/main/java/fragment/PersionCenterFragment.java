package fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kys_31.figureinformation.CollectionActivity;
import com.example.kys_31.figureinformation.CollectonSearchActivity;
import com.example.kys_31.figureinformation.DynamicAvatarViewActivity;
import com.example.kys_31.figureinformation.LoginActivity;
import com.example.kys_31.figureinformation.PersionMessageActivity;
import com.example.kys_31.figureinformation.R;
import com.example.kys_31.figureinformation.SystemSetActivity;

import java.util.Observable;

import util.BitmapUtil;
import variable.LoginStateVariable;
import variable.PersionCenterVariable;
import variable.UserMessageVariable;
import view.RoundImageView;

/**
 *@author : 老头儿
 *@email : 527672827@qq.com
 *@org : 河北北方学院 移动开发工程部 C508
 *@function : （功能） 个人中心
 */

public class PersionCenterFragment extends BaseFragment {

    private TextView mTvName;
    private TextView mTvUpLookTime;
    private LinearLayout mLlPersionMessage;
    private LinearLayout mLlCustomize;
    private LinearLayout mLlCollection;

    private LinearLayout mLlSubCription;
    private LinearLayout mLlSystemSet;
    private LinearLayout mLlInformation;
    private RoundImageView mRivHead;
    private ImageView iv_wheather;
    private TextView tv_temperatur;
    private TextView tableWord_persionCenter_tv;

    @Override
    protected int getLayoutID() {
        return R.layout.persioncenter_fragment;
    }

    @Override
    protected void initControl() {
        mTvName = (TextView)view.findViewById(R.id.name_persionCenter_tv);
        mTvUpLookTime = (TextView)view.findViewById(R.id.upLookTime_persionCenter_tv);
        mLlPersionMessage = (LinearLayout)view.findViewById(R.id.persionMessage_persionCenter_ll);
        mLlCustomize = (LinearLayout)view.findViewById(R.id.customize_persionCenter_ll);
        mLlCollection = (LinearLayout)view.findViewById(R.id.collection_persioncenter_ll);
        mLlSubCription = (LinearLayout)view.findViewById(R.id.subscription_persioncenter_ll);
        mLlSystemSet = (LinearLayout)view.findViewById(R.id.systemSet_persionCenter_ll);
        mRivHead = (RoundImageView) view.findViewById(R.id.circlehead_persionCenter_riv);
        mLlInformation = (LinearLayout)view.findViewById(R.id.ll_information);
        iv_wheather=(ImageView)view.findViewById(R.id.iv_wheather);
        tv_temperatur=(TextView)view.findViewById(R.id.tv_temperature);
        tableWord_persionCenter_tv=(TextView)view.findViewById(R.id.tableWord_persionCenter_tv);
        setWheather();
    }

    private void setWheather() {
        switch (PersionCenterVariable.weather){
            case "晴":
                iv_wheather.setBackgroundResource(R.drawable.wheater_sunny);
                break;
            case "多云":
                iv_wheather.setBackgroundResource(R.drawable.wheather_cloudy);
                break;
            case "阴":
                iv_wheather.setBackgroundResource(R.drawable.wheather_yin);
                break;
            case "阵雨":
                iv_wheather.setBackgroundResource(R.drawable.wheather_lei);
                break;
            case "雷阵雨":
                iv_wheather.setBackgroundResource(R.drawable.wheather_lei);
                break;
            case "雷阵雨伴有冰雹":
                iv_wheather.setBackgroundResource(R.drawable.wheather_lei);
                break;
            case "雨夹雪":
                iv_wheather.setBackgroundResource(R.drawable.wheather_rainsnow);
                break;
            case "小雨":
                iv_wheather.setBackgroundResource(R.drawable.wheather_rain);
                break;
            case "中雨":
                iv_wheather.setBackgroundResource(R.drawable.wheather_rain);
                break;
            case "大雨":
                iv_wheather.setBackgroundResource(R.drawable.wheather_rain);
                break;
            case "暴雨":
                iv_wheather.setBackgroundResource(R.drawable.wheather_rain);
                break;
            case "大暴雨":
                iv_wheather.setBackgroundResource(R.drawable.wheather_rain);
                break;
            case "特大暴雨":
                iv_wheather.setBackgroundResource(R.drawable.wheather_rain);
                break;
            case "小雪":
                iv_wheather.setBackgroundResource(R.drawable.wheather_snow);
                break;
            case "阵雪":
                iv_wheather.setBackgroundResource(R.drawable.wheather_snow);
                break;
            case "中雪":
                iv_wheather.setBackgroundResource(R.drawable.wheather_snow);
                break;
            case "大雪":
                iv_wheather.setBackgroundResource(R.drawable.wheather_snow);
                break;
            case "暴雪":
                iv_wheather.setBackgroundResource(R.drawable.wheather_snow);
                break;
            case "雾":
                iv_wheather.setBackgroundResource(R.drawable.wheather_fog);
                break;
            case "冻雨":
                iv_wheather.setBackgroundResource(R.drawable.wheather_rain);
                break;
            case "沙尘暴":
                iv_wheather.setBackgroundResource(R.drawable.wheather_sandstorm);
                break;
            case "小雨-中雨":
                iv_wheather.setBackgroundResource(R.drawable.wheather_rain);
                break;
            case "中雨-大雨":
                iv_wheather.setBackgroundResource(R.drawable.wheather_rain);
                break;
            case "大雨-暴雨":
                iv_wheather.setBackgroundResource(R.drawable.wheather_rain);
                break;
            case "暴雨-大暴雨":
                iv_wheather.setBackgroundResource(R.drawable.wheather_rain);
                break;
            case "大暴雨-特大暴雨":
                iv_wheather.setBackgroundResource(R.drawable.wheather_rain);
                break;
            case "小雪-中雪":
                iv_wheather.setBackgroundResource(R.drawable.wheather_snow);
                break;
            case "中雪-大雪":
                iv_wheather.setBackgroundResource(R.drawable.wheather_snow);
                break;
            case "浮尘":
                iv_wheather.setBackgroundResource(R.drawable.wheather_fog);
                break;
            case "扬沙":
                iv_wheather.setBackgroundResource(R.drawable.wheather_sandstorm);
                break;
            case "强沙尘暴":
                iv_wheather.setBackgroundResource(R.drawable.wheather_sandstorm);
                break;
            case "霾":
                iv_wheather.setBackgroundResource(R.drawable.wheather_smog);
                break;
            default:
                iv_wheather.setBackgroundResource(R.drawable.wheather_yin);
                break;
        }
        tv_temperatur.setText(PersionCenterVariable.temperature);
        tableWord_persionCenter_tv.setText("您所在的地区————"+PersionCenterVariable.location);
    }

    /*根据登录状态显示*/
    private void loginState() {
        if (LoginStateVariable.osLoginState){
            mTvUpLookTime.setText(UserMessageVariable.osUserMessage.oStrUpLookTime);
            mTvName.setText(UserMessageVariable.osUserMessage.oStrName);//用户登录时的名字
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
        }else {
            mTvUpLookTime.setText("还没登陆");
            mTvName.setText("请先登录");//用户未登录时的名字
            mRivHead.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo));//用户未登录时的头像
        }
    }

    @Override
    protected void setListener() {
        mLlPersionMessage.setOnClickListener(this);
        mLlCustomize.setOnClickListener(this);
        mLlCollection.setOnClickListener(this);
        mLlSubCription.setOnClickListener(this);
        mLlSystemSet.setOnClickListener(this);
        mTvName.setOnClickListener(this);
        mLlInformation.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.persionMessage_persionCenter_ll:
                loginCanShow(PersionMessageActivity.class);
                break;
            case R.id.customize_persionCenter_ll:
                loginCanShow(DynamicAvatarViewActivity.class);
                break;
            case R.id.collection_persioncenter_ll:
                loginCanShow(CollectionActivity.class);
                break;
            case R.id.subscription_persioncenter_ll:
                startActivity(new Intent(getActivity(), CollectonSearchActivity.class));
                break;
            case R.id.systemSet_persionCenter_ll:
                startActivity(new Intent(getActivity(), SystemSetActivity.class));
                break;
            case R.id.name_persionCenter_tv:
                login();
                break;
            case R.id.ll_information:
                if (LoginStateVariable.osLoginState)
                startActivity(new Intent(getActivity(), PersionMessageActivity.class));
                break;
            default:break;
        }
    }

    /**
     * 登录才可显示
     * @param clas 字节码
     */
    private void loginCanShow(Class clas){
        if (LoginStateVariable.osLoginState){
            startActivity(new Intent(getActivity(), clas));
        }else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    private void login(){
        if (!LoginStateVariable.osLoginState){
            startActivity(new Intent(getActivity(),  LoginActivity.class));
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        LoginStateVariable.osLoginState = (Boolean)o;
    }

    @Override
    public void onResume(){
        super.onResume();
        loginState();
    }
}
