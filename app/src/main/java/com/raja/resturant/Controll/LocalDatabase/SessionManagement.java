package com.raja.resturant.Controll.LocalDatabase;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserId(long userId) {
        editor.putLong(DatabaseHelper.COLUMN_USER_ID, userId);
        editor.apply();
    }

    public long getUserId() {
        return sharedPreferences.getLong(DatabaseHelper.COLUMN_USER_ID, -1);
    }

    public void clearUserId() {
        editor.remove(DatabaseHelper.COLUMN_USER_ID);
        editor.apply();
    }
}
