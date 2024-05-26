package com.raja.resturant.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.raja.resturant.Controll.Adapter.MyRecyclerAdapter;
import com.raja.resturant.MainActivity;
import com.raja.resturant.Model.CartItemModel;
import com.raja.resturant.Model.Category_List_Item_Model;
import com.raja.resturant.R;

import java.util.List;

public class FragmentCategoryItem extends Fragment {

    private List<Category_List_Item_Model> modelList;
    private String current_list_title;
    private RecyclerView rv_cat_list;
    private TextView tv_title;
    private MyRecyclerAdapter adapter;
    LinearLayout ll_prog;
//    private Activity activity;

    private Context context;
    private List<CartItemModel> cartList;

    public FragmentCategoryItem() {
    }

    public FragmentCategoryItem(List<Category_List_Item_Model> categoryListItemList, String titleOfCurrentList, List<CartItemModel> cartList) {
        this.modelList = categoryListItemList;
        this.current_list_title = titleOfCurrentList;
        this.cartList = cartList;
    }

    public FragmentCategoryItem(List<Category_List_Item_Model> categoryListItemList, String title_of_current_list) {
        this.modelList = categoryListItemList;
        this.current_list_title = title_of_current_list;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category_item, container, false);
        tv_title = (TextView) view.findViewById(R.id.cat_single_item_tv_title_id);
        rv_cat_list = (RecyclerView) view.findViewById(R.id.cat_single_item_rv_id);
        ll_prog = (LinearLayout) view.findViewById(R.id.second_progress_bg_id);

        tv_title.setText(current_list_title);

        rv_cat_list.setLayoutManager(new LinearLayoutManager(requireActivity()));

        adapter = new MyRecyclerAdapter(requireActivity(), modelList, cartList);
        rv_cat_list.setAdapter(adapter);

        return view;
    }

}