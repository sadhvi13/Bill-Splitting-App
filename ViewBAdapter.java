package com.example.android.billsplittingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by praty on 14-06-2018.
 */

public class ViewBAdapter extends RecyclerView.Adapter<ViewBAdapter.BViewHolder> {

    public List<ViewBRV> group_list= new ArrayList<ViewBRV>();
    Context context;
    JSONObject jsonObject;
    BaseActivity baseActivity;

    public class BViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        Button b1;
        Button settleup;
        //Button reminder;
        public BViewHolder(View itemView) {
            super(itemView);
            tv=(TextView)itemView.findViewById(R.id.btv);
            //b1=(Button)itemView.findViewById(R.id.rb);
            settleup=(Button)itemView.findViewById(R.id.sb);
            //reminder=(Button)itemView.findViewById(R.id.rb);
        }
    }

    public ViewBAdapter(List<ViewBRV> group_list, Context context){
        this.group_list=group_list;
        this.context=context;
        baseActivity=new BaseActivity();
    }

    @Override
    public ViewBAdapter.BViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_viewbalance,parent,false);
        return new ViewBAdapter.BViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewBAdapter.BViewHolder holder, final int position) {
        final ViewBRV viewBRV = group_list.get(position);
        holder.tv.setText(viewBRV.getStr());
        holder.settleup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,SettleUP.class);
                intent.putExtra("group_name",viewBRV.getGroup());
                intent.putExtra("name",viewBRV.getName());
                intent.putExtra("other",viewBRV.getOther());
                intent.putExtra("amount",viewBRV.getAmount());
                intent.putExtra("go",viewBRV.getgo());
                intent.putExtra("phone1",viewBRV.getPhone());
                intent.putExtra("phone2",viewBRV.getPhone2());
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ((ViewBalances)context).finish();
            }
        });
//        holder.reminder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("user_id",baseActivity.sharedPreferences.getString("user_id","id"));
//                    jsonObject.put("message",viewBRV.getName()+viewBRV.getStr());
//                    jsonObject.put("send to",viewBRV.getOther());
//                    jsonObject.put("phone_from",viewBRV.getPhone());
//                    jsonObject.put("phone_to",viewBRV.getPhone2());
//                    if(baseActivity.appState.getNetworkCheck()){
//                        sendData(jsonObject);
//                    }else {
//                        Toast.makeText(context," " , Toast.LENGTH_SHORT).show();
//                    }
//
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return group_list.size();
    }

    void sendData(JSONObject jsonObject){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="http://www.google.com";
        Utils.showProgressDialog(context,"adding ..........");
        Utils.showProgress();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("remind",response.toString());
                        String success,mssg;
                        try {
                            success = response.getString("success");
                            mssg = response.getString("message");
                            if(response!=null){
                                Utils.dissmisProgress();
                            }
                            if(success.equals("true")){


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
