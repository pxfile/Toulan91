package com.toulan.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 说明：TSpeech
 * <p>
 * 作者：fanly
 * <p>
 * 类型：Class
 * <p>
 * 时间：2016/9/13 12:40
 * <p>
 * 版本：verson 1.0
 */
public class TSpeech {

    public static final String SAMPLE_DIR_NAME = TConstant.APP+ File.separator+"baiduTTS";
    public static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
    public static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
    public static final String TEXT_MODEL_NAME = "bd_etts_text.dat";
    public static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";
    public static final String ENGLISH_SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male_en.dat";
    public static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";

    /**
     * 将需要的资源文件拷贝到SD卡中使用
     *
     * @param isCover 是否覆盖已存在的目标文件
     * @param source
     * @param dest
     */
    private static void copyFromAssetsToSdcard(Context context, boolean isCover, String source, String dest) {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = context.getResources().getAssets().open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
            return true;
        }else {
            return false;
        }
    }

    /**
     * 初始化
     * @param context
     */
    public static void init(final Context context){
        final String dirPath = Environment.getExternalStorageDirectory().toString() +File.separator + SAMPLE_DIR_NAME;
        if (makeDir(dirPath)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    copyFromAssetsToSdcard(context,false, SPEECH_FEMALE_MODEL_NAME, dirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
                    copyFromAssetsToSdcard(context,false, SPEECH_MALE_MODEL_NAME, dirPath + "/" + SPEECH_MALE_MODEL_NAME);
                    copyFromAssetsToSdcard(context,false, TEXT_MODEL_NAME, dirPath + "/" + TEXT_MODEL_NAME);
                    copyFromAssetsToSdcard(context,false, "english/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME, dirPath + "/"
                            + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
                    copyFromAssetsToSdcard(context,false, "english/" + ENGLISH_SPEECH_MALE_MODEL_NAME, dirPath + "/"
                            + ENGLISH_SPEECH_MALE_MODEL_NAME);
                    copyFromAssetsToSdcard(context,false, "english/" + ENGLISH_TEXT_MODEL_NAME, dirPath + "/"
                            + ENGLISH_TEXT_MODEL_NAME);
                }
            }).start();
        }
    }
}
