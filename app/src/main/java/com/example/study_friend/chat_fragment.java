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
        for (int i = 1; i <= 4; i++) {
            if (i % 2 == 0)
                mfriendItems.add(new FriendItem(R.drawable.profile_icon, i + "번째 사람", i + "번째 상태메시지"));
            else
                mfriendItems.add(new FriendItem(R.drawable.register_icon, i + "번째 사람", i + "번째 상태메시지"));
        }
        mRecyclerAdapter.setFriendList(mfriendItems);
        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }
}
