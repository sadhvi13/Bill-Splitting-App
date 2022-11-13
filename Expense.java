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

import static com.example.android.billsplittingapp.DatabaseHelper.c;

public class Expense extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseHelper mydb;
    ExpenseAdapter adapter1;
    List<ExpenseDetailRV> groupList=new ArrayList<>();
    TextView textView;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String id=bundle.getString("ID");
        String group=bundle.getString("group");
        String bill=bundle.getString("bill");
        textView=(TextView)findViewById(R.id.ename);
        recyclerView=(RecyclerView)findViewById(R.id.erv);
        textView2=(TextView)findViewById(R.id.tota);
        mydb=new DatabaseHelper(this);
        SQLiteDatabase db=mydb.getReadableDatabase();
        Cursor cursor=db.query(mydb.billtable,null,mydb.col3+"=?"+" AND "+mydb.col0+"=?"+" AND "+mydb.col1+"=?",new String[]{id,group,bill},null,null,null);
        if(cursor.moveToFirst()){
            textView.setText(cursor.getString(cursor.getColumnIndex(mydb.col1)));
            textView2.setText("Total = Rs."+cursor.getString(cursor.getColumnIndex(mydb.col8)));
            do{
                if(cursor.getInt(cursor.getColumnIndex(mydb.col5))!=0&&cursor.getInt(cursor.getColumnIndex(mydb.col7))>=0){
                    ExpenseDetailRV expenseDetailRV=new ExpenseDetailRV();
                    expenseDetailRV.setText(cursor.getString(cursor.getColumnIndex(mydb.col2))+" paid "+cursor.getInt(cursor.getColumnIndex(mydb.col5))+" and gets "+cursor.getInt(cursor.getColumnIndex(mydb.col7)));
                    groupList.add(expenseDetailRV);
                }else if(cursor.getInt(cursor.getColumnIndex(mydb.col5))!=0&&cursor.getInt(cursor.getColumnIndex(mydb.col7))<0){
                    ExpenseDetailRV expenseDetailRV=new ExpenseDetailRV();
                    expenseDetailRV.setText(cursor.getString(cursor.getColumnIndex(mydb.col2))+" paid "+cursor.getInt(cursor.getColumnIndex(mydb.col5))+" and owes "+cursor.getInt(cursor.getColumnIndex(mydb.col7)));
                    groupList.add(expenseDetailRV);
                }else if(cursor.getInt(cursor.getColumnIndex(mydb.col5))==0&&cursor.getInt(cursor.getColumnIndex(mydb.col7))<0){
                    ExpenseDetailRV expenseDetailRV=new ExpenseDetailRV();
                    expenseDetailRV.setText(cursor.getString(cursor.getColumnIndex(mydb.col2))+" owes "+cursor.getInt(cursor.getColumnIndex(mydb.col7)));
                    groupList.add(expenseDetailRV);
                }
            }while (cursor.moveToNext());
        }
        adapter1 = new ExpenseAdapter(groupList,Expense.this);
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
