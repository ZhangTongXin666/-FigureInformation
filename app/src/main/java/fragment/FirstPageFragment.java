package fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.kys_31.figureinformation.HotSpecialActivity;
import com.example.kys_31.figureinformation.R;
import com.example.kys_31.figureinformation.SearchActivity;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import adapter.FirstPageAdapter;
import adapter.TestNormalAdapter;
import database.DBUtil;
import interfaces.LoadDataSuccess;
import interfaces.MetaballMenuInterface;
import interfaces.TimePrompt;
import service.LoadNetDataService;
import service.TimeService;
import util.PermissionApplyUtil;
import util.ViewUtil;
import variable.FirstPageShowCountVariable;
import variable.NoInterestVariable;
import variable.PreShowDataVariable;
import variable.UpdataNetDataVariable;
import variable.UserMessageVariable;

import static variable.UserMessageVariable.mBrShowNewMessage;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能）
 */
public class FirstPageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    private TextView tvSearch;
    private LinearLayout mSearchLayout;
    private ScrollView mScrollView;
    private boolean isExpand = false;
    private Toolbar toolbar;
    private TransitionSet mSet;
    private ListView mListView;
    private RollPagerView mRollViewPager;
    private Button mBtOne;//编译报道
    private Button mBtThree;//情报资源
    private Button mBtFour;//全部资讯
    private Button mBtOneCopy;//编译报道
    private Button mBtThreeCopy;//情报资源
    private Button mBtFourCopy;//全部资讯
    private Button mBtTwo;//重要报告
    private Button mBtTwoCopy;//重要报告
    private String mStrNowKind = "全部资讯";
    private LinearLayout mLlReplaceEssay;
    private LinearLayout mLlReplaceEssayCopy;
    private LinearLayout mLlReplaceTheme;
    private List<HashMap<String, String>> listItemContnet = new ArrayList<>();
    private List<HashMap<String, String>> listSortByTime;
    private List<String> listClassName = new ArrayList<>();
    private TextView mTvOneTheme;
    private TextView mTvTwoTheme;
    private TextView mTvThreeTheme;
    private TextView mTvFourTheme;
    private ImageView mIvReplaceTheme;
    private ImageView mIvReplaceEssayCopy;
    private ImageView mIvReplaceEssay;
    private SwipeRefreshLayout mDownRefresh;
    private FirstPageAdapter mFirstPageAdapter;
    private static MetaballMenuInterface mMetaballMenuInterface;
    private LinearLayout mLlSearch;
    private boolean mBrFromBottomShow = false;
    private ImageView mIvUpTop;
    private boolean mBrShowUpTop = false;
    private LinearLayout mLlCopy;
    private LinearLayout mLlHotTheme;
    private LinearLayout mLlReplaceHotTheme;
    private HashMap<String, String> mMapTheme;
    private static final String TAG = "firstpagefragment";
    public static TimePrompt timePrompt;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if (!getActivity().isDestroyed() && FirstPageFragment.this.isVisible()){
                        Dialog dialog = new AlertDialog.Builder(getActivity())
                                .setTitle("最新资讯")
                                .setMessage("有多条最新资讯已经发表，是否立即查看。")
                                .setPositiveButton("立即查看", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mBrFromBottomShow = true;
                                        SharedPreferences spShowNewMessage = getActivity().getSharedPreferences("shownewmessage", 0);
                                        SharedPreferences.Editor editor = spShowNewMessage.edit();
                                        editor.putBoolean("shownewmessage", mBrShowNewMessage);
                                        upDataHotEssay(mStrNowKind);
                                        upDataHotTheme();
                                        initScrollView();
                                        initViewPager();
                                    }
                                })
                                .setNegativeButton("暂不查看", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mBrFromBottomShow = false;
                                        SharedPreferences spShowNewMessage = getActivity().getSharedPreferences("shownewmessage", 0);
                                        SharedPreferences.Editor editor = spShowNewMessage.edit();
                                        editor.putBoolean("shownewmessage", mBrShowNewMessage);
                                    }
                                })
                                .create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                    break;
                case 1:
                    UserMessageVariable.mBrShowNewMessage = true;
                    upDataHotEssay(mStrNowKind);
                    upDataHotTheme();
                    showToast("刷新成功", false);
                    if (mDownRefresh.isRefreshing()){
                        mDownRefresh.setRefreshing(false);
                    }
                    break;
                default:break;
            }
        }
    };

    @Override
    protected int getLayoutID() {
        return R.layout.firstpage_fragment_view;
    }

    @Override
    protected void initControl() {
        mBtTwo = (Button)view.findViewById(R.id.bt_two);
        mBtTwoCopy = (Button)view.findViewById(R.id.bt_twoCopy);
        mIvReplaceEssayCopy = (ImageView)view.findViewById(R.id.iv_replaceEssayCopy);
        mBtOneCopy = (Button)view.findViewById(R.id.oneCopy);
        mBtThreeCopy = (Button)view.findViewById(R.id.threeCopy);
        mBtFourCopy = (Button)view.findViewById(R.id.fourCopy);
        mLlReplaceEssayCopy = (LinearLayout)view.findViewById(R.id.ll_replaceEssayCopy);
        mLlCopy = (LinearLayout)view.findViewById(R.id.ll_copy);
        mLlHotTheme = (LinearLayout)view.findViewById(R.id.ll_hotTheme);
        mLlReplaceHotTheme = (LinearLayout)view.findViewById(R.id.ll_replaceHotTheme);

        mIvReplaceEssay = (ImageView)view.findViewById(R.id.iv_replaceEssay);
        mIvReplaceTheme = (ImageView)view.findViewById(R.id.iv_replaceTheme);
        tvSearch = (TextView)view.findViewById(R.id.tv_search);
        mSearchLayout = (LinearLayout)view.findViewById(R.id.ll_search);
        mScrollView = (ScrollView)view.findViewById(R.id.scrollView);
        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        mListView = (ListView)view.findViewById(R.id.lv_hotEssay);
        mRollViewPager = (RollPagerView)view.findViewById(R.id.roll_view_pager);
        mBtOne = (Button)view.findViewById(R.id.one);
        mBtThree = (Button)view.findViewById(R.id.three);
        mBtFour = (Button)view.findViewById(R.id.four);
        mLlReplaceEssay = (LinearLayout)view.findViewById(R.id.ll_replaceEssay);
        mLlReplaceTheme = (LinearLayout)view.findViewById(R.id.ll_replaceTheme);
        mTvOneTheme = (TextView)view.findViewById(R.id.tv_oneTheme);
        mTvTwoTheme = (TextView)view.findViewById(R.id.tv_twoTheme);
        mTvThreeTheme = (TextView)view.findViewById(R.id.tv_threeTheme);
        mTvFourTheme = (TextView)view.findViewById(R.id.tv_fourTheme);
        mDownRefresh = (SwipeRefreshLayout)view.findViewById(R.id.downRefresh);
        mLlSearch = (LinearLayout)view.findViewById(R.id.ll_search);
        mIvUpTop = (ImageView)view.findViewById(R.id.iv_upTop);
        PermissionApplyUtil.verifyStoragePermissions(getActivity());
        checkVersion();
        initInterfaceSuccess();
        initShowData();
        initTimePrompt();
        mScrollView.smoothScrollTo(0, 0);
        mBtFour.setBackgroundResource(R.drawable.allmessage_icon_click);
        mBtFourCopy.setBackgroundResource(R.drawable.allmessage_icon_click);

        Intent intent=new Intent(getActivity(),TimeService.class);
        getActivity().startService(intent);
    }

    private void initTimePrompt() {
        timePrompt=new TimePrompt() {
            @Override
            public void showTimeDialog() {
                Dialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("护眼提示")
                        .setMessage("您已连续浏览新闻半个小时了\n图情资讯提示您保护眼睛")
                        .setPositiveButton("继续浏览", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("退出应用", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        })
                        .create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        };
    }

    /*设置动画*/
    private void setAnimation(int i) {
        Animation refreshAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.refresh_anim);
        refreshAnim.setInterpolator(new LinearInterpolator());
        if (i == 0){
            mIvReplaceTheme.startAnimation(refreshAnim);
        }else if (i == 1){
            mIvReplaceEssay.startAnimation(refreshAnim);
        }else if (i == 2){
            mIvReplaceEssayCopy.startAnimation(refreshAnim);
        }
    }

    /*初始化显示的数据*/
    private void initShowData() {
        mBrShowNewMessage = getActivity().getSharedPreferences("shownewmessage", 0).getBoolean("shownewmessage", false);
        upDataHotEssay(mStrNowKind);
        upDataHotTheme();
        initScrollView();
        initViewPager();
    }

    /*初始化接口*/
    private void initInterfaceSuccess() {
        LoadNetDataService.initLoadSuccess(new LoadDataSuccess() {
            @Override
            public void loadSuccess() {
                if (UpdataNetDataVariable.osBrUpdata){
                    UpdataNetDataVariable.osBrUpdata = false;
                    handler.sendEmptyMessage(0);
                }
            }
        });

        FirstPageAdapter.initUpdateNoInterest(new FirstPageAdapter.UpdateNoInterest() {
            @Override
            public void updateNoInterest() {
                upDataHotTheme();
                upDataHotEssay(mStrNowKind);
            }
        });
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


    /*更新导航栏*/
    public static void updataMetaballMenu(MetaballMenuInterface metaballMenuInterface){
        mMetaballMenuInterface = metaballMenuInterface;
    }

    private void initViewPager() {
        mRollViewPager.setPlayDelay(10000);
        mRollViewPager.setAnimationDurtion(500);
        mRollViewPager.setAdapter(new TestNormalAdapter(listItemContnet));
        mRollViewPager.setHintView(new ColorPointHintView(getActivity(), Color.YELLOW,Color.WHITE));
    }

    @Override
    protected void setListener() {
        mBtOneCopy.setOnClickListener(this);
        mBtThreeCopy.setOnClickListener(this);
        mBtFourCopy.setOnClickListener(this);
        mLlReplaceEssayCopy.setOnClickListener(this);

        mBtOne.setOnClickListener(this);
        mBtThree.setOnClickListener(this);
        mBtFour.setOnClickListener(this);
        mLlReplaceTheme.setOnClickListener(this);
        mLlReplaceEssay.setOnClickListener(this);
        mTvOneTheme.setOnClickListener(this);
        mTvTwoTheme.setOnClickListener(this);
        mTvThreeTheme.setOnClickListener(this);
        mTvFourTheme.setOnClickListener(this);
        mDownRefresh.setOnRefreshListener(this);
        mLlSearch.setOnClickListener(this);
        mIvUpTop.setOnClickListener(this);
        mBtTwo.setOnClickListener(this);
        mBtTwoCopy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_two:
                String selectTwo = "重要报告";
                if (!mStrNowKind.equals(selectTwo)){
                    upDataHotEssay(selectTwo);
                    mStrNowKind = selectTwo;
                    restBackground();
                    mBtTwo.setBackgroundResource(R.drawable.important_report_click);
                    mBtTwoCopy.setBackgroundResource(R.drawable.important_report_click);
                }
                break;
            case R.id.bt_twoCopy:
                String selectTwoCopy = "重要报告";
                if (!mStrNowKind.equals(selectTwoCopy)){
                    upDataHotEssay(selectTwoCopy);
                    mStrNowKind = selectTwoCopy;
                    restBackground();
                    mBtTwo.setBackgroundResource(R.drawable.important_report_click);
                    mBtTwoCopy.setBackgroundResource(R.drawable.important_report_click);
                }
                break;
            case R.id.one:
                String selectOne = "编译报道";

                if (!mStrNowKind.equals(selectOne)){
                    upDataHotEssay(selectOne);
                    mStrNowKind = selectOne;
                    restBackground();
                    mBtOne.setBackgroundResource(R.drawable.one_click);
                    mBtOneCopy.setBackgroundResource(R.drawable.one_click);
                }
                break;
            case R.id.three:
                String selectThree = "情报资源";
                if (!mStrNowKind.equals(selectThree)){
                    upDataHotEssay(selectThree);
                    mStrNowKind = selectThree;
                    restBackground();
                    mBtThree.setBackgroundResource(R.drawable.two_click);
                    mBtThreeCopy.setBackgroundResource(R.drawable.two_click);
                }
                break;
            case R.id.four:
                String selectFour = "全部资讯";
                mBtFour.setBackgroundResource(R.drawable.allmessage_icon_click);
                if (!mStrNowKind.equals(selectFour)){
                    upDataHotEssay(selectFour);
                    mStrNowKind = selectFour;
                    restBackground();
                    mBtFour.setBackgroundResource(R.drawable.allmessage_icon_click);
                    mBtFourCopy.setBackgroundResource(R.drawable.allmessage_icon_click);
                }
                break;

            case R.id.ll_replaceEssay:
                setAnimation(1);
                upDataHotEssay(mStrNowKind);
                break;

            case R.id.oneCopy:
                String selectOneCopy = "编译报道";

                if (!mStrNowKind.equals(selectOneCopy)){
                    upDataHotEssay(selectOneCopy);
                    mStrNowKind = selectOneCopy;
                    restBackground();
                    mBtOne.setBackgroundResource(R.drawable.one_click);
                    mBtOneCopy.setBackgroundResource(R.drawable.one_click);
                }
                break;
            case R.id.threeCopy:
                String selectThreeCopy = "情报资源";
                if (!mStrNowKind.equals(selectThreeCopy)){
                    upDataHotEssay(selectThreeCopy);
                    mStrNowKind = selectThreeCopy;
                    restBackground();
                    mBtThree.setBackgroundResource(R.drawable.two_click);
                    mBtThreeCopy.setBackgroundResource(R.drawable.two_click);
                }
                break;
            case R.id.fourCopy:
                String selectFourCopy = "全部资讯";


                if (!mStrNowKind.equals(selectFourCopy)){
                    upDataHotEssay(selectFourCopy);
                    mStrNowKind = selectFourCopy;
                    restBackground();
                    mBtFour.setBackgroundResource(R.drawable.allmessage_icon_click);
                    mBtFourCopy.setBackgroundResource(R.drawable.allmessage_icon_click);
                }
                break;
            case R.id.ll_replaceEssayCopy:
                setAnimation(2);
                upDataHotEssay(mStrNowKind);
                break;

            case R.id.ll_replaceTheme:
                setAnimation(0);
                upDataHotTheme();
                break;
            case R.id.tv_oneTheme:
                startActivity( createHotSpecialIntent(mTvOneTheme.getText().toString()));
                break;
            case R.id.tv_twoTheme:
                startActivity( createHotSpecialIntent(mTvTwoTheme.getText().toString()));
                break;
            case R.id.tv_threeTheme:
                startActivity( createHotSpecialIntent(mTvThreeTheme.getText().toString()));
                break;
            case R.id.tv_fourTheme:
                startActivity( createHotSpecialIntent(mTvFourTheme.getText().toString()));
                break;
            case R.id.ll_search:
                Intent intentSearch = new Intent(getActivity(), SearchActivity.class);
                startActivity(intentSearch);
                break;
            case R.id.iv_upTop:
                mScrollView.smoothScrollTo(0, 0);
                break;
            default:break;
        }
    }

    private void restBackground() {
        mBtFour.setBackgroundResource(R.drawable.allmessage_icon);
        mBtFourCopy.setBackgroundResource(R.drawable.allmessage_icon);

        mBtOne.setBackgroundResource(R.drawable.one);
        mBtOneCopy.setBackgroundResource(R.drawable.one);

        mBtThree.setBackgroundResource(R.drawable.two);
        mBtThreeCopy.setBackgroundResource(R.drawable.two);

        mBtTwo.setBackgroundResource(R.drawable.important_report);
        mBtTwoCopy.setBackgroundResource(R.drawable.important_report);

    }


    private Intent createHotSpecialIntent(String strTheme) {
        Intent intent = null;
        for (String key : mMapTheme.keySet()){
            if (key.contains(strTheme)){
                intent = HotSpecialActivity.startIntnet(getActivity(),mMapTheme.get(key));
                break;
            }
        }
        return intent;
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    /*更新热点专题*/
    private void upDataHotTheme(){
        mMapTheme = PreShowDataVariable.osPreShowData.get(PreShowDataVariable.osPreShowData.size()-1);
        for (String key : mMapTheme.keySet()){
            String className = key.substring(0, key.length()-3);
            if (!NoInterestVariable.osNotInterest.contains(className)){
                listClassName.add(className);
            }
        }
        mTvOneTheme.setText(listClassName.get(new Random().nextInt(listClassName.size()-1)));
        if (mTvOneTheme.getText().toString().length()<9){
            mTvTwoTheme.setVisibility(View.VISIBLE);
            mTvTwoTheme.setText(listClassName.get(new Random().nextInt(listClassName.size()-1)));
        }else {
            mTvTwoTheme.setVisibility(View.GONE);
        }
        mTvThreeTheme.setText(listClassName.get(new Random().nextInt(listClassName.size()-1)));
        if (mTvThreeTheme.getText().toString().length()<9){
            mTvFourTheme.setVisibility(View.VISIBLE);
            mTvFourTheme.setText(listClassName.get(new Random().nextInt(listClassName.size()-1)));
        }else {
            mTvFourTheme.setVisibility(View.GONE);
        }
    }

    /*更新热点文章*/
    private void upDataHotEssay(String kind){
        listItemContnet.clear();
        if(mBrShowNewMessage){
            List<HashMap<String, String>> tempList = DBUtil.getInstance(getActivity()).getAllCardContent(kind);
            if (kind.equals("全部资讯")){
                listItemContnet.addAll(tempList);
            }else {
                for (int i = 0; i <tempList.size(); i++) {
                    if (kind.equals(tempList.get(i).get("kind"))){
                        listItemContnet.add(tempList.get(i));
                    }
                }
            }
        }else {
            if (kind.equals("全部资讯")){
                listItemContnet.addAll(PreShowDataVariable.osPreShowData.subList(0, PreShowDataVariable.osPreShowData.size()-2));
            }else {
                for (int i = 0; i < PreShowDataVariable.osPreShowData.size()-1; i++) {
                    if (kind.equals(PreShowDataVariable.osPreShowData.get(i).get("kind"))){
                        listItemContnet.add(PreShowDataVariable.osPreShowData.get(i));
                    }
                }
            }
        }

        if (listSortByTime != null && listSortByTime.size() != 0){
            listSortByTime.clear();
        }else {
            listSortByTime = new ArrayList<>();
        }
        for (int i = 0; i < FirstPageShowCountVariable.osIntFirstPageShowCount; i++){
            if (listItemContnet.size() >= 0){
                listSortByTime.add(listItemContnet.get(judgeNoInterest()));
            }
        }

        Collections.sort(listSortByTime, new FirstPageAdapter.CustomComparator());

        mFirstPageAdapter = new FirstPageAdapter(listSortByTime, "firstpage");
        mListView.setAdapter(mFirstPageAdapter);
        setListViewHeightBasedOnChildren(mListView);
        mBrFromBottomShow = false;

    }

    /*判断不感兴趣*/
    private int judgeNoInterest(){
        int randomPosition = new Random().nextInt(listItemContnet.size()-1);
        if (!NoInterestVariable.osNotInterest.contains(listItemContnet.get(randomPosition).get("classname"))){
            return randomPosition;
        }else {
            return judgeNoInterest();
        }
    }

    /*设置ListView的长度*/
    public void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED );
            totalHeight += listItem.getMeasuredHeightAndState();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /*初始化ScrollView*/
    private void initScrollView() {
        //设置toolbar初始透明度为0
        toolbar.getBackground().mutate().setAlpha(0);
        //scrollview滚动状态监听
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                //改变toolbar的透明度
                changeToolbarAlpha();
                //滚动距离>=大图高度-toolbar高度 即toolbar完全盖住大图的时候 且不是伸展状态 进行伸展操作
                if (mScrollView.getScrollY() >=mRollViewPager.getHeight() - toolbar.getHeight()  && !isExpand) {
                    expand();
                    isExpand = true;
                }
                //滚动距离<=0时 即滚动到顶部时  且当前伸展状态 进行收缩操作
                else if (mScrollView.getScrollY()<=0&& isExpand) {
                    reduce();
                    isExpand = false;
                }

                if (mScrollView.getScrollY() > 330){
                    if (!mBrShowUpTop){
                        mIvUpTop.setVisibility(View.VISIBLE);
                        ViewUtil.setShowAnimation(mIvUpTop, 1500);
                        mMetaballMenuInterface.updataMetaballMenu(false);
                    }
                    mBrShowUpTop = true;
                }else {
                    if (mBrShowUpTop){
                        mIvUpTop.setVisibility(View.GONE);
                        mMetaballMenuInterface.updataMetaballMenu(true);
                    }
                    mBrShowUpTop = false;
                }
                if (mScrollView.getScrollY() >= (mLlHotTheme.getHeight() + mLlReplaceHotTheme.getHeight() + mRollViewPager.getHeight())  ){
                        mLlCopy.setVisibility(View.VISIBLE);
                }else {
                        mLlCopy.setVisibility(View.GONE);
                }
            }
        });
    }

    private void changeToolbarAlpha() {
        int scrollY = mScrollView.getScrollY(); // 获得滚过的像素点
        //计算当前透明度比率
        float radio= Math.min(1,scrollY/(mRollViewPager.getHeight()-toolbar.getHeight()));
        //设置透明度
        toolbar.getBackground().mutate().setAlpha( (int)(radio * 0xFF));
    }

    private void expand() {
        //设置伸展状态时的布局
        tvSearch.setText("搜索图情资讯的内容");
        RelativeLayout.LayoutParams LayoutParams = (RelativeLayout.LayoutParams) mSearchLayout.getLayoutParams();
        LayoutParams.width = LayoutParams.MATCH_PARENT;
        LayoutParams.setMargins(dip2px(10), dip2px(10), dip2px(10), dip2px(10));
        mSearchLayout.setLayoutParams(LayoutParams);
        //开始动画
        beginDelayedTransition(mSearchLayout);
    }

    private void reduce() {
        //设置收缩状态时的布局
        tvSearch.setText("搜索");
        RelativeLayout.LayoutParams LayoutParams = (RelativeLayout.LayoutParams) mSearchLayout.getLayoutParams();
        LayoutParams.width = dip2px(80);
        LayoutParams.setMargins(dip2px(10), dip2px(10), dip2px(10), dip2px(10));
        mSearchLayout.setLayoutParams(LayoutParams);
        //开始动画
        beginDelayedTransition(mSearchLayout);
    }

    void beginDelayedTransition(ViewGroup view) {
        mSet = new AutoTransition();
        mSet.setDuration(300);
        TransitionManager.beginDelayedTransition(view, mSet);
    }

    private int dip2px(float dpVale) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpVale * scale + 0.5f);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            mScrollView.smoothScrollTo(0, 0);
        }
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(1, 3000);
    }

}
