package com.example.android.billsplittingapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.input;
import static com.example.android.billsplittingapp.DatabaseHelper.c;
import static com.example.android.billsplittingapp.DatabaseHelper.s1;
import static com.example.android.billsplittingapp.DatabaseHelper.s2;
import static com.example.android.billsplittingapp.DatabaseHelper.s3;
import static com.example.android.billsplittingapp.R.id.con;
import static com.example.android.billsplittingapp.R.id.ll;
import static com.example.android.billsplittingapp.R.id.rvContacts;
import static com.example.android.billsplittingapp.R.layout.contacts;

public class CreateGroup extends BaseActivity {

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    String GN;
    private EditText editText;
    private LinearLayout linearLayout;
    private boolean t1,t2,t3;
    DatabaseHelper mydb;
    public List<ContactVO2> list=new ArrayList<>();
    private ContactAdapter contactsAdapter;
    RecyclerView recyclerView;
    List<String> contact;
    String id;
    //String gn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textView1=(TextView)findViewById(R.id.house);
        textView2=(TextView)findViewById(R.id.trip);
        textView3=(TextView)findViewById(R.id.food);
        editText=(EditText)findViewById(R.id.groupname);
        linearLayout=(LinearLayout)findViewById(R.id.linear);
        recyclerView=(RecyclerView)findViewById(R.id.list);
        mydb=new DatabaseHelper(CreateGroup.this);
        //ll=(LinearLayout)findViewById(R.id.linlay);
        //t1=false;
        //t2=false;
        //t3=false;
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView1.setBackgroundColor(Color.GRAY);
                textView2.setBackgroundColor(Color.WHITE);
                textView3.setBackgroundColor(Color.WHITE);
                t1=true;
                t2=false;
                t3=false;

            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView1.setBackgroundColor(Color.WHITE);
                textView2.setBackgroundColor(Color.GRAY);
                textView3.setBackgroundColor(Color.WHITE);
                t1=false;
                t2=true;
                t3=false;

            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView1.setBackgroundColor(Color.WHITE);
                textView2.setBackgroundColor(Color.WHITE);
                textView3.setBackgroundColor(Color.GRAY);
                t1=false;
                t2=false;
                t3=true;

            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ed=editText.getText().toString();
                int el=ed.length();
                String r;
                if(t1==true){
                    r="House";
                }else if(t2==true){
                    r="Trip";
                }else{
                    r="Food";
                }
                Intent intent=new Intent(CreateGroup.this,ChooseMember.class);

                if(GN!=null){
                    intent.putExtra("group_name",GN);
                }else{
                    intent.putExtra("group_name","");
                }
                intent.putExtra("GROUP",ed);
                Intent intent1=getIntent();
                Bundle bundle1=intent1.getExtras();
                if(bundle1.getString("gnm")!=null){
                    intent.putExtra("gnm",bundle1.getString("gnm"));
                }
                intent.putExtra("group_type",r);
                startActivity(intent);
                finish();
            }
        });
        //SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(CreateGroup.this);
        contactsAdapter = new ContactAdapter(list, CreateGroup.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(contactsAdapter);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle.getString("first")!=null){
            mydb.insertDatacontact(sharedPreferences.getString("name","name"),sharedPreferences.getString("phone","phoneNo"),0,"");
            ContactVO2 contactVO1=new ContactVO2();
            contactVO1.setContactName(sharedPreferences.getString("name","name"));
            contactVO1.setContactNumber(sharedPreferences.getString("phone","phoneNo"));
            contactVO1.setGroup("");
            list.add(contactVO1);
            contactsAdapter.updateList(list);

        }
        if((bundle.getString("first"))==null){
            String title=bundle.getString("name");
            String author=bundle.getString("number");
            GN=bundle.getString("group_name");
            String GT=bundle.getString("group_type");
            String g=bundle.getString("GROUP");
            editText.setText(g);
            if(GT.equals("House")){
                textView1.setBackgroundColor(Color.GRAY);
                textView2.setBackgroundColor(Color.WHITE);
                textView3.setBackgroundColor(Color.WHITE);
                t1=true;
                t2=false;
                t3=false;
            }else if(GT.equals("Trip")){
                textView1.setBackgroundColor(Color.WHITE);
                textView2.setBackgroundColor(Color.GRAY);
                textView3.setBackgroundColor(Color.WHITE);
                t1=false;
                t2=true;
                t3=false;
            }else{
                textView1.setBackgroundColor(Color.WHITE);
                textView2.setBackgroundColor(Color.WHITE);
                textView3.setBackgroundColor(Color.GRAY);
                t1=false;
                t2=false;
                t3=true;
            }
            if(title!=null){
                int image=bundle.getInt("image");
                mydb.insertDatacontact(title,author,image,"");
            }
            SQLiteDatabase db=mydb.getReadableDatabase();
            Cursor cursor=mydb.readDataContact(db,GN);
            cursor.moveToFirst();

            do{
                String name=cursor.getString(cursor.getColumnIndex(mydb.cname));
                String phone=cursor.getString(cursor.getColumnIndex(mydb.cphone));
                int im=cursor.getInt(cursor.getColumnIndex(mydb.cimage));
                Context context=CreateGroup.this;
                ContactVO2 contactVO=new ContactVO2();
                contactVO.setContactName(name);
                contactVO.setContactNumber(phone);
                contactVO.setGroup(cursor.getString(cursor.getColumnIndex(mydb.cgroupName)));
                list.add(contactVO);
                //ImageView iv = new ImageView(context);


//children of layout2 LinearLayout

                //TextView tv1 = new TextView(context);
                //TextView tv2 = new TextView(context);
                //tv1.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                //tv2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                //iv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                //ll.addView(iv);
                //ll.addView(tv1);
                //ll.addView(tv2);
                //int id = getResources().getIdentifier(image, "drawable", getPackageName());
                //iv.setImageResource(im);
                //int i=Integer.parseInt(image);
                //Bitmap bitmap = (Bitmap) intent.getParcelableExtra("image");
                //tv1.setText(name);
                //tv2.setText(phone);

            }while(cursor.moveToNext());
            contactsAdapter.updateList(list);
            cursor.close();

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_group,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }
        Log.e("ygujn","**********************************************************");

        String s1= String.valueOf(editText.getText());
        if(TextUtils.isEmpty(s1)){
            editText.setError("please enter name");
            editText.requestFocus();
            return false;
        }

        String s2="Other";

        if(t1==true){
            s2= "House";//String.valueOf(textView1.getText());
        }else if(t2==true){
            s2= "Trip";//String.valueOf(textView2.getText());
        }else if(t3==true){
            s2= "Food";//String.valueOf(textView3.getText());
        }

        SQLiteDatabase db=mydb.getReadableDatabase();
        Intent intent1=getIntent();
        Bundle bundle=intent1.getExtras();
        String gn=bundle.getString("gnm");
        String []arg={gn};
        //Log.e("kcmdcmflsmclmc",gn);
        /*boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false; */
        //if(connected==false){
        //    return true;
        //}else{
            if(gn!=null){
                Log.e("kiugyu","**********************************************************");

                Cursor cursor=db.query(mydb.grouptable,null,mydb.column2+"=?",arg,null,null,null);
                if(cursor.moveToFirst()){
                    JSONObject jsonObject = new JSONObject();
                    JSONArray jsonArray=new JSONArray();
                    try {
                        jsonObject.put("user_id",sharedPreferences.getString("user_id","id"));
                        jsonObject.put("group_id",cursor.getString(cursor.getColumnIndex(mydb.column2)));
                        jsonObject.put("group_name",s1);
                        jsonObject.put("group_type",s2);
                        Cursor cursor1=db.query(mydb.contacts_table,null,mydb.cgroupName+"=?"+" OR "+mydb.cgroupName+"=?",new String[]{"",GN},null,null,null);
                        cursor1.moveToFirst();
                        do{
                            JSONObject jsonObject1=new JSONObject();
                            jsonObject1.put("member_phone",cursor1.getString(cursor1.getColumnIndex(mydb.cphone)));
                            jsonObject1.put("member_name",cursor1.getString(cursor1.getColumnIndex(mydb.cname)));

                            jsonArray.put(jsonObject1);

                        }while(cursor1.moveToNext());
                        jsonObject.put("group_members",jsonArray);

                        if(appState.getNetworkCheck()){
                            updateGroup(jsonObject,gn);

                            if(!s1.equals(gn)){
                                //ContentValues contentValues=new ContentValues();
                                //contentValues.put(mydb.cgroupName,s1);
                                //db.update(mydb.contacts_table,contentValues,mydb.cgroupName+"=?",arg);
                                //ContentValues contentValues1=new ContentValues();
                                //contentValues1.put(mydb.col0,s1);
                                //db.update(mydb.billtable,contentValues1,mydb.col0+"=?",arg);
                                //ContentValues contentValues2=new ContentValues();
                                //contentValues2.put(mydb.c0,s1);
                                //db.update(mydb.balancetable,contentValues2,mydb.c0+"=?",arg);
                                //ContentValues contentValues3=new ContentValues();
                                //contentValues3.put(mydb.s3,s1);
                                //db.update(mydb.settledbills,contentValues3,mydb.s3+"=?",arg);
                            }
                            return true;

                        }else {
                            Toast.makeText(CreateGroup.this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                cursor.close();
            }else{
                Log.e("ygujn","**********************************************************");

                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray=new JSONArray();
                try {
                    jsonObject.put("user_id",sharedPreferences.getString("user_id","id"));
                    Log.e("kiugyu",sharedPreferences.getString("user_id","id"));
                    jsonObject.put("group_name",s1);
                    jsonObject.put("group_type",s2);
                    Log.e("kiugyu",s2);
                    Cursor cursor1=db.query(mydb.contacts_table,null,mydb.cgroupName+"=?",new String[]{""},null,null,null);
                    cursor1.moveToFirst();
                    do{
                        JSONObject jsonObject1=new JSONObject();

                        jsonObject1.put("member_phone",cursor1.getString(cursor1.getColumnIndex(mydb.cphone)));

                        jsonObject1.put("member_name",cursor1.getString(cursor1.getColumnIndex(mydb.cname)));
                        Log.e("kiugyu",cursor1.getString(cursor1.getColumnIndex(mydb.cname)));
                        jsonArray.put(jsonObject1);

                    }while(cursor1.moveToNext());
                    jsonObject.put("group_members",jsonArray);

                    if(appState.getNetworkCheck()){
                        addGroup(jsonObject);

                        return true;
                    }else {
                        Toast.makeText(CreateGroup.this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
                    //}


        //String s=s1+"1";
        //mydb.createBill(s1);
        //mydb.createBalance(s);
        return true;
    }

    @Override
    public void onBackPressed() {
        SQLiteDatabase db=mydb.getReadableDatabase();
        db.delete(mydb.contacts_table,mydb.cgroupName+"=?",new String[]{""});
        super.onBackPressed();
    }

    public void updateGroup(JSONObject jsonObject,final String gn){

        Log.v("update_group",jsonObject + "");
        RequestQueue queue = Volley.newRequestQueue(this);
        Utils.showProgressDialog(this,"Updating group...");
        Utils.showProgress();
        String url="http://phpstack-127383-542740.cloudwaysapps.com/api/v1/editgroup";
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("create group",response.toString());
                        Utils.dissmisProgress();
                        String success,mssg;
                        try {

                            success = response.getString("success");
                            //mssg = response.getString("message");

                            if(success.equals("true")){
                                //String gn=bundle.getString("groupName");
                                String []arg={gn};
                                SQLiteDatabase db4=mydb.getReadableDatabase();
                                db4.delete("groups","groupname"+"=?",arg);
//                                cursor1.moveToFirst();
//                                int i=0;
//                                do{
//                                    ContentValues values=new ContentValues();
//                                    values.put(mydb.cname,contact.get(i));
//                                    db.update(mydb.contacts_table,values,mydb.cgroupName+"=?"+" AND "+mydb.cname+"=?",new String[]{"",contact.get(i)});
//                                    i++;
//                                }while (cursor1.moveToNext());
//                                mydb.insertDataContactGroup(cursor.getString(cursor.getColumnIndex(mydb.column2)));
//                                mydb.insertDatagroup(cursor.getString(cursor.getColumnIndex(mydb.column2)),s2,s1);
//                                Intent intent=new Intent(CreateGroup.this,MainActivity.class);
//                                startActivity(intent);
                                Log.e("hhhhhhh","yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
                                JSONObject jsonObject1=response.getJSONObject("data");
                                Log.e("hhhhhhh","KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKy");
                                JSONObject object1=jsonObject1.getJSONObject("data");
                                Log.e("hhhhhhh","KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKy");
                                JSONObject obj2=object1.getJSONObject("group");
                                Log.e("hhhhhhh","KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKy");
                                //id=obj2.getString("group_id");
                                //Log.e("hhhhhhh","KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKy");
                                JSONArray jsonArray=object1.getJSONArray("group_members");
                                String groupname=obj2.getString("group_name");
                                String grouptype=obj2.getString("group_type");
                                Log.e("hhhhhhh","KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKy");
                                String id="";
                                List<String>user=new ArrayList<>();
                                contact=new ArrayList<>();
                                for(int n = 0; n < jsonArray.length(); n++)
                                {
                                    Log.e("hhhhhhh","KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKy");
                                    JSONObject object = jsonArray.getJSONObject(n);
                                    id=object.getString("group_id");
                                    String name=object.getString("member_name");
                                    contact.add(name);
                                    user.add(object.getString("user_id"));
                                    // do some stuff....
                                }
                                //Toast.makeText(CreateGroup.this, mssg, Toast.LENGTH_SHORT).show();
                                SQLiteDatabase db=mydb.getReadableDatabase();
                                SQLiteDatabase sd=mydb.getReadableDatabase();
                                Cursor cs=sd.query(mydb.grouptable,null,null,null,null,null,null);
                                String[] cn=cs.getColumnNames();
                                Log.e("cccccccccccc",cn[0]+" "+cn[1]+" "+cn[2]);
                                mydb.insertDatagroup(id,grouptype,groupname);
                                Log.e("12345",id+" "+groupname+" "+grouptype);
                                Cursor cursor1=db.query(mydb.contacts_table,null,mydb.cgroupName+"=?",new String[]{""},null,null,null);
                                cursor1.moveToFirst();
                                Log.e("ygujn","**********************************************************");
                                int i=0;
                                do{
                                    //if(!user.get(i).equals(null)) {
                                    ContentValues values = new ContentValues();
                                    values.put(mydb.cname, contact.get(i));
                                    values.put(mydb.cuserid, user.get(i));
                                    db.update(mydb.contacts_table, values, mydb.cgroupName + "=?" + " AND " + mydb.cname + "=?", new String[]{"", contact.get(i)});
                                    //}
                                    i++;
                                }while (cursor1.moveToNext());
                                mydb.insertDataContactGroup(id);
                                Intent intent=new Intent(CreateGroup.this,MainActivity.class);
                                startActivity(intent);
                                finish();

                            }else {
                                Log.e("output",response.toString());
                                //Toast.makeText(CreateGroup.this, mssg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, errorListener) {

        };
        queue.add(jsonObjReq);

    }
    public void addGroup(JSONObject jsonObject){
        Log.e("vuhbj","######(((((((((((((((((((((((##############################");

        RequestQueue queue = Volley.newRequestQueue(this);
        Utils.showProgressDialog(this,"Adding group...");
        Utils.showProgress();
        Log.e("input",jsonObject+"");
        contact=new ArrayList<>();
        //RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.CREATE_GROUP, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("create group",response.toString());
                        Utils.dissmisProgress();
                        String success,mssg;
                        try {

                            success = response.getString("success");
                            //mssg = response.getString("message");

                            if(success.equals("true")){
                                Log.e("hhhhhhh","yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
                                JSONObject jsonObject1=response.getJSONObject("data");
                                Log.e("hhhhhhh","KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKy");
                                JSONObject object1=jsonObject1.getJSONObject("data");
                                Log.e("hhhhhhh","KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKy");
                                JSONObject obj2=object1.getJSONObject("group");
                                Log.e("hhhhhhh","KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKy");
                                //id=obj2.getString("group_id");
                                //Log.e("hhhhhhh","KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKy");
                                JSONArray jsonArray=object1.getJSONArray("group_members");
                                String groupname=obj2.getString("group_name");
                                String grouptype=obj2.getString("group_type");
                                Log.e("hhhhhhh","KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKy");
                                String id="";
                                List<String>user=new ArrayList<>();
                                for(int n = 0; n < jsonArray.length(); n++)
                                {
                                    Log.e("hhhhhhh","KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKy");
                                    JSONObject object = jsonArray.getJSONObject(n);
                                    id=object.getString("group_id");
                                    String name=object.getString("member_name");
                                    contact.add(name);
                                    user.add(object.getString("user_id"));
                                    // do some stuff....
                                }
                                //Toast.makeText(CreateGroup.this, mssg, Toast.LENGTH_SHORT).show();
                                SQLiteDatabase db=mydb.getReadableDatabase();
                                SQLiteDatabase sd=mydb.getReadableDatabase();
                                Cursor cs=sd.query(mydb.grouptable,null,null,null,null,null,null);
                                String[] cn=cs.getColumnNames();
                                Log.e("cccccccccccc",cn[0]+" "+cn[1]+" "+cn[2]);
                                mydb.insertDatagroup(id,grouptype,groupname);
                                Log.e("12345",id+" "+groupname+" "+grouptype);
                                Cursor cursor1=db.query(mydb.contacts_table,null,mydb.cgroupName+"=?",new String[]{""},null,null,null);
                                cursor1.moveToFirst();
                                Log.e("ygujn","**********************************************************");
                                int i=0;
                                do{
                                    //if(!user.get(i).equals(null)) {
                                        ContentValues values = new ContentValues();
                                        values.put(mydb.cname, contact.get(i));
                                        values.put(mydb.cuserid, user.get(i));
                                        db.update(mydb.contacts_table, values, mydb.cgroupName + "=?" + " AND " + mydb.cname + "=?", new String[]{"", contact.get(i)});
                                    //}
                                    i++;
                                }while (cursor1.moveToNext());
                                mydb.insertDataContactGroup(id);
                                Intent intent=new Intent(CreateGroup.this,MainActivity.class);
                                startActivity(intent);
                                finish();

                            }else {
                                Log.e("output",response.toString());
                                //Toast.makeText(CreateGroup.this, mssg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, errorListener) {

        };
        queue.add(jsonObjReq);

    }



}
