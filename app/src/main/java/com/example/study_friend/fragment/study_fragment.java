package com.example.study_friend.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.study_friend.Item;
import com.example.study_friend.R;
import com.example.study_friend.StudyTutor;
import com.example.study_friend.databinding.FragmentStudyFragmentBinding;
import com.example.study_friend.study_register;
import com.example.study_friend.studyrecyclerview_adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class study_fragment extends Fragment {
    final static String TAG = "RERE";
    FirebaseFirestore db;
    CollectionReference postRf;
    RecyclerView recyclerView;
    studyrecyclerview_adapter studyRecyclerAdapter;
    ArrayList<Item> items = new ArrayList<>();

    public View v;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_study_fragment, container, false);
        Log.d(TAG, "뷰 생성");

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*스터디 검색 기능 개선할 점
         * 1. 아무것도 같은게 없으면 안뜨게 해야함
         * 2. title검색하는데 tttttttt쳐도 검색되니까 이거는 수정 해야함*/


        recyclerView = v.findViewById(R.id.study_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        studyRecyclerAdapter = new studyrecyclerview_adapter(items);
        recyclerView.setAdapter(studyRecyclerAdapter);

        Log.d(TAG, "adapter연결");

        db = FirebaseFirestore.getInstance();
        postRf = db.collection("게시글");
        items = new ArrayList<Item>();
        db.collection("게시글").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                   List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                   ArrayList<Item> items1 = new ArrayList<>();
                   for(DocumentSnapshot document : documents){
                       Map<String,Object> postDocument = document.getData();

                       if(postDocument != null) {
                           String nickname = postDocument.get("nickname").toString();
                           Timestamp time1 =(Timestamp)postDocument.get("time");
                           String date = time1.toDate().toString();
                           String title = postDocument.get("제목").toString();
                           String num = postDocument.get("모집인원").toString();
                           long timeNow = Timestamp.now().getSeconds();
                           Timestamp time = (Timestamp) postDocument.get("time");
                           if (time != null) {
                               long timeThen = time.getSeconds();
                               if (timeNow < timeThen + 60 * 30) {
                                   items1.add(new Item(nickname, title, date, num));
                                   int itemposition=items.size() - 1;


                               }
                           }
                       }
                   }
                    studyRecyclerAdapter.setItemsList(items1);
                    studyRecyclerAdapter.notifyItemInserted(items1.size() - 1);
                    items = items1;
                }
            }
        });
//        postRf.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                List<DocumentChange> documentChanges = value.getDocumentChanges();
//                ArrayList<Item> items1 = new ArrayList<>();
//                for (DocumentChange documentChange : documentChanges) {
//                    DocumentSnapshot documentSnapshot = documentChange.getDocument();
//
//                    Map<String, Object> postDocument = documentSnapshot.getData();
//                    if(postDocument != null) {
//                        String nickname = postDocument.get("nickname").toString();
//                        Timestamp time1 =(Timestamp)postDocument.get("time");
//                        String date = time1.toDate().toString();
//                        String title = postDocument.get("제목").toString();
//                        String num = postDocument.get("모집인원").toString();
//                        long timeNow = Timestamp.now().getSeconds();
//                        Timestamp time = (Timestamp) postDocument.get("time");
//                        if (time != null) {
//                            long timeThen = time.getSeconds();
//                            if (timeNow < timeThen + 60 * 30) {
//                                items1.add(new Item(nickname, title, date, num));
//                                int itemposition=items.size() - 1;
//
//
//                            }
//                        }
//                    }
//                }
//                studyRecyclerAdapter.setItemsList(items1);
//                studyRecyclerAdapter.notifyItemInserted(items1.size() - 1);
//                items = items1;
//            }
//        });



        Button button = v.findViewById(R.id.writerBtn);

        button.setOnClickListener(VView -> {
            Log.d("TAG", "Click y nono?");
            Intent intent = new Intent(getActivity(), study_register.class);
            startActivity(intent);
        });





        SearchView searchView = v.findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { //검색 버튼 눌렀을 때
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) { //검색어 입력할 때마다
                db = FirebaseFirestore.getInstance();
                ArrayList<Item> filteredItems = new ArrayList<>();
                for (int i = 0; i < items.size(); i++) {
                    Item item = items.get(i);
                    if (item.getTitle().contains(newText)) {
                        filteredItems.add(item);
                        studyRecyclerAdapter.setItemsList(filteredItems);
                    }
                }
                return false;

            }
        });
        // 아래로 스크롤하면 화면 새로고침
        // 스크롤을 끝까지 내렸을 때
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState){
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1)){
                    db = FirebaseFirestore.getInstance();
                    postRf = db.collection("게시글");
                    items = new ArrayList<Item>();
                    db.collection("게시글").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            QuerySnapshot queryDocumentSnapshots = task.getResult();
                            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                            ArrayList<Item> items1 = new ArrayList<>();
                            for(DocumentSnapshot document : documents){
                                Map<String,Object> postDocument = document.getData();

                                if(postDocument != null) {
                                    String nickname = postDocument.get("nickname").toString();
                                    Timestamp time1 =(Timestamp)postDocument.get("time");
                                    String date = time1.toDate().toString();
                                    String title = postDocument.get("제목").toString();
                                    String num = postDocument.get("모집인원").toString();
                                    long timeNow = Timestamp.now().getSeconds();
                                    Timestamp time = (Timestamp) postDocument.get("time");
                                    if (time != null) {
                                        long timeThen = time.getSeconds();
                                        if (timeNow < timeThen + 60 * 30) {
                                            items1.add(new Item(nickname, title, date, num));
                                            int itemposition=items.size() - 1;
                                        }
                            }
                        }
                    }
                            studyRecyclerAdapter.setItemsList(items1);
                            studyRecyclerAdapter.notifyItemInserted(items1.size() - 1);
                            items = items1;
                        }
                    });
                }
            }
        });
    }
}