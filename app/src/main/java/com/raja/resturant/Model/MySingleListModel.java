package com.raja.resturant.Model;

import java.util.List;

public class MySingleListModel {
    private String single_title;
    private List<Category_List_Item_Model> single_list;

    public MySingleListModel() {
    }

    public MySingleListModel(String single_title, List<Category_List_Item_Model> single_list) {
        this.single_title = single_title;
        this.single_list = single_list;
    }

    public String getSingle_title() {
        return single_title;
    }

    public void setSingle_title(String single_title) {
        this.single_title = single_title;
    }

    public List<Category_List_Item_Model> getSingle_list() {
        return single_list;
    }

    public void setSingle_list(List<Category_List_Item_Model> single_list) {
        this.single_list = single_list;
    }
}
