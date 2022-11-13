package com.example.android.billsplittingapp;

/**
 * Created by praty on 20-06-2018.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Settledbill_Adapter extends RecyclerView.Adapter<Settledbill_Adapter.BillViewHolder>{
    List<Settled> set;
    Context context;
    public Settledbill_Adapter(List<Settled> set,Context context){
        this.set=set;
        this.context=context;
    }
    public class BillViewHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        TextView textView2;
        public BillViewHolder(View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.sb1);
            textView2=itemView.findViewById(R.id.sb9);
        }
    }

    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.settle_view,parent,false);
        return new Settledbill_Adapter.BillViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Settled sd=set.get(position);
        holder.textView1.setText(sd.getS1());
        holder.textView2.setText(String.valueOf(sd.getS2()));
    }

    @Override
    public int getItemCount() {
        return set.size();
    }


}