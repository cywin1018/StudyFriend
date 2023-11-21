package com.example.study_friend.activity;

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
import com.google.firebase.firestore.DocumentChange;
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
//                CollectionReference userRef = db.collection("FirebaseID.user");
//                Query query = userRef.whereEqualTo("FirebaseID.nickname",nickname);
//                Log.d(TAG,"여기까지는 성공");
//                Task<QuerySnapshot>task = query.get();
//                QueryDocumentSnapshot document;
                db.collection("users")
                        .whereEqualTo("nickname","gibomi")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    Log.d(TAG,"불러는 들어왔다");//진짜 불러는 들어왔다.
                                    QuerySnapshot snapshots = task.getResult();
                                    Log.d("snapshots",snapshots.toString());
                                    List<DocumentChange> documentChanges = snapshots.getDocumentChanges();
                                    Log.d("snapshots",documentChanges.toString());
                                    for(DocumentChange documentChange : documentChanges){
                                        Log.d(TAG,"인간적으로 이건 해라");
                                    }
                                    for(QueryDocumentSnapshot document : task.getResult()){
                                        Log.d(TAG,document.getId() + "=>" + document.getData());
                                    }
                                }else{
                                    Log.d(TAG,"Error",task.getException());
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
                            db.collection("user").document(user.getUid()).set(userMap, SetOptions.merge());

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