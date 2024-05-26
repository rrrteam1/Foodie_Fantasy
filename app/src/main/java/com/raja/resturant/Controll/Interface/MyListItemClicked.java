package com.raja.resturant.Controll.Interface;

import com.raja.resturant.Model.Category_List_Item_Model;

public interface MyListItemClicked {
    void mGetListItemDataToDisplayInDetails(int position, Category_List_Item_Model model_item, String who);
}
