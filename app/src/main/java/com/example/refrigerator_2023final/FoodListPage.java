package com.example.refrigerator_2023final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    private List<FoodItem> foodItemList = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list_page);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference userFoodsRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("foods");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(userId).child("foods");

        foodListView = findViewById(R.id.imageListView);
        auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);


        // ListView 초기화 및 어댑터 설정
        ListView imageListView = findViewById(R.id.imageListView);
        FoodListAdapter adapter = new FoodListAdapter(this, foodItemList);
        imageListView.setAdapter(adapter);

// ListView 아이템 클릭 이벤트 처리
        imageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 클릭한 아이템의 정보를 가져와서 FoodListFixPage로 전달
                FoodItem selectedItem = foodItemList.get(position);
                Intent intent = new Intent(FoodListPage.this, FoodListFixPage.class);
                intent.putExtra("foodId", selectedItem.getFoodId());
                startActivity(intent); // 기존 코드: 현재 화면에서 FoodListFixPage로 이동
            }
        });
        displayFoodList();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 새로고침 작업을 수행
                displayFoodList();
            }
        });


    }



    private void displayFoodList() {
        swipeRefreshLayout.setRefreshing(false);
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Firebase Realtime Database에서 음식 정보 가져오기
            databaseRef.child("users").child(userId).child("foods").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    foodItemList.clear(); // 리스트 초기화

                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        FoodItem food = data.getValue(FoodItem.class);
                        if (food != null) {
                            // Firebase에서 자동으로 생성된 키(ID)를 가져와서 FoodItem 객체에 설정
                            food.setFoodId(data.getKey());
                            foodItemList.add(food);
                        }
                    }

                    // 사용자가 등록한 음식 목록을 ListView에 표시
                    FoodListAdapter adapter = new FoodListAdapter(FoodListPage.this, foodItemList);
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

    public void GotoHome(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void GotoMypage(View view)
    {
        Intent intent = new Intent(this, MyPageJava.class);
        startActivity(intent);
    }
    public void GotoSearch(View view)
    {
        Intent intent = new Intent(this, SearchPageActivity.class);
        startActivity(intent);
    }
    public void GotoFoodList(View view)
    {
        Intent intent = new Intent(this, FoodListPage.class);
        startActivity(intent);
    }
}
