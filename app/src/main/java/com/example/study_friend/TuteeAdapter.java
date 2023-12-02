package com.example.study_friend;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Map;


public class TuteeAdapter extends RecyclerView.Adapter<TuteeAdapter.ViewHolder> {

    public ArrayList<Item> items;
    AlertDialog.Builder builder;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        viewHolder.selectNum.setText(item.num);
        viewHolder.title1.setText(item.title);
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
        TextView title1, name, day, selectNum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title1 =  itemView.findViewById(R.id.title1);
            name =(TextView)itemView.findViewById(R.id.name);
            day = (TextView)itemView.findViewById(R.id.day);
            selectNum = (TextView)itemView.findViewById(R.id.select_num);

            Log.d("RERE", "뷰홀더 생성");
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Log.d("RERE", "onClicked?");
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Item item = items.get(pos);
                        builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("스터디 추천하기");
                        builder.setMessage("스터디를 추천하시겠습니까?");
                        Log.d("RERE",item.name);

                        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("users")
                                        .whereEqualTo("nickname", item.name)
                                                .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                 QuerySnapshot documents = task.getResult();
                                                                 for(QueryDocumentSnapshot documentSnapshot : documents){
                                                                     Map<String,Object> document =documentSnapshot.getData();
                                                                     String uid = document.get("documentID").toString();
                                                                     int recommended = Integer.parseInt(document.get("recommended").toString());
                                                                     recommended++;
                                                                     DocumentReference docRef = db.collection("users").document(uid);
                                                                     docRef.update("recommended",recommended);
                                                                 }
                                                            }
                                                        });
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
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
