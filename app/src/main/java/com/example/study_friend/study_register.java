package com.example.study_friend;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.study_friend.databinding.ActivityStudyRegisterBinding;

import java.util.ArrayList;

public class study_register extends AppCompatActivity {
    ActivityStudyRegisterBinding binding;
    private ArrayList<Item> items=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudyRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //2.
        binding.regBttn.setOnClickListener(view -> {
            ArrayList<String> data=new ArrayList<>();
            data.add(binding.editPeople.getText().toString());
            data.add(binding.editField.getText().toString());
            data.add(binding.editPlace.getText().toString());
            data.add(binding.editNum.getText().toString());
            data.add(binding.editTitle.getText().toString());
            data.add(binding.editContent.getText().toString());
            // 0: 학부 1:분야 2:장소 3:사람수 4:제목 5:내용
            Intent intent = getIntent();
            intent.putExtra("data", data);
            setResult(Activity.RESULT_OK,intent);
            finish();
        });

    }
}