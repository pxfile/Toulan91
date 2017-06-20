package com.fast.library.http;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 说明：获取class类型
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/1/18 10:57
 * <p/>
 * 版本：verson 1.0
 */
public class ClassTypeReflect {

    public static Type getModelClazz(Class<?> subclass) {
        return getGenericType(0, subclass);
    }

    private static Type getGenericType(int index, Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (!(superclass instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) superclass).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new RuntimeException("Index outof bounds");
        }

        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return params[index];
    }
}
