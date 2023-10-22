package com.example.refrigerator_2023final;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
public class FoodListAdapter extends ArrayAdapter<FoodItem> {
    private Context context;
    private List<FoodItem> foodList;

    public FoodListAdapter(Context context, List<FoodItem> foodList) {
        super(context, R.layout.activity_food_list_item_page, foodList);
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View foodItemView = inflater.inflate(R.layout.activity_food_list_item_page, null, true);

        TextView foodNameView = foodItemView.findViewById(R.id.foodNameTextView);
        TextView buyDateView = foodItemView.findViewById(R.id.firstDateTextView);
        TextView useDateView = foodItemView.findViewById(R.id.secondDateTextView);
        ImageView foodImageView = foodItemView.findViewById(R.id.foodImageView);

        FoodItem food = foodList.get(position);

        foodNameView.setText("이름: " + food.getFoodName());
        buyDateView.setText("구매일: " + food.getbuyDate());
        useDateView.setText("소비기한: " + food.getuseDate());

        // Firebase Storage에서 이미지를 가져와서 ImageView에 표시
        Picasso.get().load(food.getImageUrl()).into(foodImageView);

        return foodItemView;
    }
}

