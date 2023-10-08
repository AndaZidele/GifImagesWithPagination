package com.example.gifimageswithpagination;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Requests {

    private static Requests instance;
    private static RequestQueue requestQueue;
    private static Context context;

    private Requests(Context contextc){
        context = contextc;
        requestQueue = getRequestQueue();
    }

    public static Requests getInstance(Context contextc){
        if(instance == null){
            instance = new Requests(contextc);
        }
        return instance;
    }

    private RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }

}