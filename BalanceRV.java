package com.example.android.billsplittingapp;

import java.io.StringReader;

/**
 * Created by praty on 14-06-2018.
 */

public class BalanceRV {
    private String text;
    private String name;
    private String group;
    private String phone;
    public void setText(String text){
        this.text=text;
    }
    public String getText(){
        return text;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getGroup(){
        return group;
    }
    public void setGroup(String group){
        this.group=group;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }
    public String getPhone(){
        return phone;
    }
}
