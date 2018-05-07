package application;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.kys_31.figureinformation.R;
import com.mob.MobSDK;
import com.mob.tools.proguard.ProtectedMemberKeeper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;

import service.LoadNetDataService;
import util.NetWorkUtil;
import util.TimeUtil;
import variable.NoInterestVariable;
import variable.PersionCenterVariable;
import variable.PreShowDataVariable;
import variable.SystemSetVariable;
import variable.UpdataNetDataVariable;
import variable.UserMessageVariable;

import static util.TimeUtil.getSystemDate;

/**
 * Created by 张同心 on 2017/9/13.
 * @function 启动调用
 */

public class MyApplication extends Application implements ProtectedMemberKeeper {

    private String mLoadTime;

    @Override
    public void onCreate(){
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                MobSDK.init(getApplicationContext(), null, null);
                initData();
            }
        }).start();
        initVariable();
    }

    /*初始化数据*/
    private void initData() {
        if (NetWorkUtil.initNetWorkUtil(this).isNetworkConnected()){
            if (!mLoadTime.equals(TimeUtil.getSystemDate()) ){ //根据时间加载
                UpdataNetDataVariable.osLoadPosition = 1;
                startService(LoadNetDataService.startIntent(this));

            }else {
                if (UpdataNetDataVariable.osLoadPosition < 160){// 根据
                    startService(LoadNetDataService.startIntent(this));
                }
            }
        }

        /*加载预显示数据*/
        try {
            PreShowDataVariable.osPreShowData = (List<HashMap<String, String>>)(new ObjectInputStream(getResources().openRawResource(R.raw.search))).readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化工程量
     */
    private void initVariable() {
        /*关于夜间模式*/
        SharedPreferences nightModelSp = getSharedPreferences("nightModel", 0);
        SystemSetVariable.osNightModel = nightModelSp.getBoolean("nightModel", false);

        /*观看次数*/
        SharedPreferences lookCountSp = getSharedPreferences("lookCount", 0);
        PersionCenterVariable.osLookCount = lookCountSp.getInt("lookCount", 0) + 1;
        SharedPreferences.Editor lookCountEditor = lookCountSp.edit();
        lookCountEditor.putInt("lookCount", PersionCenterVariable.osLookCount);
        lookCountEditor.commit();

        SharedPreferences loadSp = getSharedPreferences("loaddatacount", 0);
        UpdataNetDataVariable.osLoadPosition = loadSp.getInt(LoadNetDataService.LOADPOSITION, 1);
        mLoadTime = loadSp.getString("loadtime", getSystemDate());

        /*是否是首次登录*/
        SharedPreferences spFirstLogin = getSharedPreferences("firstlogin", 0);
        UserMessageVariable.osFirstLogin = spFirstLogin.getInt("firstlogin", 0);

        /*不敢兴趣*/
        SharedPreferences saveInterest = getSharedPreferences("nointerest", 0);
        if (mLoadTime.equals(TimeUtil.getSystemDate())){
            NoInterestVariable.osNotInterest = saveInterest.getString("nointerest", "");
        }else {
            SharedPreferences.Editor editor =  saveInterest.edit();
            editor.clear();
            editor.commit();
        }
    }

}
