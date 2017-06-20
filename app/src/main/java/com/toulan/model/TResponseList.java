package com.toulan.model;

/**
 * 说明：响应
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/14 22:11
 * <p/>
 * 版本：verson 1.0
 */
public class TResponseList<T> {
    public int code;
    public String msg;
    public T node;
}
