package com.example.study_friend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.study_friend.R;
import com.example.study_friend.databinding.ActivityStudyContentBinding;
import com.example.study_friend.fragment.study_fragment;

public class StudyContent extends AppCompatActivity {

    ActivityStudyContentBinding binding;
    Intent intent;

    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudyContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.studyRegBtn.setOnClickListener(v -> {
            builder = new AlertDialog.Builder(StudyContent.this);
            builder.setTitle("스터디 등록 전 확인");
            builder.setMessage("스터디를 등록하시겠습니까?");
            builder.setPositiveButton("확인", (dialog, which) -> {
               builder.setMessage("스터디가 등록되었습니다.");
               intent = new Intent(StudyContent.this, study_fragment.class);
            });
            builder.setNegativeButton("취소", (dialog, which) -> {
                dialog.cancel();
            });

        });
    }

}