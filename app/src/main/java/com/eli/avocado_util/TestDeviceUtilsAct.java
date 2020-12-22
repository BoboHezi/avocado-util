package com.eli.avocado_util;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

import eli.avocado.utils.DeviceUtils;

public class TestDeviceUtilsAct extends AppCompatActivity {

    private static final String TAG = "TestDeviceUtilsAct";

    private TextView deviceInfo;

    private boolean statusBarChecked;
    private boolean navigationChecked;
    private boolean fullScreenChecked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFlag(statusBarChecked, navigationChecked, fullScreenChecked);

        setContentView(R.layout.activity_device_utils);

        deviceInfo = findViewById(R.id.device_info);
        deviceInfo.setText(getDeviceInfo());

        Switch statusSwitch = findViewById(R.id.switch_status);
        Switch naviSwitch = findViewById(R.id.switch_navigation);
        Switch fullSwitch = findViewById(R.id.switch_full);

        statusSwitch.setChecked(statusBarChecked);
        naviSwitch.setChecked(navigationChecked);
        fullSwitch.setChecked(fullScreenChecked);

        statusSwitch.setOnCheckedChangeListener(this::checkedChanged);
        naviSwitch.setOnCheckedChangeListener(this::checkedChanged);
        fullSwitch.setOnCheckedChangeListener(this::checkedChanged);
    }

    private void checkedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.switch_status) {
            statusBarChecked = isChecked;
        } else if (buttonView.getId() == R.id.switch_navigation) {
            navigationChecked = isChecked;
        } else if (buttonView.getId() == R.id.switch_full) {
            fullScreenChecked = isChecked;
        }
        setFlag(statusBarChecked, navigationChecked, fullScreenChecked);
        reload(null);
    }

    public void reload(View view) {
        deviceInfo.setText(getDeviceInfo());
    }

    @SuppressLint("MissingPermission")
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
        sb.append("\nbrand: " + DeviceUtils.getPhoneBrand());
        sb.append("\nmodel: " + DeviceUtils.getPhoneModel());
        sb.append("\nisEmulator: " + DeviceUtils.isEmulator(this));
        sb.append("\nTimeZone: " + DeviceUtils.getCurrentTimeZone());
        sb.append("\nhasSIM: : " + Arrays.toString(DeviceUtils.getSimCardStatus(this)));
        sb.append("\nprovider: " + Arrays.toString(DeviceUtils.getProvidersName(this)));
        sb.append("\nCover StatusBar: " + DeviceUtils.isCoverStatusBar(this));
        sb.append("\nCover NavBar: " + DeviceUtils.isCoverNavBar(this));

        Log.i(TAG, "content: " + sb);

        return sb.toString();
    }

    private void setFlag(boolean translucentStatusBar, boolean translucentNavigation, boolean fullScreen) {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 应用占据状态栏，状态栏变成半透明（无视状态栏是否隐藏）
            if (translucentStatusBar) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            // 应用占据导航栏，状态栏，状态栏不变（无视导航栏是否隐藏）
            if (translucentNavigation) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
            // 隐藏状态栏
            if (fullScreen) {
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }
    }

    private void hideStatusBar(final boolean hide) {
        if (hide) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(0);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }
}
