package com.fast.library.tools;

/**
 * 说明：事件消息
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/7/15 10:28
 * <p/>
 * 版本：verson 1.0
 */
public class EventCenter<T>{

    public T data;//消息数据
    public String type;//消息类型

    public EventCenter(String type,T t){
        this.type = type;
        this.data = t;
    }

}
