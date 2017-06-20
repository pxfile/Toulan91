package com.toulan.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fast.library.tools.ViewTools;
import com.fast.library.utils.UIUtils;
import com.toulan91.R;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static com.toulan.ui.adapter.TodayOrderAdapter.Function.timer;

/**
 * 说明：CountDownProgress
 * <p>
 * 作者：fanly
 * <p>
 * 类型：Class
 * <p>
 * 时间：2016/9/18 16:30
 * <p>
 * 版本：verson 1.0
 */
public class CountDownProgress extends RelativeLayout{

    SimpleDateFormat formatter = null;

    public enum Status{
        NONE,INIT,START,PAUSE,STOP
    }

    public interface OnStatusChanged{
        void onTimerStart();
        void onTimerPause();
        void onTimerStop();
        void onTimerProgress(int currentTime);
    }

    private CircleProgressView cpv;
    private TextView tvCurrentTime,tvCurrentState;

    private int maxProgress,currentProgress;
    private int time1,time2;//倒计时时间，加时时间
    private Status timerStatus = Status.NONE;
    private OnStatusChanged mListener;

    private final static int TIME_UNIT = 1000;//时间单位：秒
    private final static int WHAT_GOING = 10000;

    private boolean isAddTime = false;

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case WHAT_GOING:
                    if (currentProgress <= 0){
                        setCurrentProgress(0);
                        timerStatus = Status.STOP;
                        if (mListener != null){
                            mListener.onTimerStop();
                        }
                        currentProgress = 0;
                        mHandler.removeMessages(WHAT_GOING);
                    }else {
                        if (mListener != null){
                            mListener.onTimerProgress(currentProgress);
                        }
                        if (isAddTime){
                            setCurrentProgress(maxProgress - time2);
                        }else {
                            setCurrentProgress(currentProgress);
                        }
                        currentProgress-=1;
                        mHandler.sendEmptyMessageDelayed(WHAT_GOING,TIME_UNIT);
                        if (isAddTime){
                            time2++;
                        }else {
                            time1++;
                        }
                    }
                    break;
            }
        }
    };

    public CountDownProgress(Context context) {
        super(context);
        init();
    }

    public CountDownProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CountDownProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnStatusChangedListener(OnStatusChanged listener){
        mListener = listener;
    }

    private void init(){
        View view = UIUtils.inflate(R.layout.progress_count_down);
        cpv = (CircleProgressView) view.findViewById(R.id.pb_cpv);
        tvCurrentTime = (TextView) view.findViewById(R.id.tv_current_time);
        tvCurrentState = (TextView) view.findViewById(R.id.tv_current_time_state);
        addView(view);
    }

    public int getAllTime(){
        return time1+time2;
    }

    public int getCountDownTime(){
        return time1;
    }

    public int getAddTime(){
        return time2;
    }

    /**
     * Start
     * @param second
     */
    public void initTimer(int second,SimpleDateFormat formatter){
        this.formatter = formatter;
        if (timerStatus == Status.NONE || timerStatus == Status.STOP){
            this.maxProgress = second;
            this.currentProgress = second;
            timerStatus = Status.INIT;
            tvCurrentTime.setText(convertSecond(second));
        }
    }

    /**
     * Start
     */
    public void addTimer(int maxProgress){
        if (timerStatus == Status.STOP && !isAddTime){
            this.maxProgress = maxProgress;
            this.currentProgress = maxProgress;
            timerStatus = Status.INIT;
            tvCurrentTime.setText(convertSecond(maxProgress));
            isAddTime = true;
            setCurrentProgress(maxProgress);
        }
    }

    /**
     * Pause
     */
    public void pause(){
        if (timerStatus == Status.START){
            mHandler.removeMessages(WHAT_GOING);
            timerStatus = Status.PAUSE;
            if (mListener != null){
                mListener.onTimerPause();
            }
        }
    }

    /**
     * Start
     */
    public void start(){
        if (timerStatus == Status.INIT || timerStatus == Status.PAUSE){
            setCurrentProgress(maxProgress - currentProgress);
            mHandler.sendEmptyMessage(WHAT_GOING);
            timerStatus = Status.START;
            if (mListener != null){
                mListener.onTimerStart();
            }
        }
    }

    public void setCurrentProgress(long progress){
        cpv.setProgressWithAnimation(100-(progress*1.0f/maxProgress)*100);
        if (isAddTime){
            tvCurrentTime.setText(convertSecond(time2));
        }else {
            tvCurrentTime.setText(convertSecond(progress));
        }
    }

    private String convertSecond(long s){
        return formatter.format(s*1000- TimeZone.getDefault().getRawOffset());
    }

    public Status getCurrentStatus(){
        return timerStatus;
    }

    public void setStateDesc(String desc){
        ViewTools.setText(tvCurrentState,desc);
    }
}
