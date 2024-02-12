package com.ai.courses.coursesbank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.CountDownTimer;


public class SplashActivity extends AppCompatActivity {

    private int mSplash_Timer = 6000; // Splash screen timer in milliseconds
    private CountDownTimer mTimeCounter = new CountDownTimer(mSplash_Timer, 100) {
        @Override
        public void onTick(long millisUntilFinished) {
            // Not used in this example
        }

        @Override
        public void onFinish() {
            mNextPage(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mTimeCounter.start();
    }

    private void mNextPage(Intent intent) {
        startActivity(intent);
    }
}
