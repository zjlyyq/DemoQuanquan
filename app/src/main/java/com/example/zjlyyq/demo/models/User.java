package com.example.zjlyyq.demo.models;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by jialuzhang on 2017/3/17.
 */

public class User {
    private int userId;           //用户id自增，生成用户信息时返回id，方便使用
    private  String userName;
    private String password;
    private  Bitmap userPhoto;
    private String email_adress;
    private int sex;        //性别：1代表男，0代表未知，-1代表女
    private int age;
    private int userPhone;
    private long register_time;
    private int privilege;
    public User(){
        userId = -1;
        sex = 0;
        age = -1;
        register_time = -1;
        userPhoto = null;
        userPhone = -1;
        privilege = 0;
    }
}
