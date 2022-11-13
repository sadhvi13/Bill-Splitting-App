package com.example.android.billsplittingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.fragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentOne.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentOne#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentOne extends BaseFragment {
    private RecyclerView recyclerView;
    private RAdapter1 adapter1;
    private List<RList1> groupList = new ArrayList<>();
    FloatingActionButton floatingActionButton;
    DatabaseHelper mydb;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_fragment_one, container, false);
        // Inflate the layout for this fragment
        mydb=new DatabaseHelper(getContext());
        SQLiteDatabase db2=mydb.getWritableDatabase();
        db2.delete(mydb.contacts_table,mydb.cgroupName+"=?",new String[]{""});
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        SQLiteDatabase db=mydb.getReadableDatabase();
        Cursor cursor=mydb.readGroupContact(db);
        try{
            cursor.moveToFirst();
            if(cursor.moveToFirst()){
                do{
                    String group_name=cursor.getString(cursor.getColumnIndex(mydb.column1));
                    String group_type=cursor.getString(cursor.getColumnIndex(mydb.column3));
                    String group_id=cursor.getString(cursor.getColumnIndex(mydb.column2));
                    RList1 rList1=new RList1(group_type,group_name,group_id,sharedpreferences.getString("user_id","id"));
                    groupList.add(rList1);
                }while (cursor.moveToNext());

            }
        }catch (Exception e){

        }

        adapter1 = new RAdapter1(groupList,getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter1);
        floatingActionButton=(FloatingActionButton)rootView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),CreateGroup.class);
                intent.putExtra("first","yes");
                startActivity(intent);

            }
        });
        setHasOptionsMenu(true);

        return rootView;
    }




}
