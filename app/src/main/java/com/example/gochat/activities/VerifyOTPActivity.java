package com.example.gochat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gochat.databinding.ActivityVerifyOTPBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOTPActivity extends AppCompatActivity {

    private ActivityVerifyOTPBinding binding;
    private String phoneNumber,verificationId;
    private PhoneAuthCredential phoneAuthCredential;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOTPBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpInputCodes();

        phoneNumber = getIntent().getStringExtra("phoneNumber");
        verificationId = getIntent().getStringExtra("verificationId");

        binding.textMobile.setText(phoneNumber);

        binding.verifyOTP.setOnClickListener(view -> {
            if(binding.inputCode1.toString().trim().isEmpty()
            || binding.inputCode2.toString().trim().isEmpty()
            || binding.inputCode3.toString().trim().isEmpty()
            || binding.inputCode4.toString().trim().isEmpty()
            || binding.inputCode5.toString().trim().isEmpty()
            || binding.inputCode6.toString().trim().isEmpty()){
                Toast.makeText(VerifyOTPActivity.this, "Enter valid code", Toast.LENGTH_SHORT).show();
                return;
            }

            if(verificationId != null){
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.verifyOTP.setVisibility(View.GONE);

                String code = binding.inputCode1.getText().toString() +
                        binding.inputCode2.getText().toString() +
                        binding.inputCode3.getText().toString() +
                        binding.inputCode4.getText().toString() +
                        binding.inputCode5.getText().toString() +
                        binding.inputCode6.getText().toString();

                phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);

                mAuth = FirebaseAuth.getInstance();

                signInWithPhoneAuthCredential(phoneAuthCredential);
            }
        });

        binding.textResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(VerifyOTPActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                            }

                            @Override
                            public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verificationId = newVerificationId;
                                Toast.makeText(VerifyOTPActivity.this,"OTP send",Toast.LENGTH_SHORT).show();
                            }
                        }).build();          // OnVerificationStateChangedCallback
                PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.verifyOTP.setVisibility(View.VISIBLE);
                            Intent intent = new Intent(VerifyOTPActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.verifyOTP.setVisibility(View.VISIBLE);
                            Toast.makeText(VerifyOTPActivity.this,"The Entered Code was invalid",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void setUpInputCodes(){
        binding.inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                    binding.inputCode2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                    binding.inputCode3.requestFocus();
                else
                    binding.inputCode1.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                    binding.inputCode4.requestFocus();
                else
                    binding.inputCode2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                    binding.inputCode5.requestFocus();
                else
                    binding.inputCode3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                    binding.inputCode6.requestFocus();
                else
                    binding.inputCode4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.inputCode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().isEmpty())
                    binding.inputCode5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}