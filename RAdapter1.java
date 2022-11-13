package com.example.android.billsplittingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.os.Build.VERSION_CODES.O;

/**
 * Created by praty on 05-06-2018.
 */

public class RAdapter1 extends RecyclerView.Adapter<RAdapter1.MyViewHolder> {
    public List<RList1> group_list= new ArrayList<RList1>();
    Context context;
    RList1 selectedItems;
    DatabaseHelper mydb;
    JSONObject jsonObject;
    BaseActivity baseActivity=new BaseActivity();
    GlobalVariables appState;
    private ActionMode actionMode;
    RecyclerView recyclerView;


    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.groupeditdelete,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.delete_group:
                    Log.e("ddddddd","ddddddddddddddddddddddd");
                    RList1 info=selectedItems;
                    mydb=new DatabaseHelper(context);

                    jsonObject=new JSONObject();
                    try{
                        //Log.e("uuu",baseActivity.sharedPreferences.getString("user_id","id"));
                        Log.e("uuu","uuuuuuuuuuuuuuuuuuuuuuuuuu");

                        jsonObject.put("user_id",info.getUser_id());
                        jsonObject.put("group_id",info.getGroup_id());
                        Log.e("uuu","uuuuuuuuuuuuuuuuuuuuuuuuuu");
                        if(appState.getNetworkCheck()){
                            Log.e("uuu","uuuuuuuuuuuuuuuuuuuuuuuuuu");
                            sendData(jsonObject,info);
                        }else {
                            Toast.makeText(context, " ", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    mode.finish();
                    return true;
                case R.id.edit_group:
                    RList1 inf=selectedItems;
                    int posi=group_list.indexOf(inf);
                    mydb=new DatabaseHelper(context);
                    SQLiteDatabase d=mydb.getReadableDatabase();
                    String group=inf.getGroup_id();
                    Intent intent=new Intent(context,CreateGroup.class);
                    String []arg={group};
                    Log.e("LLLLLLLLL",group);
                    Cursor cursor=d.query(mydb.grouptable,null,mydb.column2+"=?",arg,null,null,null,null);
                    cursor.moveToFirst();
                    intent.putExtra("gnm",group);
                    intent.putExtra("group_name",group);
                    intent.putExtra("GROUP",cursor.getString(cursor.getColumnIndex(mydb.column1)));
                    intent.putExtra("group_type",cursor.getString(cursor.getColumnIndex(mydb.column3)));
                    context.startActivity(intent);
                    mode.finish();
                    return true;
                default:
                    return false;
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            notifyDataSetChanged();
            actionMode=null;


        }
    };


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName;
        public ImageView groupIcon;
        public CardView cardView;
        //public RelativeLayout relativeLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            groupName=(TextView) itemView.findViewById(R.id.group_name);
            groupIcon=(ImageView) itemView.findViewById(R.id.group_icon);
            cardView=(CardView)itemView.findViewById(R.id.mcv);
            //relativeLayout=(RelativeLayout)itemView.findViewById(R.id.rl);
        }
    }
    public RAdapter1(List<RList1> group_list,Context context) {
        this.context=context;
        this.group_list=group_list;
        appState = (GlobalVariables)context.getApplicationContext();
    }

    @Override
    public RAdapter1.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view1,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RAdapter1.MyViewHolder holder, int position) {
        final RList1 rList1 = group_list.get(position);
        holder.groupName.setText(rList1.getGroup_name());
        holder.groupIcon.setImageResource(rList1.getImageType());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(actionMode!=null){
                    actionMode.finish();
                    actionMode=null;
                    return;
                }
                Intent intent=new Intent(holder.itemView.getContext(),GroupActivity.class);
                intent.putExtra("group_name",rList1.getGroup_id());
                intent.putExtra("GROUP",rList1.getGroup_name());
                intent.putExtra("icon",rList1.getType());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(actionMode!=null){
                    return false;
                }
                holder.cardView.setBackgroundResource(R.color.colorPrimary);
                selectedItems=rList1;
                context=holder.itemView.getContext();
                if(context instanceof MainActivity){
                    actionMode=((MainActivity)context).startSupportActionMode(actionModeCallbacks);

                }
                //removeItem(info);
                return true;
            }
        });
        if(actionMode==null){
            holder.cardView.setBackgroundResource(R.color.br);
        }

    }

    @Override
    public int getItemCount() {
        return group_list.size();
    }

    void sendData(JSONObject jsonObject,final RList1 info){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="http://phpstack-127383-542740.cloudwaysapps.com/api/v1/deletegroup";
        Utils.showProgressDialog(context,"deleting ..........");
        Utils.showProgress();
        Log.e("input",jsonObject.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Bill_res",response.toString());
                        if(response!=null){
                            Utils.dissmisProgress();
                        }

                        String success,mssg;
                        try {
                            success = response.getString("success");
                            mssg = response.getString("message");

                            if(success.equals("true")){
                                SQLiteDatabase db=mydb.getReadableDatabase();
                                String []args={info.getGroup_id()};
                                db.delete("groups","groupname"+"=?",args);
                                db.delete("contacts","group_name"+"=?",args);
                                db.delete(mydb.balancetable,mydb.c0+"=?",args);
                                db.delete(mydb.billtable,mydb.col0+"=?",args);
                                db.delete(mydb.settledbills,mydb.s3+"=?",args);
                                int position=group_list.indexOf(info);
                                group_list.remove(position);
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
