package com.raja.resturant.Model;

public class CartItemModel {
    private int cart_item_id;
    private int cart_item_quantity;

    public CartItemModel(int cart_item_id, int cart_item_quantity) {
        this.cart_item_id = cart_item_id;
        this.cart_item_quantity = cart_item_quantity;
    }

    public int getCart_item_id() {
        return cart_item_id;
    }

    public void setCart_item_id(int cart_item_id) {
        this.cart_item_id = cart_item_id;
    }

    public int getCart_item_quantity() {
        return cart_item_quantity;
    }

    public void setCart_item_quantity(int cart_item_quantity) {
        this.cart_item_quantity = cart_item_quantity;
    }
}
