package com.example.refrigerator_2023final;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.net.Uri;
import androidx.annotation.Nullable;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.content.DialogInterface;
import android.widget.CheckBox;


public class MyPageJava extends AppCompatActivity {

    private TextView userEmailTextView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView userView2;
    private Uri selectedImageUri;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private StorageReference storageReference;
    private Button proBtn;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        userEmailTextView = findViewById(R.id.nicknameView3);
        userView2 = findViewById(R.id.userProfile);
        userView2.getLayoutParams().width = 400;
        userView2.getLayoutParams().height = 400;
        userView2.requestLayout();
        proBtn = findViewById(R.id.profile_img_btn);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            userId = user.getUid();
            String userEmail = user.getEmail();
            String[] parts = userEmail.split("@");
            if (parts.length > 0) {
                String username = parts[0];
                userEmailTextView.setText(username);
            }
        }

        storageReference = FirebaseStorage.getInstance().getReference();

        proBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        Button allergyButton = findViewById(R.id.allergy);
        allergyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllergyDialog();
            }
        });

        loadProfileImage();
    }


    private void showAllergyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("알러지 필터");
        builder.setMessage("알러지 성분");

        View dialogView = getLayoutInflater().inflate(R.layout.allergy_filter_dialog_layout, null);
        builder.setView(dialogView);

        CheckBox[] allergyCheckBoxes = new CheckBox[10];
        for (int i = 0; i < 10; i++) {
            int checkBoxId = getResources().getIdentifier("checkBox" + (i + 1), "id", getPackageName());
            allergyCheckBoxes[i] = dialogView.findViewById(checkBoxId);
        }

        // 팝업 열릴 때 Firebase에서 알러지 정보를 읽어와서 체크박스 상태 설정
        loadAllergyInfo(allergyCheckBoxes);

        builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 체크박스 상태를 Firebase에 저장
                saveAllergyInfo(allergyCheckBoxes);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Firebase에서 알러지 정보를 읽어와서 체크박스 상태 설정
    private void loadAllergyInfo(CheckBox[] allergyCheckBoxes) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users")
                .child(userId)
                .child("allergies");

        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                for (int i = 0; i < 10; i++) {
                    String allergyKey = "allergy" + (i + 1);
                    Boolean isSelected = dataSnapshot.child(allergyKey).getValue(Boolean.class);
                    if (isSelected != null) {
                        allergyCheckBoxes[i].setChecked(isSelected);
                    }
                }
            } else {
                // 예외 처리: 데이터를 불러오지 못한 경우
                Toast.makeText(getApplicationContext(), "알러지 정보를 불러오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 체크박스 상태를 Firebase에 저장
    private void saveAllergyInfo(CheckBox[] allergyCheckBoxes) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users")
                .child(userId)
                .child("allergies");

        for (int i = 0; i < 10; i++) {
            String allergyKey = "allergy" + (i + 1);
            boolean isSelected = allergyCheckBoxes[i].isChecked();
            databaseReference.child(allergyKey).setValue(isSelected);
        }
    }

    private void loadProfileImage() {
        StorageReference profileImageRef = storageReference.child("profiles/" + userId + ".jpg");

        profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get()
                    .load(uri)
                    .resize(400, 400)
                    .centerCrop()
                    .into(userView2, new Callback() {
                        @Override
                        public void onSuccess() {
                            // 이미지 로딩 성공
                        }

                        @Override
                        public void onError(Exception e) {
                            // 이미지 로딩 실패
                            Toast.makeText(getApplicationContext(), "이미지 로딩 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "이미지 URL 가져오기 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    // 갤러리에서 선택한 이미지 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            StorageReference profileImageRef = storageReference.child("profiles/" + userId + ".jpg");
            UploadTask uploadTask = profileImageRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                loadProfileImage();
                saveProfileImageUrl(profileImageRef.toString());
            }).addOnFailureListener(e -> {
                Toast.makeText(getApplicationContext(), "이미지 업로드 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    // Firebase Realtime Database에 프로필 이미지 URL 저장
    private void saveProfileImageUrl(String imageUrl) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users")
                .child(userId)
                .child("profileImageUrl");
        databaseReference.setValue(imageUrl).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "프로필 이미지가 업로드되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "프로필 이미지 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }



}