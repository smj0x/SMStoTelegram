package com.smodj.app.smstotelegram;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smodj.app.smstotelegram.Constants.MainConstant;
import com.smodj.app.smstotelegram.Workers.StackMessages;
import com.smodj.app.smstotelegram.Workers.Storage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Created by smj on 12/28/17.
 * Broadcast Receiver Internet
 */

public class NetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {

            //calling Telegram API
            StackMessages stack = new StackMessages(context);
            if (stack.getStack()!=null){
                Set<String> unsentMsgs = stack.getStack();
                Storage read = new Storage(context);
                final String telegram_id = read.read(MainConstant.telegram_id_storage_key);
                final String url = MainConstant.telegram_url;
                for ( String unsentMsg : unsentMsgs){
                    sendToTelegramAPI(context,telegram_id,unsentMsg,url,stack);
                }
                stack.clearStack();
            }
        }

    }
    private void sendToTelegramAPI(final Context context, final String telegram_id, final String msg, final String url,final StackMessages stack) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VolleyError","That didn't work!");
                        stack.addToStack(msg);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("chat_id", telegram_id);
                params.put("text", msg);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

 }
