package com.example.android.billsplittingapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;

import java.util.List;

/**
 * Created by praty on 06-06-2018.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>{
    private static List<ContactVO2> contactVOList;

    Context context;
    ContactVO selectedItems;
    DatabaseHelper mydb;


    private Context mContext;
    public ContactAdapter(List<ContactVO2> contactVOList, Context mContext){
        this.contactVOList = contactVOList;
        this.mContext = mContext;
    }

    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.contacts, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(final ContactAdapter.ContactViewHolder holder, int position) {
        final ContactVO2 contactVO = contactVOList.get(position);
        holder.tvContactName.setText(contactVO.getContactName());
        holder.tvPhoneNumber.setText(contactVO.getContactNumber());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(mContext, holder.itemView);
                //inflating menu from xml resource
                popup.inflate(R.menu.contact_delete);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete_contact:
                                //handle menu1 click
                                ContactVO2 info=contactVO;
                                int position=contactVOList.indexOf(info);
                                mydb=new DatabaseHelper(mContext);
                                SQLiteDatabase db=mydb.getReadableDatabase();
                                String []args={info.getContactName(),info.getGroup()};
                                Cursor cursor=db.query(mydb.balancetable,null,mydb.c1+"=?"+" AND "+mydb.c0+"=?",args,null,null,null);
                                Log.e("dgdb",info.getContactName()+"    "+info.getGroup());
                                if(cursor.moveToFirst()){
                                    Log.e("ll",cursor.getString(cursor.getColumnIndex(mydb.c0))+" "+cursor.getString(cursor.getColumnIndex(mydb.c1)));
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                                    alertDialogBuilder.setTitle("Error");
                                    alertDialogBuilder.setMessage("Cannot Remove because the person is not settled up");
                                    alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }else{
                                    Log.e("ooooooooo","rrrrrrrrrrrrrr");
                                    db.delete("contacts","name"+"=?"+" AND "+mydb.cgroupName+"=?",args);
                                    contactVOList.remove(position);
                                    notifyDataSetChanged();
                                }

                                return true;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
                return true;
            }
        });


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

        }
    }

    public void updateList(List<ContactVO2> list){
        contactVOList=list;
        notifyDataSetChanged();
    }
}
