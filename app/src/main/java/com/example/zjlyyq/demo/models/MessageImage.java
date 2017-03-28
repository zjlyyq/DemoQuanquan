package com.example.zjlyyq.demo.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;

import com.example.zjlyyq.demo.MainActivity;

import java.util.ArrayList;

/**
 * Created by jialuzhang on 2017/3/18.
 */

public class MessageImage {
    private int _id;     //主键
    private int messageId;
    private int imageId;
    private static Context mContext = null;
    public MessageImage(Context context,int message_id,int image_id){
        this.mContext = context;
        this.imageId = image_id;
        this.messageId = message_id;
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
    public int insertIntoDatabase(){
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(mContext,"quanquan.db3",null,1);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = mySQLiteOpenHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("messageId",messageId);
            cv.put("imageId",imageId);
            db.insert("messageImage",null,cv);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeEverything(db,cursor);
        }
        return -1;
    }
    public static ArrayList<Bitmap> getPicturesByMessageId(int message_id){
        ArrayList<Bitmap> pictures = new ArrayList<Bitmap>();
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(mContext,"quanquan.db3",null,1);
        Log.d("TESt2","打开数据库ok");
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            Log.d("TESt2","ok2");
            db = mySQLiteOpenHelper.getReadableDatabase();
            Log.d("TESt2","ok3");
            cursor = db.rawQuery("select imageId from messageImage where messageId = " + message_id + ";",null);
            Log.d("TESt2","ok4");
            while (cursor.moveToNext()){
                Log.d("TESt2","ok5");
                int image_id = cursor.getInt(0);
                Images images = new Images(mContext);
                Bitmap bitmap = images.getImageByImageId(image_id);
                Log.d("TESt2","ok6");
                pictures.add(bitmap);
            }
            Log.d("TESt2","ok7");
        }catch (Exception e){
            Log.d("TESt2","通过状态id获取图片集合失败");
            e.printStackTrace();
        }finally {
            closeEverything(db,cursor);
        }
        return pictures;
    }
}
