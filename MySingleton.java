package com.example.android.billsplittingapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by praty on 22-06-2018.
 */

public class MySingleton {
    private static MySingleton minstsnce;
    private RequestQueue requestQueue;
    private static Context context;
    private MySingleton(Context context){
        this.context=context;
        requestQueue=getRequestQueue();
    }
    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized MySingleton getMinstsnce(Context context){
        if(minstsnce==null){
            minstsnce=new MySingleton(context);
        }
        return minstsnce;
    }
    public <t>void addToRequestQueue(Request<t> request){
        requestQueue.add(request);
    }
}
