package com.example.study_friend.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.study_friend.FriendItem;
import com.example.study_friend.MyRecyclerAdapter;
import com.example.study_friend.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class chat_fragment extends Fragment {
    RecyclerView mRecyclerView;
    MyRecyclerAdapter mRecyclerAdapter;
    View view;

    ArrayList<FriendItem> mfriendItems;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference nicknameRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerAdapter = new MyRecyclerAdapter();
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mfriendItems = new ArrayList<>();

        /* 닉네임이랑  채팅방 정보 가져와서 채팅방 만들어주기 */
        nicknameRef = db.collection("TestCollection");
        nicknameRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (int i = 0; i < task.getResult().size(); i++) {
                    Log.d("yongwon", String.valueOf(task.getResult().size()));
                    String nickname = task.getResult().getDocuments().get(i).get("nickname").toString();
                    Log.d("yongwon", nickname);
                    String date = task.getResult().getDocuments().get(i).get("date").toString();
                    String title = task.getResult().getDocuments().get(i).get("title").toString();


                    mfriendItems.add(new FriendItem(R.drawable.profile_icon, nickname, date, title));
                    mRecyclerAdapter.setFriendList(mfriendItems);
                }
            }
        });
        return view;
    }
}
