package com.toulan.utils;

/**
 * 说明：TCrash
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/13 9:21
 * <p/>
 * 版本：verson 1.0
 */

import com.fast.library.utils.CrashHandler;
import com.fast.library.utils.DateUtils;

import java.io.File;

/**
 * 说明：错误日志收集
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/7/15 10:55
 * <p/>
 * 版本：verson 1.0
 */
public class TCrash extends CrashHandler {

    private static TCrash crashHandler = new TCrash();

    public static TCrash getInstance(){
        return crashHandler;
    }

    @Override
    public void upCrashLog(File file, String error) {

    }

    @Override
    public String setFileName() {
        return "crash_" + DateUtils.getNowTime(DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS_4)+".txt";
    }

    @Override
    public String setCrashFilePath() {
        return TConstant.CRASH;
    }

    @Override
    public boolean isCleanHistory() {
        return true;
    }
}
