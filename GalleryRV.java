package com.example.android.billsplittingapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by praty on 26-06-2018.
 */

public class GalleryRV {
    @SerializedName("image")
    @Expose
    private String image;
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
