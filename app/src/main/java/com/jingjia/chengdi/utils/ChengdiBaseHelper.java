package com.jingjia.chengdi.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jingjia.chengdi.data.CDDbSchema;

/**
 * Created by Administrator on 2016/10/4.
 * sqlite 数据库助手
 */
public class ChengdiBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;//版本号

    public ChengdiBaseHelper(Context context) {
        super(context, CDDbSchema.DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建用户表
        db.execSQL("create table " + CDDbSchema.ChengdiTable.TABLE_USER
                + "(_id integer primary key autoincrement,"
                + CDDbSchema.ChengdiTable.User.UUID + ","
                + CDDbSchema.ChengdiTable.User.USERNAME + ","
                + CDDbSchema.ChengdiTable.User.PASSWORD + ","
                + CDDbSchema.ChengdiTable.User.PORTRAITURI + ","
                + CDDbSchema.ChengdiTable.User.PHONE + ","
                + CDDbSchema.ChengdiTable.User.NAME + ","
                + CDDbSchema.ChengdiTable.User.SEX + ","
                + CDDbSchema.ChengdiTable.User.AGE + ","
                + CDDbSchema.ChengdiTable.User.JOB + ","
                + CDDbSchema.ChengdiTable.User.EDUCATION + ","
                + CDDbSchema.ChengdiTable.User.LOCATION + ","
                + CDDbSchema.ChengdiTable.User.HOMETOWN + ","
                + CDDbSchema.ChengdiTable.User.SUBSCRIPT + ","
                + CDDbSchema.ChengdiTable.User.SCORE + ","
                + CDDbSchema.ChengdiTable.User.AUTHENTICATION +
                ")");
        //创建需求表
        db.execSQL("create table " + CDDbSchema.ChengdiTable.TABLE_DEMAND
                + "(_id integer primary key autoincrement,"
                + CDDbSchema.ChengdiTable.Demand.UUID + ","
                + CDDbSchema.ChengdiTable.Demand.CATEGORY + ","
                + CDDbSchema.ChengdiTable.Demand.LIMITTIME + ","
                + CDDbSchema.ChengdiTable.Demand.MONEY + ","
                + CDDbSchema.ChengdiTable.Demand.CONTENT + ","
                + CDDbSchema.ChengdiTable.Demand.REMARK + ","
                + CDDbSchema.ChengdiTable.Demand.DESTINATION + ","
                + CDDbSchema.ChengdiTable.Demand.DESTINATIONREMARK + ","
                + CDDbSchema.ChengdiTable.Demand.PUBLISHTIME + ","
                + CDDbSchema.ChengdiTable.Demand.PHONE + ","
                + CDDbSchema.ChengdiTable.Demand.USERNAME + ","
                + CDDbSchema.ChengdiTable.Demand.STATUS +
                ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
