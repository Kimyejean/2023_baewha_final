package com.example.refrigerator_2023final;

public class FoodItem {
    private String foodId; // 파이어베이스에서 생성된 고유한 ID
    private String foodName;
    private String buyDate;
    private String useDate;
    private String imageUrl;

    public FoodItem() {
        // 기본 생성자
    }

    public FoodItem(String foodId, String foodName, String buyDate, String useDate, String imageUrl) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.buyDate = buyDate;
        this.useDate = useDate;
        this.imageUrl = imageUrl;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getbuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getuseDate() {
        return useDate;
    }

    public void setUseDate(String useDate) {
        this.useDate = useDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
