package com.example.android.billsplittingapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewBalances extends AppCompatActivity {
    String gn;
    RecyclerView recyclerView;
    DatabaseHelper mydb;
    ViewBAdapter adapter1;
    List<ViewBRV> groupList=new ArrayList<>();
    //public static ViewBRV viewBRV=new ViewBRV();
    String name;
    String text;
    String ph;
    int x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_balances);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        name=bundle.getString("Name");
        gn=bundle.getString("gname");
        ph=bundle.getString("phone");
        //text=bundle.getString("text");
        //x=bundle.getInt("posi");
        mydb=new DatabaseHelper(ViewBalances.this);
        recyclerView = (RecyclerView) findViewById(R.id.brv);
        SQLiteDatabase db=mydb.getReadableDatabase();
        String []args={gn,name};
        Cursor cursor=db.query(mydb.balancetable,null,mydb.c0+"=?"+" AND "+mydb.c1+"=?",args,null,null,null);
        if(cursor.moveToFirst()){
            do{
                if(cursor.getInt(cursor.getColumnIndex(mydb.c2))!=0){
                    ViewBRV viewBRV=new ViewBRV();
                    viewBRV.setGroup(gn);
                    viewBRV.setName(name);
                    viewBRV.setPhone(ph);
                    viewBRV.setgo("owes");
                    viewBRV.setAmount(cursor.getInt(cursor.getColumnIndex(mydb.c2)));
                    viewBRV.setOther(cursor.getString(cursor.getColumnIndex(mydb.c3)));
                    viewBRV.setPhone2(cursor.getString(cursor.getColumnIndex(mydb.c7)));
                    viewBRV.setStr("owes "+String.valueOf(cursor.getInt(cursor.getColumnIndex(mydb.c2)))+" to "+cursor.getString(cursor.getColumnIndex(mydb.c3)));
                    groupList.add(viewBRV);
                    //int ind=groupList.indexOf(viewBRV);

                }else{
                    ViewBRV viewBRV=new ViewBRV();
                    viewBRV.setGroup(gn);
                    viewBRV.setPhone(ph);
                    viewBRV.setName(name);
                    viewBRV.setgo("get");
                    viewBRV.setAmount(cursor.getInt(cursor.getColumnIndex(mydb.c4)));
                    viewBRV.setOther(cursor.getString(cursor.getColumnIndex(mydb.c5)));
                    viewBRV.setPhone2(cursor.getString(cursor.getColumnIndex(mydb.c7)));
                    viewBRV.setStr("should get "+String.valueOf(cursor.getInt(cursor.getColumnIndex(mydb.c4)))+" from "+cursor.getString(cursor.getColumnIndex(mydb.c5)));
                    groupList.add(viewBRV);
                    //int ind=groupList.indexOf(viewBRV);

                }
            }while(cursor.moveToNext());
        }

        adapter1 = new ViewBAdapter(groupList,ViewBalances.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter1);
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
