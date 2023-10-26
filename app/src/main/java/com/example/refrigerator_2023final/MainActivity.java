package com.example.refrigerator_2023final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private TextView userEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userEmailTextView = findViewById(R.id.emailView); // TextView ID로 변경

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
                userEmailTextView.setText(username+" 님");
            }
        }
    }

    public void GoToFoodList(View view){
        Intent intent = new Intent(this, FoodListPage.class);
        startActivity(intent);
    }

    public void GoToMyPage(View view){
        Intent intent = new Intent(this, MyPageJava.class);
        startActivity(intent);
    }
}

