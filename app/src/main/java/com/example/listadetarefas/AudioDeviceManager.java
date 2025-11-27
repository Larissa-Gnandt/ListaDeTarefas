package com.example.listadetarefas;

import android.content.Context;
import android.media.AudioDeviceCallback;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;

public class AudioDeviceManager {
    private final AudioHelper audioHelper;
    private final AudioManager audioManager;
    private AudioDeviceCallback deviceCallback;
    private OnAudioDeviceChangeListener listener;

    public interface OnAudioDeviceChangeListener {
        void onBluetoothHeadsetConnected();

        void onBluetoothHeadsetDisconnected();

        void onSpeakerAvailable();
    }

    public AudioDeviceManager(Context context) {
        this.audioHelper = new AudioHelper(context);
        this.audioManager = audioHelper.getAudioManager();
    }

    public void registerAudioDeviceCallback(OnAudioDeviceChangeListener listener) {
        this.listener = listener;

        deviceCallback = new AudioDeviceCallback() {
            @Override
            public void onAudioDevicesAdded(AudioDeviceInfo[] addedDevices) {
                super.onAudioDevicesAdded(addedDevices);
                if (audioHelper.isBluetoothHeadsetConnected() && listener != null) {
                    listener.onBluetoothHeadsetConnected();
                }
                if (audioHelper.isSpeakerAvailable() && listener != null) {
                    listener.onSpeakerAvailable();
                }
            }

            @Override
            public void onAudioDevicesRemoved(AudioDeviceInfo[] removedDevices) {
                super.onAudioDevicesRemoved(removedDevices);
                if (!audioHelper.isBluetoothHeadsetConnected() && listener != null) {
                    listener.onBluetoothHeadsetDisconnected();
                }
            }
        };

        audioManager.registerAudioDeviceCallback(deviceCallback, null);
    }

    public void unregisterAudioDeviceCallback() {
        if (deviceCallback != null) {
            audioManager.unregisterAudioDeviceCallback(deviceCallback);
            deviceCallback = null;
        }
    }

    public AudioHelper getAudioHelper() {
        return audioHelper;
    }
}
