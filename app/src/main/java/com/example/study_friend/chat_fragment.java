package com.example.study_friend;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class chat_fragment extends Fragment {
    RecyclerView mRecyclerView;
    MyRecyclerAdapter mRecyclerAdapter;
    View view;

    ArrayList<FriendItem> mfriendItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerAdapter = new MyRecyclerAdapter();
        mRecyclerView.setAdapter(mRecyclerAdapter);

        /* adapt data */
        mfriendItems = new ArrayList<>();


        // 추후 정보를 받아 와서 채팅방 필요한 명수 만큼 만들 예정
        mfriendItems.add(new FriendItem(R.drawable.profile_icon, "gibomi", "2023-11-12","선형대수학 공부하실 분"));
        mfriendItems.add(new FriendItem(R.drawable.a, "chinno", "2023-11-15","오토마타 공부하자"));


        mRecyclerAdapter.setFriendList(mfriendItems);
        return view;
    }
}
