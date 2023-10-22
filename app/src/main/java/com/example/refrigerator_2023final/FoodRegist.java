package com.example.refrigerator_2023final;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FoodRegist extends AppCompatActivity {
    private Button selectImageBtn, saveBtn;
    private ImageView foodImageView;
    private EditText foodNameEditText, buyDateEditText, useDateEditText;

    private Uri selectedImageUri;

    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private DatabaseReference databaseRef; // Firebase Realtime Database reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_regist);

        // Firebase 초기화
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        // 뷰 초기화
        selectImageBtn = findViewById(R.id.image_upload_btn);
        saveBtn = findViewById(R.id.registButton);
        foodImageView = findViewById(R.id.food_image_view);
        foodNameEditText = findViewById(R.id.food_name);
        useDateEditText = findViewById(R.id.use_date);
        buyDateEditText = findViewById(R.id.buy_date);

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 갤러리에서 이미지 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 음식 정보를 Firebase에 저장
                saveFoodInformation();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                foodImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFoodInformation() {
        final String foodName = foodNameEditText.getText().toString().trim();
        final String useDate = useDateEditText.getText().toString().trim();
        final String buyDate = buyDateEditText.getText().toString().trim();

        if (selectedImageUri != null && !TextUtils.isEmpty(foodName) && !TextUtils.isEmpty(useDate) && !TextUtils.isEmpty(buyDate)) {
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                String userId = user.getUid();

                // Firebase Realtime Database에서 사용자별 "foods" 레퍼런스 가져오기
                DatabaseReference userFoodsRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("foods");

                // 사용자별 "foods" 레퍼런스에 데이터 추가하기
                String foodId = userFoodsRef.push().getKey(); // 사용자별 "foods"에 데이터를 추가할 고유한 키 생성
                if (foodId != null) {
                    String imageFileName = "foods/" + userId + "/" + foodId + ".jpg"; // 이미지 파일 경로 수정

                    // Firebase Storage에 이미지 업로드
                    final StorageReference storageRef = storage.getReference().child(imageFileName);
                    UploadTask uploadTask = storageRef.putFile(selectedImageUri);

                    // 이미지 업로드 성공 또는 실패 처리
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // 이미지 다운로드 URL 가져오기
                            Task<Uri> downloadUrl = storageRef.getDownloadUrl();
                            downloadUrl.addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri imageUrl = task.getResult();

                                        // 음식 정보를 저장하기 위한 데이터 맵 생성
                                        Map<String, Object> foodInfo = new HashMap<>();
                                        foodInfo.put("foodName", foodName);
                                        foodInfo.put("useDate", useDate);
                                        foodInfo.put("buyDate", buyDate);
                                        foodInfo.put("imageUrl", imageUrl.toString());

                                        // Firebase Realtime Database에 정보 저장
                                        String foodId = userFoodsRef.push().getKey();
                                        userFoodsRef.child(foodId).setValue(foodInfo);

                                        Toast.makeText(FoodRegist.this, "음식 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(FoodRegist.this, "이미지 업로드 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            } else {
                Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public void GoToFoodList(View view) {
        Intent intent = new Intent(this, FoodListPage.class);
        startActivity(intent);
    }
}
