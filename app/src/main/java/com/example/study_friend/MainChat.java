package com.example.study_friend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.study_friend.databinding.ActivityMainChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainChat extends AppCompatActivity {
    ActivityMainChatBinding binding;
    //프로필 이미지 Uri
    Uri profileUri = null;

    boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.civ.setOnClickListener(view -> clickImg());
        binding.btn.setOnClickListener(view -> clickBtn());

        //디바이스에 저장되어 있는 로그인 정보(profile)가 있는지 확인
        //SharedPrefernces에 저장되어 있는 닉네임, 프로필이미지 있다면 읽어오기
        loadData();
    }
    //SharedPrefernces에 저장된 값 읽어오기
    private void loadData() {

        SharedPreferences pref = getSharedPreferences("profile",MODE_PRIVATE);
        G.nicname = pref.getString("nicName",null);
//        G.profileUrl = pref.getString("profileUrl",null);

        if(G.nicname != null){
            binding.et.setText(G.nicname);
//            Glide.with(this).load(G.profileUrl).into(binding.civ);

            isFirst = false;
        }

    }

    private void clickImg() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        Log.d("이미지",intent.toString());
        resultLauncher.launch(intent);

    }



    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode() == RESULT_CANCELED) return;

        //Uri 구해야하는데 전역변수로
        profileUri = result.getData().getData(); //인텐트한테 데이터 받기

        Glide.with(this).load(profileUri).into(binding.civ);

    });

    private void clickBtn() {

        //채팅화면 가기전에 프로필 이미지와 닉네임을 서버에 저장 단, 처음 로그인 할 때
        saveData();
//        if (isFirst) saveData();
//        else
//            startActivity(new Intent(this, chatting.class));
    }

    private void saveData() {

        //이미지를 선택하지 않으면 채팅입장 불가
//        if(profileUri == null) return;

        //닉네임 가져와서 static 변수에 저장 - 모든 화면에서 쓰기위해
        G.nicname = binding.et.getText().toString();
        Log.d("닉네임",G.nicname);
        //이미지 업로드가 오래걸리기 때문에 FireStorge에 먼저 업로드
        //1. 스토리지로 이동
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Log.d("스토리지",storage.toString());
        //2. 참조 위치명이 중복되지않도록 날짜이용
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        StorageReference imgRef = storage.getReference("profileImg/IMG_"+sdf.format(new Date()));

        //3. 이미지 업로드
        imgRef.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MainChat.this, "이미지 등록", Toast.LENGTH_SHORT).show();
                //업로드가 성공되었으니
                //업로드 된 파일의 다운로드[URL] 주소를 얻어오기
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() { //콜백의 콜백
                    @Override
                    public void onSuccess(Uri uri) {
                        //Uri 안에 업로드 된 파일의 다운로드[URL] 옴
//                        G.profileUrl = uri.toString();

                        Toast.makeText(MainChat.this, "이미지 주소값 저장 완료", Toast.LENGTH_SHORT).show();

                        //저장 2군데에 할것
                        //1. 서버의 firestore DB에 닉네임과 이미지 Url 저장
                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                        //profiles 라는 이름의 컬렉션 참조 객체 소환
                        CollectionReference profileRef = firestore.collection("profiles");

                        //닉네임을 도큐먼트로 정하고 필드 값으로 이미지 경로 url 저장
                        Map<String, Object> profile = new HashMap<>();
//                        profile.put("profileUrl", G.profileUrl);

                        profileRef.document(G.nicname).set(profile);

                        //2. 앱을 처음 시작할때만 닉네임과 사진을 입력하도록
                        //디바이스에 영구적으로 데이터 저장 [SharedPreference]
                        SharedPreferences preferences = getSharedPreferences("profile",MODE_PRIVATE);

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("nicName",G.nicname);
//                        editor.putString("profileUrl",G.profileUrl);

                        editor.commit(); //내부적으로 트랜젝션 상태라 commit() 안해주면 안됨


                        //저장이 완료되었으니 채팅화면으로 이동
                        Intent intent = new Intent(MainChat.this, chatting.class);
                        startActivity(intent);

                        finish();
                    }
                });
            }
        });

    }
}