package com.example.study_friend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.study_friend.databinding.ActivityPasswordResetBinding;

public class PasswordReset extends AppCompatActivity {
    private ActivityPasswordResetBinding binding;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordResetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backLogin.setOnClickListener(v -> {
            intent = new Intent(this, LogIn.class);
            startActivity(intent);
        });
    }
}