package com.eli.avocado_util;

import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import eli.avocado.utils.StringUtils;
import eli.avocado.utils.SystemUtils;

public class TestMatcherAct extends AppCompatActivity {

    private static final String TAG = "TestMatcherAct";

    private static HashMap<Pattern, Object[]> PATTERNS = new HashMap<Pattern, Object[]>() {
        {
            put(StringUtils.EMAIL, new Object[]{"EMAIL", 0xFF6200EE});
            put(StringUtils.IMG_URL, new Object[]{"IMG_URL", 0xFF3700B3});
            put(StringUtils.PHONE, new Object[]{"PHONE", 0xFF03DAC5});
            put(StringUtils.VALID_URL, new Object[]{"VALID_URL", 0xFF2E771C});
            put(StringUtils.EMOJI, new Object[]{"EMOJI", 0xFF762819});
        }
    };

    private EditText input;

    private TextView matchLabel;
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
            String str = s.toString();
            matchLabel.setText("");

            SpannableStringBuilder sb = new SpannableStringBuilder(str);
            for (Pattern pattern : PATTERNS.keySet()) {
                List<Integer> indexes = StringUtils.findSth(str, pattern);
                if (indexes != null && indexes.size() > 1 && indexes.size() % 2 == 0) {
                    String label = (String) PATTERNS.get(pattern)[0];
                    int color = (int) PATTERNS.get(pattern)[1];

                    for (int i = 0; i < indexes.size(); i += 2) {
                        sb.setSpan(new BackgroundColorSpan(color),
                                indexes.get(i), indexes.get(i + 1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    SpannableStringBuilder ssb = new SpannableStringBuilder(label);
                    ssb.setSpan(new ForegroundColorSpan(color), 0, label.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    matchLabel.append(",");
                    matchLabel.append(ssb);
                }
            }
            int start = input.getSelectionStart();
            int end = input.getSelectionEnd();
            input.removeTextChangedListener(this);
            input.setText(sb);
            input.addTextChangedListener(this);
            input.setSelection(start, end);

            if (StringUtils.isEmailAddress(str)) {
                matchLabel.append("\nisEmailAddress");
            }
            if (StringUtils.isImageUrl(str)) {
                matchLabel.append("\nisImageUrl");
            }
            if (StringUtils.isPhoneNumber(str)) {
                matchLabel.append("\nisPhoneNumber");
            }
            if (StringUtils.isValidUrl(str)) {
                matchLabel.append("\nisValidUrl");
            }
            if (StringUtils.isEmoji(str)) {
                matchLabel.append("\nisEmoji");
            }
        }
    };

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

    public void hideIME(View view) {
        SystemUtils.hideKeypad(input);
    }
}