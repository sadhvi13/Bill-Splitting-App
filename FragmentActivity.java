package com.example.android.billsplittingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.R.attr.fragment;
import static com.example.android.billsplittingapp.DatabaseHelper.c;


public class FragmentActivity extends BaseFragment {

    private RecyclerView recyclerView;
    private RAdapter2 adapter2;
    private List<RList2> list = new ArrayList<>();
    String name;
    String phone;
    //SharedPreferences sharedPreferences;
    //BaseActivity baseActivity=new BaseActivity();


    public FragmentActivity() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_activity, container, false);
        JSONObject jsonObject=new JSONObject();
        if(sharedpreferences.contains("name")){
            name=sharedpreferences.getString("name","name");
            phone=sharedpreferences.getString("phone","phone");
        }
        try{
            jsonObject.put("logged_in_user_id",sharedpreferences.getString("user_id","id"));
            jsonObject.put("user_id",sharedpreferences.getString("user_id","id"));
            if(appState.getNetworkCheck()){
                activityreceive(jsonObject);
            }else {
                Toast.makeText(getContext(), getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view2);
        adapter2 = new RAdapter2(list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter2);
        // Inflate the layout for this fragment
        return rootView;
    }

    public void activityreceive(JSONObject jsonObject){

        RequestQueue queue = Volley.newRequestQueue(getContext());
        Utils.showProgressDialog(getContext(),"Loading Activities...");
        Utils.showProgress();
        String urls="http://phpstack-127383-542740.cloudwaysapps.com/api/v1/getgroupactivities";
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urls, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("activities",response.toString());
                        Utils.dissmisProgress();
                        String success,mssg;
                        try {
                            success = response.getString("success");
                            mssg = response.getString("message");
                            JSONObject jsonObject1=new JSONObject();
                            jsonObject1=response.getJSONObject("data");
                            JSONArray jsonArray=jsonObject1.getJSONArray("data");
                            //contact=new ArrayList<>();

                            for(int n = 0; n < jsonArray.length(); n++)
                            {
                                JSONObject object = jsonArray.getJSONObject(n);
                                String group=object.getString("group_id");
                                String user=object.getString("created_by");
                                DatabaseHelper mydb=new DatabaseHelper(getContext());
                                SQLiteDatabase db=mydb.getReadableDatabase();
                                Cursor cursor=db.query(mydb.grouptable,null,mydb.column2+"=?",new String[]{group},null,null,null);
                                Cursor cursor1=db.query(mydb.contacts_table,null,mydb.cuserid+"=?",new String[]{user},null,null,null);
                                cursor.moveToFirst();
                                cursor1.moveToFirst();
                                String create=" created ";
                                RList2 rList2=new RList2(cursor1.getString(cursor1.getColumnIndex(mydb.cname))+create+cursor.getString(cursor.getColumnIndex(mydb.column1)));
                                list.add(rList2);
                                // do some stuff....
                            }

                            if(success.equals("true")){

                                adapter2.changeData(list);
                                Toast.makeText(getActivity(), mssg, Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(getActivity(), mssg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },errorListener) {

        };
        queue.add(jsonObjReq);

        String url1= "http://phpstack-127383-542740.cloudwaysapps.com/api/v1/getbillactivities";


        final JsonObjectRequest jsonObjReq1 = new JsonObjectRequest(Request.Method.POST,url1 , jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("activities",response.toString());

                        String success,mssg;
                        try {
                            success = response.getString("success");
                            mssg = response.getString("message");
                            JSONObject jsonObject1=new JSONObject();
                            jsonObject1=response.getJSONObject("data");
                            JSONArray jsonArray=jsonObject1.getJSONArray("data");
                            //contact=new ArrayList<>();

                            for(int n = 0; n < jsonArray.length(); n++)
                            {
                                JSONObject object = jsonArray.getJSONObject(n);
                                String bill_id=object.getString("id");
                                String user=object.getString("created_by");
                                DatabaseHelper mydb=new DatabaseHelper(getContext());
                                SQLiteDatabase db=mydb.getReadableDatabase();
                                Cursor cursor=db.query(mydb.billtable,null,mydb.col10+"=?",new String[]{bill_id},null,null,null);
                               Cursor cursor1=db.query(mydb.contacts_table,null,mydb.cuserid+"=?",new String[]{user},null,null,null);
                                cursor.moveToFirst();
                                cursor1.moveToFirst();
                                Cursor cursor2=db.query(mydb.grouptable,null,mydb.column2+"=?",new String[]{cursor.getString(cursor.getColumnIndex(mydb.col0))},null,null,null);
                                cursor2.moveToFirst();
                                String create=" created ";
                                RList2 rList2=new RList2(cursor1.getString(cursor1.getColumnIndex(mydb.cname))+create+cursor.getString(cursor.getColumnIndex(mydb.col1))+" in "+cursor2.getString(cursor2.getColumnIndex(mydb.column1)));
                                list.add(rList2);
                                // do some stuff....
                            }

                            if(success.equals("true")){
                                Log.e("1111111111111","yyyyyyyyyyyyyyyyyyyyyyyyyyyy");
//                                adapter2 = new RAdapter2(list);
//                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//                                recyclerView.setLayoutManager(mLayoutManager);
//                                recyclerView.setItemAnimator(new DefaultItemAnimator());
//                                recyclerView.setAdapter(adapter2);
                                adapter2.changeData(list);
                                Utils.dissmisProgress();

                                Toast.makeText(getActivity(), mssg, Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(getActivity(), mssg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },errorListener) {

        };
        queue.add(jsonObjReq1);
    }


}
