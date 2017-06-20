package com.toulan.helper;

import android.content.Context;

import com.fast.library.utils.DateUtils;
import com.fast.library.utils.FileUtils;
import com.fast.library.utils.SDCardUtils;
import com.fast.library.utils.StringUtils;
import com.toulan.TApplication;
import com.toulan.utils.TConstant;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;


/**
 * 说明：记录到本地数据
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/10/20 22:29
 * <p/>
 * 版本：verson 1.0
 */
public final class WriterHelper {

    private static final String locationLogFilePath = SDCardUtils.getExternalStorage() + File.separator + TConstant.APP + File.separator + "location.txt";
    private static final File locationLogFile = new File(locationLogFilePath);
    private static final String smsLogFilePath = SDCardUtils.getExternalStorage() + File.separator + TConstant.APP + File.separator + "sms.txt";
    private static final File smsLogFile = new File(smsLogFilePath);
    private static final String phoneLogFilePath = SDCardUtils.getExternalStorage() + File.separator + TConstant.APP + File.separator + "phone.txt";
    private static final File phoneLogFile = new File(phoneLogFilePath);
    private static final String fenceLogFilePath = SDCardUtils.getExternalStorage() + File.separator + TConstant.APP + File.separator + "fence.txt";
    private static final File fenceLogFile = new File(fenceLogFilePath);

    public static void writeLocationLog(String msg){
        writeLog(locationLogFile,msg);
    }

    public static void writeSmsLog(String msg){
        writeLog(smsLogFile,msg);
    }

    public static void writeFenceLog(String msg){
        writeLog(fenceLogFile,msg);
    }

    public static void writePhoneLog(String msg){
        writeLog(phoneLogFile,msg);
    }

    private static void writeLog(File file,String msg){
        if (TApplication.getApplication().isTest()){
            if (!StringUtils.isEmpty(msg) && file != null){
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                FileWriter fileWriter = null;
                try {
                    if (!file.exists()){
                        file.createNewFile();
                    }

                    fos = TApplication.getApplication().openFileOutput(file.getName(), Context.MODE_APPEND);
                    bos = new BufferedOutputStream(fos);
                    msg = DateUtils.getNowTime(DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS_1) + "#" + msg+"\r\n\r\n";
                    bos.write(msg.getBytes());
                    bos.flush();

                    fileWriter = new FileWriter(file,true);
                    fileWriter.append(msg);
                    fileWriter.flush();
                }catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    FileUtils.closeIO(fos,bos,fileWriter);
                }
            }
        }
    }

}
