package com.example.android.billsplittingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.R.attr.author;

/**
 * Created by praty on 05-06-2018.
 */

public class ConAdapter extends RecyclerView.Adapter<ConAdapter.ContactViewHolder>{
    private static List<ContactVO> contactVOList;
    private static Context mContext;
    private static String gn,gt,gr,gro;
    public ConAdapter(List<ContactVO> contactVOList, Context mContext,String gn,String gt,String gr,String gro){
        this.contactVOList = contactVOList;
        this.mContext = mContext;
        this.gn=gn;
        this.gt=gt;
        this.gr=gr;
        this.gro=gro;
    }

    @Override
    public ConAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.contacts, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ConAdapter.ContactViewHolder holder, int position) {
        ContactVO contactVO = contactVOList.get(position);
        holder.tvContactName.setText(contactVO.getContactName());
        holder.tvPhoneNumber.setText(contactVO.getContactNumber());

    }

    @Override
    public int getItemCount() {
        return contactVOList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;

        public ContactViewHolder(final View itemView) {
            super(itemView);
            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(itemView.getContext(),CreateGroup.class);
                    intent.putExtra("name",tvContactName.getText().toString());
                    intent.putExtra("number",tvPhoneNumber.getText().toString());
                    //intent.putExtra("image",ivContactImage.getDrawable().toString());
                    intent.putExtra("image",contactVOList.get(getAdapterPosition()).getContactImage());
                    intent.putExtra("group_name",gn);
                    intent.putExtra("group_type",gt);
                    intent.putExtra("GROUP",gro);
                    if(gr!=null){
                        intent.putExtra("gnm",gr);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    itemView.getContext().startActivity(intent);
                    ((ChooseMember)itemView.getContext()).finish();
                }
            });
        }
    }

    public void updateList(List<ContactVO> list){
        contactVOList=list;
        notifyDataSetChanged();
    }
}
