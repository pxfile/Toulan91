package com.fast.library.http;

import java.io.File;

import okhttp3.MediaType;

/**
 * 说明：包装文件
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/3/31 0:16
 * <p/>
 * 版本：verson 1.0
 */
public class FileWrapper {

    private File file;
    private String fileName;
    private MediaType mediaType;
    private long fileSize;

    public FileWrapper(File file,MediaType mediaType) {
        this.file = file;
        this.fileName = file.getName();
        this.mediaType = mediaType;
        this.fileSize = file.length();
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public File getFile() {
        return file;
    }

    public String getFileName(){
        return fileName;
    }

}
