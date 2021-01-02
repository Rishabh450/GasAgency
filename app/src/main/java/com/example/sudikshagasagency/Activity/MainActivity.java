package com.example.sudikshagasagency.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.sudikshagasagency.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    CircleImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        logo = findViewById(R.id.logo);
        logo.setAlpha(0f);
        logo.animate().alpha(1f).setDuration(2000L);
        Thread background = new Thread() {
            public void run() {
                try {
                    Thread.sleep(3000L);
                    Intent intent = new Intent((Context)MainActivity.this, HomeActivity.class);
                    MainActivity.this.startActivity(intent);
                } catch (Exception var2) {
                    var2.printStackTrace();
                }

            }
        };
        background.start();
    }
}