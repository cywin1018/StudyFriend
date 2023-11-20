package com.example.study_friend;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.study_friend.databinding.FragmentStudyMakeWriteBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class StudyMakeWrite extends Fragment {
    FragmentStudyMakeWriteBinding binding;
    Intent intent;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudyMakeWriteBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Map<String,Object>post = new HashMap<>();
        binding.regBttn.setOnClickListener(v->{
            final String people = binding.editPeople.getText().toString().trim();
            final String field = binding.editField.getText().toString().trim();
            final String place = binding.editPlace.getText().toString().trim();
            final String num = binding.editNum.getText().toString().trim();
            final String title = binding.editTitle.getText().toString().trim();
            final String content = binding.editContent.getText().toString().trim();
            posting(people,field,place,num,title,content);

        });
    }
    public void posting(String people,String field,String place,String num,String title,String content){
        Map<String,Object>post = new HashMap<>();
        post.put("모집대상",people);
        post.put("분야",field);
        post.put("장소",place);
        post.put("모집인원",num);
        post.put("제목",title);
        post.put("내용",content);

        db.collection("스터디 게시글").document().set(post);
    }
}