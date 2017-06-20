package com.toulan.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 说明：CountDownTimerService
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/23 16:30
 * <p/>
 * 版本：verson 1.0
 */
public class CountDownTimerService extends Service {

    private static final long timer_unit = 1000;
    private static long mDistination_total;
    private Timer timer;
    private MyTimerTask timerTask;

    private static long timer_couting = 0;

    public static final int PREPARE = 0;
    public static final int START = 1;
    public static final int PASUSE = 2;

    private int timerStatus = PREPARE;

    public static CountDownTimerService countDownTimerService;

    private static CountDownTimerListener mCountDownTimerListener;

    public static CountDownTimerService getInstance(CountDownTimerListener countDownTimerListener
            , long distination_total) {
        if (countDownTimerService == null) {
            countDownTimerService = new CountDownTimerService();
        }
        setCountDownTimerListener(countDownTimerListener);
        mDistination_total = distination_total;
        if (timer_couting == 0) {
            timer_couting = mDistination_total;
        }
        return countDownTimerService;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * get countdowan time
     *
     * @return
     */
    public long getCountingTime() {
        return timer_couting;
    }

    /**
     * get current timer status
     *
     * @return
     */
    public int getTimerStatus() {
        return timerStatus;
    }

    /**
     * start
     */
    public void startCountDown() {
        startTimer();
        timerStatus = START;
    }

    /**
     * paust
     */
    public void pauseCountDown() {
        timer.cancel();
        timerStatus = PASUSE;
    }

    /**
     * stop
     */
    public void stopCountDown() {
        if (timer != null) {
            timer.cancel();
            initTimerStatus();
            mCountDownTimerListener.onChange();
        }
    }

    public static void setCountDownTimerListener(CountDownTimerListener countDownTimerListener) {
        mCountDownTimerListener = countDownTimerListener;
    }

    /**
     * count down task
     */
    private class MyTimerTask extends TimerTask {


        @Override
        public void run() {
            timer_couting -= timer_unit;
            mCountDownTimerListener.onChange();
            if (timer_couting == 0) {
                cancel();
                initTimerStatus();
            }
        }
    }

    /**
     * init timer status
     */
    private void initTimerStatus() {
        timer_couting = mDistination_total;
        timerStatus = PREPARE;
    }

    /**
     * start count down
     */
    private void startTimer() {
        timer = new Timer();
        timerTask = new MyTimerTask();
        timer.scheduleAtFixedRate(timerTask, 0, timer_unit);
    }

    public interface CountDownTimerListener {
        void onChange();
    }
}
