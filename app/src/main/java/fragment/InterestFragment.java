package fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kys_31.figureinformation.DynamicAvatarViewActivity;
import com.example.kys_31.figureinformation.LoginActivity;
import com.example.kys_31.figureinformation.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import adapter.InterestAdapter;
import data.ClassDataMessage;
import data.HandleClassDataMessage;
import variable.LoginStateVariable;
import variable.RemeberRandomIntVariable;
import variable.UserMessageVariable;
import view.MetaballMenu;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class InterestFragment extends BaseFragment implements View.OnScrollChangeListener{

    private LinearLayout  mLlTop;
    private TextView mTvAllMessage;
    private TextView mTvOne;
    private TextView mTvTwo;
    private ListView mLvInterest;
    private List<ClassDataMessage> mListClassDataMessage;
    private List<ClassDataMessage> mListFilterClassDataMessage = new ArrayList<>();
    private String mStrTheme = "情报资源";
    private InterestAdapter mAdapter;
    private boolean mBrNowView = false;

    @Override
    protected int getLayoutID() {
        return R.layout.interest_fragment;
    }

    @Override
    protected void initControl() {
        mLlTop = (LinearLayout)view.findViewById(R.id.ll_top);
        mTvAllMessage = (TextView)view.findViewById(R.id.tv_allMessage);
        mTvOne = (TextView)view.findViewById(R.id.tv_one);
        mTvTwo = (TextView)view.findViewById(R.id.tv_two);
        mLvInterest = (ListView)view.findViewById(R.id.lv_interest);
        mTvOne.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.decor_icon_click),null);
        checkVersion();
        loginState();
    }

    /*检查版本号*/
    private void checkVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
    private void loginState() {
            if (LoginStateVariable.osLoginState){
                initData();
            }else {
                showDialog("登录后即可查看你的专栏！", "登录", "暂不登录");
            }
    }

    private void initData(){
            if (LoginStateVariable.osLoginState){
                mTvOne.setEnabled(true);
                mTvTwo.setEnabled(true);
                mTvAllMessage.setEnabled(true);
                mListClassDataMessage = HandleClassDataMessage.readAllAttentionMessage();
                if (mBrNowView){
                    if (mListClassDataMessage.size() == 0){
                        showDialog("还没有选择感兴趣的专栏，请先进行私人定制！", "选择", "暂不选择");
                    }
                    setShowFilterData();
                }
            }
    }

    private void showDialog(String strContent, final String btYesContent, String btNoContent) {
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage(strContent)
                .setPositiveButton(btYesContent, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (btYesContent.equals("选择")){
                            startActivity(new Intent(getActivity(), DynamicAvatarViewActivity.class));
                        }else {
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(btNoContent, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    private void setItemView(List<ClassDataMessage> list){
        mAdapter = new InterestAdapter(getActivity(), list);
        mLvInterest.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        mTvOne.setOnClickListener(this);
        mTvTwo.setOnClickListener(this);
        mTvAllMessage.setOnClickListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_allMessage:
                if (!mStrTheme.equals("重要报告")){
                    mStrTheme = "重要报告";
                    resetImage();
                    mTvAllMessage.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.decor_icon_click),null);
                    setShowFilterData();
                }
                break;
            case R.id.tv_one:
                if (!mStrTheme.equals("情报资源")){
                    mStrTheme = "情报资源";
                    resetImage();
                    mTvOne.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.decor_icon_click),null);
                    setShowFilterData();
                }
                break;
            case R.id.tv_two:
                if (!mStrTheme.equals("编译报道")){
                    mStrTheme = "编译报道";
                    resetImage();
                    mTvTwo.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.decor_icon_click),null);
                    setShowFilterData();
                }
                break;
            default:break;
        }
    }

    private void resetImage() {
        mTvTwo.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.decor_icon),null);
        mTvOne.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.decor_icon),null);
        mTvAllMessage.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.decor_icon),null);
    }


    private void setShowFilterData() {
        mListFilterClassDataMessage.clear();
        for (ClassDataMessage classDataMessage : mListClassDataMessage) {
                if (classDataMessage.className.substring(classDataMessage.className.length()-4).equals(mStrTheme)){
                    mListFilterClassDataMessage.add(classDataMessage);
                }
        }
        if (mListFilterClassDataMessage.size() != 0){
            setItemView(mListFilterClassDataMessage);
        }else {
            setItemView(new ArrayList<ClassDataMessage>());
            Dialog dialog = new AlertDialog.Builder(getActivity()).setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setMessage("暂未关注此类兴趣").create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            mBrNowView = true;
            loginState();
        }else {
            mBrNowView = false;
            RemeberRandomIntVariable.osPicture = "0";
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        initData();
    }
}
