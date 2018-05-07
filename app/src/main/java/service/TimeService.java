package service;

import android.app.Dialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import interfaces.TimePrompt;

import static fragment.FirstPageFragment.timePrompt;

/**
 * Created by kys_7 on 2017/11/24.
 */

public class TimeService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)  {
        Log.w("tag", ""+Thread.currentThread().getName());
        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                    handler.sendEmptyMessage(0);
            }
        };
        timer.schedule(timerTask,30*60*1000,30*60*1000);
        /*
        TimerTask timeTask2=new TimerTask() {
            @Override
            public void run() {
                Log.e("计时","5s");
            }
        };
        timer.schedule(timeTask2,5000,5000);*/
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler handler = new Handler(Looper.getMainLooper(),new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            timePrompt.showTimeDialog();
            return false;
        }
    });

}
