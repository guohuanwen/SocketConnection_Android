package com.bcgtgjyb.snack.bigwen.sqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bigwen on 2016/5/20.
 */
public class BwSqliteHelper extends SQLiteOpenHelper {

    private String CHATMESSAGE = "create table BaseMessage ( "  +
            " chat_text text , "+
            " sendId text , " +
            " type  text, " +
            " receiverId text , " +
            " isSendSuccess text , "+
            " chat_time text primary key )";

    public BwSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public BwSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CHATMESSAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
