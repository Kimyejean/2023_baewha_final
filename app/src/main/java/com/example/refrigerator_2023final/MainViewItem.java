package com.example.refrigerator_2023final;

public class MainViewItem {

    // 선언
    private String id;
    private String imgUrl = "";
    private String mMainText = "";
    private String mSubText = "";




    public String getId() { return id; }
    public void setId(String id) {
        this.id = id;
    }



    public String getimgUrl() {
        return imgUrl;
    }

    public void setimgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }







    public String getMainText() {
        return mMainText;
    }
    public void setMainText(String mainText) {
        this.mMainText = mainText;
    }






    public String getSubText() {
        return mSubText;
    }
    public void setSubText(String subText) {
        this.mSubText = subText;
    }

}