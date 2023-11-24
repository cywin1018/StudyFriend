package com.example.study_friend;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;


import com.example.study_friend.databinding.ActivityStudyTutorBinding;
import com.example.study_friend.fragment.account_fragment;

import java.util.ArrayList;


public class StudyTutor extends AppCompatActivity {

    ActivityStudyTutorBinding binding;
    Intent intent;
    RecyclerView recyclerView;
    studyrecyclerview_adapter studyRecyclerAdapter;
    ArrayList<Item> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudyTutorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 뒤로가기 버튼, 아마 로그인 안 해서 튕기는듯
        binding.backBtn.setOnClickListener(v -> {
            intent = new Intent(StudyTutor.this, account_fragment.class);
            startActivity(intent);
        });
        /* 리사이클러뷰로 리스트 추가하는 부분 */

        items.add(new Item("김민수", "알고리즘 스터디", "월,수,금", "3/5"));
        recyclerView = findViewById(R.id.study_recyclerview);
        studyRecyclerAdapter = new studyrecyclerview_adapter(items);
        recyclerView.setAdapter(studyRecyclerAdapter); // 오류 발생, 튕김
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }
}