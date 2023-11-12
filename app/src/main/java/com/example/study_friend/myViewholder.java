package com.example.study_friend;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study_friend.databinding.ItemViewBinding;

public class myViewholder extends RecyclerView.ViewHolder {
    //여기를 바인딩으로 어떻게 불러오나??
    ImageView imageView;
    TextView title, name, day, num;

    public myViewholder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        title = itemView.findViewById(R.id.title);
        name = itemView.findViewById(R.id.name);
        day = itemView.findViewById(R.id.day);
        num = itemView.findViewById(R.id.num1);
    }
}
