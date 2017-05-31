package com.example.zjlyyq.demo.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by jialuzhang on 2017/3/15.
 */

public class Comment {
    private int commentId;
    private int userId;
    private int messageId;
    private int favourTimes;
    private long date;
    private String text;
    private static Context mContext;
    public Comment(JSONObject jsonObject) throws JSONException, ParseException {
        userId = jsonObject.getInt("userId");
        favourTimes = jsonObject.getInt("favourTimes");
        text = jsonObject.getString("text");
        String date1 = jsonObject.getString("date");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = sdf.parse(date1).getTime();
    }
    public Comment(){

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
    public void insertIntoDatabase(){
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(mContext,"quanquan.db3",null,1);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = mySQLiteOpenHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("userId",userId);
            cv.put("messageId",messageId);
            cv.put("favourTimes",favourTimes);
            cv.put("date",date);
            cv.put("text",text);
            db.insert("comments",null,cv);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeEverything(db,cursor);
        }
    }
    public ArrayList<Comment> getCommentsByMessageId(int message_id){
        ArrayList<Comment> comments = new ArrayList<Comment>();
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(mContext,"quanquan.db3",null,1);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            int index;
            db = mySQLiteOpenHelper.getWritableDatabase();
            cursor = db.rawQuery("select * from comments where messageId = " + message_id +";",null);
            while (cursor.moveToNext()){
                Comment comment = new Comment();
                index = cursor.getColumnIndex("commentId");
                int comment_id = cursor.getInt(index);
                comment.setCommentId(comment_id);
                index = cursor.getColumnIndex("userId");
                comment.setUserId(cursor.getInt(index));
                index = cursor.getColumnIndex("messageId");
                comment.setMessageId(cursor.getInt(index));
                index = cursor.getColumnIndex("favourTimes");
                comment.setFavourTimes(cursor.getInt(index));
                index = cursor.getColumnIndex("date");
                comment.setDate(cursor.getInt(index));
                index = cursor.getColumnIndex("text");
                comment.setText(cursor.getString(index));
                comments.add(comment);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeEverything(db,cursor);
        }
        return comments;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getCommentId() {
        return commentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getFavourTimes() {
        return favourTimes;
    }

    public void setFavourTimes(int favourTimes) {
        this.favourTimes = favourTimes;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
