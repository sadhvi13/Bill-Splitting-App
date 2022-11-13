package com.example.android.billsplittingapp;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Settings extends BaseActivity {
    EditText email_id;
    EditText pwd;
    ImageView imageView;
    EditText forpwd;
    ImageView imageView1;
    int count=0;
    int count1=0;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button=(Button)findViewById(R.id.btn_change_pwd);
        email_id=(EditText)findViewById(R.id.ed_pwdmail);
        pwd=(EditText)findViewById(R.id.ed_pwd);
        forpwd=(EditText)findViewById(R.id.ed_forpwd);
        imageView=(ImageView)findViewById(R.id.img_confirm_new_pwd);
        imageView1=(ImageView)findViewById(R.id.img_confirm_new_forpwd);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count ++ ;
                if(count%2==0){
                    pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageView.setImageResource(R.drawable.v_on);

                }
                else {
                    pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageView.setImageResource(R.drawable.v_off);
                }
            }
        });
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count1 ++ ;
                if(count1%2==0){
                    forpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageView1.setImageResource(R.drawable.v_on);

                }
                else {
                    forpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageView1.setImageResource(R.drawable.v_off);
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email_id.getText().toString().equals("")) {
                    email_id.setError("Please enter registered email id");
                } else if (pwd.getText().toString().equals("")) {
                    pwd.setError("Please Enter valid password");
                } else if (forpwd.getText().toString().equals("")) {
                    forpwd.setError("Please Enter valid password");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        Log.e("1233456788", "PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
                        jsonObject.put("user_id", sharedPreferences.getString("user_id", "id"));
                        jsonObject.put("current_password", pwd.getText().toString());
                        jsonObject.put("new_password", forpwd.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (appState.getNetworkCheck()) {
                        Log.e("1233456788", "LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");

                        changePassword(jsonObject);
                    } else {
                        Toast.makeText(Settings.this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }
    public void changePassword(JSONObject jsonObject){
        RequestQueue queue = Volley.newRequestQueue(this);
        Utils.showProgressDialog(this,"Changing password...");
        Utils.showProgress();
        Log.e("input",jsonObject.toString());
        String url="http://phpstack-127383-542740.cloudwaysapps.com/api/v1/changepassword";
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Utils.dissmisProgress();
                        Log.e("login_res",response.toString());
                        try {
                            JSONObject jsonObject,obj;
                            String success = response.getString("success");
                            String mssg = response.getString("message");

                            if(success.equals("true")){
                                Toast.makeText(Settings.this, mssg+"", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(Settings.this,MainActivity.class);
                                startActivity(intent);

                            }else if(success.equals("false")){
                                Toast.makeText(Settings.this, mssg+"", Toast.LENGTH_SHORT).show();
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }


                    }
                }, errorListener) {

        };
        queue.add(jsonObjReq);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }
        return false;
    }
}
