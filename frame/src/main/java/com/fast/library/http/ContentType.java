package com.fast.library.http;

/**
 * 说明：文件内容类型
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2015/12/7 14:34
 * <p/>
 * 版本：verson 1.0
 */
public enum  ContentType {

    TEXT("text/plain; charset=UTF-8"),
    PNG("image/png; charset=UTF-8"),
    JPEG("image/jpeg; charset=UTF-8"),
    JSON("application/json; charset=UTF-8");

    private String contentType;

    ContentType(String contentType){
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
