package com.example.refrigerator_2023final;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.content.DialogInterface;
import android.widget.CheckBox;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.CompoundButton;
import android.widget.ToggleButton;



public class RecipeRegistActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    private ImageView imageViewRecipe;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private Button btnAddImage, btnAllergyCheck, RecipeRegistBtn;
    private EditText editTextRecipeTitle;
    private EditText editTextShortDescription;
    private EditText editTextIngredients;
    private EditText editTextSeasoning;
    private EditText editTextStep1;
    private EditText editTextStep2;
    private EditText editTextStep3;
    private EditText editTextStep5;

    private EditText editTextStep4;
    private EditText editTextStep6;
    private EditText editTextStep7;
    private EditText editTextStep8;
    private EditText editTextStep9;
    private EditText editTextStep10;
    private EditText editTextStep11;
    private EditText editTextStep12;


    Map<String, Object> recipeData = new HashMap<>();


    private boolean[] allergyStates = new boolean[10];

    private CheckBox[] allergyCheckBoxes = new CheckBox[10];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_regist);

        // 레이아웃에서 각 요소들을 찾아서 변수에 할당
        btnAllergyCheck = findViewById(R.id.btnAllergyCheck);
        imageViewRecipe = findViewById(R.id.imageViewRecipe); //요리 대표 사진
        btnAddImage = findViewById(R.id.btnAddImage); //사진 등록 버튼
        RecipeRegistBtn = findViewById(R.id.btnAddRecipe); //레시피 등록 버튼
        editTextRecipeTitle = findViewById(R.id.editTextRecipeTitle); //레시피 제목
        editTextShortDescription = findViewById(R.id.editTextShortDescription); //레시피 한 줄 소개
        editTextIngredients = findViewById(R.id.editTextIngredients); //레시피 식재료
        editTextSeasoning = findViewById(R.id.editTextSeasoning); //레시피 양념 재료
        //요리 순서
        editTextStep1 = findViewById(R.id.editTextStep1);
        editTextStep2 = findViewById(R.id.editTextStep2);
        editTextStep3 = findViewById(R.id.editTextStep3);
        editTextStep4 = findViewById(R.id.editTextStep4);
        editTextStep5 = findViewById(R.id.editTextStep5);
        editTextStep6 = findViewById(R.id.editTextStep6);
        editTextStep7 = findViewById(R.id.editTextStep7);
        editTextStep8 = findViewById(R.id.editTextStep8);
        editTextStep9 = findViewById(R.id.editTextStep9);
        editTextStep10 = findViewById(R.id.editTextStep10);
        editTextStep11 = findViewById(R.id.editTextStep11);
        editTextStep12 = findViewById(R.id.editTextStep12);



        //사용자가 레시피를 등록할 때 입력한 정보를 읽어옴


        // Firebase 초기화
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ알러지 유발 식재료 정보 체크 다이얼로그 띄우기ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        btnAllergyCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllergyDialog();
            }
        });


        //사진 등록 버튼 클릭 시 갤러리에서 이미지 선택
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        //레시피 등록 버튼 클릭 시 데이터 저장
        RecipeRegistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            String recipeTitle = editTextRecipeTitle.getText().toString();
            String shortDescription = editTextShortDescription.getText().toString();
            String ingredients = editTextIngredients.getText().toString();
            String seasoning = editTextSeasoning.getText().toString();
            String step1 = editTextStep1.getText().toString();
            String step2 = editTextStep2.getText().toString();
            String step3 = editTextStep3.getText().toString();
            String step4 = editTextStep4.getText().toString();
            String step5 = editTextStep5.getText().toString();
            String step6 = editTextStep6.getText().toString();
            String step7 = editTextStep7.getText().toString();
            String step8 = editTextStep8.getText().toString();
            String step9 = editTextStep9.getText().toString();
            String step10 = editTextStep10.getText().toString();
            String step11 = editTextStep11.getText().toString();
            String step12 = editTextStep12.getText().toString();
                // 레시피 데이터를 Map으로 저장
            Map<String, Object> recipeData = new HashMap<>();
        recipeData.put("recipeTitle", recipeTitle);
        recipeData.put("shortDescription", shortDescription);
        recipeData.put("ingredients", ingredients);
        recipeData.put("seasoning", seasoning);
        recipeData.put("step1", step1);
        recipeData.put("step2", step2);
        recipeData.put("step3", step3);
        recipeData.put("step4", step4);
        recipeData.put("step5", step5);
        recipeData.put("step6", step6);
        recipeData.put("step7", step7);
        recipeData.put("step8", step8);
        recipeData.put("step9", step9);
        recipeData.put("step10", step10);
        recipeData.put("step11", step11);
        recipeData.put("step12", step12);

        for (int i = 0; i < allergyStates.length; i++) {
                String allergyKey = "allergy" + (i + 1);
                recipeData.put(allergyKey, allergyStates[i]);
            }
            saveRecipeData();
        }
        });




        // 현재 사용자 가져오기
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // 파이어스토어에 저장
            firestore.collection("users").document(uid).collection("recipes").add(recipeData)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // 레시피가 성공적으로 등록되면 문서의 고유 식별자 가져오기
                            String recipeId = documentReference.getId();
                            showToast("레시피가 성공적으로 등록되었습니다. Recipe ID: " + recipeId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast("레시피 등록에 실패했습니다.");
                        }
                    });
        }
        loadAllergyStatesFromFirestore();
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ알러지유발 식재료 정보 체크 다이얼로그ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    private void showToast(String message) {
        Toast.makeText(RecipeRegistActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void showAllergyDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);

        builder.setTitle("알러지 유발 식재료 체크");
        builder.setMessage("알러지 성분");

        View dialogView = getLayoutInflater().inflate(R.layout.allergy_filter_dialog_layout, null);
        builder.setView(dialogView);

        for (int i = 0; i < 10; i++) {
            int checkBoxId = getResources().getIdentifier("checkBox" + (i + 1), "id", getPackageName());
            allergyCheckBoxes[i] = dialogView.findViewById(checkBoxId);
        }

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 확인 버튼 눌렀을 때의 처리
                // 여기서 체크한 알러지 유발 재료 목록을 가져와서 사용할 수 있음
                // Update allergy states based on the checked items
                for (int j = 0; j < allergyCheckBoxes.length; j++) {
                    allergyStates[j] = allergyCheckBoxes[j].isChecked();
                }
                // Save the updated allergy states to Firestore
                saveAllergyStatesToFirestore(allergyCheckBoxes);
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 취소 버튼 눌렀을 때의 처리
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 알러지 유발 식재료 정보 체크 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    private void saveAllergyStatesToFirestore(CheckBox[] allergyCheckBoxes) {
        // 여기서 Firestore에 체크박스 상태를 저장하는 로직을 추가
        // 예를 들어, 현재 사용자의 UID를 이용해서 해당 사용자의 문서에 저장할 수 있습니다.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            Map<String, Object> allergyData = new HashMap<>();
            for (int i = 0; i < allergyCheckBoxes.length; i++) {
                String checkBoxKey = "allergy" + (i + 1);
                boolean isChecked = allergyCheckBoxes[i].isChecked();
                allergyData.put(checkBoxKey, isChecked);
            }

            FirebaseFirestore.getInstance().collection("users").document(uid)
                    .collection("allergyData")
                    .document("allergies")
                    .set(allergyData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(RecipeRegistActivity.this, "알러지 정보가 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RecipeRegistActivity.this, "알러지 정보 저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            Toast.makeText(RecipeRegistActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
        }
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ갤러리에서 이미지 선택ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewRecipe.setImageURI(imageUri);
        }
    }

    private void saveRecipeData() {
        // Clear the recipeData map before adding new values
        recipeData.clear();

        // Add non-empty values to the recipeData map
        addValueToRecipeData("recipeTitle", editTextRecipeTitle.getText().toString());
        addValueToRecipeData("shortDescription", editTextShortDescription.getText().toString());
        addIngredientsToRecipeData();
        addValueToRecipeData("seasoning", editTextSeasoning.getText().toString());
        addValueToRecipeData("step1", editTextStep1.getText().toString());
        addValueToRecipeData("step2", editTextStep2.getText().toString());
        addValueToRecipeData("step3", editTextStep3.getText().toString());
        addValueToRecipeData("step4", editTextStep4.getText().toString());
        addValueToRecipeData("step5", editTextStep5.getText().toString());
        addValueToRecipeData("step6", editTextStep6.getText().toString());
        addValueToRecipeData("step7", editTextStep7.getText().toString());
        addValueToRecipeData("step8", editTextStep8.getText().toString());
        addValueToRecipeData("step9", editTextStep9.getText().toString());
        addValueToRecipeData("step10", editTextStep10.getText().toString());
        addValueToRecipeData("step11", editTextStep11.getText().toString());
        addValueToRecipeData("step12", editTextStep12.getText().toString());

        for (int i = 0; i < allergyStates.length; i++) {
            String allergyKey = "allergy" + (i + 1);
            recipeData.put(allergyKey, allergyStates[i]);
        }

        //사용자가 선택한 이미지가 있으면 Firebase Cloud Storage에 업로드
        if (imageUri != null) {
            uploadImageToFirebase();
        }
    }

    //입력한 재료를 각각 저장하여 유사 검색이 가능하도록 함
    private void addIngredientsToRecipeData() {
        String ingredientsString = editTextIngredients.getText().toString();
        String[] ingredientsArray = ingredientsString.split("\\s*,\\s*");
        List<String> ingredientsList = new ArrayList<>(Arrays.asList(ingredientsArray));

        recipeData.put("ingredients", ingredientsList);
    }


    private void addValueToRecipeData(String key, String value) {
        // Add key-value pair to recipeData only if the value is not empty
        if (!value.trim().isEmpty()) {
            recipeData.put(key, value);
        }
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ선택한 이미지 fire store에 저장ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    private void uploadImageToFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            final String uid = currentUser.getUid();

            // Generate a unique file name (you can use recipe ID or timestamp)
            final String fileName = "recipe_image_" + System.currentTimeMillis() + ".jpg";

            StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                    .child("recipe_images")
                    .child(uid)
                    .child(fileName);

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUrl) {
                                    // Save the download URL and user ID to Firestore
                                    saveImageUrlToFirestore(downloadUrl.toString(), uid);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(RecipeRegistActivity.this, "이미지 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ선택한 이미지 firestore에 URL로 저장ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    private void saveImageUrlToFirestore(String imageUrl, String userId) {
        // Save the image URL, user ID, and other recipe data to Firestore
        recipeData.put("imageUrl", imageUrl);
        recipeData.put("userId", userId); // Add user ID to associate the recipe with the user

        firestore.collection("recipes")
                .add(recipeData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(RecipeRegistActivity.this, "레시피가 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                        // Handle successful recipe registration
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RecipeRegistActivity.this, "레시피 등록에 실패했습니다." + e.getMessage(), Toast.LENGTH_SHORT).show();
                        // Handle failed recipe registration
                    }
                });
    }


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    private void loadAllergyStatesFromFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            FirebaseFirestore.getInstance().collection("users").document(uid)
                    .collection("allergyData")
                    .document("allergies")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                for (int i = 0; i < allergyCheckBoxes.length; i++) {
                                    String checkBoxKey = "allergy" + (i + 1);
                                    allergyStates[i] = documentSnapshot.getBoolean(checkBoxKey);
                                    if (allergyCheckBoxes[i] != null) {
                                        allergyCheckBoxes[i].setChecked(allergyStates[i]);
                                    }
                                }
                            }
                        }
                    });
        }
    }




}
