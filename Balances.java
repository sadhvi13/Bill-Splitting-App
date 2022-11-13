package com.example.android.billsplittingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class Balances extends AppCompatActivity {

    String gn;
    String gn1;
    RecyclerView recyclerView;
    DatabaseHelper mydb;
    BalanceAdapter adapter1;
    List<BalanceRV> groupList=new ArrayList<>();
    String text;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balances);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        gn=bundle.getString("group_name");
        //gn1=gn+"1";
        //SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(Balances.this);
        //name=sharedPreferences.getString("ContactName","name");
        mydb=new DatabaseHelper(Balances.this);
        recyclerView = (RecyclerView) findViewById(R.id.balrv);
        SQLiteDatabase db=mydb.getReadableDatabase();
        String []args={gn};
        String []col={mydb.cname,mydb.cphone};
        Cursor cursor1=db.query(mydb.contacts_table,col,mydb.cgroupName+"=?",args,null,null,null);
        //Cursor cursor=db.query(mydb.balancetable,null,mydb.c0+"=?",args,null,null,null);
        //cursor.moveToFirst();
        cursor1.moveToFirst();

        //BalanceRV balanceRV1=new BalanceRV();
        //balanceRV1.setText("dhg");
        //groupList.add(balanceRV1);
        if(cursor1.moveToFirst()){
            do{
                int k=0;
                String h=cursor1.getString(cursor1.getColumnIndex(mydb.cname));
                String ph=cursor1.getString(cursor1.getColumnIndex(mydb.cphone));
                String []a={gn,h};
                Cursor cursor=db.query(mydb.balancetable,null,mydb.c0+"=?"+" AND "+mydb.c1+"=?",a,null,null,null);
            //    BalanceRV balanceRV2=new BalanceRV();
            //    balanceRV2.setText("vvvvvvvvvvvvv");
            //    groupList.add(balanceRV2);
                if(cursor.moveToFirst()) {
                    do {
                        //BalanceRV balanceRV4=new BalanceRV();
                       // balanceRV4.setText("sadh");
                        //groupList.add(balanceRV4);

                            if (cursor.getInt(cursor.getColumnIndex(mydb.c2)) != 0) {
                            //    BalanceRV balanceRV6=new BalanceRV();
                            //    balanceRV6.setText("oooooooooo");
                            //    groupList.add(balanceRV6);
                                k = k - cursor.getInt(cursor.getColumnIndex(mydb.c2));
                            }
                            else /**(cursor.getInt(cursor.getColumnIndex(mydb.c4)) != 0)**/ {
                                k = k + cursor.getInt(cursor.getColumnIndex(mydb.c4));
                            }


                    } while (cursor.moveToNext());
                    BalanceRV balanceRV=new BalanceRV();
                    if(k<0){
                        balanceRV.setText(h+" owes "+String.valueOf(k)+" in total ");

                    }
                    if(k>0){
                        balanceRV.setText(h+" gets back "+String.valueOf(k)+" in total ");
                    }
                   // if(k==0){
                   //     balanceRV.setText("settled");
                   // }
                    if(k!=0){
                        balanceRV.setName(h);
                        balanceRV.setGroup(gn);
                        balanceRV.setPhone(ph);
                        groupList.add(balanceRV);
                    }

            }

            }while (cursor1.moveToNext());
        }

        adapter1 = new BalanceAdapter(groupList,Balances.this);
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
