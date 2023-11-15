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

import java.util.ArrayList;

public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.ViewHolder> {

    private ArrayList<MainViewItem> recipeList;

    public MainViewAdapter(ArrayList<MainViewItem> recipeList) {
        this.recipeList = recipeList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView_item;
        TextView recipeTitle;
        // TextView recipeScore;
        TextView recipeShort;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgView_item = (ImageView) itemView.findViewById(R.id.imgView_item);
            recipeTitle = (TextView) itemView.findViewById(R.id.Recipe_Title);
            // recipeScore = (TextView) itemView.findViewById(R.id.Recipe_Score);
            recipeShort = (TextView) itemView.findViewById(R.id.ShortRecipe);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_main_list_page, parent, false);
        MainViewAdapter.ViewHolder vh = new MainViewAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewAdapter.ViewHolder holder, int position) {
        MainViewItem recipe = recipeList.get(position);

        // Load image using Glide
        Glide.with(holder.itemView.getContext())
                .load(recipe.getImgName())
                .into(holder.imgView_item);

        // Set other details
        holder.recipeTitle.setText(recipe.getMainText());
        // holder.recipeScore.setText(String.valueOf(recipe.getRecipeScore()));
        holder.recipeShort.setText(recipe.getSubText());
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }
}
