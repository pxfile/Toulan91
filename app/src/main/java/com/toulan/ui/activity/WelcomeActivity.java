package com.toulan.ui.activity;

import com.fast.library.ui.ContentView;
import com.fast.library.utils.DateUtils;
import com.fast.library.view.CircleProgressView;
import com.toulan.bean.ServerTimeBean;
import com.toulan.model.OnModelListener;
import com.toulan.model.TDataManager;
import com.toulan.ui.CommonActivity;
import com.toulan.utils.ServerTimeUtils;
import com.toulan.utils.Tsp;
import com.toulan91.R;

import butterknife.Bind;

/**
 * 说明：欢迎页
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/13 9:29
 * <p/>
 * 版本：verson 1.0
 */
@ContentView(R.layout.activity_welcome)
public class WelcomeActivity extends CommonActivity{

    @Bind(R.id.dialog_loading_cpv)
    CircleProgressView loadingCPV;

    @Override
    public void onInitStart() {
        loadingCPV.setCircleColos(R.color.colorAccent);
        if (ServerTimeUtils.getInstance().isInit()){
            processLogin();
        }else {
            getServerTime();
        }
    }

    private void getServerTime(){
//        服务器时间
        TDataManager.getInstance().getTime(new OnModelListener<ServerTimeBean>(ServerTimeBean.class) {
            @Override
            public void onFail(int what, int code, String msg) {
            }

            @Override
            public void onSuccess(int what, ServerTimeBean bean) {
                ServerTimeUtils.getInstance().init(bean.getTime(), DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS_1);
            }

            @Override
            public void onFinish(int what) {
                super.onFinish(what);
                processLogin();
            }
        });
    }

    private void processLogin(){
        if (Tsp.isLogin()){
            skipActivity(MainActivity.class);
//            UserBean bean = Tsp.getUserInfo();
//            OpenImUtils.login(bean.getImUserId(), bean.getImPwd(), new OpenImUtils.OnLoginListener() {
//                @Override
//                public void loginSuccess() {
//                    skipActivity(MainActivity.class);
//                }
//
//                @Override
//                public void loginError(String s) {
//                    skipActivity(LoginActivity.class);
//                }
//            });
        }else {
            skipActivity(LoginActivity.class);
        }
    }

    @Override
    public void onBackPressed() {
    }

}
