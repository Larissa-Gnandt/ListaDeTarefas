package com.example.listadetarefas;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);

        String[] emptyList = { getString(R.string.empty_list) };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.list_item_empty,
                emptyList);

        listView.setAdapter(adapter);
    }
}