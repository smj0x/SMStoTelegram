package com.smodj.app.smstotelegram.Workers;

import android.content.Context;

import com.smodj.app.smstotelegram.Constants.MainConstant;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by smj on 12/25/17.
 * Stack unsent messages in case of disaster :P
 */

public class StackMessages{
    private Context context;
    private Storage storage;
       //private Set<String> stack = new HashSet();
    public StackMessages(Context context){
        this.context = context;
    }
    public void addToStack(String msg){
        storage = new Storage(context);
        Set<String> initStack =  new HashSet<>();
        if (getStack()!=null){
            storage.appendSet(MainConstant.unsent_msgs_stack, msg);
        }
        else{
            storage.writeSet(MainConstant.unsent_msgs_stack, initStack);
            storage.appendSet(MainConstant.unsent_msgs_stack, msg);
        }
    }
    public Set<String> getStack(){
        storage = new Storage(context);
        return storage.readSet(MainConstant.unsent_msgs_stack);
    }
    public void clearStack(){
        storage = new Storage(context);
        storage.writeSet(MainConstant.unsent_msgs_stack,null);
    }
}
