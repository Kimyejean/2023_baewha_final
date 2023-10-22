package com.example.refrigerator_2023final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPage extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; /*파이어 베이스 인증*/
    private DatabaseReference mDatabaseRef;/*실시간 데이터 베이스*/
    private EditText mUsreEmail, mUserPass;

    private Button mRegistButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mUsreEmail = findViewById(R.id.rUserEmail);
        mUserPass = findViewById(R.id.rUserPass);
        mRegistButton = findViewById(R.id.finishRegist);

        mRegistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*회원가입 처리 시작*/
                String strEmail = mUsreEmail.getText().toString();
                String strPass = mUserPass.getText().toString();

                /*Firebase Auth 진행*/
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPass).addOnCompleteListener(RegisterPage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid()); /*firebase에 무작위로 아이디 토큰 저장*/
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(strPass);

                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                            Toast.makeText(RegisterPage.this, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(RegisterPage.this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}