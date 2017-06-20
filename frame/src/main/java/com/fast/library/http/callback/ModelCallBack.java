package com.fast.library.http.callback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 说明：返回Bean对象
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/1/18 10:45
 * <p/>
 * 版本：verson 1.0
 */
public abstract class ModelCallBack<T> extends BaseHttpCallBack<T>{

    public ModelCallBack(){
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        clazz = (Class) params[0];
    }

}
