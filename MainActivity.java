package com.example.android.billsplittingapp;

import android.Manifest;
import android.content.ContentValues;
import android.support.v7.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;
import static com.example.android.billsplittingapp.DatabaseHelper.c;
import static com.example.android.billsplittingapp.R.drawable.user;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    DatabaseHelper mydb;
    public List<ContactVO> list=new ArrayList<>();
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    public GlobalVariables appState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydb=new DatabaseHelper(this);
        appState=((GlobalVariables) getApplicationContext());
        SQLiteDatabase db=mydb.getWritableDatabase();
        RecieveBill recieveBill=new RecieveBill();
        recieveBill.execute();
        db.delete(mydb.contacts_table,mydb.cgroupName+"=?",new String[]{""});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            SQLiteDatabase db1=mydb.getWritableDatabase();
            db1.delete(mydb.phone_contacts,null,null);
            Async async=new Async();
            async.execute();
            //getAllContacts();
        }
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        toggle=new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.txt);
        TextView nav_phone=(TextView)hView.findViewById(R.id.txtName);
        nav_user.setText(sharedPreferences.getString("name","name"));
        nav_phone.setText(sharedPreferences.getString("phone","phone"));
        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        FragmentsAdapter adapter = new FragmentsAdapter(getSupportFragmentManager(),this);

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.home){
            Intent intent=new Intent(MainActivity.this,MainActivity.class);
            startActivity(intent);
            Toast.makeText(this,"this is home",Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.settings){
            Intent intent=new Intent(MainActivity.this,Settings.class);
            startActivity(intent);
            finish();
            Toast.makeText(this,"these are settings",Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.log){
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Do you want to logout");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Toast.makeText(MainActivity.this, "You've been logged out successfully", Toast.LENGTH_SHORT).show();
                    appState.logout();
                    SQLiteDatabase db=mydb.getWritableDatabase();
                    db.delete(mydb.grouptable,null,null);
                    db.delete(mydb.balancetable,null,null);
                    db.delete(mydb.contacts_table,null,null);
                    db.delete(mydb.billtable,null,null);
                    db.delete(mydb.settledbills,null,null);
                    Intent i = new Intent(MainActivity.this, Login_activity.class);
                    startActivity(i);
                    finish();
                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                }
            });
            alert.show();
            Toast.makeText(this,"logout",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                Async async=new Async();
                async.execute();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class Async extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
           getAllContacts();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    private void getAllContacts() {
        //List<ContactVO> contactVOList = new ArrayList();
        //ContactVO contactVO;

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    //contactVO = new ContactVO();
                    //contactVO.setContactName(name);
                    Cursor phoneCursor;
                    ContentResolver contentResolver1 = getContentResolver();
                    try{
                        phoneCursor = contentResolver1.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id},
                                null);
                        if (phoneCursor.moveToNext()) {
                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phoneCursor.close();
                            //contactVO.setContactNumber(phoneNumber);
                            mydb.insertPhoneContacts(name,phoneNumber);
                        }

                        phoneCursor.close();

                       // Cursor emailCursor = contentResolver.query(
                       //         ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                       //         null,
                       //         ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                       //         new String[]{id}, null);
                        //while (emailCursor.moveToNext()) {
                        //    String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        //}
                        //contactVOList.add(contactVO);
                    }catch (Exception e){

                    }

                }
            }
        }
        cursor.close();

    }



    class RecieveBill extends AsyncTask<String,String,String> {
        ArrayList<BillModel> billlist = new ArrayList<BillModel>();
        @Override
        protected String doInBackground(String... strings) {
            String url ="http://phpstack-127383-542740.cloudwaysapps.com/api/v1/getmybills";
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("user_id",sharedPreferences.getString("user_id","id"));
                if(appState.getNetworkCheck()){
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

                    Log.e("input",jsonObject+"");
                    //contact=new ArrayList<>();
                    //RequestQueue queue = Volley.newRequestQueue(this);
                    final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("get bills",response.toString());
                                    String success,mssg;
                                    try {

                                        success = response.getString("success");
                                        //mssg = response.getString("message");

                                        if (success.equals("true")) {
                                            Log.e("hhhhhhh", "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
                                            JSONObject jsonObject1 = response.getJSONObject("data");
                                            JSONArray jsonArray=jsonObject1.getJSONArray("data");
                                            int n=0;
                                            DatabaseHelper mydb=new DatabaseHelper(MainActivity.this);
                                            SQLiteDatabase db=mydb.getReadableDatabase();
                                            db.delete(mydb.billtable,null,null);
                                            for(n=0;n<jsonArray.length();n++){
                                                Log.e("gjfdn","ttttttttttttttttttttttttttttttttttttttttttttttt");
                                                //JSONObject obj2=jsonObject.getJSONObject("group");
                                                JSONObject jsonObject2=jsonArray.getJSONObject(n);
                                                String groupid=jsonObject2.getString("group_id");
                                                String col1=jsonObject2.getString("bill_name");
                                                String col2=jsonObject2.getString("user_id");
                                                String phone=jsonObject2.getString("phone");
                                                String billid=jsonObject2.getString("bill_id");
                                                String col4=jsonObject2.getString("pay_or_get");
                                                String col5=jsonObject2.getString("amount_paid");
                                                String col6=jsonObject2.getString("divide_equally_or_unequally");
                                                String col7=jsonObject2.getString("amount_to_get_or_pay");
                                                String col8=jsonObject2.getString("total_bill_amount");
                                                String col10=jsonObject2.getString("id");
                                                ///Cursor cursor;//=db.query(mydb.contacts_table,null,mydb.cgroupName+"=?",new String[]{id},null,null,null);
                                                //cursor=db.query(mydb.billtable,null,mydb.col3+"=?"+" AND "+mydb.col0+"=?"+" AND "+mydb.col9+"=?"+" AND "+mydb.col1+"=?",new String[]{billid,groupid,phone,col1},null,null,null);
                                                //if(cursor.moveToFirst()){
                                                //    Log.e("yuii","||||||||||||||||||||||||||||||||||||||||||");
                                                //}else{
                                                    SQLiteDatabase db2=mydb.getReadableDatabase();
                                                    Cursor cursor1=db2.query(mydb.contacts_table,null,mydb.cphone+"=?",new String[]{phone},null,null,null);
                                                    if(cursor1.moveToFirst()){
                                                    mydb.insertbill(groupid,col1,cursor1.getString(cursor1.getColumnIndex(mydb.cname)),billid,col4,Integer.parseInt(col5),Integer.parseInt(col6),Integer.parseInt(col7),Integer.parseInt(col8),phone,col10);
                                                    Log.e("yyyyyyyyyyyyyyyyy",cursor1.getString(cursor1.getColumnIndex(mydb.cname)));
                                                    }
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
                    //Toast.makeText(MainActivity.this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                @Override
//                public void onResponse(JSONObject response) {
//                    try {
//                        JSONArray jsonArray = response.getJSONArray("add_bill");
//                        //   JSONArray jsonArray = new JSONArray(response);
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            BillModel item;
//                            item = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), BillModel.class);
//                            billlist.add(item);
//                        }
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
        protected void onPostExecute(String response) {
//            DatabaseHelper mydb=new DatabaseHelper(MainActivity.this);
//            //  SQLiteDatabase db=mydb.getWritableDatabase();
//            int n=billlist.size();
//            int i=0;
//            if(!billlist.isEmpty()){
//                while(i<n){
//                    BillModel billModel;
//                    billModel=billlist.get(i);
//                    mydb.insertbill(billModel.getgroup(),billModel.getBill_description(),billModel.getContact_name(),billModel.getBill_id(),
//                            billModel.getPay_or_get(),billModel.getAmount_paid(),billModel.getDivide(),billModel.getAmount_to_get_or_pay(),billModel.getTotal_bill());
//
//                }
//            }

        }

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
