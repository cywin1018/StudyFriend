package com.example.study_friend;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.study_friend.R;
import com.example.study_friend.databinding.ActivityStudyContentBinding;
import com.example.study_friend.fragment.study_fragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class StudyContent extends AppCompatActivity {

    FirebaseFirestore db;
    CollectionReference nicknameRef;
    ActivityStudyContentBinding binding;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudyContentBinding.inflate(getLayoutInflater());

        Intent intent1 =getIntent();
        String sel_name = intent1.getStringExtra("name");
        String sel_name2 = intent1.getStringExtra("gibomi");

        Log.d("MYMY", "study_content: "+sel_name2.toString());

        db = FirebaseFirestore.getInstance();

        nicknameRef = db.collection("TestCollection");
        Log.d("MYMY", "nickname 찾는중입니다... ");

        nicknameRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (int i = 0; i < task.getResult().size(); i++) {

                    String nickname = task.getResult().getDocuments().get(i).get("nickname").toString();
//                    Log.d("MYMY", "database 안에 있는 nickname: "+nickname);
                    if (sel_name2.equals(nickname)) {
                        Log.d("MYMY", "equal(nickname)"+nickname);

                    }
                }

            }
        });

        //임의로 만든 테스트 닉네임이 데이터베이스랑 안맞는 데이터라서 찾기가 안됨;;

        setContentView(binding.getRoot());

        binding.studyRegBtn.setOnClickListener(v -> {
            Log.d("yongwon","스터디 신청 버튼 클릭");
            AlertDialog.Builder menu = new AlertDialog.Builder(StudyContent.this);

            String people = "컴퓨터학부";
            String grade = "1학년";
            String field = "프로그래밍";
            String place = "정보과학관 3층 중간 스터디룸";
            String num = "5명";
            menu.setTitle("작성 목록");
            menu.setMessage("모집대상 " + people + "\n" + "학년 " + grade + "\n" + "분야 " + field + "\n" + "장소 " + place + "\n" + "인원 " + num + "\n");
            menu.setPositiveButton("Ok",null);
            menu.setNegativeButton("Cancel",null);
            menu.create();
            menu.show();
        });
    }

}