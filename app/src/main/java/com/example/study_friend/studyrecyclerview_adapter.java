package com.example.study_friend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//CustomAdapter : studyfragement 목록 adapter,viewholder
public class studyrecyclerview_adapter extends RecyclerView.Adapter<studyrecyclerview_adapter.ViewHolder> {

    private ArrayList<Item> items;

    public void setItems(ArrayList<Item> data){
        items = data;
//        notifyDataSetChanged() 필요한지 검토중...
//        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public studyrecyclerview_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
//      <기준 코드>
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull studyrecyclerview_adapter.ViewHolder viewHolder, final int position) {
        viewHolder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, name, day, num;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title =  itemView.findViewById(R.id.title1);
            name =(TextView) itemView.findViewById(R.id.name);
            day = (TextView)itemView.findViewById(R.id.day);
            num = (TextView)itemView.findViewById(R.id.select_num);
        }

        void onBind(Item item){
            title.setText(item.getTitle());
            name.setText(item.getName());
            day.setText(item.getDay());
            num.setText(item.getNum());
        }

    }

}
