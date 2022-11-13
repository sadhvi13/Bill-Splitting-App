package com.example.android.billsplittingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActitvity extends BaseActivity implements View.OnClickListener {
    Button btnChangePassword;
    ImageView imgShowPassword,imgShowConfirmPassword;
    EditText edNewPassword,edConfirmNewPassword;
    int count = 0 ;
    TextView tvUserName;
    String strEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_actitvity);
        initUI();
        imgShowPassword.setOnClickListener(this);
        imgShowConfirmPassword.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);

        if(getIntent().getStringExtra("email")!=null){
            strEmail = getIntent().getStringExtra("email");
        }

        tvUserName.setText(strEmail);
    }

    public void initUI(){

        btnChangePassword = (Button)findViewById(R.id.btn_fpwd_change_pwd);
        imgShowPassword = (ImageView)findViewById(R.id.img_fpwd_show_new_pwd);
        imgShowConfirmPassword = (ImageView)findViewById(R.id.img_fpwd_show_confirm_new_pwd);
        tvUserName = (TextView) findViewById(R.id.ed_fpwd_user_name);
        edNewPassword = (EditText)findViewById(R.id.ed_fpwd_new_pwd);
        edConfirmNewPassword = (EditText)findViewById(R.id.ed_fpwd_confirm_new_pwd);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.img_fpwd_show_new_pwd :
                count ++ ;
                if(count%2==0){
                    edNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgShowPassword.setImageResource(R.drawable.v_on);

                }
                else {
                    edNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgShowPassword.setImageResource(R.drawable.v_off);
                }

                break;
            case R.id.img_fpwd_show_confirm_new_pwd :

                count ++ ;
                if(count%2==0){
                    edConfirmNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgShowConfirmPassword.setImageResource(R.drawable.v_on);

                }
                else {
                    edConfirmNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgShowConfirmPassword.setImageResource(R.drawable.v_off);
                }

                break;

            case R.id.btn_fpwd_change_pwd:

                if(edNewPassword.getText().toString().equals("")){
                    edNewPassword.setError("Please enter new password");
                }
                else if(edConfirmNewPassword.getText().toString().equals("")){
                    edConfirmNewPassword.setError("Please enter confirm new password");
                }
                else if(!edNewPassword.getText().toString().equals(edConfirmNewPassword.getText().toString())){
                    edConfirmNewPassword.setError("Both passwords does not match");
                }
                else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("email",strEmail);
                        jsonObject.put("password",edNewPassword.getText().toString().trim());
                        jsonObject.put("c_password",edConfirmNewPassword.getText().toString().trim());

                        if(appState.getNetworkCheck()){
                            resetPassword(jsonObject);
                        }else {
                            Toast.makeText(ForgotPasswordActitvity.this,getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }

    }

    public void resetPassword(JSONObject object){
        RequestQueue queue = Volley.newRequestQueue(this);
        Utils.showProgressDialog(this,"Updating password...");
        Utils.showProgress();
        String url="http://phpstack-127383-542740.cloudwaysapps.com/api/v1/resetpassword";
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Utils.dissmisProgress();

                        Log.v("login_res",response.toString());
                        try {
                            JSONObject jsonObject,obj;
                            String success = response.getString("success");
                            String strMssg = response.getString("message");

                            if(success.equals("true")){
                                Toast.makeText(ForgotPasswordActitvity.this, strMssg+"", Toast.LENGTH_SHORT).show();
                                Intent i =new Intent( ForgotPasswordActitvity.this,Login_activity.class);
                                startActivity(i);

                            }else if(success.equals("false")){
                                String mssg = response.getString("message");
                                Toast.makeText(ForgotPasswordActitvity.this, mssg+"", Toast.LENGTH_SHORT).show();
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }


                    }
                }, errorListener) {

        };
        queue.add(jsonObjReq);
    }
}
