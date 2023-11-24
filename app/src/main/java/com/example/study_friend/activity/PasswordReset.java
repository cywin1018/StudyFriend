package com.example.study_friend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_friend.databinding.ActivityPasswordResetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
/*
* 이건 그냥 끝남
*/

public class PasswordReset extends AppCompatActivity {
    private ActivityPasswordResetBinding binding;
    Intent intent;
    private final static String TAG ="PasswordReset";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordResetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.backLogin.setOnClickListener(v -> {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        binding.sendResetBtn.setOnClickListener(v -> {
            sendPasswordReset();
            intent = new Intent(this,MainActivity.class);
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
                            /*메일이 보내짐을 알리기만 하자*/
                            Log.d(TAG,"Email sent");
                        }else{
                            Log.d(TAG,"Email unsent");
                        }
                    }
                });
    }
}