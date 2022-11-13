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

import java.util.ArrayList;
import java.util.List;

public class SettledBills extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    List<Settled> set=new ArrayList<>();
    Settled sd;
    String s;
    int a;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settled_bills);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=(RecyclerView)findViewById(R.id.sbrv);
        databaseHelper=new DatabaseHelper(this);
        db=databaseHelper.getReadableDatabase();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String group=bundle.getString("group_name");
        String []args={group};
        Cursor cursor=db.query(databaseHelper.settledbills,null,databaseHelper.s3+"=?",args,null,null,null);
        if(cursor.moveToFirst()){
            do{
                sd=new Settled();
                s=cursor.getString(cursor.getColumnIndex(databaseHelper.s1));
                a=cursor.getInt(cursor.getColumnIndex(databaseHelper.s2));
                sd.setS1(s);
                sd.setS2(a);
                set.add(sd);
            }while (cursor.moveToNext());
        }
        sd=new Settled();
        sd.setS1("fyhjl");
        sd.setS2(90);
        Settledbill_Adapter sa=new Settledbill_Adapter(set,SettledBills.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(sa);
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


