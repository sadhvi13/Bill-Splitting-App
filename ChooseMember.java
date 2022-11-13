package com.example.android.billsplittingapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChooseMember extends AppCompatActivity {

    RecyclerView rvContacts;
    ConAdapter contactAdapter;
    private EditText editText;
    String gn,gt,gr,gro;
    List<ContactVO> contactVOList=new ArrayList<>();
    Cursor phoneCursor;
    DatabaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_member);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editText=(EditText)findViewById(R.id.con);
        rvContacts=(RecyclerView)findViewById(R.id.rvContacts);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        gn=bundle.getString("group_name");
        gt=bundle.getString("group_type");
        gr=bundle.getString("gnm");
        gro=bundle.getString("GROUP");
        mydb=new DatabaseHelper(this);
        getAllContacts();
        contactAdapter = new ConAdapter(contactVOList,ChooseMember.this,gn,gt,gr,gro);
        rvContacts.setLayoutManager(new LinearLayoutManager(ChooseMember.this));
        rvContacts.setAdapter(contactAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text= String.valueOf(editText.getText());
                getPartialName(text);

            }
        });
    }



    private void getAllContacts() {

            //Async async=new Async();
            //async.execute();
        SQLiteDatabase db=mydb.getReadableDatabase();
        Cursor cursor=db.query(mydb.phone_contacts,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            //List<ContactVO> contactVOList = new ArrayList();
            ContactVO contactVO;
            do{
                String name=cursor.getString(cursor.getColumnIndex(mydb.pcname));
                String phone=cursor.getString(cursor.getColumnIndex(mydb.pcphone));
                int []array=new int[phone.length()];
                int s=0;
                phone=phone.replaceAll(" ","");
                phone=phone.replaceFirst("0", "");
                if(phone.charAt(0)=='+'){
                    phone=phone.replaceFirst("[^1-9]","");
                    phone=phone.replaceFirst("9","");
                    phone=phone.replaceFirst("1","");

                }
                //phone.replaceAll("+91","");
                contactVO = new ContactVO();
                contactVO.setContactName(name);
                contactVO.setContactNumber(phone);
                contactVOList.add(contactVO);

            }while(cursor.moveToNext());
            cursor.close();
        }
    }
    public void getPartialName(String searchText){
        List<ContactVO> contactVOList = new ArrayList();
        ContactVO contactVO;
        SQLiteDatabase db=mydb.getReadableDatabase();
        //ContentResolver contentResolver = getContentResolver();
        //String   whereString = "pcname LIKE ?";
        String[] whereParams = new String[]{ "%" + searchText + "%" };
        Cursor cursor = db.query(mydb.phone_contacts, null, mydb.pcname+" LIKE ?", whereParams, null,null,null);
        if(cursor.moveToFirst()){
            do{
                String name=cursor.getString(cursor.getColumnIndex(mydb.pcname));
                String phone=cursor.getString(cursor.getColumnIndex(mydb.pcphone));
                contactVO = new ContactVO();
                contactVO.setContactName(name);
                contactVO.setContactNumber(phone);
                contactVOList.add(contactVO);

            }while(cursor.moveToNext());
            contactAdapter.updateList(contactVOList);
            cursor.close();

        }

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


