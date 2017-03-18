package com.example.zjlyyq.demo.models;

import android.content.Context;

import com.example.zjlyyq.demo.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jialuzhang on 2017/3/15.
 */

public class MessageUnion {
    private static MessageUnion messageUnion;
    //状态的List
    private ArrayList<Message> messages;
    //上下文
    private  Context mAppContext;
    private MessageUnion(Context appContext){
        mAppContext = appContext;
        messages = new ArrayList<Message>();
        Random random = new Random();
    }
    //唯一方法获得此类的实例
    public static MessageUnion getInstance(Context context){
        if(messageUnion == null){
            messageUnion = new MessageUnion(context.getApplicationContext());
        }
        return  messageUnion;
    }
    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message msg){
        messages.add(msg);
    }

    public void deleteMessage(Message msg){
        messages.remove(msg);
    }
}
