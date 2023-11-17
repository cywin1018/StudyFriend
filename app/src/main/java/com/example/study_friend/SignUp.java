package com.example.study_friend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_friend.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "signUP";
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.submitBtn.setOnClickListener(v -> {
            final String email = binding.editEmail.getText().toString().trim();
            final String password = binding.editPassword.getText().toString().trim();
            final String nickname = binding.editNickname.getText().toString().trim();
            final String univ = binding.univ.getText().toString().trim();
            final String major = binding.major.getText().toString().trim();
            final String semester = binding.semester.getText().toString().trim();
            createAccount(email,password,nickname,univ,major,semester);
            //회원가입이 성공했다면, 로그인 화면으로 이동
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("회원가입 성공");
//            builder.setPositiveButton("확인", (dialog, which) -> {
//                updateUI(null);
//            });
//            builder.show(); // 다이얼로그 표시
        });
    }
    private void createAccount(String email, String password,String nickname,String univ,String major,String semester){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"CreateUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Map<String,Object> userMap = new HashMap<>();
                            userMap.put("FirebaseID.documentID",user.getUid());
                            userMap.put("FirebaseID.email",email);
                            userMap.put("FirebaseID.nickname",nickname);
                            userMap.put("FirebaseID.univ",univ);
                            userMap.put("FirebaseId.major",major);
                            userMap.put("FirebaseID.semester",semester);
                            db.collection("FirebaseID.user").document(user.getUid()).set(userMap, SetOptions.merge());

                            AlertDialog.Builder builder = new AlertDialog.Builder(getLayoutInflater().getContext());
                            builder.setTitle("회원가입 성공");
                            builder.setPositiveButton("확인",(dialog,which) ->{
                                updateUI(null);
                            });
                            builder.show();
                        }
                    }
                });
    }
    public void updateUI(FirebaseUser user){
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}