package com.example.android.billsplittingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static java.security.AccessController.getContext;
import android.provider.Settings.Secure;

public class Register_activity extends BaseActivity {

    private EditText etn;
    private EditText ete;
    private EditText etp;
    private EditText etpw;
    private EditText etcpw;
    private Button register;
    private ImageView von;
    private ImageView von2;
    private TextView t;
    private TextView login;
    String name,email,phone,pw;

    private String android_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);
        etn=(EditText) findViewById(R.id.name);
        ete=(EditText) findViewById(R.id.remail);
        etp=(EditText) findViewById(R.id.phone);
        etpw=(EditText) findViewById(R.id.rpassword);
        etcpw=(EditText)findViewById(R.id.crpassword);
        register=(Button) findViewById(R.id.register);
        t=(TextView) findViewById(R.id.rlogin);

        von=(ImageView) findViewById(R.id.v1);
        von2=(ImageView)findViewById(R.id.v2);
        android_id = Secure.getString(Register_activity.this.getContentResolver(),
                Secure.ANDROID_ID);

        von.setOnClickListener(new View.OnClickListener() {
            int t=0;
            @Override
            public void onClick(View v) {
                if(t==0){
                    etpw.setTransformationMethod(null);
                    von.setImageResource(R.drawable.v_off);
                    int position=etpw.length();
                    etpw.setSelection(position);
                    t=1
                    ;
                }
                else
                {
                    etpw.setTransformationMethod(new PasswordTransformationMethod());
                    von.setImageResource(R.drawable.v_on);
                    int position=etpw.length();
                    etpw.setSelection(position);
                    t=0;
                }


            }
        });
        von2.setOnClickListener(new View.OnClickListener() {
            int t=0;
            @Override
            public void onClick(View v) {
                if(t==0){
                    etcpw.setTransformationMethod(null);
                    von2.setImageResource(R.drawable.v_off);
                    int position=etcpw.length();
                    etcpw.setSelection(position);
                    t=1
                    ;
                }
                else
                {
                    etcpw.setTransformationMethod(new PasswordTransformationMethod());
                    von2.setImageResource(R.drawable.v_on);
                    int position=etcpw.length();
                    etcpw.setSelection(position);
                    t=0;
                }


            }
        });
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Register_activity.this,Login_activity.class);
                startActivity(intent);
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=etn.getText().toString();
                email=ete.getText().toString();
                phone=etp.getText().toString();
                pw=etpw.getText().toString();

                if(TextUtils.isEmpty(name)){
                    etn.setError("please enter name");
                    etn.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    ete.setError("Please enter your email");
                    ete.requestFocus();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    ete.setError("Enter a valid email");
                    ete.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    etp.setError("Enter a phone");
                    etp.requestFocus();
                    return;
                }
                if(!Patterns.PHONE.matcher(phone).matches()){
                    etp.setError("Enter a valid phone");
                    etp.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(pw)) {
                    etpw.setError("Enter a password");
                    etpw.requestFocus();
                    return;
                }
                if(etpw.length()<6){
                    etpw.setError("password length should be greater than 6 ");
                    etpw.requestFocus();
                    return;
                }
                if(!etcpw.getText().toString().equals(etpw.getText().toString())){
                    etcpw.setError("did not match with password");
                    etcpw.requestFocus();
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name",etn.getText().toString().trim());
                    jsonObject.put("email",ete.getText().toString().trim());
                    jsonObject.put("phone",etp.getText().toString().trim());
                    jsonObject.put("password",etpw.getText().toString().trim());
                    jsonObject.put("c_password",etcpw.getText().toString().trim());
                    jsonObject.put("device_id",android_id);

                    if(appState.getNetworkCheck()){
                        creatrAccount(jsonObject);
                    }
                    else {
                        Toast.makeText(Register_activity.this,getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(Register_activity.this);
                //Editor editor = sharedPreferences.edit();
                //editor.putString("ContactName", name);
                //editor.putString("phone",phone);
                //Intent intent=new Intent(Register_activity.this,MainActivity.class);
                //startActivity(intent);
                //finish();

            }




        }) ;


            }

    public void creatrAccount(JSONObject jsonObject){

        Log.v("URL", Utils.REGISTER);

        Log.v("json_input",jsonObject+"");
        Utils.showProgressDialog(this,"Registering....");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.REGISTER, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {

                            Utils.dissmisProgress();

                            Log.v("reg_res",response.toString());
                            String success,mssg;
                            try {
                                success = response.getString("success");
                                mssg = response.getString("message");

                                if(success.equals("true")){
                                    Intent i = new Intent(Register_activity.this,Login_activity.class);
                                    startActivity(i);
                                    Toast.makeText(Register_activity.this, "Successfully register", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(Register_activity.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, errorListener) {
        };
        queue.add(jsonObjReq);

    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
