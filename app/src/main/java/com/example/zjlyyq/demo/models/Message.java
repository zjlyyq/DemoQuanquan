package com.example.zjlyyq.demo.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private double x;
    private double y;
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
//{"favourTimes":0,"transmitTimes":0,"publish_time":"2017-04-13 13:17:20.0","x":0,"y":0,"messageId":13,"commentTimes":0,"text":"ghu","userId":25}
    public Message(JSONObject jsonObject) throws JSONException, ParseException {
        messageId = jsonObject.getInt("messageId");
        publisherId=jsonObject.getInt("userId");
        text = jsonObject.getString("text");
        String date = jsonObject.getString("publish_time");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        publishDate = sdf.parse(date).getTime();
        Log.d("TEST",""+publishDate);
        x = jsonObject.getDouble("x");
        y = jsonObject.getDouble("y");
        transmitTimes = jsonObject.getInt("transmitTimes");
        favourTimes = jsonObject.getInt("favourTimes");
        commentTimes = jsonObject.getInt("commentTimes");
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

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
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
