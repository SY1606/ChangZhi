package com.tencent.http;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

/**
 * 语音合成
 * Created by luorenjin on 2018/9/26.
 */

public class TTSUtils {

    private static final String TAG = "TTSUtils";
    private static TextToSpeech textToSpeech;

    private static boolean isSupported = false;

    public static void init(Context context) {
        textToSpeech = new TextToSpeech(context, new TTSListener());
        textToSpeech.setPitch(0.3f);
        textToSpeech.setSpeechRate(3);
    }

    public static void speak(String text) {
        if (isSupported) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "");
        }
    }

    public static void stop() {
        if (isSupported) {
            textToSpeech.stop();
        }
    }

    public static void release() {
        textToSpeech.stop();
        textToSpeech.shutdown();
    }

    public static boolean isSpeak() {
        return textToSpeech.isSpeaking();
    }

    private static class TTSListener implements TextToSpeech.OnInitListener {

        @Override
        public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS) {
                Log.i(TAG, "onInit: TTS引擎初始化成功");
                isSupported = true;
            } else {
                Log.i(TAG, "onInit: TTS引擎初始化失败");
            }
        }
    }

}
