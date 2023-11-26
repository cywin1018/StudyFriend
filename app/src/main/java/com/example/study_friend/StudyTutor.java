package com.example.study_friend;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.example.study_friend.databinding.ActivityStudyTutorBinding;
import com.example.study_friend.fragment.account_fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class StudyTutor extends AppCompatActivity {

    ActivityStudyTutorBinding binding;
    Intent intent;
    RecyclerView recyclerView;
    studyrecyclerview_adapter studyRecyclerAdapter;
    ArrayList<Item> items = new ArrayList<>();
    Item itemData;
    /* Firestore */
    FirebaseUser user;
    String uid;
    FirebaseFirestore db;
    CollectionReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudyTutorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("게시글");
        // 뒤로가기 버튼, 아마 로그인 안 해서 튕기는듯
//        binding.backBtn.setOnClickListener(v -> {
//            intent = new Intent(StudyTutor.this, account_fragment.class);
//            startActivity(intent);
//        });
        // 현재 사용자의 정보를 가져오는 부분


        db.collection("게시글")
                .whereEqualTo("tutorUid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            recyclerView = findViewById(R.id.tutor_recyclerview_list);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            for (QueryDocumentSnapshot posts : task.getResult()) {
                                Map<String, Object> post = posts.getData();
                                String nickname = post.get("내용").toString();
                                String date = post.get("장소").toString();
                                String title = post.get("제목").toString();
                                String people = post.get("모집인원").toString();
                                itemData = new Item("닉네임", title, date, people);
                                items.add(itemData);

                                studyRecyclerAdapter = new studyrecyclerview_adapter(items);
                                recyclerView.setAdapter(studyRecyclerAdapter);
                            }
                        }
                    }
                });


        /* 리사이클러뷰로 리스트 추가하는 부분 */
        // 리사이클러뷰 초기화 및 설정
//        recyclerView = findViewById(R.id.tutor_recyclerview_list);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);

        // 아이템 추가
//        itemData = new Item("자바", "자바는 객체지향 프로그래밍 언어이다. 자바는 웹, 모바일, 빅데이터, AI 등 다양한 분야에서 활용되고 있다.", "자바","gg");
//        items.add(itemData);

        // 어댑터 설정
//        studyRecyclerAdapter = new studyrecyclerview_adapter(items);
//        recyclerView.setAdapter(studyRecyclerAdapter);


    }
}