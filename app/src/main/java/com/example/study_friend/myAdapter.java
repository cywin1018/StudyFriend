//package com.example.study_friend;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class myAdapter extends  RecyclerView.Adapter<myViewholder>{
//    Context context;
//    List<Item> items;
//
//    public myAdapter(Context context, List<Item> items) {
//        this.context = context;
//        this.items = items;
//    }
//
//    @NonNull
//    @Override
//    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new myViewholder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull myViewholder holder, int position) {
//        holder.title.setText(items.get(position).getTitle());
//        holder.num.setText(items.get(position).getNum());
//        holder.day.setText(items.get(position).getDay());
//        holder.name.setText(items.get(position).getName());
//        holder.imageView.setImageResource(items.get(position).getImage());
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//}
