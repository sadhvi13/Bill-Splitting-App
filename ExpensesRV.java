package com.example.android.billsplittingapp;

import android.support.v7.widget.RecyclerView;

/**
 * Created by praty on 08-06-2018.
 */

public class ExpensesRV {
    private String t;
    private String t1;
    private String t2;
    private String t3;
    private String id;
    private String group;
    private String user_id;
    public ExpensesRV(){

    }
    public void setUser_id(String s){
        this.user_id=s;
    }
    public String getUser_id(){
        return user_id;
    }
    public void setT(String s){
        this.t=s;
    }
    public void setT2(String s){
        this.t1=s;
    }
    public void setT3(String s){
        this.t2=s;
    }
    public void setT4(String s){
        this.t3=s;
    }
    public void setId(String s){
        this.id=s;
    }
    public void setGroup(String s){
        this.group=s;
    }
    public String getGroup(){
        return group;
    }
    public String getT(){
        return t;
    }
    public String getT2(){
        return t2;
    }
    public String getT1(){
        return t1;
    }
    public String getT3(){
        return t3;
    }
    public String getId(){
        return id;
    }

}
