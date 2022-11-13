package com.example.android.billsplittingapp;

/**
 * Created by praty on 14-06-2018.
 */

public class ViewBRV {
    private String str;
    private String g;
    private String name;
    private String getorowe;
    private int amount;
    private String other;
    private String phone1;
    private String phone2;
    public void setPhone(String s){
        this.phone1=s;
    }
    public void setPhone2(String s){
        this.phone2=s;
    }
    public String getPhone(){
        return phone1;
    }
    public String getPhone2(){
        return phone2;
    }
    public void setStr(String str){
        this.str=str;
    }
    void setGroup(String s){
        this.g=s;
    }
    String getGroup(){
        return this.g;
    }
    public String getStr(){
        return str;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getgo(){
        return getorowe;
    }
    public void setgo(String name){
        this.getorowe=name;
    }
    public int getAmount(){
        return amount;
    }
    public void setAmount(int name){
        this.amount=name;
    }
    public String getOther(){
        return other;
    }
    public void setOther(String name){
        this.other=name;
    }

}