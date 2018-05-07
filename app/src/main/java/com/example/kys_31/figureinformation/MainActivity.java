package com.example.kys_31.figureinformation;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import data.HandleUserMessage;
import data.UserMessage;
import fragment.FirstPageFragment;
import fragment.InterestFragment;
import fragment.PersionCenterFragment;
import interfaces.MetaballMenuInterface;
import service.LoadNetDataService;
import util.NetWorkUtil;
import util.SVProgressHUDUtil;
import util.TimeUtil;
import util.ViewUtil;
import variable.StartIntentServiceVariable;
import variable.UpdataNetDataVariable;
import variable.UserMessageVariable;
import view.MetaballMenu;
import view.MetaballMenuImageView;

/**
 *@author : 老头儿
 *@email : 527672827@qq.com
 *@org : 河北北方学院 移动开发工程部 C508
 *@function : （功能） 主界面
 */
    public class MainActivity extends BaseActivity implements MetaballMenu.MetaballMenuClickListener{

    private Fragment mInterestFragment;
    private Fragment mPersionCenterFragment;
    private Fragment mFirstPageFragment;
    private long mFirstTime = System.currentTimeMillis();
    private static final String TAG = "mainActivity";
    private Fragment mTopFragment;
    private MetaballMenu mMetaballMenu;
    private Timer timer;
    private MetaballMenuImageView metaballMenuImageView1;
    private MetaballMenuImageView metaballMenuImageView2;
    private MetaballMenuImageView metaballMenuImageView3;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    NetWorkUtil.initNetWorkUtil(MainActivity.this).showNoNetDialog(MainActivity.this);
                    break;
                case 1:
                    if ( NetWorkUtil.dialog != null && NetWorkUtil.dialog.isShowing()){
                        NetWorkUtil.dialog.dismiss();
                        SVProgressHUDUtil.showSuccessWithStatus(MainActivity.this, "网络已连接");
                    }
                    break;
                default:break;
            }
        }
    };

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initControl() {
        mMetaballMenu = (MetaballMenu)findViewById(R.id.metaball_menu);
        metaballMenuImageView1=(MetaballMenuImageView)findViewById(R.id.menuitem1);
        metaballMenuImageView2=(MetaballMenuImageView)findViewById(R.id.menuitem2);
        metaballMenuImageView3=(MetaballMenuImageView)findViewById(R.id.menuitem3);
        defaultShow();
        updataMetaballMenu();
        checkNetState();
    }

    /*检查网络*/
    private void checkNetState() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (!NetWorkUtil.initNetWorkUtil(MainActivity.this).isNetworkConnected()){
                        handler.sendEmptyMessage(0);
                }else {
                    handler.sendEmptyMessage(1);
                    if (!StartIntentServiceVariable.osStartIntentService){
                        StartIntentServiceVariable.osStartIntentService = true;
                        startService(LoadNetDataService.startIntent(MainActivity.this));
                    }
                }
            }
        };
        timer = new Timer();
        timer.schedule(task, 2500, 3000);
    }

    private void updataMetaballMenu() {
       FirstPageFragment.updataMetaballMenu(new MetaballMenuInterface() {
           @Override
           public void updataMetaballMenu(boolean visible) {
               if (visible){
                   ViewUtil.setShowAnimation(mMetaballMenu, 1000);
                   mMetaballMenu.setVisibility(View.VISIBLE);
               }else {
                   ViewUtil.setHideAnimation(mMetaballMenu, 1000);
                   mMetaballMenu.setVisibility(View.GONE);
               }
           }
       });
    }

    @Override
    public  void setControlListener() {
        mMetaballMenu.setMenuClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuitem1:
                setBackground(0);
                break;
            case R.id.menuitem2:
                setBackground(1);
                break;
            case R.id.menuitem3:
                setBackground(2);
                break;
        }
    }


    private void setBackground(int i) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        switch (i){
            case 1:
                ft.hide(mTopFragment);
                if (mInterestFragment == null){
                    mInterestFragment = new InterestFragment();
                    mTopFragment = mInterestFragment;
                    ft.add(R.id.fragment_content, mInterestFragment);
                }else {
                    mTopFragment = mInterestFragment;
                    ft.show(mTopFragment);
                }
                mMetaballMenu.setBackgroundColor(getResources().getColor(R.color.半透明木色));
                break;
            case 2:
                ft.hide(mTopFragment);
                if (mPersionCenterFragment == null){
                    mPersionCenterFragment = new PersionCenterFragment();
                    mTopFragment = mPersionCenterFragment;
                    ft.add(R.id.fragment_content, mTopFragment);
                }else {
                    mTopFragment = mPersionCenterFragment;
                    ft.show(mTopFragment);
                }
                mMetaballMenu.setBackgroundColor(getResources().getColor(R.color.transparent));
                break;
            case 0:
                ft.hide(mTopFragment);
                if (mFirstPageFragment == null){
                    mFirstPageFragment = new FirstPageFragment();
                    mTopFragment = mFirstPageFragment;
                    ft.add(R.id.fragment_content, mTopFragment);
                }else {
                    mTopFragment = mFirstPageFragment;
                    ft.show(mTopFragment);
                }
                mMetaballMenu.setBackgroundColor(getResources().getColor(R.color.木色));
             default:break;
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    /**
     * 默认显示首页
     */
    private void defaultShow() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (mFirstPageFragment == null || mFirstPageFragment == null || mPersionCenterFragment == null){
            mFirstPageFragment = new FirstPageFragment();
            mTopFragment = mFirstPageFragment;
            ft.add(R.id.fragment_content,mTopFragment);
        }else {
            mTopFragment = mFirstPageFragment;
            ft.show(mTopFragment);
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    public void update(Observable observable, Object o) {
        mLoginState = (Boolean)o;
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - mFirstTime > 2000){
                    showToast("再按一次退出程序", false);
                    mFirstTime = secondTime;
                    return false;
                }else {
                    /*保存用户信息*/
                    if (UserMessageVariable.osUserMessage != null){
                        UserMessage userMessage = new UserMessage(UserMessageVariable.osUserMessage.oStrPhoneNumber,
                                UserMessageVariable.osUserMessage.oStrPassword, UserMessageVariable.osUserMessage.oStrHead,
                                UserMessageVariable.osUserMessage.oStrName, UserMessageVariable.osUserMessage.oStrBirsday, UserMessageVariable.osUserMessage.oStrEmail, UserMessageVariable.osUserMessage.oIntSex,
                                UserMessageVariable.osUserMessage.oIntLookCount+1, TimeUtil.getSystemTime());
                        HandleUserMessage.saveData(userMessage);
                    }

                    /*保存加载位置*/
                    SharedPreferences sharedPreferences = getSharedPreferences("loaddatacount", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("loadposition", UpdataNetDataVariable.osLoadPosition);
                    editor.putString("loadtime", TimeUtil.getSystemDate());
                    editor.commit();

                    /*记录登录次数,用于显示进度条*/
                    if (UserMessageVariable.osFirstLogin == 0){
                        SharedPreferences spLoginFirst = getSharedPreferences("firstlogin", 0);
                        SharedPreferences.Editor editorFirstLogin = spLoginFirst.edit();
                        editorFirstLogin.putInt("firstlogin", 1);
                        editorFirstLogin.commit();
                    }
                    System.exit(0);
                }
                break;
            default:break;
        }
        return super.onKeyUp(keyCode, event);
    }

}
