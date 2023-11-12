package com.example.study_friend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.study_friend.databinding.ActivitySignUpBinding;

public class SignUp extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.submitBtn.setOnClickListener(v -> {
            //회원가입이 성공했다면, 로그인 화면으로 이동
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("회원가입 성공");
            builder.setPositiveButton("확인", (dialog, which) -> {
                intent = new Intent(this, LogIn.class);
                startActivity(intent);
            });
            builder.show(); // 다이얼로그 표시
        });
    }
}