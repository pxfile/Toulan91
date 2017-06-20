package com.toulan.openIM;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.toulan.ui.im.TribeConstants;
import com.toulan.utils.OpenImUtils;
import com.toulan91.R;

/**
 * 说明：
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/28 13:12
 * <p/>
 * 版本：verson 1.0
 */
public class SearchTribeActivity extends Activity {

    private static final String TAG = "SearchTribeActivity";

    private YWIMKit mIMKit;
    private IYWTribeService mTribeService;
    private YWTribe mTribe;
    private long mTribeId;

    private ProgressBar mProgressBar;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_search_tribe);
        init();
    }

    private void init() {
        initTitle();
        mIMKit = OpenImUtils.getImKit();
        mTribeService = mIMKit.getTribeService();

        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        final EditText tribeIdEditText = (EditText) findViewById(R.id.input_tribe_id);
        final ImageView searchButton = (ImageView) findViewById(R.id.search_tribe);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tribeId = tribeIdEditText.getText().toString();
                try {//Notification
                    mTribeId = Long.valueOf(tribeId);
                } catch (Exception e) {
                    Notification.showToastMsg(SearchTribeActivity.this, "请输入群id");
                }
                mTribe = mTribeService.getTribe(mTribeId);
                if (mTribe == null || mTribe.getTribeRole() == null) {
                    searchButton.setClickable(false);
                    mProgressBar.setVisibility(View.VISIBLE);
                    mTribeService.getTribeFromServer(new IWxCallback() {
                        @Override
                        public void onSuccess(Object... result) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    searchButton.setClickable(true);
                                    mProgressBar.setVisibility(View.GONE);
                                    startTribeInfoActivity(TribeConstants.TRIBE_JOIN);
                                }
                            });
                        }

                        @Override
                        public void onError(int code, String info) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    searchButton.setClickable(true);
                                    mProgressBar.setVisibility(View.GONE);
                                    Notification.showToastMsg(SearchTribeActivity.this, "没有搜索到该群，请确认群id是否正确！");
                                }
                            });
                        }

                        @Override
                        public void onProgress(int progress) {

                        }
                    }, mTribeId);
                } else {
                    startTribeInfoActivity(null);
                }
            }
        });
    }

    private void startTribeInfoActivity(String tribeOp) {
        Intent intent = new Intent(this, TribeInfoActivity.class);
        intent.putExtra(TribeConstants.TRIBE_ID, mTribeId);
        if (!TextUtils.isEmpty(tribeOp)) {
            intent.putExtra(TribeConstants.TRIBE_OP, tribeOp);
        }
        startActivity(intent);
    }

    private void initTitle() {
        RelativeLayout titleBar = (RelativeLayout) findViewById(R.id.title_bar);
        titleBar.setVisibility(View.VISIBLE);
        TextView titleView = (TextView) findViewById(R.id.title_self_title);

        TextView leftButton = (TextView) findViewById(R.id.left_button);
        leftButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.common_back_btn_white, 0, 0, 0);
        leftButton.setTextColor(Color.WHITE);
        leftButton.setText("返回");
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleBar.setBackgroundColor(Color.parseColor("#03ccf5"));
        titleView.setTextColor(Color.WHITE);
        titleView.setText("搜索加入群");


        TextView rightButton = (TextView) findViewById(R.id.right_button);
        rightButton.setVisibility(View.GONE);

    }
}
