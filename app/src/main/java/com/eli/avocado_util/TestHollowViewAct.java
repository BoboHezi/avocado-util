package com.eli.avocado_util;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import eli.avocado.widget.GradientLayout;

public class TestHollowViewAct extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hollow_view);

        GradientLayout gradient = findViewById(R.id.gradient_layout);

        gradient.postDelayed(new Runnable() {
            @Override
            public void run() {
                gradient.setAngle(gradient.getAngle() + 1);
                gradient.postDelayed(this, 10);
            }
        }, 1000);
    }
}
