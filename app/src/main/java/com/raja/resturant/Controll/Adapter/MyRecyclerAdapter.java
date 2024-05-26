package com.raja.resturant.Controll.Adapter;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raja.resturant.Controll.Interface.MyAddButtonClicked;
import com.raja.resturant.Controll.Interface.MyListItemClicked;
import com.raja.resturant.Model.CartItemModel;
import com.raja.resturant.Model.Category_List_Item_Model;
import com.raja.resturant.R;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private List<Category_List_Item_Model>  cat_model;
    private static int p = 0;
    private MyAddButtonClicked myAddButtonClicked;
    private MyListItemClicked myListItemClicked;

    private FragmentActivity activity;
    private List<CartItemModel> cartList;
    private boolean isAlreadyAdded;
//    public MyRecyclerAdapter(FragmentActivity activity, List<Category_List_Item_Model> cat_model) {
//        this.activity = activity;
//        this.cat_model = cat_model;
//        myAddButtonClicked = (MyAddButtonClicked) activity;
//        myListItemClicked = (MyListItemClicked) activity;
//
//    }

    public MyRecyclerAdapter(FragmentActivity activity, List<Category_List_Item_Model> modelList, List<CartItemModel> cartList) {
        this.activity = activity;
        this.cat_model = modelList;
        myAddButtonClicked = (MyAddButtonClicked) activity;
        myListItemClicked = (MyListItemClicked) activity;
        this.cartList = cartList;
    }


    @NonNull
    @Override
    public MyRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_list_item_demo, null, false);
        MyViewHolder viewHolder = new MyViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerAdapter.MyViewHolder holder, int position) {
        Category_List_Item_Model model = cat_model.get(position);
        //Log.d("data3", cat_model.size()+":    m");


        holder.tvItemTitle.setText(model.getF_name().toString());
        mSetPriceAndOffer(holder, model);

        mCheckCartAndDoNessecery(holder, position, model);

        holder.ivFavBtn.setImageResource(R.drawable.icon_fav_gray);

        Glide
                .with(activity)
                .load(model.getF_img())
                .centerCrop()
                .placeholder(R.drawable.cat_item_img_loading_placeholder)
                .into(holder.ivItemImg);

        int pos = position;
        holder.flAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.getF_id();
                myAddButtonClicked.CheckClickedItem( model, isAlreadyInCart(model), 1, null, "home");
//                mAddBtnDesignChange(isAlreadyAdded, holder);

            }
        });
        holder.cvItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListItemClicked.mGetListItemDataToDisplayInDetails(pos, model, "dtls");
            }
        });

//        int a = Integer.parseInt(holder.c.getText().toString());
        holder.c.setText(String.valueOf(++p));

    }

    private void mCheckCartAndDoNessecery(MyViewHolder holder, int position, Category_List_Item_Model model) {
        if (cartList != null){
            for (CartItemModel cm: cartList){
                if (model.getF_id() == cm.getCart_item_id()){
                    isAlreadyAdded = true;
                    holder.ivAddBtnBg.setImageResource(R.drawable.cat_item_add_btn_selected_bg);
                    holder.ivAddBtn.setImageResource(R.drawable.icon_minus);
                }


            }
        }
    }
    private boolean isAlreadyInCart(Category_List_Item_Model model) {
        boolean b = false;
        if (cartList != null){
            for (CartItemModel cm: cartList){
                if (model.getF_id() == cm.getCart_item_id()){
                    b = true;
                }
            }
        }
        return b;
    }

    private void mSetPriceAndOffer(MyViewHolder holder, Category_List_Item_Model model){
//        holder.tvItemOffer
//        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        int offer_percentage = model.getF_offer();
        if (offer_percentage>0){
            double actual_price = model.getF_price();

//            Discounted Price = Original Price (1 - Discount Percentage / 100)
            double discounted_price = actual_price * ( 1- (double) offer_percentage /100 );
            discounted_price = Math.round(discounted_price*100)/100.0;
            holder.tvItemPrice.setText("$"+discounted_price);

            holder.tvItemPrevPrice.setPaintFlags(holder.tvItemPrevPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvItemPrevPrice.setText("$"+actual_price);
            holder.tvItemOffer.setText(offer_percentage+"% off");

            holder.tvItemPrevPrice.setVisibility(View.VISIBLE);
            holder.offerBG.setVisibility(View.VISIBLE);

        }else {
            holder.tvItemPrice.setText("$"+String.valueOf(model.getF_price()));
            holder.tvItemPrevPrice.setVisibility(View.GONE);
            holder.offerBG.setVisibility(View.GONE);
        }

    };



    @Override
    public int getItemCount() {
        return cat_model.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemTitle,tvItemPrice,tvItemOffer,tvItemPrevPrice, c;
        private ImageView ivItemImg, ivFavBtn, ivAddBtn, ivAddBtnBg;
        private FrameLayout flAddBtn, offerBG;
        private CardView cvItemButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvItemTitle = (TextView) itemView.findViewById(R.id.cat_item_tv_title_id);
            tvItemPrice = (TextView) itemView.findViewById(R.id.cat_item_tv_price_id);
            tvItemPrevPrice = (TextView) itemView.findViewById(R.id.cat_item_tv_prev_price_id);
            c = (TextView) itemView.findViewById(R.id.count);
            tvItemOffer = (TextView) itemView.findViewById(R.id.cat_item_tv_offer_id);
            ivItemImg = (ImageView) itemView.findViewById(R.id.cat_item_iv_img_id);
            ivAddBtn = (ImageView) itemView.findViewById(R.id.iv_cat_item_add_btn_icon_id);
            ivAddBtnBg = (ImageView) itemView.findViewById(R.id.iv_cat_item_add_btn_bg_id);
            cvItemButton = (CardView) itemView.findViewById(R.id.cat_item_cv_card_id);
            ivFavBtn = (ImageView) itemView.findViewById(R.id.cat_item_iv_fav_btn_id);
            flAddBtn = (FrameLayout) itemView.findViewById(R.id.cat_item_fl_add_btn_id);
            offerBG = (FrameLayout) itemView.findViewById(R.id.cat_item_fl_offer_bg_id);

        }
    }
}
