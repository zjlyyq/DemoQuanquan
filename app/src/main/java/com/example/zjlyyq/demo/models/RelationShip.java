package com.example.zjlyyq.demo.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by jialuzhang on 2017/3/18.
 */

public class RelationShip {
    private int relationId;   //关系id，主键自增
    private int user1Id;
    private int user2Id;
    private boolean isTrue;
    private long beginTime;
    private static Context mContext;

    public RelationShip(Context context){
        this.mContext = context;
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
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String SqlFile = "quanquan.db3";
        Log.d("TEST","开始插入");
        try{
            MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(mContext,SqlFile,null,1);
            db = mySQLiteOpenHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("relationId",relationId);
            contentValues.put("user1Id",user1Id);
            contentValues.put("user2Id",user2Id);
            contentValues.put("isTrue",isTrue);
            contentValues.put("beginTime",beginTime);
            db.insert("relationShips",null,contentValues);
        }catch (Exception e){
            Log.d("TEST","出错了");
            e.printStackTrace();
        }finally {
            closeEverything(db,cursor);
        }
    }
    public int getRelationId() {
        return relationId;
    }

    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }

    public int getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(int user1Id) {
        this.user1Id = user1Id;
    }

    public int getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }
}
