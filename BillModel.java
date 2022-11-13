package com.example.android.billsplittingapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by praty on 25-06-2018.
 */

public class BillModel {
    @SerializedName("group_name")
    public String groupname;

    @SerializedName("description")
    public String bill_description;

    @SerializedName("contact")
    public  String contact_name;

    @SerializedName("bill_id")
    public  String bill_id;

    @SerializedName("pay or get")
    public  String pay_or_get;

    @SerializedName("amount paid")
    public  String amount_paid;

    @SerializedName("divide")
    public  String divide;

    @SerializedName("amount to pay or get")
    public  String amount_to_get_or_pay;

    @SerializedName("total bill")
    public String total_bill;

    public BillModel(){

    }

    public BillModel(String groupname,String bill_description,String contact_name,
                     String bill_id,String pay_or_get,String amount_paid,String divide,String amount_to_get_or_pay,String total_bill){
        this.divide=divide;
        this.groupname=groupname;
        this.bill_id=bill_id;
        this.bill_description=bill_description;
        this.contact_name=contact_name;
        this.pay_or_get=pay_or_get;
        this.amount_paid=amount_paid;
        this.divide=divide;
        this.amount_to_get_or_pay=amount_to_get_or_pay;
        this.total_bill=total_bill;
    }

    public int getTotal_bill(){
        return Integer.parseInt(this.total_bill);
    }


    public String getgroup(){
        return  groupname;
    }

    public String getBill_description(){
        return bill_description;
    }

    public String getContact_name() {
        return contact_name;
    }

    public String getBill_id() {
        return bill_id;
    }


    public int getAmount_paid() {
        return Integer.parseInt(amount_paid);
    }


    public String getPay_or_get() {
        return pay_or_get;
    }

    public int getDivide() {
        return Integer.parseInt(divide);
    }

    public int getAmount_to_get_or_pay() {
        return Integer.parseInt(amount_to_get_or_pay);
    }
}


