package com.example.zjlyyq.demo.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.zjlyyq.demo.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jialuzhang on 2017/3/15.
 */

public class MessageUnion {
    private static String SqlFile = "quanquan.db3";
    private static final String TAG = "MessageUnion";
    private static MessageUnion messageUnion;
    //状态的List
    private ArrayList<Message> messages;
    //上下文
    private  Context mAppContext;
    private MessageUnion(Context appContext){
        mAppContext = appContext;
        messages = new ArrayList<Message>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(appContext,SqlFile,null,1);
            db = mySQLiteOpenHelper.getReadableDatabase();
            cursor = db.rawQuery("select * from messageInfo;",null);
            cursor.moveToFirst();
            Message message = new Message(cursor);
            messages.add(message);
            while (cursor.moveToNext()){
                Message message1 = new Message(cursor);
                Log.d("TEST3","succeed");
                messages.add(message1);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,"获取动态集合出错");
        }finally {
            if (db != null && db.isOpen()){
                db.close();
            }
            if (cursor != null){
                cursor.close();
            }
        }
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
