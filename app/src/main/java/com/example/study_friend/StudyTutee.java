package com.example.study_friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.study_friend.databinding.ActivityStudyTuteeBinding;
import com.example.study_friend.databinding.ActivityStudyTutorBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class StudyTutee extends AppCompatActivity {
    ActivityStudyTuteeBinding binding;
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
        binding = ActivityStudyTuteeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = "fZ92A3CVJCgRXbO6dkuFfCck8oz2";
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("게시글");

        binding.backBtn.setOnClickListener(v -> {
            intent = new Intent(StudyTutee.this, HomeActivity.class);
            startActivity(intent);
        });
        db.collection("게시글")
                .whereArrayContains("신청자Uid", uid)
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
    }
}