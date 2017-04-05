package com.example.zjlyyq.demo.models;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;

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
    private static Context mContext;
    public User(Context context){
        userId = -1;
        sex = 0;
        age = -1;
        register_time = -1;
        userPhoto = null;
        userPhone = -1;
        privilege = 0;
        this.mContext = context;
    }
    private static void closeEverything(SQLiteDatabase db,Cursor cursor){
        Log.d("TEST","开始收尾");
        if (db != null && db.isOpen()){
            db.close();
        }
        if (cursor != null){
            Log.d("TEST","关闭游标");
            cursor.close();
        }
    }
    public boolean updateDatabase(){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String SqlFile = "quanquan.db3";
        Log.d("TEST","开始插入");
        try{
            MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(mContext,SqlFile,null,1);
            db = mySQLiteOpenHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("email_adress",email_adress);
            contentValues.put("userName",userName);
            contentValues.put("password",password);
            contentValues.put("sex",sex);
            contentValues.put("age",age);
            contentValues.put("userPhoto",getByteArrayOfbitmap(userPhoto));
            contentValues.put("userPhone",userPhone);
            contentValues.put("register_time",register_time);
            contentValues.put("privilege",privilege);
            String id = ""+userId;
            int result = db.update("UserInfo",contentValues,"userId=?",new String[]{id});
            Log.d("TEST","result = " + result);
        }catch (Exception e){
            Log.d("TEST","出错了");
            e.printStackTrace();
        }finally {
            closeEverything(db,cursor);
        }
        return false;
    }
    public int insertIntoDatebase(){     //插入数据库，返回主键
        long row = -1;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String SqlFile = "quanquan.db3";
        Log.d("TEST","开始插入");
        try{
            MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(mContext,SqlFile,null,1);
            db = mySQLiteOpenHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("email_adress",email_adress);
            contentValues.put("userName",userName);
            contentValues.put("password",password);
            contentValues.put("sex",sex);
            contentValues.put("age",age);
            contentValues.put("userPhone",userPhone);
            contentValues.put("register_time",register_time);
            contentValues.put("privilege",privilege);
            row = db.insert("UserInfo",null,contentValues);
            Log.d("TEST","row = "+row);
            String sql = "select * from UserInfo where email_adress = "+"'"+ email_adress+"';";
            Log.d("TEST","sql = " + sql);
            cursor = db.rawQuery(sql,null);
            //cursor = db.query(true,"UserInfo",new String[]{"email_adress"},"email_adress = ?",new String[]{email_adress},null,null,null,null);
            if (cursor.moveToNext()){
                int pos = cursor.getColumnIndex("userId");
                return cursor.getInt(pos);
            }

        }catch (Exception e){
            Log.d("TEST","出错了");
            e.printStackTrace();
        }finally {
            closeEverything(db,cursor);
        }
        return -1;
    }
    public  int checkExisit(String email){
        String SqlFile = "quanquan.db3";
        Cursor cursor = null;
        SQLiteDatabase db = null;
        try{
            MySQLiteOpenHelper helper = new MySQLiteOpenHelper(mContext,SqlFile,null,1);
            db = helper.getReadableDatabase();
            Log.d("TEST2","select * from UserInfo where email_adress = "+ email+";");
            cursor = db.rawQuery("select * from UserInfo where email_adress = "+ email+";",null);
            if(cursor != null){
                cursor.moveToFirst();
                int index = cursor.getColumnIndex("userId");
                return cursor.getInt(index);
            }
        }catch (Exception e){
            Log.d("TEST2","通过邮箱验证合法性出错");
            e.printStackTrace();
        }finally {
            closeEverything(db,cursor);
        }

        return -1;
    }
    public static User getUserById(int user_id){
        User user = new User(mContext);
        String SqlFile = "quanquan.db3";
        Cursor cursor = null;
        SQLiteDatabase db = null;
        try{
            MySQLiteOpenHelper helper = new MySQLiteOpenHelper(mContext,SqlFile,null,1);
            db = helper.getReadableDatabase();
            cursor = db.rawQuery("select * from UserInfo where userId = "+ user_id+";",null);
            int index = 0;
            if (cursor.moveToFirst()){
                //index
                user.setUserId(user_id);
                index = cursor.getColumnIndex("userName");
                user.setUserName(cursor.getString(index));
                index = cursor.getColumnIndex("password");
                Log.d("TEST2","密码获取成功");
                user.setPassword(cursor.getString(index));
                index = cursor.getColumnIndex("userPhoto");
                byte[] in = cursor.getBlob(index);
                Log.d("TEST2","头像字节获取成功");
                if (in != null){
                    Log.d("TEST2","头像大小为"+in.length);
                    Bitmap photo = BitmapFactory.decodeByteArray(in,0,in.length);
                    user.setUserPhoto(photo);
                }
                Log.d("TEST2","头像获取成功");
                index = cursor.getColumnIndex("email_adress");
                user.setEmail_adress(cursor.getString(index));
                index = cursor.getColumnIndex("sex");
                user.setSex(cursor.getInt(index));
                index = cursor.getColumnIndex("age");
                user.setAge(cursor.getInt(index));
                index = cursor.getColumnIndex("userPhone");
                user.setUserPhone(cursor.getInt(index));
                index = cursor.getColumnIndex("register_time");
                user.setRegister_time(cursor.getLong(index));
                index = cursor.getColumnIndex("privilege");
                user.setPrivilege(cursor.getInt(index));
            }
        }catch (Exception e){
            Log.d("TEST2","根据id获取用户对象失败");
            e.printStackTrace();
        }finally {
            closeEverything(db,cursor);
        }
        return user;
    }
    public byte[] getByteArrayOfbitmap(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        return outputStream.toByteArray();
    }
    public void updateEdit(){
        String SqlFile = "quanquan.db3";
        Cursor cursor = null;
        SQLiteDatabase db = null;
        try{
            MySQLiteOpenHelper helper = new MySQLiteOpenHelper(mContext,SqlFile,null,1);
            db = helper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("userName",userName);
            cv.put("userPhoto",getByteArrayOfbitmap(userPhoto));
            cv.put("sex",sex);
            db.update("userInfo",cv,"userId", new String[]{"" + userId});
        }catch (Exception e){
            Log.d("TEST2","根据id获取用户对象失败");
            e.printStackTrace();
        }finally {
            closeEverything(db,cursor);
        }
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

    public Bitmap getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Bitmap userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getEmail_adress() {
        return email_adress;
    }

    public void setEmail_adress(String email_adress) {
        this.email_adress = email_adress;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
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
