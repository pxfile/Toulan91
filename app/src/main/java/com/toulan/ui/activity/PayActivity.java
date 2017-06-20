package com.toulan.ui.activity;


import com.fast.library.ui.ContentView;
import com.toulan.ui.CommonActivity;
import com.toulan91.R;
import butterknife.OnClick;

/**
 * 说明：PayActivity
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/10/8 22:09
 * <p/>
 * 版本：verson 1.0
 */
@ContentView(R.layout.activity_pay)
public class PayActivity extends CommonActivity {
    @Override
    public void onInitStart() {

    }

    @OnClick(R.id.iv_pay_back)
    public void onClick() {
        finish();
    }
}
