package com.smodj.app.smstotelegram.Workers;


import android.content.Context;
import android.content.SharedPreferences;

import com.smodj.app.smstotelegram.Constants.MainConstant;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by smj on 12/9/17.
 * Storage
 */

public class Storage {
    private final String STORAGE = MainConstant.pkg+"Storage";
    private SharedPreferences preferences;
    private Context context;

    public Storage(Context context) {
        this.context = context;
    }

    public void write(String key,String data){
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, data);
        editor.apply();
    }
    public String read(String key){
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        return preferences.getString(key,null);

    }
    public void writeSet(String key,Set<String> list){
        preferences  = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // Save the list.
        editor.putStringSet(key, list);
        editor.apply();
    }
    public void appendSet(String key,String value){
        preferences  = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // Add the new value.
        Set<String> myStrings = preferences.getStringSet(key, new HashSet<String>());
        myStrings.add(value);
        editor.apply();
    }
    public Set<String> readSet(String key){
        // Get the current list.
        preferences  = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        Set<String> myStrings = preferences.getStringSet(key, new HashSet<String>());
        return myStrings;
    }
}
