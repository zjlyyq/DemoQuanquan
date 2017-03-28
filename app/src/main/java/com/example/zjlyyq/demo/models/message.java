package com.example.zjlyyq.demo.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jialuzhang on 2017/3/15.
 */

public class Message {
    private int messageId;  //自增，主键
    private int publisherId;  //外键
    private String text;
    private long publishDate;
    private int x;
    private int y;
    private int transmitTimes;  //转发次数
    private int favourTimes;   //被点赞次数
    private int commentTimes;  //评论次数
    private static Context mContext;

    public Message(Context context){
        this.mContext = context;
        favourTimes  = 0;
        transmitTimes = 0;
        commentTimes = 0;
    }
    public Message(Cursor cursor){
        messageId = cursor.getInt(0);
        publisherId=cursor.getInt(1);
        text = cursor.getString(2);
        publishDate = cursor.getLong(3);
        x = cursor.getInt(4);
        y = cursor.getInt(5);
        transmitTimes = cursor.getInt(6);
        favourTimes = cursor.getInt(7);
        commentTimes = cursor.getInt(8);
    }
    private static void closeEverything(SQLiteDatabase db, Cursor cursor){
        Log.d("TEST","开始收尾");
        if (db != null && db.isOpen()){
            db.close();
        }
        if (cursor != null){
            Log.d("TEST","关闭游标");
            cursor.close();
        }
    }
    public ContentValues initContentValues(){
        ContentValues cv = new ContentValues();
        cv.put("userId",publisherId);
        cv.put("text",text);
        cv.put("publish_time",publishDate);
        cv.put("x",x);
        cv.put("y",y);
        cv.put("transmitTimes",transmitTimes);
        cv.put("favourTimes",favourTimes);
        cv.put("commentTimes",commentTimes);
        return cv;
    }
    public int insertIntoDatabase(){
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(mContext,"quanquan.db3",null,1);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = mySQLiteOpenHelper.getWritableDatabase();
            ContentValues cv = initContentValues();
            db.insert("messageInfo",null,cv);
            cursor = db.rawQuery("select last_insert_rowid() from messageInfo;",null);
            if (cursor.moveToFirst()){
                return cursor.getInt(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeEverything(db,cursor);
        }
        return -1;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(long publishDate) {
        this.publishDate = publishDate;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getFavourTimes() {
        return favourTimes;
    }

    public void setFavourTimes(int favourTimes) {
        this.favourTimes = favourTimes;
    }

    public int getTransmitTimes() {
        return transmitTimes;
    }

    public void setTransmitTimes(int transmitTimes) {
        this.transmitTimes = transmitTimes;
    }

    public int getCommentTimes() {
        return commentTimes;
    }

    public void setCommentTimes(int commentTimes) {
        this.commentTimes = commentTimes;
    }

}
