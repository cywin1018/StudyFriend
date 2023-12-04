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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        binding.backChatBtn.setOnClickListener(view -> {
            finish();
        });
        //2.
        binding.regBttn.setOnClickListener(view -> {
//            final String people = binding.editPeople.getText().toString().trim();
            final String field = binding.editField.getText().toString().trim();
            final String place = binding.editPlace.getText().toString().trim();
            final int num = Integer.parseInt(binding.spinnerNum.getSelectedItem().toString());
            final String title = binding.editTitle.getText().toString().trim();
            final String content = binding.editContent.getText().toString().trim();
            final String major = binding.spinnerMajor.getSelectedItem().toString();
            final String grade = binding.spinnerGrade.getSelectedItem().toString();

            Timestamp timestamp = Timestamp.now();

            Long nowdate = System.currentTimeMillis();
            Date currentTimestamp = new java.sql.Timestamp(nowdate);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String mydate= String.valueOf(simpleDateFormat.format(currentTimestamp));
            Log.d("MYMY", "time: " + mydate);

            List<String> participants = new ArrayList<>();
            List<String> allPeople = new ArrayList<>();
            List<String> recommendedPeople = new ArrayList<>();
            allPeople.add(currentUser.getUid());
            db.collection("users").document(currentUser.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            String nickname = documentSnapshot.get("nickname").toString();
                            int point = Integer.parseInt(documentSnapshot.get("point").toString());
                            Log.d(TAG,documentSnapshot.get("nickname").toString());
                            posting(major,grade,field,place,num,title,content,participants,timestamp,mydate,nickname,point,allPeople,recommendedPeople);
                        }
                    });


            // 0: 학부 1:분야 2:장소 3:사람수 4:제목 5:내용
            Intent intent = getIntent();
//            intent.putExtra("data", data);
            setResult(Activity.RESULT_OK,intent);
            finish();
        });

    }

    public void posting(String major,String grade,String field,String place,int num,String title,String content,List<String> participants,Timestamp time,String date,String nickname,int point,List<String> allPeople,List<String> recommendedPeople){
        Map<String,Object> post = new HashMap<>();
        int k = 0;
        point = point + 100;
        post.put("모집대상",major);
        post.put("모집학년",grade);
        post.put("분야",field);
        post.put("장소",place);
        post.put("모집인원",num);
        post.put("제목",title);
        post.put("내용",content);
        post.put("tutorUid",currentUser.getUid());
        post.put("신청인원",k);
        post.put("신청자Uid",participants);
        post.put("time",time);
        post.put("모집기간",date);
        post.put("nickname",nickname);
        post.put("allPeople",allPeople);
        post.put("recommendedPeople",recommendedPeople);
        db.collection("게시글").document(title).set(post);
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
        docRef.update("point",point);
    }
}