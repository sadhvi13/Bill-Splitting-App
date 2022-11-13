package com.example.android.billsplittingapp;

/**
 * Created by praty on 25-06-2018.
 */

public class SettledbillModel {
    String payment;
    String amount;
    String group;
    String person1;
    String person2;
    public SettledbillModel(String payment,String amount,String group,String person1,String person2){
        this.payment=payment;
        this.amount=amount;
        this.group=group;
        this.person1=person1;
        this.person2=person2;
    }
    String getPayment(){
        return this.payment;
    }
    String getGroup(){
        return this.group;
    }
    String getAmount(){
        return this.amount;
    }
    String getPerson1(){
        return this.person1;
    }
    String getPerson2(){
        return  this.person2;
    }
}
