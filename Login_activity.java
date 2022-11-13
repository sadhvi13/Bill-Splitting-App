package com.example.android.billsplittingapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;

import com.example.android.billsplittingapp.MainActivity;
import com.example.android.billsplittingapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class Login_activity extends BaseActivity {

    private EditText et1;
    private EditText et2;
    private Button b;
    private ImageView von;
    private TextView textView;
    String email,password;
    private Button fpw;
    public SharedPreferences sharedPreferences ;
    public SharedPreferences.Editor editor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getApplicationContext().getSharedPreferences(Utils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(sharedPreferences.contains("email")&&sharedPreferences.contains("password")){

            Intent i =new Intent(Login_activity.this,Welcome.class);
            startActivity(i);
            finish();

        }
        setContentView(R.layout.activity_login_activity);
        //sp=getSharedPreferences("login",MODE_PRIVATE);
        //if(sp.getBoolean("logged",false))
        //{
        //    Intent intent=new Intent(this,MainActivity.class);
        //    startActivity(intent);
        //}
        et1=(EditText) findViewById(R.id.email);
        et2=(EditText) findViewById(R.id.password);
        b=(Button)findViewById(R.id.login);
        von=(ImageView) findViewById(R.id.im1);
        textView=(TextView)findViewById(R.id.lregister);
        fpw=(Button)findViewById(R.id.forgotpw);
        fpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=et1.getText().toString();
                password=et2.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    et1.setError("Please enter your email");
                    et1.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    et1.setError("please enter valid email");
                    et1.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    et2.setError("Enter a password");
                    et2.requestFocus();
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", et1.getText().toString().trim());
                    jsonObject.put("password", et2.getText().toString().trim());
                    //jsonObject.put("device_id",appState.getFCM_Id());

                    if(appState.getNetworkCheck()){
                        userLogin(jsonObject);
                    }else {
                        Toast.makeText(Login_activity.this,getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //sp.edit().putBoolean("logged",true).apply();
            }
        });
        von.setOnClickListener(new View.OnClickListener() {
            int t=0;
            @Override
            public void onClick(View v) {
                if(t==0){
                    et2.setTransformationMethod(null);
                    von.setImageResource(R.drawable.v_off);
                    int position=et2.length();
                    et2.setSelection(position);
                    t=1;
                }
                else
                {
                    et2.setTransformationMethod(new PasswordTransformationMethod());
                    von.setImageResource(R.drawable.v_on);
                    int position=et2.length();
                    et2.setSelection(position);
                    t=0;
                }

            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login_activity.this,Register_activity.class);
                startActivity(intent);

            }
        });
    }

    private void openDialog(){
        LayoutInflater inflater = LayoutInflater.from(Login_activity.this);
        View subView = inflater.inflate(R.layout.alert_forgot_pwd, null);
        final EditText email_id = (EditText)subView.findViewById(R.id.ed_forgot_pwd);
        final Button btnSubmit = (Button)subView.findViewById(R.id.btn_forgot_pwd);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password ?");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email_id.getText().toString().equals("")){
                    email_id.setError("Please enter registered email id");
                }else {
                    if(appState.getNetworkCheck()){
                        checkEmail(email_id.getText().toString());
                    }else {
                        Toast.makeText(Login_activity.this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        builder.show();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
