package com.example.android.billsplittingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import android.support.v7.app.ActionBar;

import android.support.v7.widget.Toolbar;

public class BaseActivity extends AppCompatActivity {

        private Toolbar toolbar;
        private ActionBar actionBar;
        public Response.ErrorListener errorListener;
        public GlobalVariables appState;
        String strEmail,strPassword;

        public SharedPreferences sharedPreferences ;
        public SharedPreferences.Editor editor ;
        boolean check = true;
        android.app.AlertDialog dialog ;

        @Override
        protected void onCreate( Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            appState = ((GlobalVariables) getApplicationContext());

            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(networkStateReceiver, filter);


            errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    String message = null;
                    if (volleyError instanceof NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                    } else if (volleyError instanceof ServerError) {
                        message = "The server could not be found. Please try again after some time!!";
                    } else if (volleyError instanceof AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!";

                    } else if (volleyError instanceof ParseError) {
                        message = "Parsing error! Please try again after some time!!";
                    } else if (volleyError instanceof NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                    } else if (volleyError instanceof TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection.";

                    }
                    Utils.dissmisProgress();
                    Toast.makeText(BaseActivity.this, message + "", Toast.LENGTH_SHORT).show();
                }
            };


            sharedPreferences = getApplicationContext().getSharedPreferences(Utils.MyPREFERENCES, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();


        }

        public void userLogin(final JSONObject object) {
            Log.v("json", object + "");
            Log.v("login_url",Utils.LOGIN);
            try {
                strEmail=object.getString("email");
                strPassword=object.getString("password");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Utils.showProgressDialog(this,"Logging....");
            Utils.showProgress();
            RequestQueue queue = Volley.newRequestQueue(this);
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.LOGIN, object,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            Utils.dissmisProgress();

                            Log.e("login_res",response.toString());
                            try {
                                JSONObject jsonObject,obj;
                                String success = response.getString("success");

                                if(success.equals("true")){

                                    jsonObject = response.getJSONObject("data");
                                    obj=jsonObject.getJSONObject("data");

                                    //appState.setToken(jsonObject.getString("token"));
                                    appState.setEmail(obj.getString("email"));
                                    appState.setName(obj.getString("name"));
                                    //appState.setId(obj.getString("id"));
                                    //appState.setUserID(obj.getString("id"));
                                    appState.setPhone(obj.getString("phone"));
                                    appState.setUserID(obj.getString("id"));
                                    //appState.setCreate_at(obj.getString("created_at"));
                                    //appState.setUpdated_at(obj.getString("updated_at"));

                                    //GlobalVariables.getInstance().setProfilePic(obj.getString("image"));

                                    appState.saveData();

                                    editor.putString("email",strEmail);
                                    editor.putString("password",strPassword );
                                    //editor.putString("user_id",obj.getString("id"));
                                    //editor.putString("profile_pic",obj.getString("image"));
                                    editor.commit();
                                    Intent intent=new Intent(BaseActivity.this,Welcome.class);
                                    startActivity(intent);

                                    //Intent i =new Intent(BaseActivity.this,MainActivity.class);
                                    //startActivity(i);
                                    //finish();

                                }else if(success.equals("false")){
                                    String mssg = response.getString("message");
                                    Toast.makeText(BaseActivity.this, mssg+"", Toast.LENGTH_SHORT).show();
                                }

                            }catch (JSONException e){
                                e.printStackTrace();
                            }


                        }
                    }, errorListener) {

            };
            queue.add(jsonObjReq);


        }

        public void checkEmail(String strEmail){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("email",strEmail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestQueue queue = Volley.newRequestQueue(this);
            Utils.showProgressDialog(this,"Checking email...");
            Utils.showProgress();
            String url="http://phpstack-127383-542740.cloudwaysapps.com/api/v1/forgotpassword";
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Utils.dissmisProgress();
                            Log.v("login_res",response.toString());
                            try {
                                JSONObject jsonObject,obj;
                                String success = response.getString("success");

                                if(success.equals("true")){
                                    jsonObject = response.getJSONObject("data");
                                    obj=jsonObject.getJSONObject("data");
                                    String strEmail = obj.getString("email");

                                    Intent i =new Intent(BaseActivity.this,ForgotPasswordActitvity.class);
                                    i.putExtra("email",strEmail);
                                    startActivity(i);

                                }else if(success.equals("false")){
                                    String mssg = response.getString("message");
                                    Toast.makeText(BaseActivity.this, mssg+"", Toast.LENGTH_SHORT).show();
                                }

                            }catch (JSONException e){
                                e.printStackTrace();
                            }


                        }
                    }, errorListener) {

            };
            queue.add(jsonObjReq);
        }
    public void checkEmailForNewPassword(String strEmail,String password){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email",strEmail);
            jsonObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        Utils.showProgressDialog(this,"Checking email...");
        Utils.showProgress();
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.CHECK_EMAIL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Utils.dissmisProgress();
                        Log.v("login_res",response.toString());
                        try {
                            JSONObject jsonObject,obj;
                            String success = response.getString("success");

                            if(success.equals("true")){
                                jsonObject = response.getJSONObject("data");
                                obj=jsonObject.getJSONObject("data");
                                String strEmail = obj.getString("email");

                                Intent i =new Intent(BaseActivity.this,ForgotPasswordActitvity.class);
                                i.putExtra("email",strEmail);
                                startActivity(i);

                            }else if(success.equals("false")){
                                String mssg = response.getString("message");
                                Toast.makeText(BaseActivity.this, mssg+"", Toast.LENGTH_SHORT).show();
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }


                    }
                }, errorListener) {

        };
        queue.add(jsonObjReq);
    }

    public BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (isOnline(context)) {
                    //  dialog(true);
                    appState.setNetworkCheck(true);
                    Log.e("onReceive", "onReceive Online Connect Intenet ");
                } else {
                    //dialog(false);
                    appState.setNetworkCheck(false);
                    // onConnectionLost();
                    Log.e("onReceive", " onReceive Conectivity Failure !!! ");
                }
            }
        };

        private boolean isOnline(Context context) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                //should check null because in airplane mode it will be null
                return (netInfo != null && netInfo.isConnected());
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if(item.getItemId() == android.R.id.home){
                onBackPressed();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        protected void onPause() {
            super.onPause();
            if(isFinishing()){
                if (dialog!= null) {
                    dialog.dismiss();
                    dialog= null;
                }
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }

            unregisterReceiver(networkStateReceiver);
        }

        public void setTextViewDrawableColor(TextView textView, int color) {
            for (Drawable drawable : textView.getCompoundDrawables()) {
                if (drawable != null) {
                    drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(this,color), PorterDuff.Mode.SRC_IN));
                }
            }
        }


}
