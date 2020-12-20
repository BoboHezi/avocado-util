package com.eli.avocado_util;

import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import eli.avocado.utils.StringUtils;
import eli.avocado.utils.SystemUtils;

public class TestMatcherAct extends AppCompatActivity {

    private static final String TAG = "TestMatcherAct";

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
            if (StringUtils.containsEmoji(str)) {
                SpannableStringBuilder ssb = new SpannableStringBuilder("Emoji");
                ssb.setSpan(new ForegroundColorSpan(0xff1d77f2), 0, "Emoji".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                matchLabel.append(" ");
                matchLabel.append(ssb);
            }
            if (StringUtils.containSpecialEmoji(str)) {
                SpannableStringBuilder ssb = new SpannableStringBuilder("Special Emoji");
                ssb.setSpan(new ForegroundColorSpan(0xff1d77f2), 0, "Special Emoji".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                matchLabel.append(" ");
                matchLabel.append(ssb);
            }
            if (StringUtils.isValidUrl(str)) {
                SpannableStringBuilder ssb = new SpannableStringBuilder("Valid Url");
                ssb.setSpan(new ForegroundColorSpan(0xff1d77f2), 0, "Valid Url".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                matchLabel.append(" ");
                matchLabel.append(ssb);
            }
            if (StringUtils.isUrl(str)) {
                SpannableStringBuilder ssb = new SpannableStringBuilder("is Url");
                ssb.setSpan(new ForegroundColorSpan(0xff1d77f2), 0, "is Url".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                matchLabel.append(" ");
                matchLabel.append(ssb);
            }
            if (StringUtils.isEmailAddress(str)) {
                SpannableStringBuilder ssb = new SpannableStringBuilder("Email Address");
                ssb.setSpan(new ForegroundColorSpan(0xff1d77f2), 0, "Email Address".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                matchLabel.append(" ");
                matchLabel.append(ssb);
            }
            if (StringUtils.isImageUrl(str)) {
                SpannableStringBuilder ssb = new SpannableStringBuilder("Image Url");
                ssb.setSpan(new ForegroundColorSpan(0xff1d77f2), 0, "Image Url".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                matchLabel.append(" ");
                matchLabel.append(ssb);
            }
            if (StringUtils.isPhoneNumber(str)) {
                SpannableStringBuilder ssb = new SpannableStringBuilder("Phone Number");
                ssb.setSpan(new ForegroundColorSpan(0xff1d77f2), 0, "Phone Number".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                matchLabel.append(" ");
                matchLabel.append(ssb);
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