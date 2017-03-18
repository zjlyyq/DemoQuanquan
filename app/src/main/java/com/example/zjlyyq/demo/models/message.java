package com.example.zjlyyq.demo.models;

import android.graphics.Bitmap;

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
    private int favourTimes;   //被点赞次数
    private int transmitTimes;  //转发次数
    private int commentTimes;  //评论次数

    public Message(){
        favourTimes  = 0;
        transmitTimes = 0;
        commentTimes = 0;
    }
}
