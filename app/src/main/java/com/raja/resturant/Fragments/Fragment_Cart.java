package com.raja.resturant.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.raja.resturant.Controll.Adapter.MainCartRecyclerAdapter;
import com.raja.resturant.Controll.Interface.MyGetCartedItemsListener;
import com.raja.resturant.Controll.Interface.MyMainPageTitleSet;
import com.raja.resturant.Controll.LocalDatabase.MySQLiteManager;
import com.raja.resturant.Controll.MyFunctionBox;
import com.raja.resturant.MainActivity;
import com.raja.resturant.Model.CartItemModel;
import com.raja.resturant.Model.Category_List_Item_Model;
import com.raja.resturant.R;

import java.util.List;

public class Fragment_Cart extends Fragment implements MyGetCartedItemsListener{


    public Fragment_Cart() {

    }

    MyFunctionBox myFunctionBox;
    private MySQLiteManager mySQLiteManager;

    private List<CartItemModel> cartItemModelList;
    private RecyclerView rv_main_cart;
    private LinearLayout ll_place_order, ll_cart_container;
    private TextView tv_total, tv_cart_item_found_or_not;

    private MyMainPageTitleSet myMainPageTitleSet;
//    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

//        linearLayoutManager = new LinearLayoutManager(context);
        myMainPageTitleSet = (MyMainPageTitleSet) getActivity();
        myMainPageTitleSet.mSetPageTitle("Your Cart");

        myFunctionBox = new MyFunctionBox(getActivity());
        myFunctionBox.myGetCartedItemsListener = (MyGetCartedItemsListener) new Fragment_Cart();
        mySQLiteManager = new MySQLiteManager(getActivity());
    }

    private static View view;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        rv_main_cart =(RecyclerView) view.findViewById(R.id.main_cart_rv_id);
        tv_total = (TextView) view.findViewById(R.id.cart_total_id);
        tv_cart_item_found_or_not = (TextView) view.findViewById(R.id.cart_found_or_not_id);
        ll_cart_container = (LinearLayout) view.findViewById(R.id.cart_content_container_id);
        ll_place_order = (LinearLayout) view.findViewById(R.id.main_cart_place_order_btn_id);


        cartItemModelList = mGetAllCartData();

//        Log.d("data11", getActivity().toString()+",  k");

        if (cartItemModelList != null){
            if (cartItemModelList.size() > 0){
                myFunctionBox.mGetOnlyCartedItem(cartItemModelList, getActivity());
            }
        }



        return view;
    }

    @Override
    public void mGetCartedItems(List<CartItemModel> cartItemModelList, List<Category_List_Item_Model> foodItemList, FragmentActivity activity) {

        if (activity instanceof  MainActivity){

            Log.d("data11", activity.toString());
        }else {

            Log.d("data11", activity.toString());
        }



        if (activity != null){
            if (activity instanceof MainActivity){
                tv_total = (TextView) activity.findViewById(R.id.cart_total_id);
                ll_place_order = (LinearLayout) activity.findViewById(R.id.main_cart_place_order_btn_id);
//            rv_main_cart = (RecyclerView) activity.findViewById(R.id.main_cart_rv_id);
                rv_main_cart = (RecyclerView) activity.findViewById(R.id.main_cart_rv_id);

                if (rv_main_cart == null){
                    Log.d("data11", "nul");
                }else {
                    Log.d("data11", "not null");
                }
                rv_main_cart.setLayoutManager(new LinearLayoutManager(activity));

                getTotalCost(cartItemModelList, foodItemList);
                tv_cart_item_found_or_not = (TextView) activity.findViewById(R.id.cart_found_or_not_id);
                ll_cart_container = (LinearLayout) activity.findViewById(R.id.cart_content_container_id);

                ll_place_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myFunctionBox = new MyFunctionBox(activity);
                        myFunctionBox.mNotice("Thank you for your order!", "Thank, Our sales executive will get you soon!");
                    }
                });

                if (foodItemList.size() > 0){
                    MainCartRecyclerAdapter adapter = new MainCartRecyclerAdapter(activity,cartItemModelList, foodItemList);
                    rv_main_cart.setAdapter(adapter);
                    tv_cart_item_found_or_not.setVisibility(View.GONE);
                    ll_cart_container.setVisibility(View.VISIBLE);
                }else {
                    tv_cart_item_found_or_not.setVisibility(View.VISIBLE);
                    ll_cart_container.setVisibility(View.GONE);
                }

                Log.d("data11", "aaa");
            }else {

                Log.d("data11", "bbb");
            }


        }else {
            Log.d("data11", "else");

        }

    }





    private void getTotalCost(List<CartItemModel> cartItemModelList, List<Category_List_Item_Model> foodItemList){
        double total_cost = 0;
        for (int i = 0; i< cartItemModelList.size(); i++){
            total_cost += (double) cartItemModelList.get(i).getCart_item_quantity() * (double) mGetPrise(foodItemList.get(i));
        }
        total_cost = Math.round(total_cost*100)/100.0;
        tv_total.setText("$"+(total_cost+10.0));
    }

    private double mGetPrise(Category_List_Item_Model it) {
        int offer_percentage = it.getF_offer();
        if (offer_percentage>0){
            double actual_price = it.getF_price();
            double discounted_price = actual_price * ( 1- (double) offer_percentage /100 );
            discounted_price = Math.round(discounted_price*100)/100.0;
            return discounted_price;

        }else {
            double d =  it.getF_price();
            d = Math.round(d*100)/100.0;
            return d;
        }
    }


    public List<CartItemModel> mGetAllCartData() {
        FirebaseAuth auth = myFunctionBox.getAuth();
        if (auth != null){
            FirebaseUser fuser = auth.getCurrentUser();
            if (fuser != null){

                mySQLiteManager.mUserExistOrNot(fuser.getEmail());;

                long user_id = mySQLiteManager.sessionManagement.getUserId();
                if (user_id != -1){
                    cartItemModelList = mySQLiteManager.mGetAllCartItems(user_id);
                    if (cartItemModelList != null && cartItemModelList.size() > 0){
                        return cartItemModelList;
                    }
                }
            }

        }

        return null;
    }

}