package com.raja.resturant.Controll.LocalDatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.UserManager;

import com.raja.resturant.Model.CartItemModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MySQLiteManager {
    Context context;
    public DatabaseHelper dbHelper;
    public UserManagement userManagement;
    public CartManagement cartManagement;
    public SessionManagement sessionManagement;

    public MySQLiteManager() {}

    public MySQLiteManager(Context context) {
        this.context = context;
        mSQLiteInit();
    }

    public void mSQLiteInit(){
        dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        userManagement = new UserManagement(db);
        cartManagement = new CartManagement(db);
        sessionManagement = new SessionManagement(context);
    };
    public void mUserExistOrNot(String mail){
        long userId = userManagement.gettingUserId(mail);
        sessionManagement.saveUserId(userId);
    };

    public boolean mAddCart(long uid, int itemID, int qnt){
        return cartManagement.addOrUpdateItemToCart(uid,itemID, qnt);
    };
    public long mRemoveCart(long uid, int itemID){
        return cartManagement.removeItemFromCart(uid,itemID);
    };
    public long mRemoveCartAll(long uid){
        return cartManagement.removeAllItemFromCart(uid);
    };
    @SuppressLint("Range")
    public List<CartItemModel> mGetAllCartItems(long uid){

        List<CartItemModel> allCartItem = new ArrayList<>();
        Cursor cursor = cartManagement.getCartItems(uid);
        while (cursor.moveToNext()){
            int item_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_CART_ITEM_ID));
            int item_quantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_CART_ITEM_QUANTITY));
            allCartItem.add(new CartItemModel(item_id, item_quantity));
        }
        cursor.close();
        return allCartItem;
    };

}
