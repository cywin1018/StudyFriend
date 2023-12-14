package com.example.study_friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.study_friend.R;
import com.example.study_friend.databinding.ActivityStudyContentBinding;
import com.example.study_friend.fragment.study_fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudyContent extends AppCompatActivity {

    StudyContent studyContent;
    ActivityStudyContentBinding binding;
    FirebaseFirestore db;
    CollectionReference contentRef;
    CollectionReference UserRef;


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    Intent intent;
    Intent intent1;
    String profilename;
    int isExist = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudyContentBinding.inflate(getLayoutInflater());
        Log.d("RERE","StudyContent");
        intent1 =getIntent();
//      <어뎁터로부터 데이터를 받다오는 부분>
//        String sel_name = intent1.getStringExtra("name");
        String sel_title = intent1.getStringExtra("title");
        Log.d("MYMY", "study_content_chan: title을 받았습니다. :  "+sel_title.toString());

        db = FirebaseFirestore.getInstance();
//      <게시글 파베에 기반해서 >
        contentRef = db.collection("게시글");
        contentRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (int i = 0; i < task.getResult().size(); i++) {
                    String data = task.getResult().getDocuments().get(i).toString();
//                    Log.d("MYMY", "게시글: database -> data: "+data);
                    String title = task.getResult().getDocuments().get(i).get("제목").toString();
                    if (sel_title.equals(title)) {
//                      <파이어베이스 연동> -> 작성시 현재 로그인 회원 정보도 "게시글"에 저장하도록 수정하자.
                        String  content = task.getResult().getDocuments().get(i).get("내용").toString();
                        String  major = task.getResult().getDocuments().get(i).get("모집대상").toString();
                        String grade = task.getResult().getDocuments().get(i).get("모집학년").toString();
                        grade = grade+"학년";
                        String  num = task.getResult().getDocuments().get(i).get("모집인원").toString();
                        String  textbook = task.getResult().getDocuments().get(i).get("분야").toString();
                        String  place = task.getResult().getDocuments().get(i).get("장소").toString();
                        profilename = task.getResult().getDocuments().get(i).get("nickname").toString();
                        binding.contentTitle.setText(title);
                        binding.studyContent.setText(content);
                        binding.studyMajor.setText(major);
                        binding.studyGrade.setText(grade);
                        binding.studyPossiblenum.setText(num+"명");
                        binding.studyTextbook.setText(textbook);
                        binding.studyPlace.setText(place);
                        binding.profileName.setText(profilename);

                    }
                }

            }
        });

        UserRef = db.collection("users");
        UserRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(int i=0; i< task.getResult().size(); i++){
                    String data = task.getResult().getDocuments().get(i).toString();
                    Log.d("MYMY", "Users database -> data: "+data);
                    String regnickname = task.getResult().getDocuments().get(i).get("nickname").toString();
                    if(profilename.equals(regnickname)){
                        Log.d("MYMY", "users have the nickname");
                        String univ = task.getResult().getDocuments().get(i).get("univ").toString();
                        String semester = task.getResult().getDocuments().get(i).get("semester").toString();
                        String recommended = task.getResult().getDocuments().get(i).get("recommended").toString();
                        String major = task.getResult().getDocuments().get(i).get("major").toString();

                        Log.d("MYMY", "데이터베이스 결과: " + univ + semester + recommended + major);

                        String profile = "학교: "+univ+"전공: "+major;
                        semester = "학기: "+semester;
                        recommended=recommended+"개";
                        binding.profileInfo.setText(profile);
                        binding.profileSemester.setText(semester);
                        binding.starNumber.setText(recommended);
                    }
                }
            }
        });

        setContentView(binding.getRoot());


        binding.studyRegBtn.setOnClickListener(v -> {
            Log.d("yongwon","스터디 신청 버튼 클릭");

            String title = intent1.getStringExtra("title");
            db.collection("게시글").document(title).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                Map<String, Object> info = documentSnapshot.getData();
                                Log.d("yongwon",info +"입니다.");
                                try {
                                    AlertDialog.Builder menu = new AlertDialog.Builder(StudyContent.this);
                                    String people = info.get("모집대상").toString();
                                    String grade = info.get("모집인원").toString();
                                    String field = info.get("분야").toString();
                                    String place = info.get("장소").toString();
                                    String num = info.get("신청인원").toString();
                                    Log.d("yongwon","isHere?");
                                    menu.setTitle("작성 목록");
                                    menu.setMessage("모집대상 " + people + "\n" + "모집인원 " + grade + "\n" + "분야 " + field + "\n" + "장소 " + place + "\n" + "인원 " + num + "\n");

                                    menu.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            db.collection("users").document(user.getUid()).get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if(task.isSuccessful()){
                                                                int point = Integer.parseInt(task.getResult().get("point").toString());
                                                                Log.d("RERE",Integer.toString(point));
                                                                if(point>=100) {
                                                                    db.collection("게시글").document(intent1.getStringExtra("title")).get()
                                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        Log.d("RERE", "1번");
                                                                                        DocumentSnapshot documentSnapshot = task.getResult();
                                                                                        Map<String, Object> info = documentSnapshot.getData();
                                                                                        int applicants = Integer.parseInt(info.get("신청인원").toString());
                                                                                        List<String> appliers = (ArrayList<String>) info.get("신청자Uid");
                                                                                        List<String> allpeople = (ArrayList<String>) info.get("allPeople");

                                                                                        Map<String, Object> newInfo = new HashMap<>();

                                                                                        //중복신청을 막기 위해 신청자Uid에 현재 사용자의 uid가 있는지 확인

                                                                                        for (int i = 0; i < allpeople.size(); i++) {
                                                                                            if (allpeople.get(i).equals(user.getUid())) {
                                                                                                Log.d("RERE",allpeople.get(i));
                                                                                                isExist = 1;
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                        allpeople.add(user.getUid());
                                                                                        appliers.add(applicants, user.getUid());
                                                                                        applicants++;
                                                                                        newInfo.put("신청인원", applicants);
                                                                                        newInfo.put("신청자Uid", appliers);
                                                                                        newInfo.put("allPeople", allpeople);

                                                                                        if (isExist==1) {
                                                                                            Toast.makeText(StudyContent.this, "이미 신청한 스터디입니다.", Toast.LENGTH_SHORT).show();
                                                                                        }else{
                                                                                            db.collection("게시글").document(intent1.getStringExtra("title"))
                                                                                                    .set(newInfo, SetOptions.merge());
                                                                                            db.collection("users").document(user.getUid())
                                                                                                    .get()
                                                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                            DocumentSnapshot documentSnapshot1 = task.getResult();
                                                                                                            int point = Integer.parseInt(documentSnapshot1.get("point").toString());
                                                                                                            point = point - 100;
                                                                                                            DocumentReference documentReference = db.collection("users").document(user.getUid());
                                                                                                            documentReference.update("point", point);
                                                                                                            Toast.makeText(StudyContent.this, "신청이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    });


                                                                                        }
                                                                                    }
                                                                                }
                                                                            });
                                                                    intent = new Intent(StudyContent.this, HomeActivity.class);
                                                                    startActivity(intent);
                                                                }else{
                                                                    Toast.makeText(StudyContent.this, "포인트가 모자랍니다.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }
                                                    });

                                        }
                                    });
                                    menu.setNegativeButton("Cancel",null);

                                    menu.show();

                                }catch (Exception e){
                                    Log.d("yongwon",e+"입니다.");
                                }
                            }
                            else{
                                Log.d("yongwon","실패");

                            }
                        }
                    });


        });
    }

}