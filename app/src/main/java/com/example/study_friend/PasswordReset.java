package com.example.study_friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.study_friend.databinding.ActivityPasswordResetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordReset extends AppCompatActivity {
    private ActivityPasswordResetBinding binding;
    Intent intent;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private final static String TAG ="PasswordReset";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordResetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String email = binding.editID.getText().toString().trim();


        binding.backLogin.setOnClickListener(v -> {
            intent = new Intent(this, LogIn.class);
            startActivity(intent);
        });
        binding.loginBtn.setOnClickListener(v -> {
            sendPasswordReset();
            intent = new Intent(this,LogIn.class);
            startActivity(intent);
        });
    }
    public void sendPasswordReset(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = binding.editID.getText().toString().trim();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"Email sent");
                        }else{
                            Log.d(TAG,"Email unsent");
                        }
                    }
                });
    }
}