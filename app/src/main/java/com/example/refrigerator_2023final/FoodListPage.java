package com.example.refrigerator_2023final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FoodListPage extends AppCompatActivity {
    private ListView foodListView;
    private FirebaseAuth auth;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list_page);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference userFoodsRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("foods");

        foodListView = findViewById(R.id.imageListView);
        auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        displayFoodList();
    }

    private void displayFoodList() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Firebase Realtime Database에서 음식 정보 가져오기
            databaseRef.child("users").child(userId).child("foods").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<FoodItem> foodList = new ArrayList<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        FoodItem food = data.getValue(FoodItem.class);
                        if (food != null) {
                            foodList.add(food);
                        }
                    }

                    // 사용자가 등록한 음식 목록을 ListView에 표시
                    FoodListAdapter adapter = new FoodListAdapter(FoodListPage.this, foodList);
                    foodListView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 에러 처리
                    // 에러 처리
                }
            });
        }
    }

    public void GoToFoodRegist(View view){
        Intent intent = new Intent(this, FoodRegist.class);
        startActivity(intent);
    }

}

