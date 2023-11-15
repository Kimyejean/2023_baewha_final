package com.example.refrigerator_2023final;



import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FoodListAdapter extends ArrayAdapter<FoodItem> {
    private Context context;
    private List<FoodItem> foodList;



    public FoodListAdapter(Context context, List<FoodItem> foodList) {
        super(context, R.layout.activity_food_list_item_page, foodList);
        this.context = context;
        this.foodList = foodList;

        // Sort the list based on useDate when the adapter is created
        Collections.sort(foodList, new Comparator<FoodItem>() {
            @Override
            public int compare(FoodItem foodItem1, FoodItem foodItem2) {
                return foodItem1.getuseDate().compareTo(foodItem2.getuseDate());
            }
        });
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

        // Calculate the difference in days between the current date and use date
        int daysDifference = calculateDaysDifference(food.getuseDate());

        // Set background color based on the condition (e.g., within the next 3 days)
        if (daysDifference == 3) {
            foodItemView.setBackgroundColor(Color.parseColor ("#CCFF99"));
        } else if (daysDifference == 1 || daysDifference <= 0) {
            foodItemView.setBackgroundColor(Color.parseColor ("#FF9999"));
        } else if (daysDifference == 2) {
            foodItemView.setBackgroundColor(Color.parseColor ("#FFFF99"));
        } else {
            foodItemView.setBackgroundColor(Color.parseColor ("#FFFFFF"));
        }

        // Firebase Storage에서 이미지를 가져와서 ImageView에 표시
        Picasso.get().load(food.getImageUrl()).into(foodImageView);

        return foodItemView;
    }

    private int calculateDaysDifference(String useDate) {
        // Assuming date format is "yyyy-MM-dd"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date currentDate = new Date();
            Date useDateDate = dateFormat.parse(useDate);

            long timeDifference = useDateDate.getTime() - currentDate.getTime();
            return (int) TimeUnit.DAYS.convert(timeDifference, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Handle the parse exception as needed
        }
    }


}
