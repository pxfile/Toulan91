package com.toulan.model;

import com.fast.library.ui.ActivityStack;
import com.fast.library.ui.ToastUtil;
import com.fast.library.utils.GsonUtils;
import com.fast.library.utils.StringUtils;
import com.fast.library.utils.UIUtils;
import com.google.gson.Gson;
import com.toulan.ui.CommonActivity;
import com.toulan.ui.activity.LoginActivity;
import com.toulan.utils.TRouter;
import com.toulan.utils.Tsp;
import com.toulan91.R;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 说明：网络请求回调
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/14 21:59
 * <p/>
 * 版本：verson 1.0
 */
public abstract class OnModelListener<T> implements OnResponseListener<String> {

    private Class clazz;
    private boolean isList;

    public OnModelListener(Class clazz){
        this(clazz,false);
    }

    public OnModelListener(Class clazz,boolean isList){
        this.clazz = clazz;
        this.isList = isList;
    }

    @Override
    public void onStart(int what) {}

    @Override
    public final void onSucceed(int what, Response<String> response) {
        if (response.isSucceed()){
            String data = response.get();
            if (clazz != null && !StringUtils.isEmpty(data)){
                try {
                    int code = GsonUtils.optInt(data,"code");
                    String msg = GsonUtils.optString(data,"msg");
                    if (code == 1){
                        String node = GsonUtils.optString(data,"node");
                        T t;
                        if (isList){
                            t = (T) GsonUtils.toList(node,clazz);
                        }else {
                            t = (T) GsonUtils.toBean(node,clazz);
                        }
                        onSuccess(what,t);
                    }else if (code == 1002 || code == 1003 || code == 1005){
                        ToastUtil.get().shortToast(msg);
                        Tsp.exit();
                        TRouter.startLogin((CommonActivity) ActivityStack.create().topActivity());
                    }else if (code == 1000){
                        ToastUtil.get().shortToast(msg);
                        onFail(what,code,msg);
                    }else {
                        onFail(what,code,msg);
                    }
                }catch (Exception e){
                    onFail(what,-1,"数据转换错误！");
                }
            }
        }
    }

    @Override
    public final void onFailed(int what, Response<String> response) {
        onFail(what,-1, UIUtils.getString(R.string.network_error));
    }

    @Override
    public void onFinish(int what) {}

    public abstract void onFail(int what,int code,String msg);

    public abstract void onSuccess(int what,T t);

    private TResponse fromJson(String json, Class clazz) {
        Gson gson = new Gson();
        Type objectType = type(TResponse.class, clazz);
        return gson.fromJson(json, objectType);
    }

    private ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }
}
