package com.example.study_friend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.study_friend.databinding.ActivityLogInBinding;

public class LogIn extends AppCompatActivity {

    TextView textView;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLogInBinding binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        textView = (TextView) binding.googleLoginBtn.getChildAt(0);
        textView.setText(getString(R.string.googleLogInBtn));

        binding.loginBtn.setOnClickListener(v -> {
            Log.d("LogIn", "LogInBtn Clicked");
            intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

        binding.SignUpBtn.setOnClickListener(v -> {
            Log.d("LogIn", "SignUpBtn Clicked");
            intent = new Intent(this, SignUp.class);
            startActivity(intent);
        });
        binding.passwordResetBtn.setOnClickListener(v -> {
            Log.d("LogIn", "passwordResetBtn Clicked");
            intent = new Intent(this, PasswordReset.class);
            startActivity(intent);
        });
    }
}