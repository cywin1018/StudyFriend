package com.example.study_friend;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TuteeAdapter extends RecyclerView.Adapter<TuteeAdapter.ViewHolder> {

    public ArrayList<Item> items;
    AlertDialog.Builder builder;
    AlertDialog.Builder builder2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;

    public TuteeAdapter (ArrayList<Item> items){
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TuteeAdapter.ViewHolder viewHolder, final int position) {

        Item item = items.get(position);
        viewHolder.day.setText(item.day);
        viewHolder.name.setText(item.name);
        viewHolder.title1.setText(item.title);
        viewHolder.selectNum.setText(item.CurTutee);
        viewHolder.setnum.setText(item.num);
        if(position%2==0){
            viewHolder.relativeLayout.setBackgroundResource(R.drawable.buttonblackline_blue);
        }
        else{
            viewHolder.relativeLayout.setBackgroundResource(R.drawable.buttonblackline);
        }
    }
    public void setItemsList(ArrayList<Item> list){
        this.items = list;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title1, name, day, selectNum,setnum;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title1 =  itemView.findViewById(R.id.title1);
            name =(TextView)itemView.findViewById(R.id.name);
            day = (TextView)itemView.findViewById(R.id.day);
            selectNum = (TextView)itemView.findViewById(R.id.select_num);
            Log.d("RERE", selectNum.getText().toString()+"여기여기");
            setnum = (TextView)itemView.findViewById(R.id.setnum);
            Log.d("RERE", setnum.getText().toString()+"여기여기");
            relativeLayout = itemView.findViewById(R.id.item_layout);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Log.d("RERE", "onClicked?");
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Item item = items.get(pos);
                        // item의 recommendedPeople에 현제 사용자의 uid가 있는지 for문으로 확인
                        // 있으면 이미 추천한 스터디라는 메시지를 띄워줌
                        // 없으면 추천하시겠습니까? 라는 메시지를 띄워줌
                        // 추천하면 해당 게시글의 recommendedPeople에 현재 사용자의 uid를 추가해줌
                        // 추천수를 1 증가시켜줌
                        // 추천수를 db에 저장해줌
                        // 추천이 완료되었습니다 라는 메시지를 띄워줌

                        builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("스터디 추천하기");
                        builder.setMessage("스터디를 추천하시겠습니까?");
                        Log.d("RERE",item.name);

                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("RERE", "확인버튼 클릭");
                                db.collection("게시글").document(item.title).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        Log.d("RERE", "게시글 가져오기");
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d("RERE", "DocumentSnapshot data: " + document.getData());
                                                Map<String, Object> post = document.getData();
                                                List<String> recommendedPeople = (List<String>) post.get("recommendedPeople");
                                                Log.d("RERE", "추천인원 가져오기");
                                                if(recommendedPeople.contains(item.name)){
                                                    Log.d("RERE", "이미 추천한 스터디");
                                                    builder2 = new AlertDialog.Builder(view.getContext());
                                                    builder2.setTitle("스터디 추천하기");
                                                    builder2.setMessage("이미 추천한 스터디입니다.");
                                                    builder2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                    builder2.show();
                                                }
                                                else{
                                                    Log.d("RERE", "추천 가능한 스터디");
                                                    recommendedPeople.add(item.name);
                                                    db.collection("게시글").document(item.title).update("recommendedPeople", recommendedPeople);
                                                    db.collection("게시글").document(item.title).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {
                                                                Map<String, Object> post = document.getData();
                                                                String tutorUid = post.get("tutorUid").toString();
                                                                db.collection("users").document(tutorUid).update("recommended", FieldValue.increment(1));
                                                                builder2 = new AlertDialog.Builder(view.getContext());
                                                                builder2.setTitle("스터디 추천하기");
                                                                builder2.setMessage("추천이 완료되었습니다.");
                                                                builder2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                });
                                                                builder2.show();
                                                            }
                                                        }
                                                    });

                                                }
                                            } else {
                                                Log.d("RERE", "No such document");
                                            }
                                        } else {
                                            Log.d("RERE", "get failed with ", task.getException());
                                        }
                                    }
                                });

                            }

                        });
                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    builder.show();
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("스터디 신청 취소하기");
                    builder.setMessage("정말로 신청을 취소하시겠습니까?");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Item item = items.get(getBindingAdapterPosition());
                            String documentId = item.title;
                            db.collection("게시글").document(documentId).update("신청인원", FieldValue.increment(-1));
                            items.remove(getBindingAdapterPosition());
                            notifyItemRemoved(getBindingAdapterPosition());
                            notifyItemRangeChanged(getBindingAdapterPosition(), items.size());
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            // 게시글의 allpeople 에서 현재 사용자의 uid를 삭제
                            db.collection("게시글").document(documentId).update("allPeople", FieldValue.arrayRemove(user.getUid()));
                            db.collection("게시글").document(documentId).update("신청자Uid", FieldValue.arrayRemove(user.getUid()));
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    return true;
                }
            });
        }

    }

}
