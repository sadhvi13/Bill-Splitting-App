package com.example.android.billsplittingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.id;
import static android.R.attr.type;
import static com.example.android.billsplittingapp.R.id.con;
import static com.example.android.billsplittingapp.R.id.groupname;
import static com.example.android.billsplittingapp.R.id.phone;

/**
 * Created by praty on 06-06-2018.
 */

    public class DatabaseHelper extends SQLiteOpenHelper {

        public static final String databaseName="group.db";
        //columns for groups
        public static final String grouptable="groups";
        public static final String column1="acgroupname";
        public static final String column2="groupname";
        public static final String column3="type";


        //columns for contacts
        public static final String contacts_table="contacts";
        public static final String cname="name";
        public static final String cphone="phone";
        public static final String cimage="image";
        public static final String cgroupName="group_name";
        public static final String cuserid="user_id";
        //   public static final String cemail="email";

    public static final String phone_contacts="phoneContacts";
    public static final String pcname="pname";
    public static final String pcphone="pphone";

    public static final String settledbills="settledbills_table";
    public static final String s1="payment";
    public static final String s2="amount";
    public static final String s3="groupNm";
    public static final String s4="per";
    public static final String s5="per1";
    public static final String s6="settled_id";
    public static final String s7="per1phone";
    public static final String s8="per2phone";

    public static final String billtable="bill";
    public static final String col0="groupnm";
    public static final String col1="description";
    public static final String col2="billcontacts";
    public static final String col3="bill";
    public static final String col4="owes";
    public static final String col5="billpaid";
    public static final String col6="divide";
    public static final String col7="amount";
    public static final String col8="total";
    public static final String col9="pn";
    public static final String col10="serverid";

    public static final String balancetable="balance";
    public static final String c0="group1";
    public static final String c1="contact";
    public static final String c2="pay";
    public static final String c3="payto";
    public static final String c4="get";
    public static final String c5="getfrom";
    public static final String c6="phone1";
    public static final String c7="phone2";

    public static final String bill_idtable="billid";
    public static final String c="id";

        public DatabaseHelper(Context context) {
            super(context, databaseName, null,3);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //db.execSQL("create table "+grouptable+" ("+column1+" TEXT,"+column2+" TEXT,"+column3+" TEXT)");
            db.execSQL("create table "+contacts_table+"(name TEXT,phone TEXT,image INTEGER,group_name TEXT,user_id TEXT)");
            db.execSQL("create table "+grouptable+"(acgroupname TEXT,groupname TEXT,type TEXT)");

            db.execSQL("create table "+phone_contacts+"(pname TEXT,pphone TEXT)");
            db.execSQL("create table "+billtable+"(groupnm TEXT,description TEXT,billcontacts TEXT,bill TEXT,owes TEXT,billpaid INTEGER,divide INTEGER,amount INTEGER,total INTEGER,pn TEXT,serverid TEXT)");
            db.execSQL("create table "+balancetable+"(group1 TEXT,contact TEXT,pay INTEGER,payto TEXT,get INTEGER,getfrom TEXT,phone1 TEXT,phone2 TEXT)");
            db.execSQL("create table "+bill_idtable+"(id TEXT)");
            db.execSQL("create table "+settledbills+"(payment TEXT,amount INTEGER,groupNm TEXT,per TEXT,per1 TEXT,settled_id TEXT,per1phone TEXT,per2phone TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+grouptable);
            db.execSQL("DROP TABLE IF EXISTS "+contacts_table);
            db.execSQL("DROP TABLE IF EXISTS "+billtable);
            db.execSQL("DROP TABLE IF EXISTS "+balancetable);
            db.execSQL("DROP TABLE IF EXISTS "+bill_idtable);
            db.execSQL("DROP TABLE IF EXISTS "+phone_contacts);
            db.execSQL("DROP  TABLE IF EXISTS "+settledbills);
            onCreate(db);
        }

    public void insertsettledbill(String a,String b,String c,String d,String e,String f,String g,String h){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(s1,a);
        contentValues.put(s2,Integer.parseInt(b));
        contentValues.put(s3,c);
        contentValues.put(s4,d);
        contentValues.put(s5,e);
        contentValues.put(s6,f);
        contentValues.put(s7,g);
        contentValues.put(s8,h);
        db.insert(settledbills,null,contentValues);
    }

        public void insertPhoneContacts(String name,String phone){
            SQLiteDatabase dbg=this.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(pcname,name);
            contentValues.put(pcphone,phone);
            dbg.insert(phone_contacts,null,contentValues);

        }
        public void insertDatagroup(String groupname,String type,String actualgroupname)
        {
            SQLiteDatabase dbg=this.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(column1,actualgroupname);
            contentValues.put(column2,groupname);
            contentValues.put(column3,type);
            dbg.insert(grouptable,null,contentValues);

        }
        public void insertid(String id){
            SQLiteDatabase dbc=this.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put(c,id);
            dbc.insert(bill_idtable,null,values);
        }
        public void updateid(String id){
            SQLiteDatabase dbc=this.getWritableDatabase();
            SQLiteDatabase d=this.getReadableDatabase();
            ContentValues values=new ContentValues();
            values.put(c,id);
            Cursor cursor= d.query(bill_idtable,null,null,null,null,null,null);
            cursor.moveToFirst();
            String []args={cursor.getString(cursor.getColumnIndex(c))};
            dbc.update(bill_idtable,values,c+"=?",args);
        }
        public void insertDatacontact(String name,String phone,int image,String groupName)
        {
            if(!check(name,phone,image,groupName)){
                SQLiteDatabase dbc=this.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put(cname,name);
                values.put(cphone,phone);
                values.put(cimage,image);
                values.put(cgroupName,"");
                dbc.insert(contacts_table,null,values);
            }

        }
    public void insertDatacontacts(String name,String phone,int image,String groupName,String userid)
    {
        if(!check(name,phone,image,groupName)){
            SQLiteDatabase dbc=this.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put(cname,name);
            values.put(cphone,phone);
            values.put(cimage,image);
            values.put(cgroupName,groupName);
            values.put(cuserid,userid);
            dbc.insert(contacts_table,null,values);
        }

    }
        public void insertDataContactGroup(String groupName){
            SQLiteDatabase db=this.getWritableDatabase();
            //String Table_Name=contacts_table;
            //String selectQuery = "SELECT  * FROM " + Table_Name+" WHERE "+cgroupName+"=?";
            String[] args={""};
            //Cursor cursor = db.rawQuery(selectQuery,args);
            //cursor.moveToFirst();
            ContentValues contentValues=new ContentValues();
            contentValues.put(cgroupName,groupName);
            db.update(contacts_table,contentValues,cgroupName+"=?",args);

        }
        public Cursor readDataContact(SQLiteDatabase db,String gn){
            String Table_Name=contacts_table;
            String selectQuery = "SELECT  * FROM " + Table_Name+" WHERE "+cgroupName+"=?"+" OR "+cgroupName+"=?";
            String[] args={"",gn};
            Cursor cursor = db.rawQuery(selectQuery,args);
            return cursor;
        }
    public Cursor readGroupContact(SQLiteDatabase db){
        String Table_Name=grouptable;
        //String selectQuery = "SELECT  * FROM " + Table_Name;
        Cursor cursor = db.query(grouptable,null,null,null,null,null,null);
        return cursor;
    }
        public boolean check(String name,String phone,int image,String groupName){
            SQLiteDatabase dbc=this.getReadableDatabase();
            String []columns={cname,cphone,cimage,cgroupName};
            String []selectionArgs={name,groupName};
            Cursor cursor=dbc.query(contacts_table,columns,cname+"=?"+" AND "+cgroupName+"=?",selectionArgs,null,null,null);
            if(cursor.moveToFirst()){
                return true;
            }else {
                return false;
            }
        }



    public void insertbill(String groupname,String desp,String contact,String bill,String owe,int paid,int divide,int amount,int total,String pn,String serverid){
        SQLiteDatabase db;
        db=this.getWritableDatabase();
        //billtable=groupname;
        //db.execSQL("DROP  TABLE IF EXISTS "+billtable);
        //db.execSQL("DROP TABLE IF EXISTS "+grouptable);
        //db.execSQL("DROP TABLE IF EXISTS "+contacts_table);
        //onCreate(db);
        //db.execSQL("create table "+billtable+"(description TEXT,billcontacts TEXT,bill TEXT,owes TEXT,billpaid INTEGER,divide INTEGER,amount INTEGER,total INTEGER)");
        //createBill(billtable);
        ContentValues contentValues=new ContentValues();
        contentValues.put(col0,groupname);
        contentValues.put(col1,desp);
        contentValues.put(col2,contact);
        contentValues.put(col3,bill);
        contentValues.put(col4,owe);
        contentValues.put(col5,paid);
        contentValues.put(col6,divide);
        contentValues.put(col7,amount);
        contentValues.put(col8,total);
        contentValues.put(col9,pn);
        contentValues.put(col10,serverid);
        db.insert(billtable,null,contentValues);
        //db.close();
    }
    public void insertbalance(String table,String contact,int pay, String payto, int get, String getfrom,String ph1,String ph2){
        SQLiteDatabase db=this.getWritableDatabase();
        //balancetable=table;
        //db.execSQL("DROP TABLE IF EXISTS "+balancetable);
        //db.execSQL("create table "+balancetable+"(contact TEXT,pay INTEGER,payto TEXT,get INTEGER,getfrom TEXT)");
        ContentValues contentValues=new ContentValues();
        contentValues.put(c0,table);
        contentValues.put(c1,contact);
        contentValues.put(c2,pay);
        contentValues.put(c3,payto);
        contentValues.put(c4,get);
        contentValues.put(c5,getfrom);
        contentValues.put(c6,ph1);
        contentValues.put(c7,ph2);
        db.insert(balancetable,null,contentValues);
        //db.close();
    }
    }

