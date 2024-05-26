package com.raja.resturant.Controll.LocalDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "restaurant.db";
    private static final int DATABASE_VERSION = 1;
    public static final String USER_TABLE_NAME = "User";
    public static final String CART_TABLE_NAME = "Cart";
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_USER_MAIL = "userMail";
    public static final String COLUMN_CART_ID = "cartId";
    public static final String COLUMN_CART_USER_ID = "userId";
    public static final String COLUMN_CART_ITEM_ID = "cartItemId";
    public static final String COLUMN_CART_ITEM_QUANTITY = "cartItemQuantity";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Cart");
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE "+USER_TABLE_NAME+" ("+
                COLUMN_USER_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_USER_MAIL+" TEXT UNIQUE NOT NULL)";

        String createCartTable = "CREATE TABLE "+CART_TABLE_NAME+" ("+
                COLUMN_CART_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_CART_USER_ID+" INTEGER, "+
                COLUMN_CART_ITEM_ID+" INTEGER, "+
                COLUMN_CART_ITEM_QUANTITY+" INTEGER, FOREIGN KEY ('"+COLUMN_CART_USER_ID+"') REFERENCES User('"+COLUMN_USER_ID+"'))";



        db.execSQL(createUserTable);
        db.execSQL(createCartTable);
    }
}
