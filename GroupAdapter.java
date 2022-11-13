package com.example.android.billsplittingapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.support.v7.view.ActionMode;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.key;
import static android.R.string.no;
import static android.media.CamcorderProfile.get;
import static com.example.android.billsplittingapp.DatabaseHelper.s2;
import static com.example.android.billsplittingapp.R.id.amount;
import static com.example.android.billsplittingapp.R.id.involve;
import static com.example.android.billsplittingapp.R.id.paid;

/**
 * Created by praty on 08-06-2018.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ExpenseViewHolder> implements Filterable{
    public List<ExpensesRV> group_list= new ArrayList<ExpensesRV>();
    private List<ExpensesRV> mFilteredList;
    Context context;
    String billname;
    HashMap<String,Integer> positive;
    HashMap<String,Integer> negative;
    private DatabaseHelper databaseHelper;
    ArrayList<String> contacts=new ArrayList<String>();
    ArrayList<String> phoneno=new ArrayList<String>();
    private DatabaseHelper mydb;
    String descrip,t1,t2;
    private SQLiteDatabase db;
    String gn;
    String group12;
    JSONObject jsonObject;
    BaseActivity baseActivity=new BaseActivity();
    List<String>posphone;
    List<String>negphone;
    GlobalVariables appState;

    ExpensesRV item1;
    ActionMode am;

    ActionMode.Callback acallback=new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.billdelete,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            databaseHelper=new DatabaseHelper(context);
            mydb=new DatabaseHelper(context);
            db=databaseHelper.getWritableDatabase();
            switch(item.getItemId()){
                case R.id.delete_bill:
                    int position=mFilteredList.indexOf(item1);
                    //group_list.remove(position);

                    String []args={descrip,group12,billname};
                    Cursor cursor1=db.query(mydb.billtable,null,mydb.col3+"=?"+" AND "+mydb.col0+"=?"+" AND "+mydb.col1+"=?",args,null,null,null);
                    cursor1.moveToFirst();
                    gn=group12;
                    jsonObject = new JSONObject();
                    try {
                        //jsonObject.put("delete_bill","delete");
                        jsonObject.put("user_id",item1.getUser_id());
                        //jsonObject.put("group_name",group12);
                        JSONArray jsonArray=new JSONArray();
                        do{
                            //JSONObject jsonObject5=new JSONObject();
                            //jsonObject5.put("id",cursor1.getString(cursor1.getColumnIndex(mydb.col10)));
                            jsonArray.put(cursor1.getString(cursor1.getColumnIndex(mydb.col10)));
                        }while (cursor1.moveToNext());
                        jsonObject.put("bill_ids",jsonArray);
                        //jsonObject.put("bill_description",billname);
                        // jsonObject.put("contact_name",cont[i]);
                        //jsonObject.put("bill_id",descrip);
//     jsonObject.put("pay_or_get",owe);
//     jsonObject.put("amount_paid",a[i]);
//     jsonObject.put("divide_equally_or_unequally","0");
//     jsonObject.put("amount_to_get_or_pay",res[i]);
//     jsonObject.put("toatl_bill_amount",total);
                        if(appState.getNetworkCheck()){
                            sendData(jsonObject,position);
                        }else {
                            Toast.makeText(context, "check internet", Toast.LENGTH_SHORT).show();
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            notifyDataSetChanged();
            am=null;

        }
    };

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView1;
        TextView textView2;
        TextView textView3;
        CardView cardView;
        public ExpenseViewHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.desp);
            textView1=(TextView)itemView.findViewById(R.id.paid);
            textView2=(TextView)itemView.findViewById(R.id.involve);
            textView3=(TextView)itemView.findViewById(R.id.amount);
            cardView=(CardView)itemView.findViewById(R.id.gcv);
        }
    }
    public GroupAdapter(List<ExpensesRV> group_list, Context context){
        this.group_list=group_list;
        this.context=context;
        this.mFilteredList=group_list;
        appState = (GlobalVariables)context.getApplicationContext();
    }

    @Override
    public GroupAdapter.ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_group,parent,false);
        return new GroupAdapter.ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GroupAdapter.ExpenseViewHolder holder, int position) {
        final ExpensesRV expensesRV = mFilteredList.get(position);
        holder.textView.setText(expensesRV.getT());
        holder.textView1.setText(expensesRV.getT1());
        holder.textView2.setText(expensesRV.getT2());
        holder.textView3.setText(expensesRV.getT3());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(am!=null){
                    am.finish();
                    am=null;
                    return;
                }
                Intent intent=new Intent(context,Expense.class);
                intent.putExtra("ID",expensesRV.getId());
                intent.putExtra("group",expensesRV.getGroup());
                intent.putExtra("bill",expensesRV.getT());
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                descrip=expensesRV.getId();
                billname=expensesRV.getT();
                group12=expensesRV.getGroup();
                if(am!=null){
                    return false;
                }
                item1=expensesRV;
                holder.cardView.setBackgroundResource(R.color.colorPrimary);
                Context context=holder.itemView.getContext();
                if(context instanceof GroupActivity){
                    am=((GroupActivity) context).startSupportActionMode(acallback);
                }
                return true;
            }
        });
        if(am==null){
            holder.cardView.setBackgroundResource(R.color.br);
        }
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = group_list;
                } else {

                    List<ExpensesRV> filteredList = new ArrayList<>();

                    for (ExpensesRV androidVersion : group_list) {

                        if (androidVersion.getT().toLowerCase().contains(charString) ) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (List<ExpensesRV>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    private class Async extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String []columns={mydb.cname,mydb.cphone};
            String []args2={gn};
            Cursor cursor2=db.query(mydb.contacts_table,columns,mydb.cgroupName+"=?",args2,null,null,null);
            cursor2.moveToFirst();
            int i=0;
            do{
                contacts.add(cursor2.getString(cursor2.getColumnIndex(mydb.cname)));
                phoneno.add(cursor2.getString(cursor2.getColumnIndex(mydb.cphone)));
                i++;
            }while (cursor2.moveToNext());
            contacts.add("multiple people");
            int j=0;
            j=0;
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
//                    Toast.makeText(context,"gdcjnhiygjfvhjfhbgfjn",Toast.LENGTH_LONG);
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
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    void sendData(JSONObject jsonObject,int position){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="http://phpstack-127383-542740.cloudwaysapps.com/api/v1/deletebill";
        Utils.showProgressDialog(context,"adding ..........");
        Utils.showProgress();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("Bill_res",response.toString());
                        if(response!=null){
                            Utils.dissmisProgress();
                        }

                        String success,mssg;
                        try {
                            success = response.getString("success");
                            mssg = response.getString("message");

                            if(success.equals("true")){
                                String[]args={descrip,group12,billname};
                                int position=mFilteredList.indexOf(item1);
                                //group_list.remove(position);
                                mFilteredList.remove(position);
                                db.delete(databaseHelper.billtable,databaseHelper.col3+"=?"+" AND "+databaseHelper.col0+"=?"+" AND "+databaseHelper.col1+"=?",args);
                                Async async=new Async();
                                async.execute();
                                notifyDataSetChanged();

                                Toast.makeText(context, mssg, Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(context, mssg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, baseActivity.errorListener);
        queue.add(jsObjRequest);

    }
}
