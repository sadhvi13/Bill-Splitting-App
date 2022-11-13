package com.example.android.billsplittingapp;

/**
 * Created by praty on 25-06-2018.
 */

public class ContactVO2 {
    private String ContactImage;
    private String ContactName;
    private String ContactNumber;
    private String Group;
    public String getGroup(){
        return this.Group;
    }
    public void setGroup(String group){
        this.Group=group;
    }

    public String getContactImage() {
        return ContactImage;
    }

    public void setContactImage(String contactImage) {
        this.ContactImage = ContactImage;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }
}
