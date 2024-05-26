package com.raja.resturant.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.raja.resturant.MainActivity;
import com.raja.resturant.R;


public class SplashScreen extends AppCompatActivity {
    private static final int SPLASH_DELAY = 4000; // 5 seconds in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        final ProgressBar progressBar = findViewById(R.id.progressBar);

        // Simulate progress for demonstration purposes (replace with actual initialization)
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int progress = 0; progress <= 100; progress += 1) {
                    try {
                        Thread.sleep(40); // Sleep for 250ms per 5% progress
                        int finalProgress = progress;
                        runOnUiThread(() -> progressBar.setProgress(finalProgress));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Navigate to MainActivity
                runOnUiThread(() -> {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
        }).start();


    }
}