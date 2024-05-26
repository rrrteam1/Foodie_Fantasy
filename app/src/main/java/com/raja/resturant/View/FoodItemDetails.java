package com.raja.resturant.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.raja.resturant.Controll.Interface.MyAddButtonClicked;
import com.raja.resturant.Controll.Interface.MyAllAddToCart;
import com.raja.resturant.Controll.LocalDatabase.MySQLiteManager;
import com.raja.resturant.Controll.MyFunctionBox;
import com.raja.resturant.MainActivity;
import com.raja.resturant.Model.CartItemModel;
import com.raja.resturant.Model.Category_List_Item_Model;
import com.raja.resturant.R;

import java.util.List;

public class FoodItemDetails extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back, iv_fav, iv_food_img;
    private TextView tv_f_name,tv_f_price,tv_f_actual_price,tv_f_offer,tv_f_desc,tv_f_cart_qnt,tv_f_total_count,tv_f_total_cost;
    private LinearLayout ll_item_plus, ll_item_minus, ll_item_add_to_cart, ll_mid;
    private FrameLayout fl_offer_bg;
    private RelativeLayout rl_secrete;
    private ConstraintLayout cl_magic;
    private int CartItemQuantity = 1;


    private String f_name;
    private String f_desc;
    private String f_img;
    private Double f_price;
    private int f_offer;
    private int f_id;
    private int f_cartid;
    private int item_position;
    private int item_cart_count;
    private int clickCount = 0;

    private Category_List_Item_Model model;
    SubsamplingScaleImageView ivZoom;
    private MyAllAddToCart myAllAddToCart;
    private MyCallForGotoHome myCallForGotoHome;
    private int position;
    private List<CartItemModel> cartItemModelList;
    private String WHO;

    public MyAddButtonClicked myAddButtonClicked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_food_item_details);

        myAddButtonClicked = (MyAddButtonClicked) new MainActivity();
        myCallForGotoHome = (MyCallForGotoHome) new MainActivity();

        init();

        Intent intent =  getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            f_name = bundle.getString("fname");
            f_desc = bundle.getString("fdesc");
            f_img = bundle.getString("fimg");
            WHO = bundle.getString("who");

            f_price = bundle.getDouble("fprice");

            f_offer = bundle.getInt("foffer");
            f_id = bundle.getInt("fid");
            f_cartid = bundle.getInt("fcartid");
            item_position = bundle.getInt("position");
            item_cart_count = bundle.getInt("count_cart");
            CartItemQuantity = item_cart_count>0?item_cart_count:1;

            this.position = item_position;
            this.model = new Category_List_Item_Model(f_id,f_cartid,f_name,f_desc,f_price,f_offer,f_img);

            mSetValue();
        }


        ll_item_plus.setOnClickListener(this);
        ll_item_minus.setOnClickListener(this);
        ll_item_add_to_cart.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_fav.setOnClickListener(this);
        iv_food_img.setOnClickListener(this);
        rl_secrete.setOnClickListener(this);
        ll_mid.setOnClickListener(this);
        cl_magic.setOnClickListener(this);

    }
    private void mPlus(){
        ++CartItemQuantity;
        tv_f_cart_qnt.setText(String.valueOf(CartItemQuantity));
        tv_f_total_count.setText(String.valueOf(CartItemQuantity));
        tv_f_total_cost.setText(mCalculateTotalCost(CartItemQuantity, mGetCurrentPrice()));
    }
    private void mMinus(){
        if (CartItemQuantity > 1){
            --CartItemQuantity;
            tv_f_cart_qnt.setText(String.valueOf(CartItemQuantity));
            tv_f_total_count.setText(String.valueOf(CartItemQuantity));
            tv_f_total_cost.setText(mCalculateTotalCost(CartItemQuantity, mGetCurrentPrice()));

//            tv_f_total_cost.setText(mCalculateTotalCost(CartItemQuantity, model.getF_price()));
        }
    }
    private void mDefaultQntAndCost(){

        double actual_price = model.getF_price();
//      Discounted Price = Original Price (1 - Discount Percentage / 100)
        double discounted_price = actual_price * ( 1- (double) model.getF_offer() /100 );
        discounted_price = Math.round(discounted_price*100)/100.0;

        tv_f_total_count.setText(String.valueOf(CartItemQuantity));
        tv_f_cart_qnt.setText(String.valueOf(CartItemQuantity));
        tv_f_total_cost.setText(mCalculateTotalCost(CartItemQuantity, mGetCurrentPrice()));
//        tv_f_total_cost.setText("$"+discounted_price);

    };
    private double mGetCurrentPrice(){
        double actual_price = model.getF_price();
        double discounted_price = actual_price * ( 1- (double) model.getF_offer() /100 );
        discounted_price = Math.round(discounted_price*100)/100.0;
        return discounted_price;
    };
    private String mCalculateTotalCost(int qnt, double price){
        double cost = (double) qnt * price;
        cost = Math.round(cost*100)/100.0;
        return "$"+cost;
    };
    private void mSetValue() {

        mDefaultQntAndCost();

        tv_f_name.setText(model.getF_name());

        mSetPriceAndOfferInDetails();
        tv_f_desc.setText(model.getF_desc());
        Glide
                .with(getApplicationContext())
                .load(model.getF_img())
                .centerCrop()
                .placeholder(R.drawable.cat_item_img_loading_placeholder)
                .into(iv_food_img);


    }
    private void mSetPriceAndOfferInDetails(){

        int offer_percentage = model.getF_offer();
        if (offer_percentage>0){
            double actual_price = model.getF_price();

            double discounted_price = actual_price * ( 1- (double) offer_percentage /100 );
            discounted_price = Math.round(discounted_price*100)/100.0;
            tv_f_price.setText("$"+discounted_price);

            tv_f_actual_price.setPaintFlags(tv_f_actual_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv_f_actual_price.setText("$"+actual_price);
            tv_f_offer.setText(offer_percentage+"% off");

            tv_f_actual_price.setVisibility(View.VISIBLE);
            fl_offer_bg.setVisibility(View.VISIBLE);

        }else {
            tv_f_price.setText("$"+String.valueOf(model.getF_price()));
            tv_f_actual_price.setVisibility(View.GONE);
            fl_offer_bg.setVisibility(View.GONE);
        }

    };


    private void init() {
        iv_back = (ImageView) findViewById(R.id.dtls_iv_food_back_btn_id);
        iv_fav = (ImageView) findViewById(R.id.dtls_iv_food_fav_id);
        iv_food_img = (ImageView) findViewById(R.id.dtls_iv_food_img_id);
        tv_f_name = (TextView) findViewById(R.id.dtls_tv_food_title_id);
        tv_f_price = (TextView) findViewById(R.id.dtls_tv_food_price_id);
        tv_f_actual_price = (TextView) findViewById(R.id.dtls_tv_food_actual_price_id);
        tv_f_offer = (TextView) findViewById(R.id.dtls_tv_food_offer_id);
        tv_f_desc = (TextView) findViewById(R.id.dtls_tv_food_desc_id);
        tv_f_cart_qnt = (TextView) findViewById(R.id.dtls_tv_food_cart_qnt_id);
        tv_f_total_count = (TextView) findViewById(R.id.dtls_tv_food_total_count_id);
        tv_f_total_cost = (TextView) findViewById(R.id.dtls_tv_food_total_cost_id);
        ll_item_plus = (LinearLayout) findViewById(R.id.dtls_ll_food_cart_plus_id);
        ll_item_minus = (LinearLayout) findViewById(R.id.dtls_ll_food_cart_minus_id);
        ll_item_add_to_cart = (LinearLayout) findViewById(R.id.dtls_ll_food_add_to_cart_btn_id);
        ll_mid = (LinearLayout) findViewById(R.id.dtls_middle);
        fl_offer_bg = (FrameLayout) findViewById(R.id.dtls_fl_food_offer_bg_id);
        rl_secrete = (RelativeLayout) findViewById(R.id.secrete_screen_id);
        cl_magic = (ConstraintLayout) findViewById(R.id.dtls_cl_magic_bg_id);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dtls_ll_food_cart_plus_id){
            mPlus();
        }else if (v.getId() == R.id.dtls_ll_food_cart_minus_id){
            mMinus();
        }else if (v.getId() == R.id.dtls_ll_food_add_to_cart_btn_id){
            mAddAllToCart();

        }else if (v.getId() == R.id.dtls_iv_food_back_btn_id){

            myCallForGotoHome.MyCallHome(cartItemModelList);
            Intent intent = new Intent(FoodItemDetails.this, MainActivity.class);
            if (WHO != null){
                intent.putExtra("who", WHO);
            }
            intent.putExtra("refresh", true);
            intent.putExtra("position", position);
            startActivity(intent);


        }else if (v.getId() == R.id.dtls_iv_food_fav_id){
//            Toast.makeText(this, "fav", Toast.LENGTH_SHORT).show();
        }else if (v.getId() == R.id.dtls_iv_food_img_id){
//            Toast.makeText(this, "zoom", Toast.LENGTH_SHORT).show();
            mZoomImg();
        }else if (v.getId() == R.id.secrete_screen_id || v.getId() == R.id.dtls_middle || v.getId() == R.id.dtls_cl_magic_bg_id){
            mSecrateFunctionality();
        }
    }


    private void mSecrateFunctionality() {
        clickCount++;
        if (clickCount > 5) {
            Intent intent = new Intent(FoodItemDetails.this, SecreteScreen.class);
            startActivity(intent);

            clickCount = 0;
        }

    }

    public interface MyCallForGotoHome{
        void MyCallHome(List<CartItemModel>  cartItemModelList);
    };
    private List<CartItemModel> mGetAllCartDataInDetails() {
        MyFunctionBox myFunctionBox = new MyFunctionBox(FoodItemDetails.this);
        MySQLiteManager mySQLiteManager = new MySQLiteManager(FoodItemDetails.this);

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

    private void mAddAllToCart() {
//        myAllAddToCart.mMyAllAdded(CartItemQuantity, model, );
        myAddButtonClicked.CheckClickedItem(model, false, CartItemQuantity, this, "dtls");
        Log.d("data8", CartItemQuantity+",   s");

    }

    private void mZoomImg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.image_zoom, null);

        ivZoom = (SubsamplingScaleImageView) dialogLayout.findViewById(R.id.img_zoom_id);

        Glide.with(getApplicationContext())
                .asBitmap()
                .load(Uri.parse(model.getF_img()))
                .placeholder(R.drawable.cat_item_img_loading_placeholder)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ivZoom.setImage(ImageSource.bitmap(resource));
                    }
                });

        builder.setView(dialogLayout);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();


    }


    @Override
    public void onBackPressed() {
        myCallForGotoHome.MyCallHome(cartItemModelList);
        Intent intent = new Intent(FoodItemDetails.this, MainActivity.class);
        if (WHO != null){
            intent.putExtra("who", WHO);
        }
        intent.putExtra("refresh", true);
        intent.putExtra("position", position);
        startActivity(intent);

        super.onBackPressed();
    }
}