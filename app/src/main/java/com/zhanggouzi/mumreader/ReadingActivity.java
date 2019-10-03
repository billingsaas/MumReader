package com.zhanggouzi.mumreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ReadingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_activity);
        initService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText editText = findViewById(R.id.editText);
        CharSequence clipText = ClipboardHelper.getText(this);
        editText.setText(clipText);
        if (!TextUtils.isEmpty(clipText)) {
            ReaderHelper.readText(editText.getText().toString());
        }

        Button button = findViewById(R.id.clearText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                ReaderHelper.readText("");
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReaderHelper.readText(editText.getText().toString());
            }
        });
    }

    private void initService() {
        Intent intent = new Intent();
        intent.setClass(this, PasteCopyService.class);
        startForegroundService(intent);
    }
}
