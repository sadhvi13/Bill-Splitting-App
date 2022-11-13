package com.example.android.billsplittingapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by praty on 25-06-2018.
 */

public class GroupModel {

    @SerializedName("group_name")
    String group;
    @SerializedName("group_type")
    String groupType;
    @SerializedName("group_id")
    String groupId;
    ContactModel[] contacts;
    public GroupModel(String group,String groupType,ContactModel[] contacts,String groupId){
        this.group=group;
        this.groupType=groupType;
        this.contacts=contacts;
        this.groupId=groupId;
    }
    String getGroupId(){
        return this.groupId;
    }
    String getGroup(){
        return this.group;
    }
    String getGroupType(){
        return this.groupType;
    }
    ContactModel[] getContacts(){
        return this.contacts;
    }

}