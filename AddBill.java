package com.example.android.billsplittingapp;

import android.content.ContentValues;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.R.attr.id;
import static android.R.attr.key;
import static android.R.attr.max;

import static android.R.attr.y;
import static com.example.android.billsplittingapp.DatabaseHelper.col0;
import static com.example.android.billsplittingapp.DatabaseHelper.col1;
import static com.example.android.billsplittingapp.DatabaseHelper.col3;
import static com.example.android.billsplittingapp.DatabaseHelper.phone_contacts;
import static com.example.android.billsplittingapp.DatabaseHelper.s1;
import static com.example.android.billsplittingapp.DatabaseHelper.s2;
import static com.example.android.billsplittingapp.DatabaseHelper.s5;
import static com.example.android.billsplittingapp.R.id.food;
import static com.example.android.billsplittingapp.R.id.phone;
import static com.example.android.billsplittingapp.R.layout.contacts;
import static java.nio.file.Paths.get;

public class AddBill extends BaseActivity {
    TextView paidBy;
    TextView paidTo;
    String gn;
    String type;
    String group;
    int count;
    ArrayList<String> contacts=new ArrayList<String>();
    ArrayList<String> contactid=new ArrayList<String>();
    DatabaseHelper mydb;
    boolean b,b1,m=false,m1=false;
    int person;
    String []etr;
    String []etStr;
    ArrayList<String> seletedItems;
    EditText editText;
    EditText editText1;
    HashMap<String,Integer> positive;
    List<String>posphone;
    List<String>negphone;
    HashMap<String,Integer> negative;
    List<EditText> e=new ArrayList<>();
    List<EditText> e2=new ArrayList<>();
    JSONObject jsonObject;
    List<String>col0=new ArrayList<>();
    List<String>col1=new ArrayList<>();
    List<String>col2=new ArrayList<>();
    List<String>col3=new ArrayList<>();
    List<String>col4=new ArrayList<>();
    List<String>col5=new ArrayList<>();
    List<String>col6=new ArrayList<>();
    List<String>col7=new ArrayList<>();
    List<String>col8=new ArrayList<>();
    ArrayList<String> phoneno=new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editText=(EditText)findViewById(R.id.desp);
        editText1=(EditText)findViewById(R.id.money);
        paidBy=(TextView)findViewById(R.id.who);
        paidTo=(TextView)findViewById(R.id.to);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        gn=bundle.getString("group_name");
        type=bundle.getString("icon");
        group=bundle.getString("GROUP");
        mydb=new DatabaseHelper(this);
        SQLiteDatabase db=mydb.getReadableDatabase();
        String []columns={mydb.cname,mydb.cuserid,mydb.cphone};
        String []args={gn};
        Log.e("sgfhbgj",gn+"++++++++++++++++++++++++++++++");
        Cursor cursor=db.query(mydb.contacts_table,columns,mydb.cgroupName+"=?",args,null,null,null);
        cursor.moveToFirst();
        final int length=cursor.getCount();
        //contacts=new String[length+1];
        int i=0;
        do{
            contacts.add(cursor.getString(cursor.getColumnIndex(mydb.cname)));
            contactid.add((cursor.getString(cursor.getColumnIndex(mydb.cuserid))));
            phoneno.add(cursor.getString(cursor.getColumnIndex(mydb.cphone)));
            i++;
        }while (cursor.moveToNext());
        contacts.add("multiple people");
        final String []s=new String[length+1];
        int p=0;
        while(p<contacts.size())
        {
            s[p]=contacts.get(p);
            p=p+1;
        }
        //etStr=new String[contacts.size()-1];
        //for(int z=0;z<contacts.size()-1;z++){
        //    etStr[z]="@";
        //}
       // etStr[0]=String.valueOf(editText1.getText());
        paidBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s2=editText1.getText().toString();
                if(TextUtils.isEmpty(s2)){
                    editText1.setError("please enter amount");
                    editText1.requestFocus();
                    return;
                }
                final CharSequence colors[] = s;//new CharSequence[] {"red", "green", "blue", "black"};

                AlertDialog.Builder builder = new AlertDialog.Builder(AddBill.this);
                builder.setTitle("Pick who paid");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // the user clicked on colors[which]
                        if(colors[which].equals("multiple people")){
                            b=true;
                            m=true;
                            etStr=new String[length];
                            for(int z=0;z<length;z++){
                                etStr[z]="@";
                            }
                            paidBy.setText("Multiple People");
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddBill.this);

                            ScrollView scrollView=new ScrollView(AddBill.this);
                            ScrollView.LayoutParams param=new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT,ScrollView.LayoutParams.MATCH_PARENT);
                            LinearLayout layout = new LinearLayout(AddBill.this);
                            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layout.setOrientation(LinearLayout.VERTICAL);
                            layout.setLayoutParams(parms);

                            layout.setGravity(Gravity.CLIP_VERTICAL);
                            layout.setPadding(2, 2, 2, 2);
                            int t=length,k=0;
                            e=new ArrayList<EditText>();
                            while(k<t){
                                LinearLayout child=new LinearLayout(AddBill.this);
                                LinearLayout.LayoutParams parms1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                child.setOrientation(LinearLayout.HORIZONTAL);
                                child.setLayoutParams(parms1);
                                TextView tv1 = new TextView(AddBill.this);
                                tv1.setText(contacts.get(k));
                                EditText et = new EditText(AddBill.this);
                                //et.setId(k);
                                //etStr[k] = et.getText().toString();
                                LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(800, LinearLayout.LayoutParams.WRAP_CONTENT);
                                tv1Params.bottomMargin = 5;
                                child.addView(tv1,tv1Params);
                                LinearLayout.LayoutParams ev1Params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                //ev1Params.gravity=Gravity.RIGHT;
                                child.addView(et, ev1Params);
                                layout.addView(child);
                                k++;
                                e.add(et);

                            }
                            scrollView.addView(layout);
                            alertDialogBuilder.setView(scrollView);
                            alertDialogBuilder.setTitle("Enter the amount paid by each person");
                            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int sum = 0;
                                    for (EditText ex : e) {
                                        if (!ex.getText().toString().equals("")) {
                                            sum = sum + Integer.parseInt(ex.getText().toString());
                                        }
                                    }
                                    if (sum != Integer.parseInt(editText1.getText().toString())) {
                                        AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(AddBill.this);
                                        alertDialogBuilder1.setTitle("Error");
                                        alertDialogBuilder1.setMessage("The amount you entered didnot match with total amount");
                                        alertDialogBuilder1.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                        AlertDialog alertDialog = alertDialogBuilder1.create();
                                        alertDialog.show();
                                    }
                                }
                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();

                            try {
                                alertDialog.show();
                            } catch (Exception e) {
                                // WindowManager$BadTokenException will be caught and the app would
                                // not display the 'Force Close' message
                                e.printStackTrace();
                            }


                        }
                        else{
                            etStr=new String[length];
                            for(int z=0;z<length;z++){
                                etStr[z]="@";
                            }
                            person=which;
                            //etStr[which]= String.valueOf(editText1.getText());
                            b=false;
                            paidBy.setText(contacts.get(which));
                            m=true;
                        }
                    }
                });
                builder.show();

            }
        });
        /*b1=true;
        seletedItems=new ArrayList<String>();
        for(int o=0;o<contacts.size()-1;o++){
            seletedItems.add(String.valueOf(contacts.get(o)));
        }*/
        paidTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s2=editText1.getText().toString();
                if(TextUtils.isEmpty(s2)){
                    editText1.setError("please enter amount");
                    editText1.requestFocus();
                    return;
                }
                final CharSequence []eq={"Equally","Unequally"};
                AlertDialog.Builder adBuilder=new AlertDialog.Builder(AddBill.this);
                adBuilder.setTitle("Select how to divide");
                adBuilder.setItems(eq, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(eq[i].equals("Equally")){
                            b1=true;
                            m1=true;
// arraylist to keep the selected items
                            seletedItems=new ArrayList<String>();
                            boolean[] checkedValues = new boolean[length];

                            String []con=new String[length];
                            for(int j=0;j<length;j++){
                                con[j]=contacts.get(j);
                            }
                            final CharSequence[] items = con;
                            for(int j=0;j<length;j++){
                                checkedValues[j]=true;
                                seletedItems.add(String.valueOf(items[j]));
                            }

                            AlertDialog dialog = new AlertDialog.Builder(AddBill.this)
                                    .setTitle("Choose persons")
                                    .setMultiChoiceItems(items, checkedValues, new DialogInterface.OnMultiChoiceClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {

                                            if (isChecked) {
                                                // If the user checked the item, add it to the selected items
                                                seletedItems.add(String.valueOf(items[indexSelected]));
                                            } else if (seletedItems.contains(items[indexSelected])) {
                                                // Else, if the item is already in the array, remove it
                                                seletedItems.remove(items[Integer.valueOf(indexSelected)]);
                                            }
                                        }
                                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {

                                            //  Your code when user clicked on OK
                                            //  You can write the code  to save the selected item here
                                        }
                                    }).create();
                            dialog.show();
                        }else{
                            b1=false;
                            m1=true;
                            paidTo.setText("UnEqually");
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddBill.this);

                            ScrollView scrollView=new ScrollView(AddBill.this);
                            ScrollView.LayoutParams param=new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT,ScrollView.LayoutParams.MATCH_PARENT);
                            LinearLayout layout = new LinearLayout(AddBill.this);
                            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layout.setOrientation(LinearLayout.VERTICAL);
                            layout.setLayoutParams(parms);

                            layout.setGravity(Gravity.CLIP_VERTICAL);
                            layout.setPadding(2, 2, 2, 2);
                            int t=length,k=0;
                            etr=new String[t];
                            for(int g=0;g<t;g++){
                                etr[g]="@";
                            }
                            e2=new ArrayList<EditText>();
                            while(k<t){
                                LinearLayout child=new LinearLayout(AddBill.this);
                                LinearLayout.LayoutParams parms1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                child.setOrientation(LinearLayout.HORIZONTAL);
                                child.setLayoutParams(parms1);
                                TextView tv1 = new TextView(AddBill.this);
                                tv1.setText(contacts.get(k));
                                EditText et = new EditText(AddBill.this);
                                //etr[k] = et.getText().toString();
                                LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(800, LinearLayout.LayoutParams.WRAP_CONTENT);
                                tv1Params.bottomMargin = 5;
                                child.addView(tv1,tv1Params);
                                child.addView(et, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                layout.addView(child);
                                e2.add(et);
                                k++;

                            }
                            scrollView.addView(layout);
                            alertDialogBuilder.setView(scrollView);
                            alertDialogBuilder.setTitle("Enter the amount paid by each person");
                            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int sum=0;
                                    for (EditText ex : e2) {
                                        if(!ex.getText().toString().equals("")){
                                            sum=sum+Integer.parseInt(ex.getText().toString());
                                        }
                                    }
                                    if(sum!=Integer.parseInt(editText1.getText().toString())){
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddBill.this);
                                        alertDialogBuilder.setTitle("Error");
                                        alertDialogBuilder.setMessage("The amount you entered didnot match with total amount");
                                        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                    }

                                }
                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();

                            try {
                                alertDialog.show();
                            } catch (Exception e) {
                                // WindowManager$BadTokenException will be caught and the app would
                                // not display the 'Force Close' message
                                e.printStackTrace();
                            }
                        }
                    }
                });
                adBuilder.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_group,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }
            String bill;
        boolean t=false;
            SQLiteDatabase d=mydb.getReadableDatabase();
        Cursor cu4=d.query(mydb.billtable,null,mydb.col0+"=?",new String[]{gn},null,null,null);
        if(cu4.moveToFirst()){
            do{
                if(cu4.getString(cu4.getColumnIndex(mydb.col1)).equals(editText.getText().toString())){
                    t=true;
                    if(t==true){
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddBill.this);
                        builder1.setTitle("Error");
                        builder1.setMessage("bill name alreday exists in the group please give another name");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                        return true;
                    }
                }
            }while (cu4.moveToNext());
        }


            Cursor curs=d.query(mydb.bill_idtable,null,null,null,null,null,null);
            if(curs.moveToFirst()){
                String id=curs.getString(curs.getColumnIndex(mydb.c));
                int q=Integer.parseInt(id);
                q=q+1;
                mydb.updateid(String.valueOf(q));
            }else{
                mydb.insertid("0");
            }
            curs=d.query(mydb.bill_idtable,null,null,null,null,null,null);
            curs.moveToFirst();
            bill=curs.getString(curs.getColumnIndex(mydb.c));

        Log.w("myApp", "no network???????????????????????????????????????????????????/ttttttttttttt");
        //Async as=new Async();
        String s1=editText.getText().toString();
        String s2=editText1.getText().toString();
        if(TextUtils.isEmpty(s1)){
            editText.setError("please enter description");
            editText.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(s2)){
            editText1.setError("please enter amount");
            editText1.requestFocus();
            return false;
        }
        if(m==false||m1==false){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(AddBill.this);
            builder1.setTitle("Error");
            builder1.setMessage("You did not select who paid OR to split among who");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }else{
            if(b==true){
                int u=0;
                for (EditText ex : e) {
                    etStr[u]=ex.getText().toString();
                    u++;
                }
            }else{
                etStr[person]=editText1.getText().toString();
            }
            if(b1==false){
                int u=0;
                for (EditText ex : e2) {
                    etr[u]=ex.getText().toString();
                    u++;
                }
            }
        }
        int sum=0;
        if(b==true){
            sum=0;
            for (EditText ex : e) {
                if(!ex.getText().toString().equals("")){
                    sum=sum+Integer.parseInt(ex.getText().toString());
                }
            }
            if(sum!=Integer.parseInt(editText1.getText().toString())){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddBill.this);
                alertDialogBuilder.setTitle("Error");
                alertDialogBuilder.setMessage("The amount you entered didnot match with total amount");
                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            }
        }

        if(b1==false){
            sum=0;
            for (EditText ex : e2) {
                if(!ex.getText().toString().equals("")){
                    sum=sum+Integer.parseInt(ex.getText().toString());
                }
            }
            if(sum!=Integer.parseInt(editText1.getText().toString())){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddBill.this);
                alertDialogBuilder.setTitle("Error");
                alertDialogBuilder.setMessage("The amount you entered didnot match with total amount in split");
                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            }

        }else{
            if(seletedItems.isEmpty()){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddBill.this);
                alertDialogBuilder.setTitle("Error");
                alertDialogBuilder.setMessage("User unselected all the persons in equal division");
                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            }
        }

//        Async as=new Async();
//        as.execute(s2);
//        while(as.getStatus()!=AsyncTask.Status.FINISHED){

//        }
            String edit1=s2;//strings[0];
            String edit=s1;//strings[1];
        count=0;
            int i=0,j=0;
            int[] res=new int[contacts.size()-1];
            int total=Integer.parseInt(edit1.toString());
            int[] a=new int[contacts.size()-1];
            String []cont=new String[contacts.size()-1];
            String []contid=new String[contacts.size()-1];
            while(b1==true&&i<(contacts.size()-1))
            {
                int r=total/seletedItems.size();
                if((!etStr[i].equals("@")&&!etStr.equals(""))&&!seletedItems.contains(contacts.get(i)))
                {
                    cont[i]=contacts.get(i);
                    contid[i]=contactid.get(i);
                    a[i]=Integer.parseInt(etStr[i]);
                    res[i]=Integer.parseInt(etStr[i]);
                }
                else if(seletedItems.contains(contacts.get(i))){
                    cont[contacts.indexOf(seletedItems.get(j))]= String.valueOf(seletedItems.get(j));
                    contid[contacts.indexOf(seletedItems.get(j))]=contactid.get(contacts.indexOf(seletedItems.get(j)));
                    if(etStr[contacts.indexOf(seletedItems.get(j))].equals("@")||etStr[contacts.indexOf(seletedItems.get(j))].equals("")){
                        a[i]=0;
                    }
                    else{
                        a[i]=Integer.parseInt(etStr[contacts.indexOf(seletedItems.get(j))]);
                    }

                    res[i]=a[i]-r;
                    j++;
                }
                else{
                    cont[i]="#";
                    contid[i]="#";
                }
                i++;
            }
            int n=0;
            i=0;
            String owe;
            while(b1==true&&n<contacts.size()-1) {

                if(!cont[i].equals("#")){
                    if(res[i]<0)
                    {
                        owe="pay";
                    }
                    else
                        owe="get";
//                    jsonObject = new JSONObject();
//                    try {
//                        jsonObject.put("group_name",gn);
//                        jsonObject.put("bill_description",edit.toString());
//                        jsonObject.put("contact_name",cont[i]);
//                        jsonObject.put("bill_id",bill);
//                        jsonObject.put("pay_or_get",owe);
//                        jsonObject.put("amount_paid",a[i]);
//                        jsonObject.put("divide_equally_or_unequally",0);
//                        jsonObject.put("amount_to_get_or_pay",res[i]);
//                        jsonObject.put("toatl_bill_amount",total);
//                        if(appState.getNetworkCheck()){
//                            sendData(jsonObject);
//                        }else {
//                            Toast.makeText(AddBill.this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
//                        }
//
//                    }catch(Exception e){
//                        e.printStackTrace();
//                    }
                    col0.add(gn);
                    col1.add(edit.toString());
                    col2.add(contid[i]);
                    col3.add(bill);
                    col4.add(owe);
                    col5.add(String.valueOf(a[i]));
                    col6.add(String.valueOf(0));
                    col7.add(String.valueOf(res[i]));
                    col8.add(String.valueOf(total));
                    count=count+1;
                    //mydb.insertbill(gn, edit.toString(),cont[i], bill,owe, a[i], 0,res[i],total);

                }
                i++;
                n++;
            }
            n=0;
            i=0;
            while(b1==false&&n<contacts.size()-1){
                int y;
                if(!etr[i].equals("@")&&!etr[i].equals("")){
                    if(etStr[i].equals("@")||etStr[i].equals("")){
                        res[i]=0-Integer.parseInt(etr[i]);
                        y=0;
                    }else{
                        res[i]=Integer.parseInt(etStr[i])-Integer.parseInt(etr[i]);
                        y=Integer.parseInt(etStr[i]);
                    }

                    if(res[i]<0)
                        owe="pay";
                    else
                        owe="get";
//                    jsonObject = new JSONObject();
//                    try {
//                        jsonObject.put("group_name",gn);
//                        jsonObject.put("bill_description",edit.toString());
//                        jsonObject.put("contact_name",contacts.get(i));
//                        jsonObject.put("bill_id",bill);
//                        jsonObject.put("pay_or_get",owe);
//                        jsonObject.put("amount_paid",y);
//                        jsonObject.put("divide_equally_or_unequally",Integer.parseInt(etr[i]));
//                        jsonObject.put("amount_to_get_or_pay",res[i]);
//                        jsonObject.put("toatl_bill_amount",total);
//                        if(appState.getNetworkCheck()){
//                            sendData(jsonObject);
//                        }else {
//                            Toast.makeText(AddBill.this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
//                        }
//
//                    }catch(Exception e){
//                        e.printStackTrace();
//                    }
                    col0.add(gn);
                    col1.add(edit.toString());
                    col2.add(contactid.get(i));
                    col3.add(bill);
                    col4.add(owe);
                    col5.add(String.valueOf(y));
                    col6.add(String.valueOf(etr[i]));
                    col7.add(String.valueOf(res[i]));
                    col8.add(String.valueOf(total));
                    count=count+1;
                    //mydb.insertbill(gn, edit.toString(),contacts.get(i), bill, owe,y, Integer.parseInt(etr[i]),res[i],total);
                    Log.d("Error",gn+edit+cont[i]+bill+owe+contacts.get(i)+etr[i]+res[i]+total+"#########################################################################################3");
                }
                else{
                    if(!etStr[i].equals("@")&&!etStr[i].equals("")){
                        owe="get";
//                        jsonObject = new JSONObject();
//                        try {
//                            jsonObject.put("group_name",gn);
//                            jsonObject.put("bill_description",edit.toString());
//                            jsonObject.put("contact_name",contacts.get(i));
//                            jsonObject.put("bill_id",bill);
//                            jsonObject.put("pay_or_get",owe);
//                            jsonObject.put("amount_paid",Integer.parseInt(etStr[i]));
//                            jsonObject.put("divide_equally_or_unequally",0);
//                            jsonObject.put("amount_to_get_or_pay",Integer.parseInt(etStr[i]));
//                            jsonObject.put("toatl_bill_amount",total);
//                            if(appState.getNetworkCheck()){
//                                sendData(jsonObject);
//                            }else {
//                                Toast.makeText(AddBill.this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
//                            }
//
//                        }catch(Exception e){
//                            e.printStackTrace();
//                        }
                        col0.add(gn);
                        col1.add(edit.toString());
                        col2.add(contactid.get(i));
                        col3.add(bill);
                        col4.add(owe);
                        col5.add(String.valueOf(etStr[i]));
                        col6.add(String.valueOf(0));
                        col7.add(String.valueOf(etStr[i]));
                        col8.add(String.valueOf(total));
                        count=count+1;
                        //mydb.insertbill(gn, edit.toString(),contacts.get(i), bill, owe,Integer.parseInt(etStr[i]),0,Integer.parseInt(etStr[i]),total);

                    }
                }
                i++;
                n++;
            }
            jsonObject = new JSONObject();
                        try {
                            //JSONObject jsonObject2=new JSONObject();
                            //jsonObject2.put("user_id",contactid.get(0));
                            jsonObject.put("user_id",contactid.get(0));
                            JSONArray jsonArray=new JSONArray();
                            for(i=0;i<count;i++){
                                JSONObject jsonObject1=new JSONObject();
                                jsonObject1.put("group_id",col0.get(i));
                                jsonObject1.put("bill_name",col1.get(i));
                                jsonObject1.put("user_id",col2.get(i));
                                jsonObject1.put("contact_name",contacts.get(contactid.indexOf(col2.get(i))));
                                jsonObject1.put("phone",phoneno.get(contactid.indexOf(col2.get(i))));
                                jsonObject1.put("bill_id",col3.get(i));
                                jsonObject1.put("pay_or_get",col4.get(i));
                                jsonObject1.put("amount_paid",col5.get(i));
                                jsonObject1.put("divide_equally_or_unequally",col6.get(i));
                                jsonObject1.put("amount_to_get_or_pay",col7.get(i));
                                jsonObject1.put("total_bill_amount",col8.get(i));
                                jsonArray.put(jsonObject1);
                            }
                            jsonObject.put("bill_table",jsonArray);
                            if(appState.getNetworkCheck()){
                                sendData(jsonObject);
                            }else {
                                Toast.makeText(AddBill.this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                            }

                        }catch(Exception e){
                            e.printStackTrace();
                        }

            return true;

    }
    private class Async extends AsyncTask<String,String,String>{
        int sum;
        String s2;
        @Override
        protected String doInBackground(String... strings) {
            s2=strings[0];

            sum=0;
            for(int r=0;r<contacts.size()-1;r++){
                if(!etStr[r].equals("@")&&!etStr[r].equals("")){
                    sum=sum+Integer.parseInt(etStr[r]);
                }
            }

            if(b1==false){
                sum=0;
                for(int r=0;r<contacts.size()-1;r++){
                    if(!etr[r].equals("@")&&!etr[r].equals("")){
                        sum=sum+Integer.parseInt(etr[r]);
                    }
                }

            }
            return s2;
        }

        @Override
        protected void onPostExecute(String s) {
            if(m==false||m1==false){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(AddBill.this);
                builder1.setTitle("Error");
                builder1.setMessage("You did not select who paid OR to split among who");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            if(!s2.equals(String.valueOf(sum))){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(AddBill.this);
                builder1.setTitle("Error");
                builder1.setMessage("The sum of amount paid by multiple persons and total amount entered didnot match");
                //builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
            if(b1==false){
                if(!s2.equals(String.valueOf(sum))){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AddBill.this);
                    builder1.setTitle("Error");
                    builder1.setMessage("The sum of amount to be paid by multiple persons unequally and total amount entered didnot match");
                    //builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }

            super.onPostExecute(s);

        }
    }

    void sendData(JSONObject jsonObject){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://phpstack-127383-542740.cloudwaysapps.com/api/v1/createbill";
        Log.e("input",jsonObject.toString());
        Utils.showProgressDialog(this,"adding ..........");
        Utils.showProgress();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Bill_res",response.toString());
                        String success,mssg;
                        ArrayList<String>ids=new ArrayList<>();
                        try {
                            success = response.getString("success");
                            mssg = response.getString("message");
                            if(response!=null){
                                Utils.dissmisProgress();
                            }
                            int i;
                            if(success.equals("true")){
                                JSONObject jsonObject1=response.getJSONObject("data");
                                JSONArray jsonArray=jsonObject1.getJSONArray("data");
                                for(i=0;i<jsonArray.length();i++){
                                    //JSONObject jsonObject2=jsonArray.getJSONObject(i);
                                    ids.add(jsonArray.getString(i));
                                }
                                for(i=0;i<count;i++){
                                    mydb.insertbill(col0.get(i),col1.get(i),contacts.get(contactid.indexOf(col2.get(i))),col3.get(i),col4.get(i),Integer.parseInt(col5.get(i)),Integer.parseInt(col6.get(i)),Integer.parseInt(col7.get(i)),Integer.parseInt(col8.get(i)),phoneno.get(contactid.indexOf(col2.get(i))),ids.get(i));
                                }
                                Log.e("1111111111","?????????????????????????????????");
                                int j=0;
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
                                    String ph=phoneno.get(j);
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
                                        Toast.makeText(AddBill.this,"gdcjnhiygjfvhjfhbgfjn",Toast.LENGTH_LONG);
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
                                Log.e("555555555555","8888888888888888888888888888888");
                                Intent intent=new Intent(AddBill.this,GroupActivity.class);
                                intent.putExtra("group_name",gn);
                                intent.putExtra("icon",type);
                                intent.putExtra("GROUP",group);
                                startActivity(intent);
                                finish();

                                Toast.makeText(AddBill.this, mssg, Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(AddBill.this, mssg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, errorListener);
        queue.add(jsObjRequest);

    }

}