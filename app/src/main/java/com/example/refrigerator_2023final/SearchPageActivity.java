package com.example.refrigerator_2023final;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.refrigerator_2023final.RecipeItem;
import com.example.refrigerator_2023final.RecipeListAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchPageActivity extends AppCompatActivity {
    private CheckBox checkAllergyFilter;
    private EditText editTextSearch;
    private ImageButton btnSearch;
    private ListView listViewRecipes;

    private FirebaseFirestore db;
    private List<String> userAllergyData;
    private int selectedRecipePosition;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchpage);

        // UI 요소 초기화
        editTextSearch = findViewById(R.id.editTextSearch);
        btnSearch = findViewById(R.id.btnSearch);
        listViewRecipes = findViewById(R.id.listViewRecipes);
        checkAllergyFilter = findViewById(R.id.checkAllergyFilter);
        // Firestore 초기화
        db = FirebaseFirestore.getInstance();

        // 검색 버튼 클릭 리스너 설정
        btnSearch.setOnClickListener(v -> {
            // 검색어 가져오기
            String searchQuery = editTextSearch.getText().toString().trim();
            if (!TextUtils.isEmpty(searchQuery)) {
                // 스피너에서 선택된 검색 옵션 가져오기
                Spinner spinnerSearchOption = findViewById(R.id.spinner);
                String selectedSearchOption = spinnerSearchOption.getSelectedItem().toString();

                // 선택된 옵션에 따라 검색 메소드 호출
                if ("제목".equals(selectedSearchOption)) {
                    searchRecipes(searchQuery, false);
                } else if ("재료".equals(selectedSearchOption)) {
                    searchRecipes(searchQuery, true);
                } else {
                    // 다른 검색 옵션에 대한 처리 (필요에 따라 추가)
                    Toast.makeText(SearchPageActivity.this, "지원하지 않는 검색 옵션입니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SearchPageActivity.this, "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 스피너 초기화 및 리스너 설정
        Spinner spinnerSearchOption = findViewById(R.id.spinner);
        String[] searchOptions = getResources().getStringArray(R.array.search_options);
        ArrayAdapter<String> searchOptionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, searchOptions);
        searchOptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSearchOption.setAdapter(searchOptionAdapter);
        spinnerSearchOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 스피너에서 선택된 아이템 처리 (필요에 따라 추가)
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무것도 선택되지 않았을 때의 처리
            }
        });

        // Retrieve the searchText and spinnerSelection from the intent
        String searchText = getIntent().getStringExtra("searchText");
        String spinnerSelection = getIntent().getStringExtra("spinnerSelection");

        // Set the text in the editTextSearch
        editTextSearch.setText(searchText);
        String[] spinnerOptions = {"제목", "재료"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSearchOption.setAdapter(spinnerAdapter);

        int position = spinnerAdapter.getPosition(spinnerSelection);

        if (position != -1) {
            spinnerSearchOption.setSelection(position);
        }

        listViewRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the clicked item from the adapter
                RecipeItem clickedRecipe = (RecipeItem) parent.getItemAtPosition(position);

                // Save the selected position
                selectedRecipePosition = position;

                // Check for allergens
                if (clickedRecipe != null && containsAllergen(userAllergyData, clickedRecipe.getIngredients())) {
                    // Display an alert message for recipes containing allergens
                    showAlert("알러지 유발 식재료가 포함된 레시피 입니다");
                } else {
                    // Launch RecipeDetailActivity with the clicked recipeId
                    if (clickedRecipe != null) {
                        // ...

                        // No need to use 'position' here
                        boolean isAllergyFilterChecked = checkAllergyFilter.isChecked();
                        Intent intent = new Intent(SearchPageActivity.this, RecipeDetailActivity.class);
                        intent.putExtra("recipeId", clickedRecipe.getId());
                        intent.putExtra("isAllergyFilterChecked", isAllergyFilterChecked);
                        startActivity(intent);
                    }
                }
            }
        });


        userAllergyData = getUserAllergyData();

    }

    private boolean containsAllergen(List<String> userAllergyData, List<String> recipeIngredients) {
        for (String allergen : userAllergyData) {
            /*if (recipeIngredients.contains(allergen)) {
                return true;
            }*/
        }
        return false;
    }

    private boolean hasAllergens(List<Boolean> allergens) {
        for (Boolean allergen : allergens) {
            if (allergen) {
                return true;
            }
        }
        return false;
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    // Method to get user's allergy data (replace this with your actual implementation)
    private List<String> getUserAllergyData() {
        // Fetch and return user's allergy data from wherever it is stored (e.g., Firebase, SharedPreferences, etc.)
        // This is just a placeholder; replace it with your actual implementation
        List<String> allergies = new ArrayList<>();
        allergies.add("Peanuts");
        allergies.add("Shellfish");
        // Add more allergens as needed
        return allergies;
    }

    // Combined search method
    private void searchRecipes(String searchQuery, boolean allergyFilter) {
        if (TextUtils.isEmpty(searchQuery)) {
            return;
        }

        List<RecipeItem> searchResults = new ArrayList<>();

        // Search by title
        db.collectionGroup("recipes")
                .whereEqualTo("recipeTitle", searchQuery)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        RecipeItem recipeItem = createRecipeItemFromDocument(document);

                        // Apply allergy filter if needed
                        if (!allergyFilter || !containsAllergen(userAllergyData, recipeItem.getIngredients())) {
                            searchResults.add(recipeItem);
                        }
                    }

                    // Continue to search by ingredient
                    searchRecipesByIngredient(searchQuery, allergyFilter, searchResults);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SearchPageActivity.this, "Title search error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("SearchPageActivity", "Error searching recipes by title", e);
                });
    }

    // Helper method for searching by ingredient
    private void searchRecipesByIngredient(String searchQuery, boolean allergyFilter, List<RecipeItem> searchResults) {
        db.collectionGroup("recipes")
                .whereArrayContainsAny("ingredients", Arrays.asList(searchQuery.split("\\s*,\\s*")))
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        RecipeItem recipeItem = createRecipeItemFromDocument(document);

                        // Apply allergy filter if needed
                        if (!allergyFilter || !containsAllergen(userAllergyData, recipeItem.getIngredients())) {
                            searchResults.add(recipeItem);
                        }
                    }

                    // Display the combined search results
                    showSearchResults(searchResults);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SearchPageActivity.this, "Ingredient search error", Toast.LENGTH_SHORT).show();
                    Log.e("SearchPageActivity", "Error searching recipes by ingredient", e);
                });
    }

    // Firestore 문서에서 RecipeItem으로 변환하는 메소드
    private RecipeItem createRecipeItemFromDocument(QueryDocumentSnapshot document) {
        String recipeId = document.getId();
        String recipeTitle = document.getString("recipeTitle");
        String shortDescription = document.getString("shortDescription");
        String imageUrl = document.getString("imageUrl");

        // Add the following lines to fetch the 'ingredients' field
        List<String> ingredients = (List<String>) document.get("ingredients");

        RecipeItem recipeItem = new RecipeItem(recipeId, recipeTitle, shortDescription, imageUrl);
        recipeItem.setId(recipeId);
        recipeItem.setTitle(recipeTitle);
        recipeItem.setDescription(shortDescription);
        recipeItem.setImageUrl(imageUrl);

        // Set the ingredients in the recipeItem object
        recipeItem.setIngredients(ingredients);

        return recipeItem;
    }

    // 검색 결과를 화면에 표시하는 메소드
    private void showSearchResults(List<RecipeItem> results) {
        if (!results.isEmpty()) {
            RecipeListAdapter adapter = new RecipeListAdapter(SearchPageActivity.this, results);
            listViewRecipes.setAdapter(adapter);
        }
    }

    public void GoToSearchPage(View view) {
        Intent intent = new Intent(SearchPageActivity.this, SearchPageActivity.class);
        startActivity(intent);
    }

    public void GoToFoodList(View view) {
        Intent intent = new Intent(SearchPageActivity.this, FoodListPage.class);
        startActivity(intent);
    }

    public void GotoHome(View view) {
        Intent intent = new Intent(SearchPageActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void GoToMyPage(View view) {
        Intent intent = new Intent(SearchPageActivity.this, MyPageJava.class);
        startActivity(intent);
    }

    public void GoToRecipeDetail(View view) {
        // Use the saved position
        RecipeItem clickedRecipe = (RecipeItem) listViewRecipes.getItemAtPosition(selectedRecipePosition);

        if (clickedRecipe != null) {
            boolean isAllergyFilterChecked = checkAllergyFilter.isChecked();
            Intent intent = new Intent(SearchPageActivity.this, RecipeDetailActivity.class);
            intent.putExtra("recipeId", clickedRecipe.getId());
            intent.putExtra("isAllergyFilterChecked", isAllergyFilterChecked);
            startActivity(intent);
        }
    }
}