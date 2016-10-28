package com.jingjia.chengdi.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jingjia.chengdi.data.CDDbSchema;
import com.jingjia.chengdi.data.encapsulation.DemandInfo;
import com.jingjia.chengdi.data.encapsulation.User;

/**
 * Created by Administrator on 2016/10/4.
 * sql数据库工具
 */
public class SQLiteDbUtils {
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public SQLiteDbUtils(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ChengdiBaseHelper(mContext).getWritableDatabase();
    }

    /**
     * 将user添加到user表中
     *
     * @param user
     */
    public void addUser(User user) {
        mDatabase.insert(CDDbSchema.ChengdiTable.TABLE_USER, null, getUserContentValues(user));
    }

    /**
     * 将demand添加到demand表中
     *
     * @param demandInfo
     */
    public void addDemand(DemandInfo demandInfo) {
        mDatabase.insert(CDDbSchema.ChengdiTable.TABLE_DEMAND, null, getDemandContentValues(demandInfo));
    }

    public void update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        mDatabase.update(table, values, whereClause, whereArgs);
    }

    public void delete(String table, String whereClause, String[] whereArgs) {
        mDatabase.delete(table, whereClause, whereArgs);
    }

    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy) {
        Cursor cursor = mDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        return cursor;
    }

    /**
     * ContentValues,专门用于数据库的写入、更新的辅助类，类使用Map
     * User的ContentValues
     *
     * @param user
     * @return
     */
    public static ContentValues getUserContentValues(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CDDbSchema.ChengdiTable.User.UUID, user.getId());
        contentValues.put(CDDbSchema.ChengdiTable.User.USERNAME, user.getUsername());
        contentValues.put(CDDbSchema.ChengdiTable.User.PASSWORD, user.getPassword());
        contentValues.put(CDDbSchema.ChengdiTable.User.PORTRAITURI, user.getPortrait());
        contentValues.put(CDDbSchema.ChengdiTable.User.PHONE, user.getPhone());
        contentValues.put(CDDbSchema.ChengdiTable.User.NAME, user.getName());
        contentValues.put(CDDbSchema.ChengdiTable.User.SEX, ""+user.getSex());
        contentValues.put(CDDbSchema.ChengdiTable.User.AGE, user.getAge());
        contentValues.put(CDDbSchema.ChengdiTable.User.JOB, user.getJob());
        contentValues.put(CDDbSchema.ChengdiTable.User.EDUCATION, user.getEducation());
        contentValues.put(CDDbSchema.ChengdiTable.User.LOCATION, user.getLocation());
        contentValues.put(CDDbSchema.ChengdiTable.User.HOMETOWN, user.getHometown());
        contentValues.put(CDDbSchema.ChengdiTable.User.SUBSCRIPT, user.getSubScript());
        contentValues.put(CDDbSchema.ChengdiTable.User.SCORE, user.getScore());
        contentValues.put(CDDbSchema.ChengdiTable.User.AUTHENTICATION, user.getHaveAuthentication());
        return contentValues;
    }

    /**
     * 生成需求表的ContentValues
     */
    public static ContentValues getDemandContentValues(DemandInfo demand) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CDDbSchema.ChengdiTable.Demand.UUID, demand.getUserId());
        contentValues.put(CDDbSchema.ChengdiTable.Demand.CATEGORY, demand.getCategory());
        contentValues.put(CDDbSchema.ChengdiTable.Demand.LIMITTIME, demand.getLimittime());
        contentValues.put(CDDbSchema.ChengdiTable.Demand.MONEY, demand.getMoney());
        contentValues.put(CDDbSchema.ChengdiTable.Demand.CONTENT, demand.getContent());
        contentValues.put(CDDbSchema.ChengdiTable.Demand.REMARK, demand.getRemark());
        contentValues.put(CDDbSchema.ChengdiTable.Demand.DESTINATION, demand.getDestination());
        contentValues.put(CDDbSchema.ChengdiTable.Demand.DESTINATIONREMARK, demand.getDestinationRemark());
        contentValues.put(CDDbSchema.ChengdiTable.Demand.PUBLISHTIME, demand.getPublishTime());
        contentValues.put(CDDbSchema.ChengdiTable.Demand.PHONE, demand.getPhone());
        contentValues.put(CDDbSchema.ChengdiTable.Demand.USERNAME, demand.getUser().getUsername());
        contentValues.put(CDDbSchema.ChengdiTable.Demand.STATUS, demand.getStatus());
        return contentValues;
    }

    /**
     * 判断用户是否存在与数据库中
     *
     * @param phone
     * @return
     */
    public boolean haveExistUser(String phone) {
        Cursor cursor = query(CDDbSchema.ChengdiTable.TABLE_USER, null, CDDbSchema.ChengdiTable.User.PHONE + "=?",
                new String[]{phone}, null, null, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }


    public void updateUser(User user, String phone) {
        update(CDDbSchema.ChengdiTable.TABLE_USER,
                SQLiteDbUtils.getUserContentValues(user), CDDbSchema.ChengdiTable.User.PHONE + "=?", new String[]{phone});
    }

    public User getUser(String phone) {
        User user = new User();
        Cursor cursor = query(CDDbSchema.ChengdiTable.TABLE_USER, null, CDDbSchema.ChengdiTable.User.PHONE + "=?", new String[]{phone}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            user.setUsername(cursor.getString(cursor.getColumnIndex(CDDbSchema.ChengdiTable.User.USERNAME)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(CDDbSchema.ChengdiTable.User.PHONE)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(CDDbSchema.ChengdiTable.User.PASSWORD)));
            user.setAge(cursor.getInt(cursor.getColumnIndex(CDDbSchema.ChengdiTable.User.AGE)));
            user.setPortrait(cursor.getString(cursor.getColumnIndex(CDDbSchema.ChengdiTable.User.PORTRAITURI)));
            user.setSex(""+cursor.getString(cursor.getColumnIndex(CDDbSchema.ChengdiTable.User.SEX)));
            user.setName(cursor.getString(cursor.getColumnIndex(CDDbSchema.ChengdiTable.User.NAME)));
            user.setEducation(cursor.getString(cursor.getColumnIndex(CDDbSchema.ChengdiTable.User.EDUCATION)));
            user.setJob(cursor.getString(cursor.getColumnIndex(CDDbSchema.ChengdiTable.User.JOB)));
            user.setHometown(cursor.getString(cursor.getColumnIndex(CDDbSchema.ChengdiTable.User.HOMETOWN)));
            user.setLocation(cursor.getString(cursor.getColumnIndex(CDDbSchema.ChengdiTable.User.LOCATION)));
            user.setSubScript(cursor.getString(cursor.getColumnIndex(CDDbSchema.ChengdiTable.User.SUBSCRIPT)));
            user.setScore(cursor.getInt(cursor.getColumnIndex(CDDbSchema.ChengdiTable.User.SCORE)));
            user.setHaveAuthentication(cursor.getInt(cursor.getColumnIndex(CDDbSchema.ChengdiTable.User.AUTHENTICATION)));
            cursor.moveToNext();
        }
        return user;
    }
}
