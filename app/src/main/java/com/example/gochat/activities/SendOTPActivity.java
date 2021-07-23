package com.example.gochat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gochat.databinding.ActivityVerifyOTPBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTPActivity extends AppCompatActivity {

    private ActivityVerifyOTPBinding binding;
    private PhoneAuthOptions phoneAuthOptions;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOTPBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.sendOTP.setOnClickListener(view -> {

            if(binding.inputCountryCode.getText().toString().trim().isEmpty()
                    || binding.inputPhoneNumber.getText().toString().trim().isEmpty()){
                Toast.makeText(this,"Enter Phone Number",Toast.LENGTH_SHORT).show();
                return;
            }

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.sendOTP.setVisibility(View.GONE);

            phoneNumber = binding.textPlus.getText().toString()
                    + binding.inputCountryCode.getText().toString()
                    + binding.inputPhoneNumber.getText().toString();


            phoneAuthOptions = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                            .setPhoneNumber(phoneNumber)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.sendOTP.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.sendOTP.setVisibility(View.VISIBLE);
                                    Toast.makeText(SendOTPActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.sendOTP.setVisibility(View.VISIBLE);

                                    Intent intent = new Intent(SendOTPActivity.this, VerifyOTPActivity.class);
                                    intent.putExtra("verificationId",verificationId);
                                    intent.putExtra("phoneNumber",phoneNumber);
                                    startActivity(intent);
                                }
                            }).build();          // OnVerificationStateChangedCallback
            PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
        });
    }
}