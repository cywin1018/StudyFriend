package com.example.study_friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.study_friend.databinding.ActivityStudyTuteeBinding;
import com.example.study_friend.databinding.ActivityStudyTutorBinding;
import com.example.study_friend.fragment.account_fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudyTutee extends AppCompatActivity {
    ActivityStudyTuteeBinding binding;
    Intent intent;
    RecyclerView recyclerView;
    TuteeAdapter tuteeAdapter;
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
        uid = user.getUid();
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("게시글");

        binding.backBtn.setOnClickListener(v -> {
           finish();
        });
        db.collection("게시글")
                .whereArrayContains("신청자Uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            recyclerView = findViewById(R.id.tutee_recyclerview_list);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            for (QueryDocumentSnapshot posts : task.getResult()) {
                                Map<String, Object> post = posts.getData();
                                String nickname = post.get("nickname").toString();
                                String date = post.get("모집기간").toString();
                                String title = post.get("제목").toString();
                                String people = post.get("모집인원").toString();
                                String curTutee = post.get("신청인원").toString();
                                List<String> recommendedPeople = (List<String>) post.get("recommendedPeople");
                                itemData = new Item(nickname, title, date, people, recommendedPeople,curTutee);
                                items.add(itemData);

                                tuteeAdapter = new TuteeAdapter(items);
                                recyclerView.setAdapter(tuteeAdapter);
                            }
                        }
                    }
                });
    }
}