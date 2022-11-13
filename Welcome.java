package com.example.android.billsplittingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.os.Handler;

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

import static android.R.attr.id;
import static android.net.sip.SipErrorCode.TIME_OUT;
import static com.example.android.billsplittingapp.DatabaseHelper.c;

public class Welcome extends BaseActivity {
    DatabaseHelper mydb;
    private static int TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        RecieveGroup recieveGroup=new RecieveGroup();
        recieveGroup.execute();
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Intent intent=new Intent(Welcome.this,MainActivity.class);
//                startActivity(intent);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Welcome.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, TIME_OUT);

//        RelativeLayout layout= (RelativeLayout) findViewById(R.id.relativeLayout1);
//        layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(Welcome.this,MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }

    class RecieveGroup extends AsyncTask<String,String,String> {
        List<GroupModel> grouplist=new ArrayList<>();

        @Override
        protected void onPreExecute() {
            //Utils.showProgressDialog(MainActivity.this,"Adding group...");
            //Utils.showProgress();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url ="http://phpstack-127383-542740.cloudwaysapps.com/api/v1/getmygroups";
            JSONObject jsonObject1=new JSONObject();
            try {
                jsonObject1.put("user_id",sharedPreferences.getString("user_id","id"));
                if(appState.getNetworkCheck()){
                    RequestQueue queue = Volley.newRequestQueue(Welcome.this);

                    Log.e("input",jsonObject1+"");
                    //contact=new ArrayList<>();
                    //RequestQueue queue = Volley.newRequestQueue(this);
                    final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject1,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("get groups",response.toString());
                                    String success,mssg;
                                    try {

                                        success = response.getString("success");
                                        //mssg = response.getString("message");

                                        if (success.equals("true")) {
                                            Log.e("hhhhhhh", "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
                                            JSONObject jsonObject1 = response.getJSONObject("data");
                                            JSONArray jsonArray=jsonObject1.getJSONArray("data");
                                            int n=0;
                                            DatabaseHelper mydb=new DatabaseHelper(Welcome.this);
                                            SQLiteDatabase db=mydb.getReadableDatabase();
                                            db.delete(mydb.grouptable,null,null);
                                            db.delete(mydb.contacts_table,null,null);
                                            for(n=0;n<jsonArray.length();n++){
                                                Log.e("gjfdn","ttttttttttttttttttttttttttttttttttttttttttttttt");
                                                JSONObject jsonObject=jsonArray.getJSONObject(n);
                                                //JSONObject obj2=jsonObject.getJSONObject("group");
                                                JSONArray jsonArray1=jsonObject.getJSONArray("group_members");
                                                String groupname=jsonObject.getString("group_name");
                                                String grouptype=jsonObject.getString("group_type");
                                                String id="";
                                                //JSONObject objectx = jsonArray1.getJSONObject(0);
                                                id=jsonObject.getString("id");

                                                //Cursor cursor;//=db.query(mydb.contacts_table,null,mydb.cgroupName+"=?",new String[]{id},null,null,null);
                                                //cursor=db.query(mydb.grouptable,null,mydb.column2+"=?",new String[]{id},null,null,null);
                                                //if(cursor.moveToFirst()){
                                                //    Log.e("yuii","||||||||||||||||||||||||||||||||||||||||||");
                                                //}else{
                                                    Log.e("qwert","ooooooooooooooooooooooooooooooooooooooooooooooo");
                                                    mydb.insertDatagroup(id,grouptype,groupname);
                                                    for(int k = 0; k < jsonArray1.length(); k++)
                                                    {
                                                        JSONObject object = jsonArray1.getJSONObject(k);
                                                        //id=object.getString("group_id");
                                                        String name=object.getString("member_name");
                                                        Log.e("qwert",name);
                                                        String phone=object.getString("member_phone");
                                                        String userid=object.getString("user_id");
                                                        mydb.insertDatacontacts(name,phone,0,id,userid);
                                                        // do some stuff....
                                                    }
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
                    //Toast.makeText(Welcome.this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                @Override
//                public void onResponse(JSONObject response) {
//                    try {
//                        JSONArray jsonArray = response.getJSONArray("groups");
//                        //   JSONArray jsonArray = new JSONArray(response);
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            GroupModel item;
//                            item = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), GroupModel.class);
//                            grouplist.add(item);
//                        }
//
//
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
        protected  void onPostExecute(String response){
            //Utils.dissmisProgress();
            super.onPostExecute(response);
//            DatabaseHelper mydb=new DatabaseHelper(MainActivity.this);
//            //     SQLiteDatabase db=mydb.getWritableDatabase();
//            int n=grouplist.size();
//            int i=0,j=0;
//            if(!grouplist.isEmpty()){
//                while(i<n){
//                    GroupModel groupModel;
//                    groupModel=grouplist.get(i);
//                    mydb.insertDatagroup(groupModel.getGroupId(),groupModel.getGroupType(),groupModel.getGroup());
//                    ContactModel[] cm=groupModel.getContacts();
//                    int p=cm.length;
//                    while(j<p){
//                        mydb.insertDatacontact(cm[j].getName(),cm[j].getPhone(),0,groupModel.getGroupId());
//                    }
//                }
//            }
        }
    }
    class RecieveUser extends AsyncTask<String,String,String> {
        List<GroupModel> grouplist=new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url ="http://phpstack-127383-542740.cloudwaysapps.com/api/v1/getmygroups";
            JSONObject jsonObject1=new JSONObject();
            try {
                SQLiteDatabase db=mydb.getReadableDatabase();
                Cursor cursor=db.query(mydb.contacts_table,null,mydb.cuserid+"=?",new String[]{null},null,null,null);
                if(cursor.moveToFirst()){
                    do{
                        jsonObject1.put("phone",cursor.getString(cursor.getColumnIndex(mydb.cphone)));
                    }while (cursor.moveToNext());
                }
                //jsonObject1.put("user_id",sharedPreferences.getString("user_id","id"));
                if(appState.getNetworkCheck()){
                    RequestQueue queue = Volley.newRequestQueue(Welcome.this);

                    Log.e("input",jsonObject1+"");
                    //contact=new ArrayList<>();
                    //RequestQueue queue = Volley.newRequestQueue(this);
                    final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject1,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("get users",response.toString());
                                    String success,mssg;
                                    try {

                                        success = response.getString("success");
                                        //mssg = response.getString("message");

                                        if (success.equals("true")) {
                                            Log.e("hhhhhhh", "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
                                            JSONObject jsonObject1 = response.getJSONObject("data");
                                            JSONArray jsonArray=jsonObject1.getJSONArray("data");
                                            int n=0;
                                            for(n=0;n<jsonArray.length();n++){
                                                Log.e("gjfdn","ttttttttttttttttttttttttttttttttttttttttttttttt");
                                                JSONObject jsonObject=jsonArray.getJSONObject(n);

                                                String id=jsonObject.getString("user_id");
                                                String ph=jsonObject.getString("phone");
                                                String name=jsonObject.getString("name");
                                                DatabaseHelper mydb=new DatabaseHelper(Welcome.this);
                                                SQLiteDatabase db=mydb.getReadableDatabase();
                                                Cursor cursor;//=db.query(mydb.contacts_table,null,mydb.cgroupName+"=?",new String[]{id},null,null,null);
                                                cursor=db.query(mydb.contacts_table,null,mydb.cphone+"=?",new String[]{ph},null,null,null);
                                                if(cursor.moveToFirst()){
                                                    Log.e("yuii","||||||||||||||||||||||||||||||||||||||||||");
                                                }else{
                                                    Log.e("qwert","ooooooooooooooooooooooooooooooooooooooooooooooo");
                                                    ContentValues values=new ContentValues();
                                                    values.put(mydb.cuserid,id);
                                                    values.put(mydb.cname,name);
                                                    db.update(mydb.contacts_table,values,mydb.cphone+"=?",new String[]{ph});
                                                }
                                            }
                                        }
                                    }catch (JSONException e){

                                    }

                                }
                            }, errorListener) {

                    };
                    queue.add(jsonObjReq);
                }else {
                    //Toast.makeText(Welcome.this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//
            return null;
        }

        @Override
        protected  void onPostExecute(String response){
            //Utils.dissmisProgress();
            super.onPostExecute(response);
//
        }
    }
}
