package com.bcgtgjyb.snack.bigwen.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bcgtgjyb.snack.bigwen.base.MyApplication;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigwen on 2016/5/20.
 */
public class BwSqlUtil {

    private String DBNAME = "biwen";
    private int version = 1;
    private SQLiteDatabase db;
    private static BwSqlUtil bwSqlUtil;
    private String TAG = BwSqlUtil.class.getSimpleName();


    public static BwSqlUtil getInstance() {
        if (bwSqlUtil == null) {
            synchronized (BwSqlUtil.class) {
                if (bwSqlUtil == null) {
                    bwSqlUtil = new BwSqlUtil();
                }
            }
        }
        return bwSqlUtil;
    }

    private BwSqlUtil() {
        BwSqliteHelper bwSqliteHelper = new BwSqliteHelper(MyApplication.getContext(), DBNAME, null, version);
        db = bwSqliteHelper.getWritableDatabase();
    }


    public void saveObject(Object object) {
        Field[] fs = object.getClass().getFields();
        ContentValues contentValues = new ContentValues();
        try {
            for (Field field : fs) {
                contentValues.put(field.getName(), field.get(object).toString());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        db.replace(object.getClass().getSimpleName(), null, contentValues);
    }

    public void saveObjects(List<Object> objects){
        for (Object o:objects){
            saveObject(o);
        }
    }

    public List<Object> loadObject(Object o) {
        Cursor cursor = db.query(o.getClass().getSimpleName(), null, null, null, null, null, null);
        Field[] fs = o.getClass().getFields();
        List<Object> objects = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                try {
                    Object oo = o.getClass().newInstance();
                    for (Field field : fs) {
                        if (field.getType() == Integer.class || field.getType() == int.class) {
                            int t = cursor.getInt(cursor.getColumnIndex(field.getName()));
                            try {
                                field.set(oo, t);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        } else if (field.getType() == String.class) {
                            String t = cursor.getString(cursor.getColumnIndex(field.getName()));
                            try {
                                field.set(oo, t);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }else if (field.getType() == Long.class || field.getType() == long.class){
                            long t = cursor.getLong(cursor.getColumnIndex(field.getName()));
                            try {
                                field.set(oo, t);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    objects.add(oo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        return objects;
    }
}
