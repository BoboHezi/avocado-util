package com.eli.avocado_util;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import eli.avocado.utils.DeviceUtils;
import eli.avocado.utils.SystemUtils;

public class TestMatcherAct extends AppCompatActivity {

    private static final String TAG = "TestMatcherAct";

    private EditText input;

    private TextView matchLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_matcher);
        input = findViewById(R.id.test_ed);
        matchLabel = findViewById(R.id.labels);

        input.requestFocus();
        input.addTextChangedListener(watcher);

        input.postDelayed(() -> {
            SystemUtils.showKeypad(input);
        }, 60);
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //Log.i(TAG, "beforeTextChanged: " + s);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Log.i(TAG, "onTextChanged: " + s);
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.i(TAG, "afterTextChanged: " + s);
        }
    };
}
