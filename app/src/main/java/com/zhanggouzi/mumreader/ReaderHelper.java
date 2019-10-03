package com.zhanggouzi.mumreader;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.Closeable;
import java.util.Locale;

public class ReaderHelper implements Closeable {
    private static final String TAG = "ReaderHelper";
    private static final String utteranceId = "utteranceId";
    private static ReaderHelper INST;
    private static String needReadText = "";
    private static TextToSpeech textToSpeech;
    private final Context context;
    private boolean ttsInit = false;

    private ReaderHelper(Context context) {
        this.context = context;
    }

    public static synchronized ReaderHelper getInstance(Context context) {
        if (INST == null && context != null) {
            INST = new ReaderHelper(context);
            INST.init();
        }

        return INST;
    }

    public static synchronized void readText(String text) {
        if (INST == null) {
            needReadText = text;
        } else {
            INST.innerReadText(text);
        }

    }

    private void init() {
        if(textToSpeech != null){
            return;
        }
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.CHINA);
                    if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
                            && result != TextToSpeech.LANG_AVAILABLE) {
                        Toast.makeText(context, "暂不支持朗读！", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Not support reading");
                    } else {
                        Log.i(TAG, "Init TTS succeed");
                        ttsInit = true;
                        if (!TextUtils.isEmpty(needReadText)) {
                            textToSpeech.speak(needReadText, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                        }
                    }
                } else {
                    Log.e(TAG, "Init TTS Failed!");
                }
            }
        });

        textToSpeech.setPitch(0.9f); //数值越大声音越尖
        textToSpeech.setSpeechRate(0.8f); //语音音速
    }

    private synchronized void innerReadText(String text) {

        if (ttsInit) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        } else {
            needReadText = text;
        }
    }

    @Override
    public void close() {
        Log.i(TAG, "close ReaderHelper");

        ttsInit = false;
        if (textToSpeech != null) {
            textToSpeech.shutdown();
            textToSpeech.stop();
            textToSpeech = null;
        }
    }
}
