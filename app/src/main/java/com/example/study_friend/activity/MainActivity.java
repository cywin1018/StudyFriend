package com.example.study_friend.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.study_friend.HomeActivity;
import com.example.study_friend.R;
import com.example.study_friend.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
/*
* 일단 대부분 다 되는데 이메일 비밀번호 틀렸을 때 어떤식으로 나타낼 지 생각해보면 될 듯
*/

public class MainActivity extends Activity {
    private static final String TAG = "RERE";
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;
    private static final int RC_SIGN_IN= 9001;

    private GoogleSignInClient mGoogleSignInClient;
    ActivityMainBinding binding;

    TextView textView;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        textView = (TextView) binding.googleLoginBtn.getChildAt(0);
        textView.setText(getString(R.string.googleLogInBtn));
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        mAuth = FirebaseAuth.getInstance();

        binding.SuperBtn.setOnClickListener(v-> {
            Log.d(TAG, "SuperBtn Clicked");
            intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

        binding.loginBtn.setOnClickListener(v -> {
            Log.d(TAG, "LogInBtn Clicked");
            signIn(binding.editID.getText().toString(),binding.editPassword.getText().toString());
        });

        binding.SignUpBtn.setOnClickListener(v -> {
            Log.d(TAG, "SignUpBtn Clicked");
            intent = new Intent(this, SignUp.class);
            startActivity(intent);
        });
        binding.passwordResetBtn.setOnClickListener(v -> {
            Log.d(TAG, "passwordResetBtn Clicked");
            intent = new Intent(this, PasswordReset.class);
            startActivity(intent);
        });
        binding.googleLoginBtn.setOnClickListener(v -> {
            Log.d(TAG,"googleLogInBtn Clicked");
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent,RC_SIGN_IN);
        });
    }

    private void signIn(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Log.d(TAG,"updateUI 실행");
                        }
                        else{
                            Log.w(TAG,"signUpWithEmail:Failure");
                            Toast.makeText(MainActivity.this,"아이디 또는 비밀번호가 다릅니다",Toast.LENGTH_SHORT).show();
                            binding.editID.setText("");
                            binding.editPassword.setText("");
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG,user.getUid() + " " + user.getEmail() + " " + user.getDisplayName());
                            db.collection("users")
                                            .whereEqualTo("documentID",user.getUid())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    Log.d(TAG,"여기");
                                                    QuerySnapshot documents = task.getResult();
                                                    if(documents.isEmpty()){
                                                        Log.d(TAG,"이거?");
                                                        intent = new Intent(getApplicationContext(), GoogleUserInfoSet.class);
                                                        startActivity(intent);
                                                    }else{
                                                        updateUI(user);
                                                    }
                                                }
                                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    private void reload(){

    }
    private void updateUI(FirebaseUser user){
        intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }
}