package com.example.gifimageswithpagination.Models;

public class Image {
    private String imgUrl;

    public Image(){
    }
    public Image(String imgUrl){
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
