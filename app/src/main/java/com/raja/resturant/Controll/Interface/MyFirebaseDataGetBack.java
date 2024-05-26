package com.raja.resturant.Controll.Interface;

import androidx.fragment.app.FragmentActivity;

import com.raja.resturant.Model.MySingleListModel;

import java.util.List;

public interface MyFirebaseDataGetBack {
    void mGetFirebaseDataReady(List<MySingleListModel> mySingleListModelList, FragmentActivity fragmentActivity);
//    void mGetFirebaseDataReady(List<MySingleListModel> mySingleListModelList, FragmentActivity fragmentActivity);

//    void mGetFirebaseDataReady(List<String> list_of_title, List<List<Category_List_Item_Model>> list_of_list, FragmentActivity fragmentActivity);
}
