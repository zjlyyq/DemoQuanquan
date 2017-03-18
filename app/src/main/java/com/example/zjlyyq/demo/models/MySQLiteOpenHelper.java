package com.example.zjlyyq.demo.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jialuzhang on 2017/3/18.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE_USERINFO_SQL = "create table UserInfo(" +
            "_id integer primary key autoincrement," +
            "email_adress varchar(50)," +
            "userName varchar(20)," +
            "password varchar(20)," +
            "register_time integer," +
            "userPhoto blob," +
            "sex integer," +
            "age integer," +
            "privilege integer," +
            "userPhone integer(11)";
    private static final String CREATE_TABLE_MESSAGE_SQL = "create table messageInfo(" +
            "_id integer primary key autoincrement," +
            "userId integer"+
            "text varchar(300)"+
            "publish_time integer," +
            "x integer," +
            "y integer," +
            "transmitTimes integer," +
            "favourTimes integer," +
            "commentTimes integer(11)";
    private static final String CREATE_TABLE_MESSAGEIMAGE_SQL = "create table messageInfo(" +
            "_id integer primary key autoincrement," +
            "messageId integer"+
            "imageId integer,";
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USERINFO_SQL);
        sqLiteDatabase.execSQL(CREATE_TABLE_MESSAGE_SQL);
        sqLiteDatabase.execSQL(CREATE_TABLE_MESSAGEIMAGE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
