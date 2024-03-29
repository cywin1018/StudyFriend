package com.example.study_friend;

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

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class TutorAdapter extends RecyclerView.Adapter<TutorAdapter.ViewHolder> {

    public ArrayList<Item> items;
    AlertDialog.Builder builder;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public TutorAdapter (ArrayList<Item> items){
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorAdapter.ViewHolder viewHolder, final int position) {

        Item item = items.get(position);
        viewHolder.day.setText(item.day);
        viewHolder.name.setText(item.name);
        viewHolder.selectNum.setText(item.num);
        viewHolder.title1.setText(item.title);
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
        TextView title1, name, day, selectNum;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title1 =  itemView.findViewById(R.id.title1);
            name =(TextView)itemView.findViewById(R.id.name);
            day = (TextView)itemView.findViewById(R.id.day);
            selectNum = (TextView)itemView.findViewById(R.id.select_num);
            relativeLayout = itemView.findViewById(R.id.item_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Item item = items.get(getBindingAdapterPosition());
                    String curTutee =   item.CurTutee;
                    builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("스터디 인원 현황");
                    builder.setMessage("현재 신청 인원 : " + curTutee + "명");


                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

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
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("스터디 삭제하기");
                    builder.setMessage("정말로 삭제하시겠습니까?");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Item item = items.get(getBindingAdapterPosition());
                            String documentId = item.title;
                            db.collection("게시글").document(documentId).delete();
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
