package com.example.refrigerator_2023final;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import java.util.List;

public class RecipeListAdapter extends ArrayAdapter<RecipeItem> {

    public RecipeListAdapter(@NonNull Context context, @NonNull List<RecipeItem> recipeItems) {
        super(context, 0, recipeItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_recipe, parent, false);
        }

        ImageView imageViewRecipe = convertView.findViewById(R.id.imageViewRecipeItem); // 고유한 ID로 수정
        TextView textViewRecipeTitle = convertView.findViewById(R.id.textViewRecipeTitle);
        TextView textViewRecipeDescription = convertView.findViewById(R.id.textViewRecipeDescription);

        RecipeItem recipeItem = getItem(position);

        if (recipeItem != null) {
            Log.d("RecipeListAdapter", "Recipe ID: " + recipeItem.getId());
            Log.d("RecipeListAdapter", "Image URL: " + recipeItem.getImageUrl());
            // 이미지 로드
            if (recipeItem.getImageUrl() != null && !recipeItem.getImageUrl().isEmpty()) {

                Picasso.get()
                        .load(recipeItem.getImageUrl())
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(imageViewRecipe);

                // 이미지 로드
                /*if (recipeItem.getimageUrl() != null && !recipeItem.getimageUrl().isEmpty()) {
                    // Picasso.get().load(recipeItem.getimageUrl()).into(imageViewRecipe);
                    imageViewRecipe.setImageResource(R.drawable.calendar); // 테스트를 위해 임시로 정적 이미지로 설정
                }*/

            }
            textViewRecipeTitle.setText(recipeItem.getRecipeTitle());
            textViewRecipeDescription.setText(recipeItem.getShortDescription());
        }else{
            Log.d("RecipeListAdapter", "RecipeItem is null at position: " + position);
        }

        return convertView;
    }
}
