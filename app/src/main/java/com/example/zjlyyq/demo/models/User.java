package com.example.zjlyyq.demo.models;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by jialuzhang on 2017/3/17.
 */

public class User {
    private int userId;           //用户id自增，生成用户信息时返回id，方便使用
    private  String userName;
    private String password;
    private  String userPhoto;
    private String email_adress;
    private String sex;        //性别：1代表男，0代表未知，-1代表女
    private int age;
    private int userPhone;
    private long register_time;
    private int privilege;
    private static Context mContext;
    public User(JSONObject object) throws JSONException {
        Log.d("JSON",object.toString());
        //{"password":"296675700","userPhoto":null,"sex":0,"userPhone":0,"privilege":0,"userName":"zjl","userId":26,"email_adress":"2868989685@163.com","register_time":0,"age":0}
        this.userId = object.getInt("userId");
        sex = object.getString("sex");
        age = object.getInt("age");
        userPhoto = object.getString("imageUrl");
        this.userName = object.getString("userName");
        //userPhone = -1;
        //privilege = 0;
        //this.mContext = context;
    }
    public User(Context context){
        userId = -1;
        sex = "男";
        age = -1;
        register_time = -1;
        userPhoto = "null";
        userPhone = -1;
        privilege = 0;
        this.mContext = context;
    }



    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getEmail_adress() {
        return email_adress;
    }

    public void setEmail_adress(String email_adress) {
        this.email_adress = email_adress;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(int userPhone) {
        this.userPhone = userPhone;
    }

    public long getRegister_time() {
        return register_time;
    }

    public void setRegister_time(long register_time) {
        this.register_time = register_time;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }
}
