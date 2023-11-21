package com.example.study_friend;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.study_friend.databinding.FragmentStudyFragmentBinding;

import java.util.ArrayList;


public class study_fragment extends Fragment {
    FragmentStudyFragmentBinding binding;

    androidx.recyclerview.widget.RecyclerView RecyclerView;
    studyrecyclerview_adapter RecyclerAdapter;

    ArrayList<Item> Itemlist;

    private View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudyFragmentBinding.inflate(inflater);

        v = inflater.inflate(R.layout.fragment_study_fragment,container,false);

        RecyclerView = v.findViewById(R.id.study_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerAdapter = new studyrecyclerview_adapter();
        RecyclerView.setAdapter(RecyclerAdapter);

        Itemlist = new ArrayList<>();
        //sever에 데이터를 어레이 형식으로 불러와야한다.
        for (int i = 1; i <= 4; i++) {
            Itemlist.add(new Item("chanho"+i,"study 모집합니다"+i,"2023-11-1"+i,""+i));
        }
        RecyclerAdapter.setItems(Itemlist);

        ActivityResultLauncher<Intent> resultLanch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent intent = result.getData();
                        ArrayList<String> list =intent.getStringArrayListExtra("data");
                        //필요한데이터 작성자, 제목, 시간(, 사람수, 이미지
                        // <작성페이지:regactivity 데이터 인덱스> 0: 학부 1:분야 2:장소 3:사람수 4:제목 5:내용

                        //서버에서 가져와야하는것 작성자
                        //날짜는 현재 생성일
                        Item item = new Item("서버에서불러온 작성자",list.get(4),"2023-11-25",list.get(3));
                        RecyclerAdapter.addItems(item);
                    }
                }
        );


        Button button = v.findViewById(R.id.wirterbttn);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), study_register.class);
//            intent.putExtra("data",1);
            resultLanch.launch(intent);
        });


        return v;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.wirterbttn.setOnClickListener(v ->{
            replaceFragment(new StudyMakeWrite());
        });

    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.changeFrame,fragment);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
    }
}