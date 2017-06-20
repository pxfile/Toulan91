package com.fast.library.bean;


import com.fast.library.utils.GsonUtils;

/**
 * 说明：Bean继承该类
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/3/25 20:45
 * <p/>
 * 版本：verson 1.0
 */
public class Pojo implements I_POJO {
    @Override
    public String getType() {
        return getClass().getName();
    }

    @Override
    public <T> T toBean(String json) {
        return GsonUtils.toBean(json, getType());
    }

    @Override
    public <T> T toList(String json) {
        return GsonUtils.toList(json,getType());
    }
}
