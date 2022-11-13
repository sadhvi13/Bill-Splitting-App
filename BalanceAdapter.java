package com.example.android.billsplittingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.billsplittingapp.DatabaseHelper.c;
import static com.example.android.billsplittingapp.R.id.con;

/**
 * Created by praty on 14-06-2018.
 */

public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.BalViewHolder> {
    List<BalanceRV> list=new ArrayList<>();
    Context context;
    public  class BalViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public BalViewHolder(View itemView) {
            super(itemView);
            textView=(TextView) itemView.findViewById(R.id.bal);
        }
    }

    public BalanceAdapter(List<BalanceRV> list, Context context){
        this.list=list;
        this.context=context;
    }

    @Override
    public BalanceAdapter.BalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_balance,parent,false);
        return new BalanceAdapter.BalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BalanceAdapter.BalViewHolder holder, final int position) {
        final BalanceRV balanceRV=list.get(position);
        holder.textView.setText(balanceRV.getText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ViewBalances.class);
                intent.putExtra("Name",balanceRV.getName());
                intent.putExtra("gname",balanceRV.getGroup());
                intent.putExtra("phone",balanceRV.getPhone());
               // intent.putExtra("text",balanceRV.getName());
                //intent.putExtra("posi",position);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
