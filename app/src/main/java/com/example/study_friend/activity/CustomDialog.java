package com.example.study_friend.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.study_friend.databinding.ActivityCustomDialogBinding;

public class CustomDialog extends AppCompatActivity {


    ActivityCustomDialogBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}