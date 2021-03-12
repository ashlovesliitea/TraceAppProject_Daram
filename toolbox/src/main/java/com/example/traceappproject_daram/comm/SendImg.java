package com.example.traceappproject_daram.comm;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traceappproject_daram.data.LoginInfo;

public class SendImg {
    private String fileName;
    private int num;
    private String url;
    private Context context;
    private String TAG = "SEND_IMGS";
    public SendImg(String fileName, int num, Context context){
        this.fileName = fileName;
        this.num = num;
        this.context = context;
    }

    public void sendVolley(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://www.google.com/search?q=youtube&oq=youtube&aqs=chrome..69i57j35i39j0j0i433j69i60l4.7623j0j7&sourceid=chrome&ie=UTF-8"; //나중에 여기 주석처리

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i(TAG,"Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i(TAG,"error : "+error.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

}
