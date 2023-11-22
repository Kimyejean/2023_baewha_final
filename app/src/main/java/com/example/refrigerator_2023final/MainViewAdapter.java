package com.example.refrigerator_2023final;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.ViewHolder> {

    private List<MainViewItem> recipeList;
    private OnItemClickListener mListener;
    private List<MainViewItem> mList1;
    private List<MainViewItem> mList2;
    private List<MainViewItem> mList3;

    public interface OnItemClickListener {
        void onItemClick(int position, int listIndex);
    }
    public MainViewAdapter(List<MainViewItem> recipeList, List<MainViewItem> list1, List<MainViewItem> list2, List<MainViewItem> list3, OnItemClickListener listener) {
        this.recipeList = recipeList;
        this.mList1 = list1;
        this.mList2 = list2;
        this.mList3 = list3;
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView_item;
        TextView Recipe_Title;
        TextView ShortRecipe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgView_item = itemView.findViewById(R.id.imgView_item);
            Recipe_Title = itemView.findViewById(R.id.Recipe_Title);
            ShortRecipe = itemView.findViewById(R.id.ShortRecipe);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.activity_main_list_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainViewItem recipe = recipeList.get(position);

        // Load image using Picasso
        Picasso.get()
                .load(recipe.getimgUrl())
                .fit()
                .centerCrop()
                .into(holder.imgView_item);

        // Set other details
        holder.Recipe_Title.setText(recipe.getMainText());
        holder.ShortRecipe.setText(recipe.getSubText());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    // Use holder.getAdapterPosition() to get the correct adapter position
                    int listIndex;
                    if (recipeList == mList1) {
                        listIndex = 1;
                    } else if (recipeList == mList2) {
                        listIndex = 2;
                    } else if (recipeList == mList3) {
                        listIndex = 3;
                    } else {
                        listIndex = 0; // Handle other cases or provide a default value
                    }
                    mListener.onItemClick(holder.getAdapterPosition(), listIndex);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }
}
