package com.example.android.billsplittingapp;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.id;
import static com.example.android.billsplittingapp.DatabaseHelper.cphone;
import static com.example.android.billsplittingapp.DatabaseHelper.databaseName;

public class SettleUP extends BaseActivity {

    TextView textView;
    TextView textView1;
    CardView cardView;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    CardView cardView1;
    JSONObject jsonObject;
    String ph1,ph2;
    String group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseHelper=new DatabaseHelper(this);
        db=databaseHelper.getWritableDatabase();
        textView=findViewById(R.id.payment);
        textView1=findViewById(R.id.price);
        cardView=findViewById(R.id.card);
        cardView1=(CardView)findViewById(R.id.card2);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://paytm.com"));
                startActivity(i);
            }
        });
        final Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        final String s=bundle.getString("name");
        final String m=bundle.getString("other");
        final String m1=bundle.getString("go");
        final int m2=bundle.getInt("amount");
        group=bundle.getString("group_name");
        ph1=bundle.getString("phone1");
        ph2=bundle.getString("phone2");
        if(m1.equals("get")){
            textView.setText(m+" paid "+s);
        }else{
            textView.setText(s+" paid "+m);
        }

        textView1.setText("Rs: "+m2);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m3=String.valueOf(m2);
                if(m1.equals("get")){

                    String[] args={group,s,m};
                    db.delete(databaseHelper.balancetable,databaseHelper.c0+"=?"+" AND "+databaseHelper.c1+"=?"+" AND "+databaseHelper.c5+"=?",args);
                    db.delete(databaseHelper.balancetable,databaseHelper.c0+"=?"+" AND "+databaseHelper.c3+"=?"+" AND "+databaseHelper.c1+"=?",args);
                    jsonObject = new JSONObject();
                    Log.e("lll","llllllllllllllllllllllllllllllllllll");
                    try {
                        Cursor cursor4=db.query(databaseHelper.contacts_table,null,databaseHelper.cphone+"=?",new String[]{ph1},null,null,null);
                        cursor4.moveToFirst();
                        Cursor cursor5=db.query(databaseHelper.contacts_table,null,databaseHelper.cphone+"=?",new String[]{ph2},null,null,null);
                        cursor5.moveToFirst();
                        jsonObject.put("user_id",sharedPreferences.getString("user_id","id"));
                        jsonObject.put("payment",m+" paid "+s);
                        jsonObject.put("amount",m3);
                        jsonObject.put("groupNm",group);
                        jsonObject.put("per",cursor5.getString(cursor5.getColumnIndex(databaseHelper.cuserid)));
                        jsonObject.put("perl",cursor4.getString(cursor4.getColumnIndex(databaseHelper.cuserid)));
                        jsonObject.put("person1_phone",ph2);
                        jsonObject.put("person2_phone",ph1);
                        if(appState.getNetworkCheck()){
                            Log.e("asd","uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu");
                            sendData(jsonObject);
                        }else {
                            Toast.makeText(SettleUP.this, " ", Toast.LENGTH_SHORT).show();
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    //databaseHelper.insertsettledbill(m+" paid "+s,m3,group,m,s);
                }else{
                    String[] args={group,m,s};
                    db.delete(databaseHelper.balancetable,databaseHelper.c0+"=?"+" AND "+databaseHelper.c1+"=?"+" AND "+databaseHelper.c5+"=?",args);
                    db.delete(databaseHelper.balancetable,databaseHelper.c0+"=?"+" AND "+databaseHelper.c3+"=?"+" AND "+databaseHelper.c1+"=?",args);
                    jsonObject = new JSONObject();
                    Log.e("aaaaaaaaaaa","aaaaaaaaaaaaaaaaaaaaaaaa");
                    try {
                        Cursor cursor4=db.query(databaseHelper.contacts_table,null,databaseHelper.cphone+"=?",new String[]{ph1},null,null,null);
                        Cursor cursor5=db.query(databaseHelper.contacts_table,null,databaseHelper.cphone+"=?",new String[]{ph2},null,null,null);
                        cursor4.moveToFirst();
                        cursor5.moveToFirst();
                        jsonObject.put("user_id",sharedPreferences.getString("user_id","id"));
                        jsonObject.put("payment",s+" paid "+m);
                        jsonObject.put("amount",m3);
                        jsonObject.put("groupNm",group);
                        jsonObject.put("per",cursor4.getString(cursor4.getColumnIndex(databaseHelper.cuserid)));
                        jsonObject.put("perl",cursor5.getString(cursor5.getColumnIndex(databaseHelper.cuserid)));
                        jsonObject.put("person1_phone",ph1);
                        jsonObject.put("person2_phone",ph2);
                        if(appState.getNetworkCheck()){
                            Log.e("fhghj","yyyyyyyyyyyyyyyyyyyyyyyyyyyy");
                            sendData(jsonObject);
                        }else {
                            Toast.makeText(SettleUP.this, " ", Toast.LENGTH_SHORT).show();
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    //databaseHelper.insertsettledbill(s+" paid "+m,m3,group,s,m);
                }

            }
        });
    }
    void sendData(JSONObject jsonObject){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://phpstack-127383-542740.cloudwaysapps.com/api/v1/savesettledbill";
        Utils.showProgressDialog(this,"adding ..........");
        Utils.showProgress();
        Log.e("input",jsonObject.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Settle_up",response.toString());
                        if(response!=null){
                            Utils.dissmisProgress();
                        }

                        String success,mssg;
                        try {
                            success = response.getString("success");
                            Log.e("suc",success);
                            mssg = response.getString("message");
                            JSONObject jsonObject1=response.getJSONObject("data");
                            JSONObject jsonObject2=jsonObject1.getJSONObject("data");
                            String id=jsonObject2.getString("id");
                            String pay=jsonObject2.getString("payment");
                            String am=jsonObject2.getString("amount");
                            String gid=jsonObject2.getString("groupNm");
                            String per1=jsonObject2.getString("per");
                            String per2=jsonObject2.getString("perl");
                            String perp1=jsonObject2.getString("person1_phone");
                            String perp2=jsonObject2.getString("person2_phone");
                            if(success.equals("true")){
                                Log.e("tttt","iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
                                Cursor cursor4=db.query(databaseHelper.contacts_table,null,databaseHelper.cphone+"=?",new String[]{ph1},null,null,null);
                                Cursor cursor5=db.query(databaseHelper.contacts_table,null,databaseHelper.cphone+"=?",new String[]{ph2},null,null,null);
                                cursor4.moveToFirst();
                                cursor5.moveToFirst();
                                Log.e("qqqqqqqqqq","qqqqqqqqqqqqqqqqqqqqqqqqqqqq");
                                databaseHelper.insertsettledbill(pay,am,gid,cursor4.getString(cursor4.getColumnIndex(databaseHelper.cname)),cursor5.getString(cursor5.getColumnIndex(databaseHelper.cname)),id,perp1,perp2);
                                Toast.makeText(SettleUP.this, mssg, Toast.LENGTH_SHORT).show();
                                Intent intent1=new Intent(SettleUP.this,Balances.class);
                                intent1.putExtra("group_name",group);
                                startActivity(intent1);
                                finish();

                            }else {
                                Toast.makeText(SettleUP.this, mssg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, errorListener);
        queue.add(jsObjRequest);

    }
}