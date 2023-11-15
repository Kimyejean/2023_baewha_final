package com.example.refrigerator_2023final;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> recipeItems;
    private Context context;

    public RecipeAdapter(Context context, List<Recipe> recipeItems) {
        this.context = context;
        this.recipeItems = recipeItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_list_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipeItem = recipeItems.get(position);

        // 이미지 로드
        Glide.with(context)
                .load(recipeItem.getRecipeImgUrl())
                .into(holder.imgView_item);

        // 텍스트 설정
        holder.recipeTitle.setText(recipeItem.getRecipeTitle());
        holder.shortRecipe.setText(recipeItem.getShortDescription());
        //holder.recipeScore.setText(String.valueOf(recipeItem.()));
    }

    @Override
    public int getItemCount() {
        return recipeItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView_item;
        TextView recipeTitle;
        TextView shortRecipe;
        TextView recipeScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView_item = itemView.findViewById(R.id.imgView_item);
            recipeTitle = itemView.findViewById(R.id.ShortRecipe);
            shortRecipe = itemView.findViewById(R.id.Recipe_Title);
            recipeScore = itemView.findViewById(R.id.Recipe_Score);
        }
    }
}
