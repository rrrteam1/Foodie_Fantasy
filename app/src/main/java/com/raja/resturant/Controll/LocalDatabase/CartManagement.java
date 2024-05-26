package com.raja.resturant.Controll.LocalDatabase;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CartManagement{
    private SQLiteDatabase db;

    public CartManagement(SQLiteDatabase db) {
        this.db = db;
    }

    @SuppressLint("Range")
    public boolean addOrUpdateItemToCart(long userId, int itemID, int itemQuantity) {
        try {
            String selectQuery = "SELECT " + DatabaseHelper.COLUMN_CART_ITEM_QUANTITY +
                    " FROM " + DatabaseHelper.CART_TABLE_NAME +
                    " WHERE " + DatabaseHelper.COLUMN_USER_ID + " = " + userId +
                    " AND " + DatabaseHelper.COLUMN_CART_ITEM_ID + " = " + itemID;

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {
                //there is my previous cart item already added, so need to update
                String updateQuery = "UPDATE " + DatabaseHelper.CART_TABLE_NAME +
                        " SET " + DatabaseHelper.COLUMN_CART_ITEM_QUANTITY + " = " + itemQuantity +
                        " WHERE " + DatabaseHelper.COLUMN_USER_ID + " = " + userId +
                        " AND " + DatabaseHelper.COLUMN_CART_ITEM_ID + " = " + itemID;

                db.execSQL(updateQuery);
            } else {
                //there is no item in cart, so i will add it in cart
                String insertQuery = "INSERT INTO " + DatabaseHelper.CART_TABLE_NAME + " (" +
                        DatabaseHelper.COLUMN_USER_ID + ", " +
                        DatabaseHelper.COLUMN_CART_ITEM_ID + ", " +
                        DatabaseHelper.COLUMN_CART_ITEM_QUANTITY + ") VALUES (" +
                        userId + ", " + itemID + ", " + itemQuantity + ")";

                db.execSQL(insertQuery);
            }

            if (cursor != null) {
                cursor.close();
            }

            //ei porjonto asche mane sob ok
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            //eikhan e asche mane kisu ok na, problem ase...
            return false;
        }
    }



    public int removeItemFromCart(long userId, int itemID) {

        String remove_sql = "DELETE FROM " + DatabaseHelper.CART_TABLE_NAME +
                " WHERE " + DatabaseHelper.COLUMN_USER_ID + " = " + userId +
                " AND " + DatabaseHelper.COLUMN_CART_ITEM_ID + " = " + itemID;

        try {
            db.execSQL(remove_sql);
            return  1;
        } catch (SQLiteException e) {
            return -1;
        }


    }

    public long removeAllItemFromCart(long userId) {
        String remove_all_sql = "DELETE FROM " + DatabaseHelper.CART_TABLE_NAME +
                " WHERE " + DatabaseHelper.COLUMN_USER_ID + " = " + userId;

        try {
            db.execSQL(remove_all_sql);
            return  1;
        } catch (SQLiteException e) {
            return -1;
        }

    }

    public Cursor getCartItems(long userId) {
        // Construct the query string
        String query = "SELECT * FROM "+DatabaseHelper.CART_TABLE_NAME+" WHERE "+DatabaseHelper.COLUMN_CART_USER_ID+" = " + userId;

        return db.rawQuery(query, null);
    }




}
