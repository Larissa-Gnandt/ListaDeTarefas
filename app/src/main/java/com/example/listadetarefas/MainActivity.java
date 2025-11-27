package com.example.listadetarefas;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private AudioDeviceManager audioDeviceManager;
    private AudioPlayer audioPlayer;
    private TextView speakerStatusText;
    private TextView bluetoothStatusText;
    private Button buttonTestAudio;
    private Button buttonBluetooth;
    private Button buttonReadMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        initializeAudioComponents();
        setupListeners();
        updateAudioStatus();
    }

    private void initializeViews() {
        ListView listView = findViewById(R.id.listView);
        speakerStatusText = findViewById(R.id.speakerStatusText);
        bluetoothStatusText = findViewById(R.id.bluetoothStatusText);
        buttonTestAudio = findViewById(R.id.buttonTestAudio);
        buttonBluetooth = findViewById(R.id.buttonBluetooth);
        buttonReadMessage = findViewById(R.id.buttonReadMessage);

        String[] emptyList = { getString(R.string.empty_list) };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.list_item_empty,
                emptyList);
        listView.setAdapter(adapter);
    }

    private void initializeAudioComponents() {
        audioDeviceManager = new AudioDeviceManager(this);
        audioPlayer = new AudioPlayer(this);

        audioPlayer.setOnInitializedListener(success -> {
            runOnUiThread(() -> {
                if (success) {
                    Toast.makeText(MainActivity.this, "Áudio pronto para uso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Erro ao inicializar áudio", Toast.LENGTH_SHORT).show();
                }
            });
        });

        audioDeviceManager.registerAudioDeviceCallback(new AudioDeviceManager.OnAudioDeviceChangeListener() {
            @Override
            public void onBluetoothHeadsetConnected() {
                runOnUiThread(() -> {
                    bluetoothStatusText.setText(getString(R.string.bluetooth_connected));
                    bluetoothStatusText.setTextColor(0xFF4CAF50);
                    Toast.makeText(MainActivity.this, "Fone Bluetooth conectado", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onBluetoothHeadsetDisconnected() {
                runOnUiThread(() -> {
                    bluetoothStatusText.setText(getString(R.string.bluetooth_disconnected));
                    bluetoothStatusText.setTextColor(0xFF888888);
                    Toast.makeText(MainActivity.this, "Fone Bluetooth desconectado", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onSpeakerAvailable() {
                runOnUiThread(() -> {
                    speakerStatusText.setText(getString(R.string.speaker_available));
                    speakerStatusText.setTextColor(0xFF4CAF50);
                });
            }
        });
    }

    private void setupListeners() {
        buttonTestAudio.setOnClickListener(v -> {
            String testMessage = getString(R.string.test_message);
            attemptSpeak(testMessage, "Reproduzindo no alto-falante");
        });

        buttonBluetooth.setOnClickListener(v -> {
            BluetoothHelper.openBluetoothSettings(this);
            Toast.makeText(this, "Abrindo configurações Bluetooth", Toast.LENGTH_SHORT).show();
        });

        buttonReadMessage.setOnClickListener(v -> {
            String message = getString(R.string.notification_message);
            attemptSpeak(message, "Lendo mensagem");
        });
    }

    private void attemptSpeak(String text, String successMessage) {
        if (audioPlayer.isInitialized()) {
            audioPlayer.speak(text);
            Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Inicializando áudio, aguarde...", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler(Looper.getMainLooper());

            handler.postDelayed(() -> {
                if (audioPlayer.isInitialized()) {
                    audioPlayer.speak(text);
                    Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
                } else {
                    handler.postDelayed(() -> {
                        if (audioPlayer.isInitialized()) {
                            audioPlayer.speak(text);
                            Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this,
                                    "TTS não disponível no emulador. Teste em dispositivo real.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }, 3000);
                }
            }, 2000);
        }
    }

    private void updateAudioStatus() {
        AudioHelper audioHelper = audioDeviceManager.getAudioHelper();

        if (audioHelper.isSpeakerAvailable()) {
            speakerStatusText.setText(getString(R.string.speaker_available));
            speakerStatusText.setTextColor(0xFF4CAF50);
        } else {
            speakerStatusText.setText(getString(R.string.speaker_unavailable));
            speakerStatusText.setTextColor(0xFF888888);
        }

        if (audioHelper.isBluetoothHeadsetConnected()) {
            bluetoothStatusText.setText(getString(R.string.bluetooth_connected));
            bluetoothStatusText.setTextColor(0xFF4CAF50);
        } else {
            bluetoothStatusText.setText(getString(R.string.bluetooth_disconnected));
            bluetoothStatusText.setTextColor(0xFF888888);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioDeviceManager != null) {
            audioDeviceManager.unregisterAudioDeviceCallback();
        }
        if (audioPlayer != null) {
            audioPlayer.shutdown();
        }
    }
}