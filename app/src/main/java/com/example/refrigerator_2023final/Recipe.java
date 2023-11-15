package com.example.refrigerator_2023final;

import com.google.firebase.firestore.PropertyName;

public class Recipe {

    private String recipeImgUrl;
    private String recipeTitle;
    private String shortDescription;
    private String ingredients;
    private String seasoning;
    private String allergyCheck;
    private String step1;
    private String step2;
    private String step3;
    private String step4;
    private String step5;
    private String step6;
    private String step7;
    private String step8;
    private String step9;
    private String step10;
    private String step11;
    private String step12;
    // 나머지 필드들에 대한 정의

    // 기본 생성자 (Firebase에서 객체를 가져올 때 필요)
    public Recipe() {
        // 필요하다면 아무 작업도 수행하지 않아도 됩니다.
    }

    @PropertyName("RecipeImgUrl")
    public String getRecipeImgUrl() { return recipeImgUrl; }
    @PropertyName("RecipeImgUrl")
    public void setRecipeImgUrl(String recipeImgUrl) { this.recipeImgUrl = recipeImgUrl;  }

    // Getter 및 Setter 메서드 정의
    @PropertyName("RecipeTitle")
    public String getRecipeTitle() { return recipeTitle; }
    @PropertyName("RecipeTitle")
    public void setRecipeTitle(String recipeTitle) { this.recipeTitle = recipeTitle;  }

    @PropertyName("ShortDescription")
    public String getShortDescription() { return shortDescription; }
    @PropertyName("ShortDescription")
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    @PropertyName("Ingredients")
    public String getIngredients() {return ingredients;}
    @PropertyName("Ingredients")
    public void setIngredients(String ingredients) {this.ingredients = ingredients;}

    @PropertyName("Seasoning")
    public String getSeasoning() {return seasoning; }
    @PropertyName("Seasoning")
    public void setSeasoning(String seasoning) { this.seasoning = seasoning; }

    @PropertyName("AllergyCheck")
    public String getAllergyCheck() { return allergyCheck; }
    @PropertyName("AllergyCheck")
    public void setAllergyCheck(String allergyCheck) { this.allergyCheck = allergyCheck; }

    @PropertyName("Step1")
    public String getStep1() { return step1; }
    @PropertyName("Step1")
    public void setStep1(String step1) { this.step1 = step1; }

    @PropertyName("Step2")
    public String getStep2() { return step2; }
    @PropertyName("Step2")
    public void setStep2(String step2) { this.step2 = step2; }

    @PropertyName("Step3")
    public String getStep3() { return step3; }
    @PropertyName("Step3")
    public void setStep3(String step3) { this.step3 = step3; }

    @PropertyName("Step4")
    public String getStep4() { return step4;  }
    @PropertyName("Step4")
    public void setStep4(String step4) { this.step4 = step4; }

    @PropertyName("Step5")
    public String getStep5() { return step5; }
    @PropertyName("Step5")
    public void setStep5(String step5) { this.step5 = step5; }

    @PropertyName("Step6")
    public String getStep6() { return step6; }
    @PropertyName("Step6")
    public void setStep6(String step6) { this.step6 = step6; }

    @PropertyName("Step7")
    public String getStep7() { return step7; }
    @PropertyName("Step7")
    public void setStep7(String step7) { this.step7 = step7; }

    @PropertyName("Step8")
    public String getStep8() { return step8; }
    @PropertyName("Step8")
    public void setStep8(String step8) { this.step8 = step8; }

    @PropertyName("Step9")
    public String getStep9() { return step9; }
    @PropertyName("Step9")
    public void setStep9(String step9) { this.step9 = step9; }

    @PropertyName("Step10")
    public String getStep10() { return step10; }
    @PropertyName("Step10")
    public void setStep10(String step10) { this.step10 = step10; }

    @PropertyName("Step11")
    public String getStep11() { return step11; }
    @PropertyName("Step11")
    public void setStep11(String step11) { this.step11 = step11; }

    @PropertyName("Step12")
    public String getStep12() { return step12; }
    @PropertyName("Step12")
    public void setStep12(String step12) { this.step12 = step12; }
    // 나머지 필드들에 대한 Getter 및 Setter 메서드 정의
}
