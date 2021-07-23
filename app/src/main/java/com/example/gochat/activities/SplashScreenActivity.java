package com.example.gochat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gochat.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {

    private ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashScreenActivity.this, SendOTPActivity.class));
            finish();
        }, 2000);
    }
}