package com.example.listadetarefas;

import android.content.Intent;
import android.provider.Settings;
import android.app.Activity;

public class BluetoothHelper {
    public static void openBluetoothSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("EXTRA_CONNECTION_ONLY", true);
        intent.putExtra("EXTRA_CLOSE_ON_CONNECT", true);
        intent.putExtra("android.bluetooth.devicepicker.extra.FILTER_TYPE", 1);
        activity.startActivity(intent);
    }
}
