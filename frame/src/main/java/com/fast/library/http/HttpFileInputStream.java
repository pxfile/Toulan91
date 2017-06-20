package com.fast.library.http;

import java.io.InputStream;
import java.io.Serializable;

/**
 * 说明：上传文件数据模型
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2015/12/7 14:15
 * <p/>
 * 版本：verson 1.0
 */
public class HttpFileInputStream implements Serializable{

    private static final long serialVersionUID = 1L;

    private InputStream inputStream;
    private String name;
    private long fileSize;

    public HttpFileInputStream(InputStream stream,String filename,long size){
        this.inputStream = stream;
        this.name = filename;
        this.fileSize = size;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

}
