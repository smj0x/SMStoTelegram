package com.smodj.app.smstotelegram.Workers;

import android.content.Context;

import com.smodj.app.smstotelegram.Constants.MainConstant;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by smj on 12/25/17.
 */

public class StackMessages{
    private Context context;
       //private Set<String> stack = new HashSet();

    public StackMessages(Context context){
        this.context = context;
    }
    public void addToStack(String msg){
        Storage storage = new Storage(context);
        Set<String> initStack =  new HashSet<>();
        if (getStack()!=null){
            storage.appendSet(MainConstant.unsent_msgs_stack, msg);
        }
        else{
            storage.writeSet(MainConstant.unsent_msgs_stack, initStack);
        }
    }
    public Set<String> getStack(){
        Storage storage = new Storage(context);
        return storage.readSet(MainConstant.unsent_msgs_stack);
    }
}
