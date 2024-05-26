package com.raja.resturant.Model;

public class Category_List_Item_Model {
    private int f_id;
    private int cat_id;
    private String f_name;
    private String f_desc;
    private double f_price;
    private int f_offer;
    private String f_img;

//    public Category_List_Item_Model(int f_id, int cat_id, String f_name, String f_desc, int f_price, int f_offer, String f_img) {
    public Category_List_Item_Model(int f_id, int cat_id, String f_name, String f_desc, double f_price, int f_offer, String f_img) {
        this.f_id = f_id;
        this.cat_id = cat_id;
        this.f_name = f_name;
        this.f_desc = f_desc;
        this.f_price = f_price;
        this.f_offer = f_offer;
        this.f_img = f_img;
    }


    public Category_List_Item_Model() {
    }

    public int getF_id() {
        return f_id;
    }

    public void setF_id(int f_id) {
        this.f_id = f_id;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getF_desc() {
        return f_desc;
    }

    public void setF_desc(String f_desc) {
        this.f_desc = f_desc;
    }

    public double getF_price() {
        return f_price;
    }

    public void setF_price(int f_price) {
        this.f_price = f_price;
    }

    public int getF_offer() {
        return f_offer;
    }

    public void setF_offer(int f_offer) {
        this.f_offer = f_offer;
    }

    public String getF_img() {
        return f_img;
    }

    public void setF_img(String f_img) {
        this.f_img = f_img;
    }
}
