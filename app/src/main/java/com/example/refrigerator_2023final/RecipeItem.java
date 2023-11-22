package com.example.refrigerator_2023final;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeItem {
    private String recipeTitle;
    private String shortDescription;
    private String imageUrl; // 사용자가 등록한 사진의 파일 경로나 URI
    private String id;
    private List<String> ingredients; // List to store recipe ingredients
    private Map<String, Boolean> allergenInfo;
    public RecipeItem(String recipeTitle, String shortDescription, String imageUrl) {
        this.recipeTitle = recipeTitle;
        this.shortDescription = shortDescription;
        this.imageUrl = imageUrl;
    }

    public RecipeItem(String id, String recipeTitle, String description, String imageUrl) {
        this.id = id;
        this.recipeTitle = recipeTitle;
        this.shortDescription = description;
        this.imageUrl = imageUrl;
        this.allergenInfo = allergenInfo;

        }
    // Getter and Setter for ingredients
    public Map<String, Boolean> getAllergenInfo() {
        return allergenInfo;
    }
    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }
    public void setTitle(String title) {
        this.recipeTitle = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }
    public void setDescription(String description) {
        this.shortDescription = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
