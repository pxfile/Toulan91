package com.toulan.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fast.library.ui.ToastUtil;
import com.fast.library.utils.DateUtils;
import com.fast.library.utils.StringUtils;
import com.fast.library.utils.UIUtils;
import com.toulan.bean.JobTabBean;
import com.toulan.utils.ServerTimeUtils;
import com.toulan91.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 说明：工作标签
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/18 12:41
 * <p/>
 * 版本：verson 1.0
 */
public class JobTabLayout extends LinearLayout implements View.OnClickListener {

    private String refreshTime;
    private Context mContext;
    private long currentTime = -1;
    private int currentIndex = -1;
    private Calendar mCalendar;
    private ArrayList<JobTabBean> tabList = new ArrayList<>(TAB_COUNT);
    private ArrayList<TextView> tvList = new ArrayList<>(TAB_COUNT);
    private OnJobTabListener mJobTabListener;

    private static final int TAB_COUNT = 6;
    public static final String[] WEEKDAYS = {"今天","周日","周一","周二","周三","周四","周五","周六"};

    private String TAB_SELECTED_TEXT_COLOR = "#03ccf5";
    private String TAB_NORMAL_TEXT_COLOR = "#f0ece3";

    @Override
    public void onClick(View v) {
        JobTabBean bean = (JobTabBean) v.getTag();
        if (bean != null){
            if (currentIndex != bean.index){
                onTabSelected(bean.index);
                if (mJobTabListener != null && bean != null){
                    mJobTabListener.onJobTabSelected(bean);
                }
                currentIndex = bean.index;
            }
        }
    }

    public interface OnJobTabListener{
        void onJobTabSelected(JobTabBean bean);
        void onJobTabChanged();
    }

    public JobTabLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public JobTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public JobTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public void setOnJobTabListener(OnJobTabListener listener){
        this.mJobTabListener = listener;
    }

    public void setCurrentTime(long time){
        this.currentTime = time;
        init();
    }

    private long getCurrentTime(){
        if (currentTime == -1){
            currentTime = ServerTimeUtils.getInstance().getMillSeconds();
        }
        return currentTime;
    }

    private void init(){
        currentIndex = -1;
        currentTime = -1;
        setOrientation(LinearLayout.HORIZONTAL);
        initTabTitle();
        initView();
        refreshTime = ServerTimeUtils.getInstance().getTime(DateUtils.FORMAT_YYYY_MM_DD_1);
    }

    public void updateView(){
        String current = ServerTimeUtils.getInstance().getTime(DateUtils.FORMAT_YYYY_MM_DD_1);
        if (!StringUtils.isEquals(current,refreshTime)){
            init();
            if (mJobTabListener != null){
                mJobTabListener.onJobTabChanged();
            }
        }
    }

    private void initView(){
        removeAllViews();
        tvList.clear();
        int width = UIUtils.screenWidth() / TAB_COUNT;
        for (int i = 0;i < TAB_COUNT;i++){
            LinearLayout linearLayout = new LinearLayout(mContext);
            LayoutParams lllp = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
            linearLayout.setLayoutParams(lllp);
            linearLayout.setOrientation(HORIZONTAL);

            TextView tv = new TextView(mContext);
            MarginLayoutParams params = new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(params);
            lp.setMargins(UIUtils.dip2px(5.0f),UIUtils.dip2px(3.0f),UIUtils.dip2px(5.0f),0);
            tv.setPadding(0,UIUtils.dip2px(5.0f),0,0);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setText(tabList.get(i).data + "\n" + tabList.get(i).weekDay);
            tv.setTextSize(14);
            tv.setLayoutParams(lp);
            tv.setTag(tabList.get(i));
            tv.setOnClickListener(this);
            tvList.add(tv);

            linearLayout.addView(tv);
            addView(linearLayout);
        }
    }

    public void setCurrentTab(int index){
        if (index >=0 && index < TAB_COUNT){
            TextView tv = tvList.get(index);
            onClick(tv);
        }
    }

    private void onTabSelected(int index){
        for (int i = 0;i < TAB_COUNT;i++){
            TextView tv = tvList.get(i);
            if (index == i){
                tv.setTextColor(Color.parseColor(TAB_SELECTED_TEXT_COLOR));
                tv.setBackgroundResource(R.drawable.job_tab_shape);
            }else {
                tv.setTextColor(Color.parseColor(TAB_NORMAL_TEXT_COLOR));
                tv.setBackgroundResource(android.R.color.transparent);
            }
        }
    }

    private void initTabTitle(){
        tabList.clear();
        mCalendar = Calendar.getInstance();
        String data;
        for (int i = 0;i < TAB_COUNT;i++){
            JobTabBean bean = new JobTabBean();
            bean.index = i;
            bean.dataMills = getCurrentTime() + (i * 24 * 60 * 60 * 1000);
            data = DateUtils.getLongToStr(bean.dataMills,"MM-dd");
            if (data.startsWith("0")){
                bean.data = data.substring(1);
            }else {
                bean.data = data;
            }
            if (i == 0){
                bean.weekDay = WEEKDAYS[0];
            }else {
                bean.weekDay = WEEKDAYS[getWeekDay(bean.dataMills)];
            }
            tabList.add(bean);
        }
    }

    private int getWeekDay(long currentTime){
        mCalendar.setTimeInMillis(currentTime);
        return mCalendar.get(Calendar.DAY_OF_WEEK);
    }
}
