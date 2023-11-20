package com.example.study_friend;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.study_friend.databinding.FragmentStudyFragmentBinding;


public class study_fragment extends Fragment {
    FragmentStudyFragmentBinding binding;

    Intent intent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudyFragmentBinding.inflate(inflater);

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