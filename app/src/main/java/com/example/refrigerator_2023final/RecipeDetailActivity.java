package com.example.refrigerator_2023final;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private ImageView imageViewRecipeDetail;
    private TextView titleDetail, ShortDescriptionDetail, IngredientsDetail, TextViewSeasoningDetail;
    private TextView StepDetail_1, StepDetail_2, StepDetail_3, StepDetail_4, StepDetail_5,
            StepDetail_6, StepDetail_7, StepDetail_8, StepDetail_9, StepDetail_10,
            StepDetail_11, StepDetail_12;

    private FirebaseFirestore firestore;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);

        firestore = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        } else {
            // Handle the case where the user is not logged in
            // You might want to redirect the user to the login page or handle it accordingly
            finish();
        }

        // Initialize layout elements
        imageViewRecipeDetail = findViewById(R.id.imageViewRecipeDetail);
        titleDetail = findViewById(R.id.titleDetail);
        ShortDescriptionDetail = findViewById(R.id.ShortDescriptionDetail);
        IngredientsDetail = findViewById(R.id.IngredientsDetail);
        TextViewSeasoningDetail = findViewById(R.id.TextViewSeasoningDetail);
        StepDetail_1 = findViewById(R.id.StepDetail_1);
        StepDetail_2 = findViewById(R.id.StepDetail_2);
        StepDetail_3 = findViewById(R.id.StepDetail_3);
        StepDetail_4 = findViewById(R.id.StepDetail_4);
        StepDetail_5 = findViewById(R.id.StepDetail_5);
        StepDetail_6 = findViewById(R.id.StepDetail_6);
        StepDetail_7 = findViewById(R.id.StepDetail_7);
        StepDetail_8 = findViewById(R.id.StepDetail_8);
        StepDetail_9 = findViewById(R.id.StepDetail_9);
        StepDetail_10 = findViewById(R.id.StepDetail_10);
        StepDetail_11 = findViewById(R.id.StepDetail_11);
        StepDetail_12 = findViewById(R.id.StepDetail_12);

        String recipeId = getIntent().getStringExtra("recipeId");

        if (recipeId != null) {
            Log.d("RecipeDetailActivity", "Recipe ID: " + recipeId);
            // Call the method to fetch recipe details from Firestore
            loadRecipeDetails(recipeId);
        } else {
            // Handle the case where recipeId is null
            Log.e("RecipeDetailActivity", "Recipe ID is null");
            finish(); // Finish the activity or handle the null case appropriately
        }
    }

    private void loadRecipeDetails(String recipeId) {
        if (recipeId != null) {
            firestore.collection("recipes").document(recipeId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null && document.exists()) {
                                    // Apply the retrieved recipe information to the layout
                                    Log.d("RecipeDetailActivity", "Document data: " + document.getData());
                                    displayRecipeDetails(document);
                                } else {
                                    // Handle the case where the recipe information is not available
                                    Log.e("RecipeDetailActivity", "Recipe not found");
                                }
                            } else {
                                // Handle the error case
                                Log.e("RecipeDetailActivity", "Error getting document", task.getException());
                            }
                        }
                    });
        } else {
            // Handle the case where recipeId is null
            Log.e("RecipeDetailActivity", "Recipe ID is null");
            finish(); // Finish the activity or handle the null case appropriately
        }
    }

    private void displayRecipeDetails(DocumentSnapshot document) {
        // Get recipe information from the document
        String title = document.getString("recipeTitle");
        String imageUrl = document.getString("imageUrl");
        String shortDescription = document.getString("shortDescription");
        String seasoning = document.getString("seasoning");

        // Handle ingredients as an array
        List<String> ingredientsList = (List<String>) document.get("ingredients");
        if (ingredientsList != null && !ingredientsList.isEmpty()) {
            // Convert ingredients list to a formatted string
            String ingredientsText = TextUtils.join(", ", ingredientsList);
            IngredientsDetail.setText(ingredientsText);
        } else {
            IngredientsDetail.setText("No ingredients available");
        }

        // Handle recipe steps dynamically
        for (int i = 1; i <= 12; i++) {
            String stepKey = "step" + i;
            String imageViewId = "imageView" + i;
            if (document.contains(stepKey)) {
                // If the step key is present in Firestore, retrieve and display the step
                String step = document.getString(stepKey);
                TextView stepTextView = getStepTextView(i);
                ImageView stepImageView = findViewById(getResources().getIdentifier(imageViewId, "id", getPackageName()));

                if (step != null && !step.isEmpty()) {
                    // Display the step
                    stepTextView.setText(step);
                    stepImageView.setVisibility(View.VISIBLE);
                } else {
                    // If step is not available, hide the corresponding TextView
                    stepTextView.setVisibility(View.GONE);
                    stepImageView.setVisibility(View.GONE);
                }
            } else {
                // If the step key is not present, hide the corresponding TextView
                getStepTextView(i).setVisibility(View.GONE);
                findViewById(getResources().getIdentifier(imageViewId, "id", getPackageName())).setVisibility(View.GONE);
            }
        }



        // Set the recipe information to the layout elements
        titleDetail.setText(title);
        ShortDescriptionDetail.setText(shortDescription);
        TextViewSeasoningDetail.setText(seasoning);

        // Load the recipe image using Picasso
        Picasso.get().load(imageUrl).into(imageViewRecipeDetail);

        Log.d("RecipeDetailActivity", "Recipe ID: " + title);


        /*for (int i = 1; i <= 10; i++) {
            String allergyKey = "allergy" + i;
            if (document.contains(allergyKey)) {
                boolean hasAllergy = document.getBoolean(allergyKey);
                if (hasAllergy) {
                    // Show an alert message
                    showAlert("알러지 유발 재료가 포함되어 있습니다!");
                    break; // No need to check further, as we already found an allergy
                }
            }
        }*/

        fetchUserAllergenValues(userId, new OnAllergenValuesFetchedListener() {
            @Override
            public void onAllergenValuesFetched(List<Boolean> userAllergenValues) {
                if (userAllergenValues != null) {
                    // Compare recipe allergens with user allergens
                    for (int i = 1; i <= 10; i++) {
                        String allergyKey = "allergy" + i;
                        if (document.contains(allergyKey)) {
                            boolean hasRecipeAllergy = document.getBoolean(allergyKey);
                            boolean hasUserAllergy = userAllergenValues.size() >= i && userAllergenValues.get(i - 1);

                            if (hasRecipeAllergy && hasUserAllergy) {
                                // Both recipe and user have this allergen
                                showAlert("알러지 유발 재료가 포함되어 있습니다!");
                                break; // No need to check further, as we already found an allergy
                            }
                        }
                    }
                }
            }
        });

    }
    // Inside your RecipeDetailActivity class
    private void fetchUserAllergenValues(String userId, OnAllergenValuesFetchedListener listener) {
        // Implement the logic to fetch user allergen values from the real-time database
        // For simplicity, let's assume your user allergen values are stored in a "users" node
        // with the userId as the key
        DatabaseReference userAllergensRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId)
                .child("allergies");

        userAllergensRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Boolean> userAllergenValues = new ArrayList<>();

                for (DataSnapshot allergenSnapshot : snapshot.getChildren()) {
                    Boolean allergenValue = allergenSnapshot.getValue(Boolean.class);
                    if (allergenValue != null) {
                        userAllergenValues.add(allergenValue);
                    }
                }

                // Notify the listener with the fetched user allergen values
                listener.onAllergenValuesFetched(userAllergenValues);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if needed
                listener.onAllergenValuesFetched(null);
            }
        });
    }

    private interface OnAllergenValuesFetchedListener {
        void onAllergenValuesFetched(List<Boolean> userAllergenValues);
    }

    private void showAlert(String message) {
        // Implement your alert dialog here
        // You can use AlertDialog or any other method to show the alert
        // For example, using AlertDialog:

        new AlertDialog.Builder(this)
                .setTitle("알림")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle OK button click if needed
                    }
                })
                .show();
    }
    private TextView getStepTextView(int stepNumber) {
        // Return the corresponding TextView for the step number
        switch (stepNumber) {
            case 1:
                return StepDetail_1;
            case 2:
                return StepDetail_2;
            case 3:
                return StepDetail_3;
            case 4:
                return StepDetail_4;
            case 5:
                return StepDetail_5;
            case 6:
                return StepDetail_6;
            case 7:
                return StepDetail_7;
            case 8:
                return StepDetail_8;
            case 9:
                return StepDetail_9;
            case 10:
                return StepDetail_10;
            case 11:
                return StepDetail_11;
            case 12:
                return StepDetail_12;
            default:
                return null;
        }
    }
}
