package com.raja.resturant.Controll.LocalDatabase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserManagement {
    private SQLiteDatabase db;

    public UserManagement(SQLiteDatabase db) {
        this.db = db;
    }
    @SuppressLint("Range")
    public long gettingUserId(String email) {

//        String query = "SELECT userId FROM User WHERE email = '" + email + "'";
        String query = "SELECT "+DatabaseHelper.COLUMN_USER_ID+" FROM "+DatabaseHelper.USER_TABLE_NAME+" WHERE "+DatabaseHelper.COLUMN_USER_MAIL+" = '" + email + "'";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            long userId = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_ID));
            cursor.close();
            return userId;
        } else {
            // If user doesn't exist, insert the new email into the User table
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_USER_MAIL, email);
            return db.insert(DatabaseHelper.USER_TABLE_NAME, null, values);
        }
    }
}
