package com.example.study_friend.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.study_friend.R;
import com.example.study_friend.StudyTutee;
import com.example.study_friend.StudyTutor;
import com.example.study_friend.activity.MainActivity;
import com.example.study_friend.databinding.FragmentAccountFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
/*
 * 1. 내가 만든 스터디 내가 참여한 스터디 연결
 * 2. 개추 시스템
 * 3. 포인트 시스템
 */

public class account_fragment extends Fragment {
    FragmentAccountFragmentBinding binding;
    Intent intent;
    FirebaseUser user;
    String uid;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountFragmentBinding.inflate(inflater);

        TextView tutorBtn = binding.getRoot().findViewById(R.id.tutorBtn);
        TextView tuteeBtn = binding.getRoot().findViewById(R.id.tuteeBtn);
        tutorBtn.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "튜터로 이동합니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), StudyTutor.class);
            startActivity(intent);
        });
        tuteeBtn.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "튜티로 이동합니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), StudyTutee.class);
            startActivity(intent);
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        binding.logoutBtn.setOnClickListener(v->{
            FirebaseAuth.getInstance().signOut();
            intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        });
        /*oncreateview로 이동이 될 것인가?*/
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Map<String, Object> users = document.getData();
                        String nickname = (String)users.get("nickname");
                        String univ =(String)users.get("univ");
                        String semester = (String)users.get("semester");
                        String major =(String)users.get("major");
                        binding.profileName.setText(nickname +"님");
                        binding.profileSemester.setText(semester +"학기");
                        binding.profileInfo.setText(univ + " " + major);
                    }
                }
            }
        });
    }
}