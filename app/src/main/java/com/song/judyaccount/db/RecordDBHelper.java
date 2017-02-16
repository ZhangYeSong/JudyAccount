package com.song.judyaccount.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Judy on 2017/2/16.
 */

public class RecordDBHelper extends SQLiteOpenHelper {
    public RecordDBHelper(Context context) {
        this(context, "Records.db", null, 1);
    }

    public RecordDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table t_record (_id integer primary key, _budget integer, _type integer, _money real, _des text, _time integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
