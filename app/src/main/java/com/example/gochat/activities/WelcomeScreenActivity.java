package com.example.gochat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.gochat.R;
import com.example.gochat.databinding.ActivityWelcomeScreenBinding;

public class WelcomeScreenActivity extends AppCompatActivity {

    private ActivityWelcomeScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_welcome_screen);

        binding.agreeBtn.setOnClickListener(view -> {
            startActivity(new Intent(WelcomeScreenActivity.this, LoginActivity.class));
            finish();
        });
    }
}