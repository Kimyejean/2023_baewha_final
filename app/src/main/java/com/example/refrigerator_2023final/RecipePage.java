package com.example.refrigerator_2023final;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
public class RecipePage extends AppCompatActivity {
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);

        // Firebase Firestore 초기화
        firestore = FirebaseFirestore.getInstance();

        // 레시피 정보를 Firestore에서 가져오기
        getRecipeDataFromFirestore();

        // 등록 버튼에 대한 클릭 리스너 설정
        Button reviewRegist = findViewById(R.id.btnReiviewRegist);
        reviewRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 레시피 등록 버튼이 클릭되었을 때의 동작 추가
                // 예를 들어, 레시피를 Firestore에 등록하는 코드 등을 추가
            }
        });
    }

    private void getRecipeDataFromFirestore() {
        // Firestore에서 레시피 정보 가져오기
        firestore.collection("recipes").document("your_recipe_document_id")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Document에서 데이터 추출
                                Recipe recipe = document.toObject(Recipe.class);

                                // 화면에 데이터 설정
                                setRecipeDataOnScreen(recipe);
                            } else {
                                // Document가 존재하지 않을 때의 처리
                            }
                        } else {
                            // 작업이 실패한 경우의 처리
                        }
                    }
                });
    }

    private void setRecipeDataOnScreen(Recipe recipe) {
        // TextView 초기화
        TextView recipeTitleTextView = findViewById(R.id.RecipeTitle);
        TextView shortDescriptionTextView = findViewById(R.id.ShortDescription);
        TextView ingredientsTextView = findViewById(R.id.Ingredients);
        TextView seasoningTextView = findViewById(R.id.Seasoning);
        TextView allergyCheckTextView = findViewById(R.id.AllergyCheck);
        TextView step1TextView = findViewById(R.id.Step1);
        TextView step2TextView = findViewById(R.id.Step2);
        TextView step3TextView = findViewById(R.id.Step3);
        TextView step4TextView = findViewById(R.id.Step4);
        TextView step5TextView = findViewById(R.id.Step5);
        TextView step6TextView = findViewById(R.id.Step6);
        TextView step7TextView = findViewById(R.id.Step7);
        TextView step8TextView = findViewById(R.id.Step8);
        TextView step9TextView = findViewById(R.id.Step9);
        TextView step10TextView = findViewById(R.id.Step10);
        TextView step11TextView = findViewById(R.id.Step11);
        TextView step12TextView = findViewById(R.id.Step12);

        ImageView imageViewRecipe = findViewById(R.id.imageViewRecipe);

        // 레시피 정보를 TextView에 설정
        recipeTitleTextView.setText(recipe.getRecipeTitle());
        shortDescriptionTextView.setText(recipe.getShortDescription());
        ingredientsTextView.setText(recipe.getIngredients());
        seasoningTextView.setText(recipe.getSeasoning());
        allergyCheckTextView.setText(recipe.getAllergyCheck());
        step1TextView.setText(recipe.getStep1());
        step2TextView.setText(recipe.getStep2());
        step3TextView.setText(recipe.getStep3());
        step4TextView.setText(recipe.getStep4());
        step5TextView.setText(recipe.getStep5());
        step6TextView.setText(recipe.getStep6());
        step7TextView.setText(recipe.getStep7());
        step8TextView.setText(recipe.getStep8());
        step9TextView.setText(recipe.getStep9());
        step10TextView.setText(recipe.getStep10());
        step11TextView.setText(recipe.getStep11());
        step12TextView.setText(recipe.getStep12());

        String imageUrl = recipe.getRecipeImgUrl(); // 여기서 imageUrl는 Recipe 클래스에 추가해야 하는 필드입니다.
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewRecipe);

    }
}