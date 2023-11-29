package com.example.study_friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.study_friend.databinding.ActivityStudyRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class study_register extends AppCompatActivity {
    final static String TAG = "RERE";
    ActivityStudyRegisterBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudyRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //2.
        binding.regBttn.setOnClickListener(view -> {
            final String people = binding.editPeople.getText().toString().trim();
            final String field = binding.editField.getText().toString().trim();
            final String place = binding.editPlace.getText().toString().trim();
            final int num = Integer.parseInt(binding.editNum.getText().toString().trim());
            final String title = binding.editTitle.getText().toString().trim();
            final String content = binding.editContent.getText().toString().trim();
            Timestamp timestamp = Timestamp.now();
            List<String> participants = new ArrayList<>();
            participants.add(currentUser.getUid());
            db.collection("users").document(currentUser.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            String nickname = documentSnapshot.get("nickname").toString();
                            int point = Integer.parseInt(documentSnapshot.get("point").toString());
                            Log.d(TAG,documentSnapshot.get("nickname").toString());
                            posting(people,field,place,num,title,content,participants,timestamp,nickname,point);
                        }
                    });


            // 0: 학부 1:분야 2:장소 3:사람수 4:제목 5:내용
            Intent intent = getIntent();
//            intent.putExtra("data", data);
            setResult(Activity.RESULT_OK,intent);
            finish();
        });

    }

    public void posting(String people,String field,String place,int num,String title,String content,List<String> participants,Timestamp time,String nickname,int point){
        Map<String,Object> post = new HashMap<>();
        int k = 1;
        point = point + 100;
        post.put("모집대상",people);
        post.put("분야",field);
        post.put("장소",place);
        post.put("모집인원",num);
        post.put("제목",title);
        post.put("내용",content);
        post.put("tutorUid",currentUser.getUid());
        post.put("신청인원",k);
        post.put("신청자Uid",participants);
        post.put("time",time);
        post.put("nickname",nickname);

        db.collection("게시글").document(title).set(post);
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
        docRef.update("point",point);
    }
}