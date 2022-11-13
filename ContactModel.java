package com.example.android.billsplittingapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by praty on 25-06-2018.
 */

public class ContactModel {
    @SerializedName("name")
    String name;
    @SerializedName("phone")
    String phone;
    public ContactModel(String name,String phone){
        this.name=name;
        this.phone=phone;
    }
    String getName(){
        return this.name;
    }
    String getPhone(){
        return this.phone;
    }
}


