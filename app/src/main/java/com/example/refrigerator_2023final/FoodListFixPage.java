package com.example.refrigerator_2023final;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import android.net.Uri;
import com.google.android.gms.tasks.OnSuccessListener;


public class FoodListFixPage extends AppCompatActivity {
    private EditText foodNameEditText, buyDateEditText, useDateEditText;
    private Button selectImageBtn, fixButton, deleteButton;
    private String foodId;
    private FirebaseStorage storage;
    private ImageView foodImageView;
    private Uri selectedImageUri;
    private FirebaseAuth auth;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list_fix_page);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        foodImageView = findViewById(R.id.food_image_view);
        selectImageBtn = findViewById(R.id.image_upload_btn);
        foodNameEditText = findViewById(R.id.fix_food_name);
        useDateEditText = findViewById(R.id.fix_use_date);
        buyDateEditText = findViewById(R.id.fix_buy_date);
        fixButton = findViewById(R.id.fixButton);
        deleteButton = findViewById(R.id.deleteButton);

        useDateEditText.addTextChangedListener(useDateTextWatcher);
        buyDateEditText.addTextChangedListener(buyDateTextWatcher);
        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            foodId = extras.getString("foodId");
        }



        loadFoodInformation(); //foodId를 사용하여 사용자의 Firebase 데이터베이스에서 특정 음식 정보를 가져옴. 해당 정보를 EditText 위젯에 표시

        fixButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFoodInformation(); //EditText 위젯에서 사용자가 제공한 수정된 음식 정보를 가져와 Firebase 데이터베이스에 업데이트.
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFoodInformation(); //Firebase 데이터베이스에서 특정 음식 정보를 삭제
            }
        });

        ImageButton foodSearchButton = findViewById(R.id.btnSearch);
        foodSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the text from fix_food_name
                EditText fixFoodNameEditText = findViewById(R.id.fix_food_name);
                String searchText = fixFoodNameEditText.getText().toString();

                // Pass the text and the selection to the FoodSearchPage
                Intent intent = new Intent(FoodListFixPage.this, SearchPageActivity.class);
                intent.putExtra("searchText", searchText);
                intent.putExtra("spinnerSelection", "재료"); // Change this to whatever text represents "재료" in your spinner
                startActivity(intent);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                foodImageView.setImageBitmap(bitmap);
                uploadImageToFirebaseStorage(selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();

        if (imageUri != null) {
            String imageFileName = "foods/" + userId + "/" + foodId + ".jpg";
            StorageReference storageRef = storage.getReference().child(imageFileName);
            UploadTask uploadTask = storageRef.putFile(imageUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri imageUrl = task.getResult();

                                DatabaseReference foodRef = databaseRef.child("users").child(userId).child("foods").child(foodId);
                                foodRef.child("imageUrl").setValue(imageUrl.toString());

                                Toast.makeText(FoodListFixPage.this, "이미지가 업로드되었습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(FoodListFixPage.this, "이미지 업로드 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }



    private void loadFoodInformation() {
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();
        if (foodId != null) {    // foodId가 null이 아닌 경우에만 Firebase에서 데이터를 가져오도록 수정
            DatabaseReference userFoodsRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("foods").child(foodId);

            userFoodsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        FoodItem food = dataSnapshot.getValue(FoodItem.class);
                        if (food != null) {
                            // 가져온 데이터를 EditText 위젯에 표시
                            foodNameEditText.setText(food.getFoodName());
                            buyDateEditText.setText(food.getbuyDate());
                            useDateEditText.setText(food.getuseDate());
                        }
                    } else {
                        // 해당 식재료의 정보가 존재하지 않는 경우, 예를 들어 삭제된 경우 처리
                        Toast.makeText(FoodListFixPage.this, "해당 식재료의 정보가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 데이터베이스에서 오류 발생 시 처리
                    Toast.makeText(FoodListFixPage.this, "데이터베이스에서 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // foodId가 null일 때 처리
            Toast.makeText(FoodListFixPage.this, "foodId가 null입니다", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateFoodInformation() {
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();


        String updatedFoodName = foodNameEditText.getText().toString();
        String updatedBuyDate = buyDateEditText.getText().toString();
        String updatedUseDate = useDateEditText.getText().toString();

        DatabaseReference foodRef = databaseRef.child("users").child(userId).child("foods").child(foodId);
        foodRef.child("foodName").setValue(updatedFoodName);
        foodRef.child("buyDate").setValue(updatedBuyDate);
        foodRef.child("useDate").setValue(updatedUseDate);

        Toast.makeText(FoodListFixPage.this, "식재료 정보가 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
        finish();

    }

    private void deleteFoodInformation() {
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();
        DatabaseReference foodRef = databaseRef.child("users").child(userId).child("foods").child(foodId);

        foodRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(FoodListFixPage.this, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(FoodListFixPage.this, "삭제를 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showDatePickerDialog(View v) {
        if (v.getId() == R.id.fix_buy_date) {
            // "구매한 날짜" EditText를 클릭했을 때
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    // 선택한 날짜를 EditText에 설정
                    String selectedDate = String.format(Locale.getDefault(), "%04d%02d%02d", year, month + 1, day);
                    buyDateEditText.setText(selectedDate);
                }
            }, year, month, day);

            datePickerDialog.show();
        }

        if (v.getId() == R.id.fix_use_date) {
            // "구매한 날짜" EditText를 클릭했을 때
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    // 선택한 날짜를 EditText에 설정
                    String selectedDate = String.format(Locale.getDefault(), "%04d%02d%02d", year, month + 1, day);
                    useDateEditText.setText(selectedDate);
                }
            }, year, month, day);

            datePickerDialog.show();
        }
    }

    TextWatcher useDateTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // 필요한 경우 구현
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 필요한 경우 구현
        }
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 8) {
                // 입력된 길이가 8인 경우 (예: 20231102)
                String inputDate = s.toString();
                String formattedDate = inputDate.substring(0, 4) + "-" + inputDate.substring(4, 6) + "-" + inputDate.substring(6, 8);
                useDateEditText.setText(formattedDate);
            }
        }
    };
    TextWatcher buyDateTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // 필요한 경우 구현
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 필요한 경우 구현
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 8) {
                // 입력된 길이가 8인 경우 (예: 20231102)
                String inputDate = s.toString();
                String formattedDate = inputDate.substring(0, 4) + "-" + inputDate.substring(4, 6) + "-" + inputDate.substring(6, 8);
                buyDateEditText.setText(formattedDate);
            }
        }
    };
}
