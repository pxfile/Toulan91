package com.toulan.utils;

import android.support.v4.app.Fragment;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;

/**
 * 说明：阿里云旺
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Singleton
 * <p/>
 * 时间：2016/9/23 11:14
 * <p/>
 * 版本：verson 1.0
 */
public class OpenImUtils {

    public static YWIMKit getImKit(String imUserId){
        return YWAPI.getIMKitInstance(imUserId,TConstant.OPENIM_APP_KEY);
    }

    public static YWIMKit getImKit(){
        return YWAPI.getIMKitInstance(Tsp.getUserInfo().getImUserId(),TConstant.OPENIM_APP_KEY);
    }

    public interface OnLoginListener{
        void loginSuccess();
        void loginError(String s);
    }

    public static void login(String imUserId,String imPwd,final OnLoginListener listener){
        IYWLoginService loginService = getImKit(imUserId).getLoginService();
        YWLoginParam loginParam = YWLoginParam.createLoginParam(imUserId,imPwd);
        loginService.login(loginParam, new IWxCallback() {
            @Override
            public void onSuccess(Object... objects) {
                if (listener != null){
                    listener.loginSuccess();
                }
            }

            @Override
            public void onError(int i, String s) {
                if (listener != null){
                    listener.loginError(s);
                }
            }

            @Override
            public void onProgress(int i) {

            }
        });
    }

    /**
     * 会话列表
     * @return
     */
    public static Fragment getConversationFragment(){
        Fragment c = getImKit().getConversationFragment();
        return c;
    }

    /**
     * 联系人
     * @return
     */
    public static Fragment getContactsFragment(){
        Fragment c = getImKit().getContactsFragment();
        return c;
    }

//    public static Fragment getMessageFragemnt(){
//        Fragment c = getImKit().getContactsFragment();
//        return c;
//    }
//
//    public static Class<? extends Fragment> getMessageFragemntClass(){
//        Class<? extends Fragment> c = getImKit().getContactsFragmentClass();
//        return c;
//    }
}
