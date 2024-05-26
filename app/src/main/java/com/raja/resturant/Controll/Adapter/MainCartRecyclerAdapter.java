package com.raja.resturant.Controll.Adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainCartRecyclerAdapter extends RecyclerView.Adapter<MainCartRecyclerAdapter.MyViewHolder> {

//    private Context context ;
    FragmentActivity fragmentActivity;
    private List<Category_List_Item_Model> modelList;
    private List<CartItemModel> cartItemList;
    private MyListItemClicked myListItemClicked;

    private MyAddButtonClicked myAddButtonClicked;

    public MainCartRecyclerAdapter(FragmentActivity activity, List<CartItemModel> cartItemModelList, List<Category_List_Item_Model> foodItemList) {
        this.fragmentActivity = activity;
        this.modelList = foodItemList;
        this.cartItemList = cartItemModelList;
        myAddButtonClicked = (MyAddButtonClicked) activity;
        myListItemClicked = (MyListItemClicked) activity;
    }



    @NonNull
    @Override
    public MainCartRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_cart_item_demo, null, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainCartRecyclerAdapter.MyViewHolder holder, int position) {
        Category_List_Item_Model model = modelList.get(position);
        CartItemModel cartItem = cartItemList.get(position);

        holder.tvName.setText(model.getF_name().toString());
        holder.tvQnt.setText(String.valueOf(cartItem.getCart_item_quantity()));
        mSetPriceAndOffer(holder, model, cartItem);


        Glide
                .with(fragmentActivity)
                .load(model.getF_img())
                .centerCrop()
                .placeholder(R.drawable.cat_item_img_loading_placeholder)
                .into(holder.ivImg);


        holder.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAddButtonClicked.CheckClickedItem(model, true, 1, null, "cart");
            }
        });
        int pos = position;
        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListItemClicked.mGetListItemDataToDisplayInDetails(pos, model, "cart");
            }
        });

    }




    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImg, ivCross;
        private TextView tvName, tvPrice, tvActualPrice, tvOffer, tvQnt, tvCost;
        private CardView cvItem;

        private FrameLayout  flOfferBG;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImg = (ImageView) itemView.findViewById(R.id.main_cat_item_iv_img_id);
            ivCross = (ImageView) itemView.findViewById(R.id.main_cat_item_cross);
            tvName = (TextView) itemView.findViewById(R.id.main_cat_item_tv_title_id);
            tvPrice = (TextView) itemView.findViewById(R.id.main_cat_item_tv_price_id);
            tvOffer = (TextView) itemView.findViewById(R.id.main_cat_item_tv_offer_id);
            tvActualPrice = (TextView) itemView.findViewById(R.id.main_cat_item_tv_prev_price_id);
            tvQnt = (TextView) itemView.findViewById(R.id.main_cat_item_tv_count_id);
            tvCost = (TextView) itemView.findViewById(R.id.main_cat_item_tv_total_cost_id);
            flOfferBG = (FrameLayout) itemView.findViewById(R.id.main_cat_item_fl_offer_bg_id);
            cvItem = (CardView) itemView.findViewById(R.id.main_cat_item_cv_card_id);

        }
    }



    private void mSetPriceAndOffer(MyViewHolder holder, Category_List_Item_Model model, CartItemModel cartItem){

        int offer_percentage = model.getF_offer();
        if (offer_percentage>0){
            double actual_price = model.getF_price();

//            Discounted Price = Original Price (1 - Discount Percentage / 100)
            double discounted_price = actual_price * ( 1- (double) offer_percentage /100 );
            discounted_price = Math.round(discounted_price*100)/100.0;
            holder.tvPrice.setText("$"+discounted_price);

            double total_cost = discounted_price * cartItem.getCart_item_quantity();
            total_cost = Math.round(total_cost*100)/100.0;
            holder.tvCost.setText("$"+total_cost);

            holder.tvActualPrice.setPaintFlags(holder.tvActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvActualPrice.setText("$"+actual_price);

            holder.tvOffer.setText(offer_percentage+"% off");

            holder.tvActualPrice.setVisibility(View.VISIBLE);
            holder.flOfferBG.setVisibility(View.VISIBLE);

        }else {
            holder.tvPrice.setText("$"+String.valueOf(model.getF_price()));

            double total_cost = model.getF_price() * cartItem.getCart_item_quantity();
            total_cost = Math.round(total_cost*100)/100.0;
            holder.tvCost.setText("$"+total_cost);

            holder.tvActualPrice.setVisibility(View.GONE);
            holder.flOfferBG.setVisibility(View.GONE);
        }

    };

}
