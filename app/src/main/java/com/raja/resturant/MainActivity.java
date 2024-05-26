package com.raja.resturant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.raja.resturant.Authentication.Login;
import com.raja.resturant.Controll.Interface.MyAddButtonClicked;
import com.raja.resturant.Controll.Interface.MyListItemClicked;
import com.raja.resturant.Controll.Interface.MyMainPageTitleSet;
import com.raja.resturant.Controll.LocalDatabase.MySQLiteManager;
import com.raja.resturant.Controll.MyFunctionBox;
import com.raja.resturant.Fragments.Fragment_Cart;
import com.raja.resturant.Fragments.Fragment_Home;
import com.raja.resturant.Model.CartItemModel;
import com.raja.resturant.Model.Category_List_Item_Model;
import com.raja.resturant.Model.UserAuthModel;
import com.raja.resturant.View.FoodItemDetails;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyMainPageTitleSet, View.OnLongClickListener,MyFunctionBox.MyGetUserDataForMainActivity, FoodItemDetails.MyCallForGotoHome, MyListItemClicked, MyAddButtonClicked {


    private ImageView iv_logout;
    private TextView tv_name, tv_mail, tvPageTitle;
    private BottomNavigationView bottomNav;
    private LinearLayout ll_fragment_container;
    private MySQLiteManager mySQLiteManager;
    private MyFunctionBox myFunctionBox;
    private long CURRENT_USER_ID;
    private Intent intent_ref;
    private List<CartItemModel> cartItemModelList;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        myFunctionBox = new MyFunctionBox(MainActivity.this);
        mySQLiteManager = new MySQLiteManager(MainActivity.this);

        iv_logout = (ImageView) findViewById(R.id.main_iv_logout_btn_id);
        tv_name = (TextView) findViewById(R.id.main_tv_user_name_id);
        tv_mail = (TextView) findViewById(R.id.main_tv_user_mail_id);
        tvPageTitle = (TextView) findViewById(R.id.main_tv_page_title_id);

        ll_fragment_container = (LinearLayout) findViewById(R.id.main_frag_container_id);
        bottomNav = (BottomNavigationView)findViewById(R.id.main_bottom_nav_id);


        intent_ref = getIntent();
        if (intent_ref != null){
            String WHO = intent_ref.getStringExtra("who");
            if (WHO!=null){
                if (WHO.equals("dtls")){
                    mGotoFragment(new Fragment_Home());
                } else if (WHO.equals("cart")) {
                    mGotoFragment(new Fragment_Home());
                }
            }
        }
        mCheckIsUserLoggedInOrNot();

//        mGotoFragment(new Fragment_Home(cartItemModelList));
        mGotoFragment(new Fragment_Home(cartItemModelList));
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if (item.getItemId() == R.id.nav_home_id){
                    fragment = new Fragment_Home(cartItemModelList);
                }
                else if (item.getItemId() == R.id.nav_cart_id){
                    fragment = new Fragment_Cart();
                }
                mGotoFragment(fragment);
                return true;
            }
        });

        iv_logout.setOnClickListener(this);
        iv_logout.setOnLongClickListener(this);



    }



    private void mCheckIsUserLoggedInOrNot() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            myFunctionBox.mReturnDataWithCurrentUserName(user, MainActivity.this);


        }else {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
    }


    @Override
    public void mSetUserData(UserAuthModel userAuthModel) {
        tv_name.setText(userAuthModel.getUser_name());
        tv_mail.setText(userAuthModel.getUser_mail());


        mySQLiteManager.mUserExistOrNot(userAuthModel.getUser_mail());
        CURRENT_USER_ID = mySQLiteManager.sessionManagement.getUserId();
        if (CURRENT_USER_ID != -1){
            cartItemModelList = mySQLiteManager.mGetAllCartItems(CURRENT_USER_ID);
        }
    }
    private void mGetUpdatedCartList(FoodItemDetails foodItemDetails){
        myFunctionBox = new MyFunctionBox(foodItemDetails);
        mySQLiteManager = new MySQLiteManager(foodItemDetails.getApplicationContext());

        mySQLiteManager.mUserExistOrNot(myFunctionBox.getAuth().getCurrentUser().getEmail());
        CURRENT_USER_ID = mySQLiteManager.sessionManagement.getUserId();
        if (CURRENT_USER_ID != -1){
            cartItemModelList = mySQLiteManager.mGetAllCartItems(CURRENT_USER_ID);
        }

    };





    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_iv_logout_btn_id){
            myFunctionBox.mNotice("Wnan\'t to Logout?", "You may want to log out. Long press to get logged out.");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.main_iv_logout_btn_id){
            myFunctionBox.signOut(new Login());
        }
        return true;
    }





    public void mGotoFragment(Fragment fragment){

        if (fragment != null) {
            try {
                FoodItemDetails foodItemDetails = new FoodItemDetails();
                MainActivity mainActivity = new MainActivity();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(ll_fragment_container.getId(), fragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
            }catch (Exception e){
                myFunctionBox.mNotice("Problem", "There is a problem occurs when switching among screen. You many wait until any screen completely loaded. ");
            }

        }


    };

    @Override
    public void CheckClickedItem(Category_List_Item_Model model, boolean isWantToDelete, int qnt, FoodItemDetails foodItemDetails, String who) {

        long res;
        boolean isSuccess = false;
        if (who.equals("home")){
            Log.d("data10", "home");
            if (isWantToDelete){
                //TODO: already added, its want to remove item
                res = mySQLiteManager.mRemoveCart(CURRENT_USER_ID, model.getF_id());
                if (res != -1){
                    myFunctionBox.mNotice("Success", "Item successfully removed from cart!");
                }else {
                    Toast.makeText(this, "Item not removed successfully!", Toast.LENGTH_SHORT).show();
                }
            }else {

                mySQLiteManager.mUserExistOrNot(myFunctionBox.getAuth().getCurrentUser().getEmail());
                CURRENT_USER_ID = mySQLiteManager.sessionManagement.getUserId();

                isSuccess = mySQLiteManager.mAddCart(CURRENT_USER_ID, model.getF_id(), qnt);

                if (isSuccess){
                    myFunctionBox.mNotice("Success", qnt+" Item successfully added into cart!");
                }else {
                    Toast.makeText(foodItemDetails, qnt+" Item not added successfully!", Toast.LENGTH_SHORT).show();
                }

            }

            mGotoFragment(new Fragment_Home());

        }else if (who.equals("dtls")){
            Log.d("data10", "dtls");
            if (!isWantToDelete){
                if (foodItemDetails != null){
                    myFunctionBox = new MyFunctionBox(foodItemDetails);
                    mySQLiteManager = new MySQLiteManager(foodItemDetails);
                }

                mySQLiteManager.mUserExistOrNot(myFunctionBox.getAuth().getCurrentUser().getEmail());
                CURRENT_USER_ID = mySQLiteManager.sessionManagement.getUserId();

                isSuccess = mySQLiteManager.mAddCart(CURRENT_USER_ID, model.getF_id(), qnt);

                if (isSuccess){
                    myFunctionBox.mNotice("Success", qnt+" Item successfully added into cart!");
                }else {
                    Toast.makeText(foodItemDetails, qnt+" Item not added successfully!", Toast.LENGTH_SHORT).show();
                }

                mGetUpdatedCartList(foodItemDetails);
            }

        }else if (who.equals("cart")){
            Log.d("data10", "cart");
            if (isWantToDelete){
                //TODO: already added, its want to remove item
                res = mySQLiteManager.mRemoveCart(CURRENT_USER_ID, model.getF_id());
                if (res != -1){
                    myFunctionBox.mNotice("Success", "Item successfully removed from cart!");
                }else {
                    Toast.makeText(this, "Item not removed successfully!", Toast.LENGTH_SHORT).show();
                }
            }else {
                mySQLiteManager.mUserExistOrNot(myFunctionBox.getAuth().getCurrentUser().getEmail());
                CURRENT_USER_ID = mySQLiteManager.sessionManagement.getUserId();

                isSuccess = mySQLiteManager.mAddCart(CURRENT_USER_ID, model.getF_id(), qnt);

                if (isSuccess){
                    myFunctionBox.mNotice("Success", qnt+" Item successfully added into cart!");
                }else {
                    Toast.makeText(foodItemDetails, qnt+" Item not added successfully!", Toast.LENGTH_SHORT).show();
                }

            }

            mGotoFragment(new Fragment_Cart());
        }




    }


    @Override
    public void mGetListItemDataToDisplayInDetails(int position, Category_List_Item_Model model_item, String who) {
//        Toast.makeText(this, "clicked for details", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, FoodItemDetails.class);
        Bundle bundle = new Bundle();
        bundle.putString("fname", model_item.getF_name());
        bundle.putString("fdesc", model_item.getF_desc());
        bundle.putString("fimg", model_item.getF_img());

        bundle.putDouble("fprice", model_item.getF_price());

        bundle.putInt("foffer", model_item.getF_offer());
        bundle.putInt("fid", model_item.getF_id());
        bundle.putInt("fcartid", model_item.getCat_id());
        bundle.putInt("position", position);
        bundle.putInt("count_cart", mGetCartCount(model_item.getF_id()));
        bundle.putString("who", who);

        intent.putExtras(bundle);
        startActivity(intent);
    }
    public int mGetCartCount(int fID){
        int r = -1;
        List<CartItemModel> items = mySQLiteManager.mGetAllCartItems(CURRENT_USER_ID);
        if (items != null){
            for (CartItemModel it: items){
                if (fID == it.getCart_item_id()){
                  return it.getCart_item_quantity();
                }
            }
        }
        return r;
    };

    @Override
    public void MyCallHome(List<CartItemModel> list) {
        cartItemModelList = list;
    }

    @Override
    public void mSetPageTitle(String title) {
        tvPageTitle = (TextView) findViewById(R.id.main_tv_page_title_id);
        tvPageTitle.setText(title);
    }


}
