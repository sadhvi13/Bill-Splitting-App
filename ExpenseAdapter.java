package com.example.android.billsplittingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by praty on 18-06-2018.
 */

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseVH> {
    List<ExpenseDetailRV> list=new ArrayList<>();
    Context context;
    public ExpenseAdapter(List<ExpenseDetailRV> list, Context context){
        this.list=list;
        this.context=context;
    }
    @Override
    public ExpenseAdapter.ExpenseVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_expense,parent,false);
        return new ExpenseAdapter.ExpenseVH(itemView);
    }

    @Override
    public void onBindViewHolder(ExpenseAdapter.ExpenseVH holder, int position) {
        ExpenseDetailRV expenseDetailRV=list.get(position);
        holder.textView.setText(expenseDetailRV.getText());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ExpenseVH extends RecyclerView.ViewHolder{
        TextView textView;
        public ExpenseVH(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.expense);
        }
    }
}
