package com.example.refrigerator_2023final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyPageJava extends AppCompatActivity {

    private TextView userEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        userEmailTextView = findViewById(R.id.nicknameView3); // TextView ID로 변경

        // Firebase Authentication에서 현재 사용자 가져오기
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            // 사용자가 로그인한 경우 사용자 이메일 가져오기
            String userEmail = user.getEmail();

            // 이메일 주소를 "@"를 기준으로 분할
            String[] parts = userEmail.split("@");

            // "@" 이전의 이메일 주소 부분을 가져오기
            if (parts.length > 0) {
                String username = parts[0];
                userEmailTextView.setText(username);
            }
        }
    }
}