package com.eli.avocado_util;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import eli.avocado.utils.DeviceUtils;

public class TestDeviceUtilsAct extends AppCompatActivity {

    private static final String TAG = "TestDeviceUtilsAct";

    private TextView deviceInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_device_utils);

        deviceInfo = findViewById(R.id.device_info);
        deviceInfo.setText(getDeviceInfo());
    }

    public void reload(View view) {
        deviceInfo.setText(getDeviceInfo());
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
        sb.append("\nbrand: " + DeviceUtils.getPhoneBrand());
        sb.append("\nmodel: " + DeviceUtils.getPhoneModel());
        sb.append("\nisEmulator: " + DeviceUtils.isEmulator(this));
        sb.append("\nTimeZone: " + DeviceUtils.getCurrentTimeZone());
        sb.append("\nhasSIM: : " + DeviceUtils.hasSimCard(this));
        sb.append("\nprovider: " + DeviceUtils.getProvidersName(this));

        Log.i(TAG, "content: " + sb);

        return sb.toString();
    }
}
