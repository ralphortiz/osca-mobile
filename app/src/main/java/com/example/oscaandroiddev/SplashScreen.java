package com.example.oscaandroiddev;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import pl.droidsonroids.gif.GifImageView;

public class SplashScreen extends AppCompatActivity {

    private final static int SPLASH_SCREEN = 1400;
    ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        ivLogo = findViewById(R.id.ivLogo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, Login.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(ivLogo, "logo_transition");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            }
        }, SPLASH_SCREEN);
    }
}