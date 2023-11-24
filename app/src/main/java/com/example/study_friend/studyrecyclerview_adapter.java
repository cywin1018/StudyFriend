package com.example.study_friend;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study_friend.chat.chatting;
import com.example.study_friend.databinding.FragmentStudyFragmentBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

//CustomAdapter : studyfragement 목록 adapter,viewholder
public class studyrecyclerview_adapter extends RecyclerView.Adapter<studyrecyclerview_adapter.ViewHolder> {

    FirebaseFirestore db;
    CollectionReference nicknameRef;
    Intent intent;
    public ArrayList<Item> items;

    //===== [Click 이벤트 구현을 위해 추가된 코드] ==========================
    // OnItemClickListener 인터페이스 선언
    public interface OnItemClickListener {
        void onItemClicked(int position, String data);
    }

    // OnItemClickListener 참조 변수 선언
    private OnItemClickListener itemClickListener;

    // OnItemClickListener 전달 메소드
    public void setOnItemClickListener (OnItemClickListener listener) {
        itemClickListener = listener;
    }
    //======================================================================

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

        studyrecyclerview_adapter.ViewHolder viewHolder = new ViewHolder(itemView);


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

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title1, name, day, selectNum;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title1 =  itemView.findViewById(R.id.title1);
            name =(TextView)itemView.findViewById(R.id.name);
            day = (TextView)itemView.findViewById(R.id.day);
            selectNum = (TextView)itemView.findViewById(R.id.select_num);

//          chat의 리사이클러뷰 클릭함수 적극 반영
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getBindingAdapterPosition(); //아이템 위치를 알아냄
                    if(pos != RecyclerView.NO_POSITION){
                        Item studyitem = items.get(pos);

                        intent = new Intent(view.getContext(), StudyContent_chan.class);
                        intent.putExtra("name",studyitem.name);
                        intent.putExtra("title",studyitem.title);
                        view.getContext().startActivity(intent);

                    }
                }
            });

        }

        //        void onBind(Item item){
//            title.setText(item.getTitle());
//            name.setText(item.getName());
//            day.setText(item.getDay());
//            num.setText(item.getNum());
//        }

    }

}
