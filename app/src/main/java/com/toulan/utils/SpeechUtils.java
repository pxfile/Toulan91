package com.toulan.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.fast.library.ui.ToastUtil;

/**
 * 说明：百度语音播放
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/13 10:18
 * <p/>
 * 版本：verson 1.0
 */
public class SpeechUtils implements SpeechSynthesizerListener {

    private interface SpeakParam{
        String SPEAKER = "0";//发音人，目前支持女声(0)和男声(1)
        String VOLUME = "5";//音量，取值范围[0, 9]，数值越大，音量越大
        String SPEED = "5";//朗读语速，取值范围[0, 9]，数值越大，语速越快
        String PITCH = "5";//音调，取值范围[0, 9]，数值越大，音量越高
    }

    private String mSampleDirPath;
    private SpeechSynthesizer mSpeechSynthesizer;

    public SpeechUtils(Context context){
        if (mSampleDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString();
            mSampleDirPath = sdcardPath + "/" + TSpeech.SAMPLE_DIR_NAME;
        }
        init(context);
    }

    private void init(Context context){
        // 获取语音合成对象实例
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        // 设置context
        mSpeechSynthesizer.setContext(context);
        // 设置语音合成状态监听器
        mSpeechSynthesizer.setSpeechSynthesizerListener(this);
        // 设置在线语音合成授权，需要填入从百度语音官网申请的api_key和secret_key
        mSpeechSynthesizer.setApiKey(TConstant.BaiDuSpeak.API_KEY, TConstant.BaiDuSpeak.SECRET_KEY);
        // 设置离线语音合成授权，需要填入从百度语音官网申请的app_id
        mSpeechSynthesizer.setAppId(TConstant.BaiDuSpeak.APP_ID);
        setParam();
        // 获取语音合成授权信息
        AuthInfo authInfo = mSpeechSynthesizer.auth(TtsMode.MIX);
        // 判断授权信息是否正确，如果正确则初始化语音合成器并开始语音合成，如果失败则做错误处理
        if (authInfo.isSuccess()) {
            mSpeechSynthesizer.initTts(TtsMode.MIX);
        } else {
            // 授权失败
        }
    }

    private void setParam(){
        // 设置语音合成文本模型文件
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/"
                + TSpeech.TEXT_MODEL_NAME);
        // 设置语音合成声音模型文件
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/"
                + TSpeech.SPEECH_FEMALE_MODEL_NAME);
        // 设置语音合成声音授权文件
//        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, mSampleDirPath + "/"
//                + TSpeech.LICENSE_FILE_NAME);
        // 设置网络模式
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_NETWORK);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, SpeakParam.SPEAKER);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, SpeakParam.VOLUME);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, SpeakParam.SPEED);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, SpeakParam.PITCH);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_ENCODE,
                SpeechSynthesizer.AUDIO_ENCODE_AMR);//音频格式，支持bv/amr/opus/mp3，取值详见随后常量声明
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_RATE,
                SpeechSynthesizer.AUDIO_BITRATE_AMR_15K85);//音频比特率，各音频格式支持的比特率详见随后常量声明
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOCODER_OPTIM_LEVEL,"2");
    }

    public void speak(final String str){
        if (!TextUtils.isEmpty(str)){
            mSpeechSynthesizer.speak(str);
        }
    }

    public void release(){
        try {
            mSpeechSynthesizer.release();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSynthesizeStart(String s) {
    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
    }

    @Override
    public void onSynthesizeFinish(String s) {
    }

    @Override
    public void onSpeechStart(String s) {
    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {
    }

    @Override
    public void onSpeechFinish(String s) {
    }

    @Override
    public void onError(String s, SpeechError speechError) {
        ToastUtil.get().shortToast(speechError.description);
    }

}
