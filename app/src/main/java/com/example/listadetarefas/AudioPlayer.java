package com.example.listadetarefas;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.util.Locale;

public class AudioPlayer implements TextToSpeech.OnInitListener {
    private static final String TAG = "AudioPlayer";
    private TextToSpeech textToSpeech;
    private boolean isInitialized = false;
    private Context context;
    private OnInitializedListener listener;
    private int initAttempts = 0;
    private static final int MAX_INIT_ATTEMPTS = 3;

    public interface OnInitializedListener {
        void onInitialized(boolean success);
    }

    public AudioPlayer(Context context) {
        this.context = context;
        initializeTTS();
    }

    private void initializeTTS() {
        if (initAttempts >= MAX_INIT_ATTEMPTS) {
            Log.e(TAG, "Máximo de tentativas de inicialização atingido");
            if (listener != null) {
                listener.onInitialized(false);
            }
            return;
        }

        initAttempts++;
        Log.d(TAG, "Tentativa de inicialização #" + initAttempts);

        textToSpeech = new TextToSpeech(context, this);

        if (textToSpeech == null) {
            Log.e(TAG, "TextToSpeech é null após criação");
            if (listener != null) {
                listener.onInitialized(false);
            }
        }
    }

    public void setOnInitializedListener(OnInitializedListener listener) {
        this.listener = listener;
        if (isInitialized && this.listener != null) {
            this.listener.onInitialized(true);
        }
    }

    @Override
    public void onInit(int status) {
        Log.d(TAG, "onInit chamado com status: " + status);

        if (status == TextToSpeech.SUCCESS) {
            if (textToSpeech == null) {
                Log.e(TAG, "TextToSpeech é null no onInit");
                if (listener != null) {
                    listener.onInitialized(false);
                }
                return;
            }

            Locale[] localesToTry = {
                    Locale.getDefault(),
                    new Locale("pt", "BR"),
                    new Locale("pt"),
                    Locale.ENGLISH,
                    Locale.US
            };

            boolean languageSet = false;
            for (Locale locale : localesToTry) {
                int result = textToSpeech.setLanguage(locale);
                Log.d(TAG, "Tentando idioma " + locale + ", resultado: " + result);

                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    isInitialized = true;
                    languageSet = true;
                    Log.d(TAG, "TextToSpeech inicializado com sucesso! Idioma: " + locale);
                    if (listener != null) {
                        listener.onInitialized(true);
                    }
                    break;
                }
            }

            if (!languageSet) {
                Log.e(TAG, "Nenhum idioma disponível após tentar todos");
                if (initAttempts < MAX_INIT_ATTEMPTS) {
                    Log.d(TAG, "Tentando reinicializar...");
                    if (textToSpeech != null) {
                        textToSpeech.shutdown();
                    }
                    textToSpeech = null;
                    isInitialized = false;
                    initializeTTS();
                } else {
                    if (listener != null) {
                        listener.onInitialized(false);
                    }
                }
            }
        } else if (status == TextToSpeech.ERROR) {
            Log.e(TAG, "Erro na inicialização do TextToSpeech");
            if (initAttempts < MAX_INIT_ATTEMPTS) {
                Log.d(TAG, "Tentando reinicializar após erro...");
                if (textToSpeech != null) {
                    textToSpeech.shutdown();
                }
                textToSpeech = null;
                isInitialized = false;
                initializeTTS();
            } else {
                if (listener != null) {
                    listener.onInitialized(false);
                }
            }
        } else {
            Log.e(TAG, "Status desconhecido na inicialização: " + status);
            if (listener != null) {
                listener.onInitialized(false);
            }
        }
    }

    public void speak(String text) {
        if (textToSpeech == null) {
            Log.w(TAG, "TextToSpeech é null, reinicializando...");
            textToSpeech = new TextToSpeech(context, this);
            return;
        }

        if (isInitialized) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            Log.d(TAG, "Reproduzindo: " + text);
        } else {
            Log.w(TAG,
                    "TextToSpeech não está inicializado ainda. Status: " + (textToSpeech != null ? "existe" : "null"));
        }
    }

    public boolean trySpeak(String text) {
        if (isInitialized && textToSpeech != null) {
            speak(text);
            return true;
        }
        return false;
    }

    public void speak(String text, boolean addToQueue) {
        if (isInitialized && textToSpeech != null) {
            int queueMode = addToQueue ? TextToSpeech.QUEUE_ADD : TextToSpeech.QUEUE_FLUSH;
            textToSpeech.speak(text, queueMode, null, null);
        } else {
            Log.w(TAG, "TextToSpeech não está inicializado");
        }
    }

    public void stop() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    public boolean isSpeaking() {
        return textToSpeech != null && textToSpeech.isSpeaking();
    }

    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    public boolean isInitialized() {
        return isInitialized;
    }
}
