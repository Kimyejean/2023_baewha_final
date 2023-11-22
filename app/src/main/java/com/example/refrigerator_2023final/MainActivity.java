package com.example.refrigerator_2023final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainViewAdapter.OnItemClickListener {
    private TextView userEmailTextView;
    private EditText editTextSearch;
    private ImageButton btnSearch;

    private RecyclerView mMainView1;
    private RecyclerView mMainView2;
    private RecyclerView mMainView3;

    private List<MainViewItem> mList1;
    private List<MainViewItem> mList2;
    private List<MainViewItem> mList3;

    private MainViewAdapter mMainViewAdapter1;
    private MainViewAdapter mMainViewAdapter2;
    private MainViewAdapter mMainViewAdapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userEmailTextView = findViewById(R.id.emailView);

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
                userEmailTextView.setText(username + " 님");
            }
        }

        //editTextSearch = findViewById(R.id.editTextSearch);
        btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextSearch = findViewById(R.id.editTextSearch);
                String searchText = editTextSearch.getText().toString();

                Intent intent = new Intent(MainActivity.this, SearchPageActivity.class);
                intent.putExtra("searchText", searchText);
                startActivity(intent);
            }
        });

        mMainView1 = findViewById(R.id.recyclerView);
        mList1 = new ArrayList<>();
        loadRandomRecipesFromFirestore(mList1, 5);
        mMainViewAdapter1 = new MainViewAdapter(mList1, mList1, mList2, mList3, this); // Set the click listener
        mMainView1.setAdapter(mMainViewAdapter1);
        mMainView1.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        // Second RecyclerView
        mMainView2 = findViewById(R.id.recyclerView2);
        mList2 = new ArrayList<>();
        loadRandomRecipesFromFirestore(mList2, 5);
        mMainViewAdapter2 = new MainViewAdapter(mList2, mList1, mList2, mList3, this);// Set the click listener
        mMainView2.setAdapter(mMainViewAdapter2);
        mMainView2.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        // Third RecyclerView
        mMainView3 = findViewById(R.id.recyclerView3);
        mList3 = new ArrayList<>();
        loadRandomRecipesFromFirestore(mList3, 5);
        mMainViewAdapter3 = new MainViewAdapter(mList3, mList1, mList2, mList3, this); // Set the click listener
        mMainView3.setAdapter(mMainViewAdapter3);
        mMainView3.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

    }

    // Implement the onItemClick method from the OnItemClickListener interface

    @Override
    public void onItemClick(int position, int listIndex) {
        List<MainViewItem> clickedList;

        if (listIndex == 1) {
            clickedList = mList1;
        } else if (listIndex == 2) {
            clickedList = mList2;
        } else if (listIndex == 3) {
            clickedList = mList3;
        } else {
            return; // Invalid list index
        }
        MainViewItem clickedItem = clickedList.get(position);
        Log.d("MainActivity", "List size: " + clickedList.size());
        Log.d("MainActivity", "Clicked Item: " + clickedItem.toString());
        String recipeId = clickedItem.getId();
        Log.d("MainActivity", "Clicked Recipe ID: " + recipeId);
        Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
        intent.putExtra("recipeId", recipeId);
        startActivity(intent);
    }


    private void loadRandomRecipesFromFirestore(List<MainViewItem> list, int numberOfRecipes) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("recipes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                    int totalRecipes = documents.size();
                    int recipesToLoad = Math.min(numberOfRecipes, totalRecipes);

                    // Shuffle the list of documents to get random recipes
                    Collections.shuffle(documents);

                    for (int i = 0; i < recipesToLoad; i++) {
                        DocumentSnapshot document = documents.get(i);
                        String documentId = document.getId(); // Get the document ID

                        String imgUrl = document.getString("imageUrl");
                        String recipeTitle = document.getString("recipeTitle");
                        String shortDescription = document.getString("shortDescription");

                        addItem(list, imgUrl, documentId, recipeTitle, shortDescription);
                    }

                    // Notify the adapter that the data has changed
                    if (list == mList1) {
                        mMainViewAdapter1.notifyDataSetChanged();
                    } else if (list == mList2) {
                        mMainViewAdapter2.notifyDataSetChanged();
                    } else if (list == mList3) {
                        mMainViewAdapter3.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                });
    }



    public void addItem(List<MainViewItem> list, String imgUrl, String id, String recipeTitle, String shortDescription) {
        MainViewItem item = new MainViewItem();
        item.setId(id);
        item.setimgUrl(imgUrl);
        item.setMainText(recipeTitle);
        item.setSubText(shortDescription);
        list.add(item);
    }

    public void GotoHome(View view){
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void GoToFoodList(View view){
        Intent intent = new Intent(MainActivity.this, FoodListPage.class);
        startActivity(intent);
    }

    public void GoToMyPage(View view){
        Intent intent = new Intent(MainActivity.this, MyPageJava.class);
        startActivity(intent);
    }

    public void GoToSearchPage(View view){
        Intent intent = new Intent(MainActivity.this, SearchPageActivity.class);
        startActivity(intent);
    }
}
