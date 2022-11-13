package com.example.android.billsplittingapp;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by praty on 05-06-2018.
 */

public class RAdapter2 extends RecyclerView.Adapter<RAdapter2.MyViewHolder>  {

    public List<RList2> activity_list= new ArrayList<RList2>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView1;
        public LinearLayout linearLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView1=(TextView) itemView.findViewById(R.id.text1);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.ll);
        }
    }

    public RAdapter2(List<RList2> activity_list){
        this.activity_list=activity_list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view2,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final RList2 rList2 = activity_list.get(position);
        holder.textView1.setText(rList2.getT1());
    }

    @Override
    public int getItemCount() {
        return activity_list.size();
    }
    public void changeData(List<RList2> list){
        activity_list=list;
        notifyDataSetChanged();
    }
}
