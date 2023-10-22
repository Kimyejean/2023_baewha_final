package com.example.refrigerator_2023final;

public class FoodItem {
    private String foodName;
    private String buyDate;
    private String useDate;
    private String imageUrl;

    public FoodItem() {

    }

    public FoodItem(String foodName, String buyDate, String useDate, String imageUrl) {
        this.foodName = foodName;
        this.buyDate = buyDate;
        this.useDate = useDate;
        this.imageUrl = imageUrl;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getbuyDate() {
        return buyDate;
    }

    public String getuseDate() {
        return useDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
