package com.example.android.billsplittingapp;

import android.widget.RelativeLayout;

/**
 * Created by praty on 04-06-2018.
 */

public class RList1 {
    private String group_type;
    private String group_name;
    private String group_id;
    String user_id;
    private RelativeLayout relativeLayout;

    public RList1(String group_type,String group_name,String group_id,String user_id){
        this.group_type=group_type;
        this.group_name=group_name;
        this.group_id=group_id;
        this.user_id=user_id;
    }
    public void setUser_id(String s){
        this.user_id=s;
    }
    public String getUser_id(){
        return user_id;
    }
    public  void setGroup_id(String s){
        this.group_id=s;
    }
    public String getGroup_id(){
        return group_id;
    }
    public void setImage(String im){
        this.group_type=im;
    }
    public void setName(String name){
        this.group_name=name;
    }
    public String getType(){
        return group_type;
    }
    public int getImageType(){
        if(group_type.equals("House")){
            return R.drawable.house;
        }else if(group_type.equals("Trip")){
            return R.drawable.tour;
        }else if(group_type.equals("Food")){
            return R.drawable.food;
        }else{
            return R.drawable.other;
        }
    }
    public String getGroup_name(){
        return group_name;
    }
}
