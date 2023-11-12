package com.example.study_friend;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.study_friend.databinding.FragmentStudyFragmentBinding;


public class study_fragment extends Fragment {
    FragmentStudyFragmentBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudyFragmentBinding.inflate(inflater,container,false);

//        binding.wirterbttn.setOnClickListener(view -> {
//            RecyclerView recyclerView = binding.recyclerview;
//            Log.d("MYMY", "onCreateView: 작성버튼 클릭");
//            List<Item> items = new ArrayList<Item>();
//            items.add(new Item("dlcksgh8511","study recruit","2023/11/1~2023/12/25","6", R.drawable.a));
//            items.add(new Item("dlcksgh8511","study recruit","2023/11/1~2023/12/25","6", R.drawable.a));
//
//            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//            recyclerView.setAdapter(new myAdapter(getActivity(),items));
//
//        });
//


        return binding.getRoot();
    }
}