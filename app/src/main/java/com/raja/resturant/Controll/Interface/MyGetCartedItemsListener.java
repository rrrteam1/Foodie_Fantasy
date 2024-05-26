package com.raja.resturant.Controll.Interface;

import androidx.fragment.app.FragmentActivity;

import com.raja.resturant.Model.CartItemModel;
import com.raja.resturant.Model.Category_List_Item_Model;

import java.util.List;

public interface MyGetCartedItemsListener {
    void mGetCartedItems(List<CartItemModel> cartItemModelList, List<Category_List_Item_Model> foodItemList, FragmentActivity activity);
//    void mGetCartedItems(List<CartItemModel> cartItemModelList, List<Category_List_Item_Model> foodItemList, FragmentActivity activity);
//    void mGetCartedItems(List<CartItemModel> cartItemModelList, List<Category_List_Item_Model> foodItemList, FragmentActivity activity);
}
