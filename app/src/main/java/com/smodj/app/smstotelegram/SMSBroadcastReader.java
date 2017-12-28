package com.smodj.app.smstotelegram;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by smj on 12/25/17.
 * SMS Broadcast Receiver
 */

public class SMSBroadcastReader  extends BroadcastReceiver {
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    @Override
    public void onReceive(Context context, Intent intent) {
// Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        String senderNum="",message="";
        try {

            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                     senderNum = currentMessage.getDisplayOriginatingAddress();
                     message = message + currentMessage.getDisplayMessageBody();
                    //Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
                }// end for loop

                //Set all parameters
                Storage read = new Storage(context);
                final String telegram_id = read.read("TelegramID");
                final String msg = "From: "+senderNum+"\nDevice Info: "+android.os.Build.MANUFACTURER+"|"+android.os.Build.MODEL+"\nMessage:\n"+message;
                String url = MainConstant.telegram_url;

                //calling Telegram API
                StackMessages stack = new StackMessages(context);

                if (stack.getStack()!=null){
                    Set<String> unsentMsgs = stack.getStack();
                    for ( String unsentMsg : unsentMsgs){
                        sendToTelegramAPI(context, telegram_id, unsentMsg, url,stack);
                    }
                    sendToTelegramAPI(context, telegram_id, msg, url,stack);
                    stack.clearStack();

                }
                else{
                    sendToTelegramAPI(context, telegram_id, msg, url,stack);
                }



            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

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
