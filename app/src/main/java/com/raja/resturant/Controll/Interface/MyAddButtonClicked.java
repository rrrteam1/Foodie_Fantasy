package com.raja.resturant.Controll.Interface;

import com.raja.resturant.Model.Category_List_Item_Model;
import com.raja.resturant.View.FoodItemDetails;

public interface MyAddButtonClicked {

//    void CheckClickedItem(int position, Category_List_Item_Model model, Resources resources);
    void CheckClickedItem(Category_List_Item_Model model, boolean isWantToDelete, int qnt, FoodItemDetails foodItemDetails, String who);
//    void CheckClickedItem(Category_List_Item_Model model, boolean isAlreadyAdded, int qnt);
//    void CheckClickedItem(int position, Category_List_Item_Model model);
}
