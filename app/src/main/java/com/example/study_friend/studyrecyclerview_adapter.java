package com.example.study_friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//CustomAdapter : studyfragement 목록 adapter,viewholder
public class studyrecyclerview_adapter extends RecyclerView.Adapter<studyrecyclerview_adapter.ViewHolder> {

    public ArrayList<Item> items;

    public studyrecyclerview_adapter(ArrayList<Item> items){
        this.items = items;
    }
//    public void setItems(ArrayList<Item> data){
//        items = data;
////        notifyDataSetChanged() 필요한지 검토중...
////        notifyDataSetChanged();
//    }

    //    public void addItems(Item item){
//        items.add(item);
////        notifyDataSetChanged() 필요한지 검토중...
//        notifyDataSetChanged();
//    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
//      <기준 코드>
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View itemView = inflater.inflate(R.layout.item_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull studyrecyclerview_adapter.ViewHolder viewHolder, final int position) {
//        viewHolder.onBind(items.get(position));
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
//        return 2;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title1, name, day, selectNum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title1 =  itemView.findViewById(R.id.title1);
            name =(TextView)itemView.findViewById(R.id.name);
            day = (TextView)itemView.findViewById(R.id.day);
            selectNum = (TextView)itemView.findViewById(R.id.select_num);
        }

//        void onBind(Item item){
//            title.setText(item.getTitle());
//            name.setText(item.getName());
//            day.setText(item.getDay());
//            num.setText(item.getNum());
//        }

    }

}
