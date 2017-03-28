package com.example.zjlyyq.demo.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by jialuzhang on 2017/3/18.
 */

public class Images {
    private int imageId;   //自增
    private Bitmap bitmap;
    private static Context mAppContext;
    public Images(Context context){
        this.mAppContext = context;
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
    public byte[] getByteArrayOfbitmap(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        return outputStream.toByteArray();
    }
    //图片插入到数据库
    public int insertIntoDatabase(){
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(mAppContext,"quanquan.db3",null,1);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = mySQLiteOpenHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("image",getByteArrayOfbitmap(bitmap));
            db.insert("images",null,cv);
            cursor = db.rawQuery("select last_insert_rowid() from images",null);
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
    //获得图片资源通过图片ID
    public static Bitmap getImageByImageId(int image_id){
        Bitmap bitmap = null;
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(mAppContext,"quanquan.db3",null,1);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = mySQLiteOpenHelper.getReadableDatabase();
            cursor = db.rawQuery("select * from images where _id = " + image_id + ";",null);
            if (cursor.moveToFirst()){
                int index = cursor.getColumnIndex("image");
                byte[] in = cursor.getBlob(index);
                Bitmap bitmap1 = BitmapFactory.decodeByteArray(in,0,in.length);
                return bitmap1;
            }
        }catch (Exception e){
            Log.d("TEST2","通过图片id获取图片失败");
            e.printStackTrace();
        }finally {
            closeEverything(db,cursor);
        }
        return bitmap;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
