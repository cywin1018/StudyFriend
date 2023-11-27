package com.example.study_friend.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/*
* 일단 아직 안한거!!
* 1. 클릭하면 채팅방으로 넘어가지는데 아직 다른 채팅방으로 넘기는건 아님
* 2. 뷰에서 조금 개선할거 있는데 굳이 지금 할 필요는 없음
*/

public class chat_fragment extends Fragment {
    RecyclerView mRecyclerView;
    MyRecyclerAdapter mRecyclerAdapter;
    View view;

    ArrayList<FriendItem> mfriendItems;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;
    CollectionReference nicknameRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        Log.d("RERE","프레그먼트는 넘어옴");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerAdapter = new MyRecyclerAdapter();
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mfriendItems = new ArrayList<FriendItem>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("RERE","계정정보는 받아옴");

        /* 닉네임이랑  채팅방 정보 가져와서 채팅방 만들어주기 */
//        nicknameRef = db.collection("TestCollection");
//        nicknameRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                List<DocumentChange>documentChanges = value.getDocumentChanges();
//
//                for(DocumentChange documentChange : documentChanges){
//                    DocumentSnapshot documentSnapshot = documentChange.getDocument();
//
//                    Map<String,Object>chatRoom = documentSnapshot.getData();
//                    String nickname = chatRoom.get("nickname").toString();
//                    String date = chatRoom.get("date").toString();
//                    String title = chatRoom.get("title").toString();
//
//                    mfriendItems.add(new FriendItem(R.drawable.profile_icon,nickname,date,title));
//                    mRecyclerAdapter.setFriendList(mfriendItems);
//                    mRecyclerAdapter.notifyItemInserted(mfriendItems.size()-1);
//                }
//            }
//        });
        /*자자 이걸 이제 내가 참여했던 채팅방을 가져오려면 어떻게 해야 할까?
         * 그것은 말이지 나도 잘은 모르겠지만 일단 각 유저의 정보에 따른 채팅방을 만들면 되는거 아니겠어??
         * 그렇다는건 유저 정보에 맞는 친구들을 띄워주면 되는거잖아? */
        String uid = user.getUid();
        db.collection("게시글")
                .whereArrayContains("신청자Uid",uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.d("RERE","이건 되는거냐?");
                            for(QueryDocumentSnapshot chatRoom : task.getResult()){
                                Map<String,Object> chatRoomData = chatRoom.getData();
                                String nickname = chatRoomData.get("모집대상").toString();
                                String date = chatRoomData.get("분야").toString();
                                String title = chatRoomData.get("제목").toString();
                                Log.d("RERE",nickname+" "+date + " " + title);

                                mfriendItems.add(new FriendItem(R.drawable.profile_icon,nickname,date,title));
                                mRecyclerAdapter.setFriendList(mfriendItems);
                            }
                        }
                    }
                });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
