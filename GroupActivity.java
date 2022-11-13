package com.example.android.billsplittingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.android.billsplittingapp.DatabaseHelper.col0;
import static com.example.android.billsplittingapp.DatabaseHelper.col1;
import static com.example.android.billsplittingapp.DatabaseHelper.col4;
import static com.example.android.billsplittingapp.DatabaseHelper.col5;
import static com.example.android.billsplittingapp.DatabaseHelper.col6;
import static com.example.android.billsplittingapp.DatabaseHelper.col7;
import static com.example.android.billsplittingapp.DatabaseHelper.col8;
import static com.example.android.billsplittingapp.DatabaseHelper.s2;
import static com.example.android.billsplittingapp.R.id.amount;
import static com.example.android.billsplittingapp.R.id.nm;
import static com.example.android.billsplittingapp.R.id.paid;
import static com.example.android.billsplittingapp.R.id.phone;
import static com.example.android.billsplittingapp.R.layout.contacts;

public class GroupActivity extends BaseActivity {

    FloatingActionButton floatingActionButton;
    ImageView imageView;
    TextView textView;
    TextView textView1;
    String gn;
    RecyclerView recyclerView;
    DatabaseHelper mydb;
    GroupAdapter adapter1;
    List<ExpensesRV> groupList=new ArrayList<>();
    String name;
    TextView balan;
    String type;
    SearchView searchView;
    String group;
    HashMap<String,Integer> positive;
    HashMap<String,Integer> negative;
    List<String> contacts=new ArrayList<>();
    List<String> posphone=new ArrayList<>();
    List<String> negphone=new ArrayList<>();
    List<String> phoneno=new ArrayList<>();
    List<String>contactid=new ArrayList<>();
    TextView gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        floatingActionButton=(FloatingActionButton) findViewById(R.id.fab1);
        imageView=(ImageView) findViewById(R.id.logo);
        textView=(TextView) findViewById(R.id.nm);
        textView1=(TextView)findViewById(R.id.sbr);
        gallery=(TextView)findViewById(R.id.gal);

        searchView=(SearchView)findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter1.getFilter().filter(query);
                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                adapter1.getFilter().filter(newText);
                return true;
            }
        });
        RecieveSettledBills recieveSettledBills=new RecieveSettledBills();
        recieveSettledBills.execute();
        balan=(TextView)findViewById(R.id.bala);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        gn=bundle.getString("group_name");
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GroupActivity.this,SettledBills.class);
                intent.putExtra("group_name",gn);
                startActivity(intent);
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GroupActivity.this,Gallery.class);
                intent.putExtra("group",gn);
                startActivity(intent);

            }
        });
        type=bundle.getString("icon");
        group=bundle.getString("GROUP");
        if(type!=null){
            if(type.equals("House")){
                imageView.setImageResource(R.drawable.house);
            }else if(type.equals("Trip")){
                imageView.setImageResource(R.drawable.tour);
            }else if(type.equals("Food")){
                imageView.setImageResource(R.drawable.food);
            }else{
                imageView.setImageResource(R.drawable.other);
            }
            textView.setText(bundle.getString("GROUP"));
        }else{
            textView.setText(gn);
        }

        //imageView.setImageResource(getIntent().getExtras().getInt("icon"));
        //(getIntent().getExtras().getString("group_name"));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(GroupActivity.this,AddBill.class);
                    intent.putExtra("group_name",gn);
                    intent.putExtra("icon",type);
                    intent.putExtra("GROUP",group);
                    startActivity(intent);
                }
        });
        balan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String []columns={mydb.cname,mydb.cphone};
                String []args2={gn};
                SQLiteDatabase db6=mydb.getReadableDatabase();
                Cursor cursor2=db6.query(mydb.contacts_table,columns,mydb.cgroupName+"=?",args2,null,null,null);
                cursor2.moveToFirst();
                contacts=new ArrayList<String>();
                phoneno=new ArrayList<String>();
                int i=0;
                do{
                    contacts.add(cursor2.getString(cursor2.getColumnIndex(mydb.cname)));
                    phoneno.add(cursor2.getString(cursor2.getColumnIndex(mydb.cphone)));
                    i++;
                }while (cursor2.moveToNext());
                contacts.add("multiple people");
                int j=0;
                j=0;
                SQLiteDatabase db=mydb.getReadableDatabase();
                SQLiteDatabase db1=mydb.getWritableDatabase();
                String []ar={gn};
                db1.delete(mydb.balancetable,mydb.c0+"=?",ar);
                positive=new HashMap<>();
                posphone=new ArrayList<>();
                negphone=new ArrayList<>();
                negative=new HashMap<>();
                while(j<contacts.size()-1){
                    String p=contacts.get(j);
                    Log.e("contactttt",contacts.get(j));
                    String ph=phoneno.get(j);
                    Log.e("phoneno",ph);
                    int k=0;
                    String []col={mydb.col7};
                    String []args={p,gn};
                    Cursor cursor=db.query(mydb.billtable,col,mydb.col2+"=?"+" AND "+mydb.col0+"=?",args,null,null,null);
                    cursor.moveToFirst();
                    if(cursor.moveToFirst()){
                        do{
                            k=k+cursor.getInt(cursor.getColumnIndex(mydb.col7));
                        }while (cursor.moveToNext());
                        if(k>=0){
                            positive.put(p,k);
                            posphone.add(ph);
                        }else{
                            negative.put(p,k);
                            negphone.add(ph);
                        }
                    }
                    Log.e("22222222","+++++++++++++++++++++++++++++++++++");
                    j++;
                }
                Cursor cur=db.query(mydb.settledbills,null,mydb.s3+"=?",new String[]{gn},null,null,null);
                if(cur.moveToFirst()){
                    do{
                        String na=cur.getString(cur.getColumnIndex(mydb.s4));
                        String na2=cur.getString(cur.getColumnIndex(mydb.s5));
                        if(positive.containsKey(na)){
                            Log.w("myApp", "no network!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            int v=positive.get(na);
                            positive.put(na,v+cur.getInt(cur.getColumnIndex(mydb.s2)));
                        }
                        else if(negative.containsKey(na)){
                            Log.w("myApp", "no network@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                            int v=negative.get(na);
                            if(v+cur.getInt(cur.getColumnIndex(mydb.s2))>0){
                                positive.put(na,v+cur.getInt(cur.getColumnIndex(mydb.s2)));
                            }else{
                                negative.put(na,v+cur.getInt(cur.getColumnIndex(mydb.s2)));

                            }
                        } else{
                            positive.put(na,cur.getInt(cur.getColumnIndex(mydb.s2)));
                            posphone.add(cur.getString(cur.getColumnIndex(mydb.s7)));
                            Log.w("myApp", "no network{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{");
                        }
                        if(positive.containsKey(na2)){
                            Log.w("myApp", "no network----------------------------------------------");
                            int v=positive.get(na2);
                            if(v-cur.getInt(cur.getColumnIndex(mydb.s2))>0){
                                positive.put(na2,v-cur.getInt(cur.getColumnIndex(mydb.s2)));
                            }else{
                                negative.put(na2,v-cur.getInt(cur.getColumnIndex(mydb.s2)));
                            }

                        }
                        else if(negative.containsKey(na2)){
                            Log.w("myApp", "no network+++++++++++++++++++++++++++++++++++++++++++++++++");
                            int v=negative.get(na2);
                            negative.put(na2,v-cur.getInt(cur.getColumnIndex(mydb.s2)));
                        } else{
                            negative.put(na2,0-cur.getInt(cur.getColumnIndex(mydb.s2)));
                            negphone.add(cur.getString(cur.getColumnIndex(mydb.s8)));
                            Log.w("myApp", "no network???????????????????????????????????????????????????/");
                        }

                    }while(cur.moveToNext());
                }
                //String m=gn+"1";
                //db.execSQL("DELETE FROM "+m);
                int l=0;
                while(!negative.isEmpty()){
                    Set< Map.Entry< String,Integer> > st = positive.entrySet();
                    Set< Map.Entry< String,Integer> > st1 = negative.entrySet();
                    int max=0;
                    String maxi=null;
                    String maxi2=null;
                    int max2=0;
                    String maxip=null;
                    String maxip2=null;
                    i=0;
                    for (Map.Entry< String,Integer> me:st)
                    {
                        Log.e("3333333333333333","----------------------------------");
                        //System.out.print(me.getKey()+":");
                        //System.out.println(me.getValue());
                        if(max<me.getValue()){
                            max=me.getValue();
                            maxi=me.getKey();
                            maxip=posphone.get(i);
                        }
                        i++;
                    }
                    i=0;
                    for (Map.Entry< String,Integer> me:st1)
                    {
                        Log.e("44444444444444","00000000000000000000000000000000");
                        if(max2<0-me.getValue()){
                            max2=me.getValue();
                            maxi2=me.getKey();
                            maxip2=negphone.get(i);
                        }
                        i++;
                    }
                    Log.e("6666666666666666","LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
                    if(max>=0-max2){
                        Log.e("777777777777777777","PPPPPPPPPPPPPPPPPPPPPP");
                        Toast.makeText(GroupActivity.this,"gdcjnhiygjfvhjfhbgfjn",Toast.LENGTH_LONG);
                        mydb.insertbalance(gn,maxi,0,null,0-max2,maxi2,maxip,maxip2);
                        mydb.insertbalance(gn,maxi2,0-max2,maxi,0,null,maxip2,maxip);
                        positive.put(maxi,max+max2);
                        if(max+max2==0){

                            positive.remove(maxi);
                            posphone.remove(maxip);
                        }
                        negative.remove(maxi2);
                        negphone.remove(maxip2);
                    }else{
                        mydb.insertbalance(gn,maxi,0,null,max,maxi2,maxip,maxip2);
                        mydb.insertbalance(gn,maxi2,max,maxi,0,null,maxip2,maxip);
                        negative.put(maxi2,max+max2);
                        positive.remove(maxi);
                        posphone.remove(maxip);
                    }

                }
                Intent intent=new Intent(GroupActivity.this,Balances.class);
                intent.putExtra("group_name",gn);
                startActivity(intent);
            }
        });
        //SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(GroupActivity.this);
        name=sharedPreferences.getString("name","name");

        mydb=new DatabaseHelper(GroupActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        SQLiteDatabase db=mydb.getReadableDatabase();
        //String []col={mydb.col1,mydb.col2,mydb.col3,mydb.col4,mydb.col5,mydb.col6,mydb.col7,mydb.col8};
        String []args={gn};
        Cursor cursor=db.query(mydb.billtable,null,col0+"=?",args,null,null,null);
        Cursor cursor2=db.query(mydb.billtable,null,col0+"=?",args,null,null,null);
        cursor2.moveToFirst();
        cursor2.moveToNext();
        cursor.moveToFirst();
        if(cursor.moveToFirst()){
            do{
                boolean involved=false;
                String desp="desp";
                int total=0;
                String payorreceive="payorreceive";
                int amo=0;
                String paid=" paid ";
                String cur=cursor.getString(cursor.getColumnIndex(mydb.col3));
                desp=cursor.getString(cursor.getColumnIndex(col1));
                total=cursor.getInt(cursor.getColumnIndex(col8));
                while(!cursor.isAfterLast()&&cursor.getString(cursor.getColumnIndex(mydb.col3)).equals(cur)){
                    //desp=cursor.getString(cursor.getColumnIndex(mydb.col1));
                    if(cursor.getString(cursor.getColumnIndex(mydb.col2)).equals(name)){
                        involved=true;
                        if(cursor.getInt(cursor.getColumnIndex(col7))>0){
                            payorreceive="receive";
                            amo=cursor.getInt(cursor.getColumnIndex(col7));
                        }else{
                            payorreceive="pay";
                            amo=0-cursor.getInt(cursor.getColumnIndex(col7));
                        }
                    }
                    if(cursor.getInt(cursor.getColumnIndex(col5))!=0){
                        paid=cursor.getString(cursor.getColumnIndex(mydb.col2))+", "+paid;
                    }
                    cursor.moveToNext();
                    //if(cursor.getInt(cursor.getColumnIndex(mydb.col7))>0){
                    //    paid=cursor.getString(cursor.getColumnIndex(mydb.col2))+", "+paid;
                    //}
                    //Log.i("ERROR",cursor.getString(cursor.getColumnIndex(mydb.col2)));
                    //cursor.moveToNext();
                    //Log.i("error",cursor.getString(cursor2.getColumnIndex(mydb.col2)));
                    //cursor2.moveToNext();

                }

                //if(cursor.getString(cursor.getColumnIndex(mydb.col2))==name){
                //    involved=true;
                //    total=cursor.getInt(cursor.getColumnIndex(mydb.col8));
                //    if(cursor.getInt(cursor.getColumnIndex(mydb.col7))>0){
                //        payorreceive="receive";
                //        amo=cursor.getInt(cursor.getColumnIndex(mydb.col7));
                //    }else{
                //        payorreceive="pay";
                //        amo=cursor.getInt(0-cursor.getColumnIndex(mydb.col7));
                //    }
                //}

                //cursor.moveToNext();
                //if(!cursor2.isAfterLast())
                //{
                //cursor2.moveToNext();
                //}

                if(involved==true){
                    ExpensesRV expensesRV=new ExpensesRV();
                    paid=paid+total;
                    expensesRV.setT(desp);
                    expensesRV.setT2(paid);
                    expensesRV.setT3(payorreceive);
                    expensesRV.setT4(String.valueOf(amo));
                    expensesRV.setId(cur);
                    expensesRV.setGroup(gn);
                    expensesRV.setUser_id(sharedPreferences.getString("user_id","id"));
                    groupList.add(expensesRV);
                }
                else{
                    ExpensesRV expensesRV=new ExpensesRV();
                    expensesRV.setT(desp);
                    expensesRV.setT2(" ");
                    expensesRV.setT3("Not Involved");
                    expensesRV.setT4("Settled");
                    expensesRV.setId(cur);
                    expensesRV.setGroup(gn);
                    expensesRV.setUser_id(sharedPreferences.getString("user_id","id"));
                    groupList.add(expensesRV);
                }
                cursor.moveToPrevious();

            }while (cursor.moveToNext());

        }
        adapter1 = new GroupAdapter(groupList,GroupActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter1);

    }
    class RecieveSettledBills extends AsyncTask<String,String,String> {
        List<SettledbillModel> sbilllist=new ArrayList<>();
        @Override
        protected String doInBackground(String... strings) {
            String url ="http://phpstack-127383-542740.cloudwaysapps.com/api/v1/getsettledbills";
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("user_id",sharedPreferences.getString("user_id","id"));
                jsonObject.put("group_id",gn);
                if(appState.getNetworkCheck()){
                    RequestQueue queue = Volley.newRequestQueue(GroupActivity.this);

                    Log.e("input",jsonObject+"");
                    //contact=new ArrayList<>();
                    //RequestQueue queue = Volley.newRequestQueue(this);
                    final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("get settled bills",response.toString());
                                    String success,mssg;
                                    try {

                                        success = response.getString("success");
                                        //mssg = response.getString("message");

                                        if (success.equals("true")) {
                                            Log.e("hhhhhhh", "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
                                            JSONObject jsonObject1 = response.getJSONObject("data");
                                            JSONArray jsonArray=jsonObject1.getJSONArray("data");
                                            int n=0;
                                            DatabaseHelper mydb=new DatabaseHelper(GroupActivity.this);
                                            SQLiteDatabase db=mydb.getReadableDatabase();
                                            db.delete(mydb.settledbills,null,null);
                                            for(n=0;n<jsonArray.length();n++){
                                                Log.e("gjfdn","ttttttttttttttttttttttttttttttttttttttttttttttt");
                                                //JSONObject obj2=jsonObject.getJSONObject("group");
                                                JSONObject jsonObject2=jsonArray.getJSONObject(n);
                                                String groupid=jsonObject2.getString("groupNm");
                                                String payment=jsonObject2.getString("payment");
                                                String amount=jsonObject2.getString("amount");
                                                String person1=jsonObject2.getString("per");
                                                String person2=jsonObject2.getString("perl");
                                                String id12=jsonObject2.getString("id");
                                                String ph1=jsonObject2.getString("person1_phone");
                                                String ph2=jsonObject2.getString("person2_phone");


                                                //Cursor cursor;//=db.query(mydb.contacts_table,null,mydb.cgroupName+"=?",new String[]{id},null,null,null);
                                                //cursor=db.query(mydb.settledbills,null,mydb.s3+"=?",new String[]{id12},null,null,null);
                                                //if(cursor.moveToFirst()){
                                                //    Log.e("yuii","||||||||||||||||||||||||||||||||||||||||||");
                                                //}else{
                                                    SQLiteDatabase db2=mydb.getReadableDatabase();
                                                    mydb.insertsettledbill(payment,amount,groupid,person1,person2,id12,ph1,ph2);
                                                    Log.e("qwert","ooooooooooooooooooooooooooooooooooooooooooooooo");

                                                //}
                                            }
                                        }
                                    }catch (JSONException e){

                                    }

                                }
                            }, errorListener) {

                    };
                    queue.add(jsonObjReq);
                }else {
                    //Toast.makeText(GroupActivity.this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                @Override
//                public void onResponse(JSONObject response) {
//                    try {
//                        JSONArray jsonArray = response.getJSONArray("settledbills");
//                        //   JSONArray jsonArray = new JSONArray(response);
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            SettledbillModel item;
//                            item = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), SettledbillModel.class);
//                            sbilllist.add(item);
//                        }
//                    }catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    error.printStackTrace();
//                }
//            });
            return null;
        }
        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
//            DatabaseHelper mydb=new DatabaseHelper(GroupActivity.this);
//            //  SQLiteDatabase db=mydb.getWritableDatabase();
//            int n=sbilllist.size();
//            int i=0;
//            if(!sbilllist.isEmpty()){
//                while(i<n){
//                    SettledbillModel settledbillModel;
//                    settledbillModel=sbilllist.get(i);
//                    mydb.insertsettledbill(settledbillModel.getPayment(),settledbillModel.getAmount(),settledbillModel.getGroup(),
//                            settledbillModel.getPerson1(),settledbillModel.getPerson2());
//                }
            }

        }



}