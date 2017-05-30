package com.example.zjlyyq.demo.models;

import android.content.Context;
import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by jialuzhang on 2017/4/17.
 */

public class IMessage {
    private int id;
    private long date;
    private int user1;    //id较小的用户的id
    private int user2;
    private String text;
    private Bitmap image;
    private int typeImessage;    //消息类型(0:文本消息,1:图片消息，等级一次递增，也就是说当消息类型是1时，同样检测是否包含文字)
    private int direction;   //方向，1是user1->user2 -1是user2->user1
    private static Context mContext;
    public IMessage(JSONObject jsonObject) throws JSONException {
        this.date = jsonObject.getLong("date");
        this.user1 = jsonObject.getInt("user1");
        this.user2 = jsonObject.getInt("user2");
        this.text = jsonObject.getString("text");
        this.direction = jsonObject.getInt("direction");
    }
    public IMessage() {
        this.date = new Date().getTime();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getUser1() {
        return user1;
    }

    public void setUser1(int user1) {
        this.user1 = user1;
    }

    public int getUser2() {
        return user2;
    }

    public void setUser2(int user2) {
        this.user2 = user2;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getTypeImessage() {
        return typeImessage;
    }

    public void setTypeImessage(int typeImessage) {
        this.typeImessage = typeImessage;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
