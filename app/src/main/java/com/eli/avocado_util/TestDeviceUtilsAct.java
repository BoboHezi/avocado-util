package com.eli.avocado_util;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import eli.avocado.utils.DeviceUtils;

public class TestDeviceUtilsAct extends AppCompatActivity {

    private static final String TAG = "TestDeviceUtilsAct";

    private static final long DELAY = 500;

    private TextView deviceInfo;
    private View content;

    private boolean statusBarHided;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_device_utils);

        hideStatusBar(true);

        content = getWindow().getDecorView().findViewById(android.R.id.content);

        deviceInfo = findViewById(R.id.device_info);

        deviceInfo.postDelayed(() -> {
            deviceInfo.setText(getDeviceInfo());
        }, DELAY);
    }

    public void reload(View view) {
        hideStatusBar(!statusBarHided);

        deviceInfo.postDelayed(() -> {
            deviceInfo.setText(getDeviceInfo());
        }, DELAY);
    }

    private String getDeviceInfo() {
        StringBuilder sb = new StringBuilder();

        int deviceWidth = DeviceUtils.deviceWidth(this);
        int deviceHeight = DeviceUtils.deviceHeight(this);

        int appWidth = DeviceUtils.appWidth(this);
        int appHeight = DeviceUtils.appHeight(this);

        int statusBarHeight = DeviceUtils.statusBarHeight(this);
        int navigationHeight = DeviceUtils.navigationBarHeight(this);

        sb.append("device: " + deviceWidth + " x " + deviceHeight);
        sb.append("\napp: " + appWidth + " x " + appHeight);
        sb.append("\nstatusBarHeight: " + statusBarHeight);
        sb.append("\nnavigationHeight: " + navigationHeight);
        sb.append("\ncontent: {" + content.getLeft() + ", " + content.getTop() + ", " + content.getRight() + ", " + content.getBottom() + "}");

        Log.i(TAG, "content: " + content);

        return sb.toString();
    }

    private void hideStatusBar(final boolean hide) {
        if (hide) {
            if (Build.VERSION.SDK_INT >= 21) {
                View decorView = getWindow().getDecorView();
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                //getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(0);
                //getWindow().setStatusBarColor(Color.BLACK);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
        statusBarHided = hide;
    }
}
