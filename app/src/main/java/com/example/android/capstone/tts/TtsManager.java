package com.example.android.capstone.tts;

import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

import timber.log.Timber;

public class TtsManager {
    private final static int TTS_INSTALLED_CHECK = 1000;
    private final static int TTS_LANGUAGE_CHECK = 1001;

    private final static Locale LANGUAGE_LOCALE = Locale.US;

    private Activity activity;
    private Callbacks callbacks;
    private TextToSpeech textToSpeech;
    private boolean isInitalized;

    public interface Callbacks {
        void onInitCompleted(boolean success);
    }

    public void init(Activity activity, Callbacks callbacks) {
        this.activity = activity;
        this.callbacks = callbacks;
        checkTextToSpeechInstalled(activity);
    }

    private void checkTextToSpeechInstalled(Activity activity) {
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        activity.startActivityForResult(checkIntent, TTS_INSTALLED_CHECK);
    }

    public void checkTextToSpeechInstalledResult(int requestCode, int resultCode) {
        switch (requestCode) {
            case TTS_INSTALLED_CHECK:
                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    Timber.d("TTS installed");
                    setupTextToSpeech();
                } else {
                    onInitComplete(false);
                }
                break;
            case TTS_LANGUAGE_CHECK:
                setLanguage();
                break;
        }
    }

    private void setupTextToSpeech() {
        textToSpeech = new TextToSpeech(activity.getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            isInitalized = true;
                            setLanguage();
                        } else {
                            Toast.makeText(activity, "Error with TTS, coach unavailable",
                                    Toast.LENGTH_LONG).show();
                            onInitComplete(false);
                        }
                    }
                });
    }

    private void setLanguage() {
        int result = textToSpeech.setLanguage(LANGUAGE_LOCALE);
        if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
            installLanguage();
        } else {
            onInitComplete(false);
        }
    }

    private void installLanguage() {
        Toast.makeText(activity, "Please install English/US language",
                Toast.LENGTH_LONG).show();

        Intent installIntent = new Intent();
        installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        activity.startActivityForResult(installIntent, TTS_LANGUAGE_CHECK);
    }

    private void onInitComplete(boolean success) {
        activity = null;
        callbacks.onInitCompleted(success);
    }

    public void speak(String text) {
        speak(text, TextToSpeech.QUEUE_ADD);
    }

    public void speak(String text, int queueMode) {
        if (isInitalized) {
            textToSpeech.speak(text, queueMode, null);
        }
    }
}
