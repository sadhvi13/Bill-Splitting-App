package com.example.android.billsplittingapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by praty on 23-06-2018.
 */

public class GlobalVariables extends Application {
    private static GlobalVariables mInstance;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    private String userEmailID;
    private String password;
    private String userID;
    private String id;
    private String name;
    private String email;
    private String phone;
    private String token;
    private String updated_at;
    private String create_at;
    private String profilePic;
    private boolean networkCheck;
    private String FCM_Id;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        sharedpreferences = getSharedPreferences(Utils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        loadData();
    }

    public static synchronized GlobalVariables getInstance() {
        return mInstance;
    }

    public void loadData() {
        userID = sharedpreferences.getString("user_id", "");
        userEmailID = sharedpreferences.getString("email", "");
        password = sharedpreferences.getString("password", "");
        name = sharedpreferences.getString("name", "");
        //id = sharedpreferences.getString("id", "");
        //email = sharedpreferences.getString("email", "");
        phone = sharedpreferences.getString("phone", "");

        networkCheck = sharedpreferences.getBoolean("networkCheck", true);

    }

    public void saveData() {
        editor.putString("user_id", userID);
        editor.putString("name", name);
        //editor.putString("email", userEmailID);
        editor.putString("password", password);
        editor.putString("phone", phone);
        editor.putString("email",email);
        //editor.putString("updated_at", updated_at);
        //editor.putString("create_at", create_at);
        //editor.putString("token",token);
        editor.commit();
    }

    public boolean contains() {
        if (sharedpreferences.contains(email) && sharedpreferences.contains(password)) {
            return true;
        }
        return false;
    }


    public String getFCM_Id() {
        return FCM_Id;
    }

    public void setFCM_Id(String FCM_Id) {
        this.FCM_Id = FCM_Id;
    }

    public boolean getNetworkCheck() {
        return networkCheck;
    }

    public void setNetworkCheck(boolean networkCheck) {
        this.networkCheck = networkCheck;
    }

    //public String getToken() {
    //    return token;
    //}

    //public void setToken(String token) {
    //    this.token = token;
    //}

    //public String getProfilePic() {
    //    return profilePic;
    //}

    //public void setProfilePic(String profilePic) {
    //    this.profilePic = profilePic;
    //}

    //public String getUserEmailID() {
    //    return userEmailID;
    //}

    //public void setUserEmailID(String userEmailID) {
    //    this.userEmailID = userEmailID;
    //}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    //public String getId()// {
    //    return id;
    //}

    //public void setId(String id) {
    //    this.id = id;
    //}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /*public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }*/


    public void logout() {
        //userEmailID = null;
        password = null;
        //userID = null;
        //id = null;
        name = null;
        email = null;
        phone = null;
        //updated_at = null;
        //create_at = null;
        editor.clear();
        editor.commit();
    }
}
