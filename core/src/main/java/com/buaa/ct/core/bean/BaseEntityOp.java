package com.buaa.ct.core.bean;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10202 on 2015/11/18.
 */
public class BaseEntityOp<T> {
    protected SQLiteDatabase db;

    protected void getDatabase() {
    }

    public void saveItemImpl(T data) {

    }

    public void saveData(T data) {
        getDatabase();
        saveItemImpl(data);
        db.close();
    }

    public void saveData(List<T> datas) {
        if (datas == null || datas.isEmpty()) {
            return;
        }
        getDatabase();
        int size = datas.size();
        db.beginTransaction();
        try {
            for (int i = 0; i < size; i++) {
                saveItemImpl(datas.get(i));
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public String getSearchCondition() {
        return "";
    }

    public T fillData(@NonNull Cursor cursor) {
        return null;
    }

    public List<T> fillDatas(@NonNull Cursor cursor) {
        List<T> datas = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            datas.add(fillData(cursor));
        }
        cursor.close();
        db.close();
        return datas;
    }
}
