package com.example.study_friend;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study_friend.chat.chatting;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    FirebaseFirestore db;
    CollectionReference nicknameRef;
    Intent intent;
    public ArrayList<FriendItem> mFriendList = new ArrayList<>();

    @NonNull
    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerAdapter.ViewHolder holder, int position) {

        holder.onBind(mFriendList.get(position));
    }

    public void setFriendList(ArrayList<FriendItem> list){
        this.mFriendList = list;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name;
        TextView message;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile = (ImageView) itemView.findViewById(R.id.profile);
            name = (TextView) itemView.findViewById(R.id.name);
            message = (TextView) itemView.findViewById(R.id.message);
            title   =(TextView) itemView.findViewById(R.id.studyTitle);
            Log.d("yongwon", "ViewHolder: -> title : " + name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("yongwon", "ViewHolder: clicked?");
                    int pos = getBindingAdapterPosition(); //아이템 위치를 알아냄
                    Log.d("yongwon", "where are u? : " + pos);
                    if(pos != RecyclerView.NO_POSITION){
                        FriendItem friendItem = mFriendList.get(pos);
                        Log.d("yongwon", "onClick: " + friendItem.title);
                        chatting.chatName = friendItem.title;
                        intent = new Intent(view.getContext(), chatting.class);
                        intent.putExtra("title", friendItem.title);
                        view.getContext().startActivity(intent);
                        // 이후에 닉네임, 방장에 맞는걸 store에서 받아와서 intent하면 될듯
//                        nicknameRef = db.collection("TestCollection");
//                        nicknameRef.get().addOnCompleteListener(task->{
//                            if(task.isSuccessful()){
//                                for(int i=0; i<task.getResult().size(); i++){
//
//                                    String nickname = task.getResult().getDocuments().get(i).get("nickname").toString();
//                                    Log.d("MYYY : ", nickname);
//
//                                    if(friendItem.name.equals(nickname)){
//                                        intent = new Intent(view.getContext(), chatting.class);
//
////                                        intent.putExtra("nickname", nickname);
//                                        view.getContext().startActivity(intent);
//                                    }
//                                }
//
//                            }
//                        });
                    }
                }
            });
        }

        void onBind(FriendItem item){
            profile.setImageResource(item.getResourceId());
            name.setText(item.getName());
            message.setText(item.getDate());
            title.setText(item.getTitle());
        }
    }
}