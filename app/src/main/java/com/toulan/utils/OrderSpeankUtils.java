package com.toulan.utils;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.fast.library.utils.StringUtils;
import com.toulan.TApplication;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 说明：OrderSpeankUtils
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Singleton
 * <p/>
 * 时间：2016/9/25 10:22
 * <p/>
 * 版本：verson 1.0
 */
public class OrderSpeankUtils {
    private static OrderSpeankUtils ourInstance = new OrderSpeankUtils();
    private static MediaPlayer sPlayer;
    private SpeechUtils mSpeech;

    public static OrderSpeankUtils getInstance() {
        return ourInstance;
    }

    private OrderSpeankUtils() {
    }

    public void init(){
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (mSpeech == null){
                            mSpeech = new SpeechUtils(TApplication.getApplication());
                        }
                        if (sPlayer == null){
                            sPlayer = new MediaPlayer();
                        }
                    }
                });
    }

    public void speak(final String text){
        if (!StringUtils.isEmpty(text)){
            sPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Observable.just(text)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(new Action1<String>() {
                                @Override
                                public void call(String s) {
                                    if (mSpeech == null){
                                        mSpeech = new SpeechUtils(TApplication.getApplication());
                                    }
                                    mSpeech.speak(s);
                                }
                            });
                }
            });
            try {
                sPlayer.reset();
                AssetFileDescriptor assetFileDescriptor = TApplication.getApplication().getAssets().openFd("mp3/start.mp3");
                sPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),assetFileDescriptor.getStartOffset(),assetFileDescriptor.getLength());
                sPlayer.prepare();
                sPlayer.start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}