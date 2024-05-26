package com.raja.resturant.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.raja.resturant.Controll.Adapter.FragmentHomeRecyclerAdapter;
import com.raja.resturant.Controll.Interface.MyFirebaseDataGetBack;
import com.raja.resturant.Controll.Interface.MyMainPageTitleSet;
import com.raja.resturant.Controll.LocalDatabase.MySQLiteManager;
import com.raja.resturant.Controll.MyFunctionBox;
import com.raja.resturant.MainActivity;
import com.raja.resturant.Model.CartItemModel;
import com.raja.resturant.Model.MySingleListModel;
import com.raja.resturant.R;

import java.util.List;


public class Fragment_Home extends Fragment implements MyFirebaseDataGetBack {

    private Context context;
    private RecyclerView rv_main_list;
    private LinearLayout ll_prog;
//    private List<List<Category_List_Item_Model>> list_of_list;
//    private List<String> list_of_titles;
    MyFunctionBox myFunctionBox;
    private MySQLiteManager mySQLiteManager;
    private List<CartItemModel> cartItemModelList;

//    private long userID;

    private MyMainPageTitleSet myMainPageTitleSet;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myFunctionBox = new MyFunctionBox(context);
        myFunctionBox.myFirebaseDataGetBack = (MyFirebaseDataGetBack) new Fragment_Home();
        mySQLiteManager = new MySQLiteManager(context);

        myMainPageTitleSet = (MyMainPageTitleSet) getActivity();
        myMainPageTitleSet.mSetPageTitle("Home");

    }
    public Fragment_Home(List<CartItemModel> cartItemList) {
        this.cartItemModelList = cartItemList;
    }



    public Fragment_Home() {

    }
    public View view;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        init();
        mMakeReadyHomeData();

//        FragmentHomeRecyclerAdapter mainRecyclerAdapter = new FragmentHomeRecyclerAdapter(getActivity(),list_of_list, list_of_titles);
//        rv_main_list.setAdapter(mainRecyclerAdapter);


//        new MyTask().execute();


//        mHideProg();
        return view;
    }

    private void init() {
        rv_main_list = (RecyclerView) view.findViewById(R.id.home_main_rv_id);
        ll_prog = (LinearLayout) view.findViewById(R.id.homeFrag_progress_bg_id);
    }

    private void mMakeReadyHomeData() {
        mShowProg();
        myFunctionBox.mGatAllFoodData(getActivity());

    }
    private void mShowProg(){
        ll_prog.setVisibility(View.VISIBLE);
    };
    private void mHideProg(){
        ll_prog.setVisibility(View.GONE);
    };
    @Override
    public void mGetFirebaseDataReady(List<MySingleListModel> mySingleListModelList, FragmentActivity activity) {
        if (activity != null){

            try {

                ll_prog = (LinearLayout) activity.findViewById(R.id.homeFrag_progress_bg_id);
                rv_main_list = (RecyclerView) activity.findViewById(R.id.home_main_rv_id); rv_main_list.setLayoutManager(new LinearLayoutManager(activity));

                mShowProg();

                if (cartItemModelList == null){
                    List<CartItemModel> cartList = mGetAllCartData(activity);
                    cartItemModelList = cartList;
                    Log.d("data8","this is from home calling");
                }else {
                    Log.d("data8","this is from main activity calling");
                }

                FragmentHomeRecyclerAdapter mainRecyclerAdapter = new FragmentHomeRecyclerAdapter(activity, mySingleListModelList, cartItemModelList);
//            FragmentHomeRecyclerAdapter mainRecyclerAdapter = new FragmentHomeRecyclerAdapter(activity, mySingleListModelList, cartList);
//            FragmentHomeRecyclerAdapter mainRecyclerAdapter = new FragmentHomeRecyclerAdapter(activity, mySingleListModelList);
//            Log.d("data5", "ts:"+mySingleListModelList.size()+", ls:"+mySingleListModelList.get(3).getSingle_list().size()+"  : "+mySingleListModelList.size());


                rv_main_list.setAdapter(mainRecyclerAdapter);
                mHideProg();

            }catch (Exception e){
                mHideProg();
            }


        }
        
    }

    public List<CartItemModel> mGetAllCartData(FragmentActivity activity) {
        myFunctionBox = new MyFunctionBox(activity);
        mySQLiteManager = new MySQLiteManager(activity);

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