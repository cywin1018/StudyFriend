package com.example.study_friend.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study_friend.R;
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
    private static final String TAG = "RERE";
    Intent intent;
    String email;
    String password;
    String passwordconfirm;

    String emailValidation = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
    String passwordValidation = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.editEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @SuppressLint("ResourceAsColor")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email = binding.editEmail.getText().toString().trim();
                if(email.matches(emailValidation)){
                    binding.emailCheck.setText("유효한 이메일입니다");
                    binding.emailCheck.setTextColor(R.color.green);
                }
                else{
                    binding.emailCheck.setText("올바르지 않은 이메일 형식입니다.");

                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        } );
        binding.editPassword.addTextChangedListener(new TextWatcher(){

            @SuppressLint("ResourceAsColor")
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password = binding.editPassword.getText().toString().trim();
                if(password.matches(passwordValidation)){
                    binding.passwordValidation.setText("유효한 비밀번호입니다");
                    binding.passwordValidation.setTextColor(R.color.green);
                }
                else{
                    binding.passwordValidation.setText("비밀번호는 8자리 이상, 영문자와 숫자를 포함해야 합니다.");
                    binding.passwordValidation.setTextColor(R.color.red);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.editPasswordConfirm.addTextChangedListener(new TextWatcher(){

            @SuppressLint("ResourceAsColor")
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password = binding.editPassword.getText().toString().trim();
                passwordconfirm = binding.editPasswordConfirm.getText().toString().trim();
                if(password.equals(passwordconfirm)){
                    binding.passwordCheck.setText("비밀번호가 일치합니다");
                    binding.passwordCheck.setTextColor(R.color.green);
                }
                else{
                    binding.passwordCheck.setText("비밀번호가 일치하지 않습니다");
                    binding.passwordCheck.setTextColor(R.color.red);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.confirm.setOnClickListener(v->{
            Log.d(TAG,"중복확인 버튼이 눌림");
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
                                        binding.submitBtn.setOnClickListener(v -> {
                                            final String email = binding.editEmail.getText().toString().trim();
                                            final String password = binding.editPassword.getText().toString().trim();
                                            final String nickname = binding.editNickname.getText().toString().trim();
                                            final String univ = binding.univ.getText().toString().trim();
                                            final String major = binding.major.getText().toString().trim();
                                            final String semester = binding.semester.getText().toString().trim();
                                            createAccount(email, password, nickname, univ, major, semester);
                                        });
                                    }
                                    else{
                                        Toast.makeText(SignUp.this,"이미 존재하는 닉네임입니다",Toast.LENGTH_SHORT).show();
                                        binding.editNickname.setText("");
                                        binding.submitBtn.setOnClickListener(v -> {
                                            Toast.makeText(SignUp.this,"중복확인을 먼저 해주세요",Toast.LENGTH_SHORT).show();
                                        });
                                    }

                                }
                            }
                        });
            }
        });
        binding.submitBtn.setOnClickListener(v -> {
            Toast.makeText(SignUp.this,"중복확인을 먼저 해주세요",Toast.LENGTH_SHORT).show();
        });
    }
    private void createAccount(String email, String password,String nickname,String univ,String major,String semester){

        //유효한 이메일 주소라면 계정 생성
        //1.파이어베이스 인증 객체 가져오기
        //2.이메일 비밀번호로 계정 생성하기
        //3.계정 생성에 성공하면 UI 갱신
        //4.계정 생성에 실패하면 실패 이유 보여주기
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"CreateUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            int recommendedValue = 0;
                            int point = 100;
                            Map<String,Object> userMap = new HashMap<>();
                            userMap.put("documentID",user.getUid());
                            userMap.put("email",email);
                            userMap.put("nickname",nickname);
                            userMap.put("univ",univ);
                            userMap.put("major",major);
                            userMap.put("semester",semester);
                            userMap.put("recommended",recommendedValue);
                            userMap.put("point",point);
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