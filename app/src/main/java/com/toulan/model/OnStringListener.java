package com.toulan.model;

import com.fast.library.ui.ActivityStack;
import com.fast.library.ui.ToastUtil;
import com.fast.library.utils.GsonUtils;
import com.fast.library.utils.StringUtils;
import com.toulan.ui.CommonActivity;
import com.toulan.utils.TRouter;
import com.toulan.utils.Tsp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Response;

/**
 * 说明：OnStringListener
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/20 22:59
 * <p/>
 * 版本：verson 1.0
 */
public abstract class OnStringListener implements OnResponseListener<String> {
    @Override
    public void onStart(int what) {

    }

    public abstract void onSuccess(int what,String node);

    @Override
    public void onSucceed(int what, Response<String> response) {
        if (response.isSucceed()){
            String data = response.get();
            if (!StringUtils.isEmpty(data)){
                int code = GsonUtils.optInt(data,"code");
                String msg = GsonUtils.optString(data,"msg");
                if (code == 1){
                    String node = GsonUtils.optString(data,"node");
                    onSuccess(what,node);
                }else {
                    if (code == 1002 || code == 1003 || code == 1005){
                        ToastUtil.get().shortToast(msg);
                        Tsp.exit();
                        TRouter.startLogin((CommonActivity) ActivityStack.create().topActivity());
                    }else if (code == 1000){
                        ToastUtil.get().shortToast("签名验证错误");
                        onFail(what,code,msg);
                    }else {
                        onFail(what,code,msg);
                    }
                }
            }else {
                onFailed(what,response);
            }
        }
    }

    public abstract void onFail(int what,int code,String msg);

    @Override
    public void onFailed(int what, Response<String> response) {

    }

    @Override
    public void onFinish(int what) {

    }
}
