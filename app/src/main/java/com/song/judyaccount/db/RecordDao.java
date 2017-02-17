package com.song.judyaccount.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.song.judyaccount.model.RecordBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Judy on 2017/2/16.
 */

public class RecordDao {
    private RecordDBHelper mRecordDBHelper;

    public RecordDao(Context context) {
        mRecordDBHelper = new RecordDBHelper((context));
    }


    public void insertRecord(boolean isIncome, int type, double money, String des, long time) {
        SQLiteDatabase db = mRecordDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (isIncome) {
            values.put("_budget", 0);
        } else {
            values.put("_budget", 1);
        }
        values.put("_type", type);
        values.put("_money", money);
        values.put("_des", des);
        values.put("_time", time);
        db.insert("t_record", null, values);
        db.close();
    }

    public void deleteRecord(long time) {
        SQLiteDatabase db = mRecordDBHelper.getWritableDatabase();
        db.delete("t_record", "_time = ?", new String[] {time+""});
        db.close();
    }

    public RecordBean queryRecord(long time) {
        RecordBean recordBean = new RecordBean();
        SQLiteDatabase db = mRecordDBHelper.getWritableDatabase();
        Cursor cursor = db.query("t_record", new String[]{"_budget","_type","_money","_des"}, "_time = ?", new String[] {time+""}, null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            int isIncome = cursor.getInt(0);
            int type = cursor.getInt(1);
            double money = cursor.getDouble(2);
            String des = cursor.getString(3);

            recordBean.isIncome = isIncome == 0;
            recordBean.type = type;
            recordBean.money = money;
            recordBean.des = des;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            recordBean.calendar = calendar;
        }
        cursor.close();
        db.close();
        return recordBean;
    }

    public List<RecordBean> queryAllRecord() {
        List<RecordBean> recordBeanList = new ArrayList<>();
        SQLiteDatabase db = mRecordDBHelper.getWritableDatabase();
        Cursor cursor = db.query("t_record", new String[]{"_budget","_type","_money","_des", "_time"}, null, null, null, null, "_time desc");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                RecordBean recordBean = new RecordBean();
                int isIncome = cursor.getInt(0);
                int type = cursor.getInt(1);
                double money = cursor.getDouble(2);
                String des = cursor.getString(3);
                long time = cursor.getLong(4);

                recordBean.isIncome = isIncome == 0;
                recordBean.type = type;
                recordBean.money = money;
                recordBean.des = des;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(time);
                recordBean.calendar = calendar;
                recordBeanList.add(recordBean);
            }
        }
        cursor.close();
        db.close();
        return recordBeanList;
    }

    public void updateRecord(boolean isIncome, int type, double money, String des, long time) {
        SQLiteDatabase db = mRecordDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (isIncome) {
            values.put("_budget", 0);
        } else {
            values.put("_budget", 1);
        }
        values.put("_type", type);
        values.put("_money", money);
        values.put("_des", des);
        values.put("_time", time);
        db.update("t_record", values, "_time = ?", new String[] {time+""});
        db.close();
    }

}
