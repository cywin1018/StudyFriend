package com.example.study_friend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_friend.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
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
        binding.confirm.setOnClickListener(v->{
            Log.d(TAG,"sex");
            String nickname = binding.editNickname.getText().toString().trim();
            Log.d(TAG,nickname);
            if(nickname != null){
                db.collection("users")
                        .whereEqualTo("nickname",nickname)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    QuerySnapshot snapshots = task.getResult();
                                    List<DocumentChange> documentChanges = snapshots.getDocumentChanges();
                                    if(documentChanges.isEmpty()){
                                        Toast.makeText(SignUp.this,"가능한 닉네임입니다",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(SignUp.this,"이미 존재하는 닉네임입니다",Toast.LENGTH_SHORT).show();
                                        binding.editNickname.setText("");
                                    }
                                }
                            }
                        });
            }
        });
        binding.submitBtn.setOnClickListener(v -> {
            final String email = binding.editEmail.getText().toString().trim();
            final String password = binding.editPassword.getText().toString().trim();
            final String nickname = binding.editNickname.getText().toString().trim();
            final String univ = binding.univ.getText().toString().trim();
            final String major = binding.major.getText().toString().trim();
            final String semester = binding.semester.getText().toString().trim();
            createAccount(email,password,nickname,univ,major,semester);
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
                            userMap.put("documentID",user.getUid());
                            userMap.put("email",email);
                            userMap.put("nickname",nickname);
                            userMap.put("univ",univ);
                            userMap.put("major",major);
                            userMap.put("semester",semester);
                            db.collection("users").document(user.getUid()).set(userMap, SetOptions.merge());

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