package com.raja.resturant.Controll;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.raja.resturant.Controll.Interface.MyFirebaseDataGetBack;
import com.raja.resturant.Controll.Interface.MyGetCartedItemsListener;
import com.raja.resturant.Fragments.Fragment_Cart;
import com.raja.resturant.Model.CartItemModel;
import com.raja.resturant.Model.CategoryModel;
import com.raja.resturant.Model.Category_List_Item_Model;
import com.raja.resturant.Model.MySingleListModel;
import com.raja.resturant.Model.UserAuthModel;

import java.util.ArrayList;
import java.util.List;

public class MyFunctionBox {


    Context context;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseUser user;
    public MyFirebaseDataGetBack myFirebaseDataGetBack;
    public MyGetCartedItemsListener myGetCartedItemsListener;

    public FirebaseAuth getAuth() {
        return auth;
    }

    public MyFunctionBox(Context context) {
        this.context = context;
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

//        myFirebaseDataGetBack = (MyFirebaseDataGetBack) context;
    }


    public void mFirebaseLogIn(String sUserMail, String sUserPass) {
        signIn(sUserMail, sUserPass, (OnSignInListener) context);
    }

    public void mFirebaseRegistration(String user_name, String user_mail, String user_pass) {
        UserAuthModel userModelObj = new UserAuthModel(user_name, user_mail);
        signUp(userModelObj,user_pass, (OnSignUpListener) context);

    }


    int i, j = 0;
    public void mGatAllFoodData(FragmentActivity fragmentActivity) {
        List<Integer> cat_id_list = new ArrayList<>();
        List<List<Category_List_Item_Model>> list_of_list = new ArrayList<>();
        List<String> category_title = new ArrayList<>();
        List<MySingleListModel> mySingleListModelList = new ArrayList<>();



//        myFirebaseDataGetBack = (MyFirebaseDataGetBack) fragmentActivity;

        reference = firebaseDatabase.getReference("list_category");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()){


                        int cat_id = ds.getValue(Integer.class);
                        reference = firebaseDatabase.getReference("category").child(String.valueOf(cat_id));
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot sn) {
                                if (sn.exists()){
                                    category_title.add(sn.getValue(CategoryModel.class).getCat_name());
                                    cat_id_list.add(Integer.valueOf(sn.getKey()));


                                }
                                if (category_title.size() == snapshot.getChildrenCount()){
//                                    Log.d("data5", "t: "+ category_title.size()+", id:"+cat_id_list.size());
                                    //TODO: i think this is perfect place to call back send data. here all title comes. hopefully all list also will come here

                                    for (int id: cat_id_list){
                                        reference = firebaseDatabase.getReference("food_db");
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snshot) {
                                                if (snshot.exists()){
                                                    List<Category_List_Item_Model> singleList = new ArrayList<>();

                                                    for (DataSnapshot ssn: snshot.getChildren()){

                                                        if (id == ssn.getValue(Category_List_Item_Model.class).getCat_id()){

                                                            singleList.add(ssn.getValue(Category_List_Item_Model.class));

                                                        }

                                                    }

                                                    list_of_list.add(singleList);

                                                    if (list_of_list.size() == snapshot.getChildrenCount()){
//                                                        Log.d("data5", "t: "+ category_title.size()+", id:"+cat_id_list.size()+", ls:"+list_of_list.size());
                                                        //TODO: this is the condition to know, is my firebase data extraction is completed or not. so, now can return value safely.

                                                        for (int m = 0; m < category_title.size(); m++) {
                                                            MySingleListModel ms = new MySingleListModel(category_title.get(m),list_of_list.get(m));
                                                            mySingleListModelList.add(ms);
                                                        }

                                                         myFirebaseDataGetBack.mGetFirebaseDataReady(mySingleListModelList, fragmentActivity);
                                                    }

                                                }


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        j++;

                                    }




                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        i++;
//                        reference = firebaseDatabase.getReference("food_db");
//                        reference.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snshot) {
//                                if (snshot.exists()){
//                                    List<Category_List_Item_Model> singleList = new ArrayList<>();
//
//                                    for (DataSnapshot ssn: snshot.getChildren()){
//
////                                                    int item_cat_id = ssn.getValue(Category_List_Item_Model.class).getCat_id();
////                                                    if (cat_id == item_cat_id){
////                                                    int item_cat_id = ssn.getValue(Category_List_Item_Model.class).getCat_id();
//                                        if (cat_id == ssn.getValue(Category_List_Item_Model.class).getCat_id()){
//
//                                            singleList.add(ssn.getValue(Category_List_Item_Model.class));
////                                            Log.d("data3", ""+ssn.getValue(Category_List_Item_Model.class).getF_name());
////                                            Log.d("data3", snapshot.getChildrenCount()+"    "+snshot.getChildrenCount()+", i="+i+", s="+singleList.size()+", "+list_of_list.size());
////                                            if ()
//                                        }
//                                    }
//
//                                    Log.d("data5", "id: "+cat_id);
//                                    list_of_list.add(singleList);
//                                    if (list_of_list.size() == snapshot.getChildrenCount()){
//                                        Log.d("data5", "s: "+list_of_list.get(0).size());
//                                        //TODO: this is the condition to know, is my firebase data extraction is completed or not. so, now can return value safely.
////                                                    myFirebaseDataGetBack.mGetFirebaseDataReady(category_title, list_of_list, fragmentActivity);
//                                    }else {
////                                                    Log.d("data5", "lol size != sn ch count ");
//
//                                    }
//
//                                }
//
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });



//                        i ++;


//                        if (category_title.size() == snapshot.getChildrenCount()){
//                            //TODO: this is the condition to know, is my firebase data extraction is completed or not. so, now can return value safely.
//                            Log.d("data5", "l1: "+list_of_list.get(0).size());
////                            myFirebaseDataGetBack.mGetFirebaseDataReady(category_title, list_of_list, fragmentActivity);
//                        }else {
//                            Log.d("data5", "lol size != sn count. s:"+snapshot.getChildrenCount()+", t:"+category_title.size());
//
//                        }


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void signUp(UserAuthModel userAuthModel , String user_pass, OnSignUpListener listener) {
        //TODO: Firstly check is already exist or not.
        auth.signInWithEmailAndPassword(userAuthModel.getUser_mail(), user_pass)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //TODO: Email is already exist.
                    signOut();
                    listener.mOnEmailIsAlreadyExist(userAuthModel, user_pass);
                } else {
                    //TODO: Email is not exist. Can be register...
                    auth.createUserWithEmailAndPassword(userAuthModel.getUser_mail(), user_pass)
                            .addOnCompleteListener(mTask -> {

                                if (mTask.isSuccessful()) {
                                    user = auth.getCurrentUser();
                                    if (user != null) {
                                        String userId = user.getUid();
                                        saveUserData(userId, userAuthModel);
                                    }
                                    mReturnDataWithCurrentUserName(user, listener);
//                                    listener.onSignUpSuccess(user);
                                } else {
                                    mNotice("Sorry", "User Registration failed. "+mTask.getException().getMessage());
//                                    Toast.makeText(context, "User Registration failed. "+mTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });




                }
            });


    }

    private void saveUserData(String userId, UserAuthModel userAuthModel) {
        reference = firebaseDatabase.getReference().child("customer_users").child(userId);
        reference.setValue(userAuthModel);
    }



    public void mReturnDataWithCurrentUserName(FirebaseUser user, Object listener){
        String uid = user.getUid();
        reference = firebaseDatabase.getReference().child("customer_users").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    UserAuthModel model = snapshot.getValue(UserAuthModel.class);
                    if (listener instanceof OnSignInListener){
                        ((OnSignInListener) listener).onSignInSuccess(model);
                    } else if (listener instanceof OnSignUpListener) {
                        ((OnSignUpListener) listener).onSignUpSuccess(model);
                    } else if (listener instanceof MyGetUserDataForMainActivity) {
                        ((MyGetUserDataForMainActivity) listener).mSetUserData(model);
                    }
                    Log.d("data2", model.getUser_name()+"    :res");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    };
    private void signIn(String email, String password, OnSignInListener listener) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mReturnDataWithCurrentUserName(task.getResult().getUser(), listener);
//                        listener.onSignInSuccess(task.getResult().getUser());

                    } else {
//                        Toast.makeText(context, "User LogIn failed.", Toast.LENGTH_SHORT).show();
                        mNotice("Sorry", "User Login failed. \nCause: "+task.getException().getMessage());
                    }
                });
    }

    public void signOut() {
        if (auth != null){
            auth.signOut();
//            mNotice("Success", "Successfully signed out this user.");
        }else {
            mNotice("Error", "Already signed out. No need to sign out again.");
        }
    }
    public void signOut(Activity activity) {
        if (auth != null){
            auth.signOut();
            mNotice("Success", "Successfully signed out this user.", "Logout",activity);
        }else {
            mNotice("Error", "Already signed out. No need to sign out again.");
        }
    }

    public void mGetOnlyCartedItem(List<CartItemModel> cartItemModelList, FragmentActivity activity) {
        List<Category_List_Item_Model> food_item = new ArrayList<>();
        for (CartItemModel cartItem: cartItemModelList){

            reference = firebaseDatabase.getReference("food_db").child(String.valueOf(cartItem.getCart_item_id()));
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        food_item.add(snapshot.getValue(Category_List_Item_Model.class));
                        if (food_item.size() == cartItemModelList.size()){
//                            Log.d("data9", food_item.size()+",   "+cartItemModelList.size());
                            if (myGetCartedItemsListener != null){
                                myGetCartedItemsListener.mGetCartedItems(cartItemModelList,food_item, activity);
                                Log.d("data9", "listener  is not null");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


    }


    public interface OnSignUpListener {
        void onSignUpSuccess(UserAuthModel model);
//        void onSignUpSuccess(FirebaseUser user);
        void mOnEmailIsAlreadyExist(UserAuthModel userAuthModel, String user_pass);
    }
    public interface MyGetUserDataForMainActivity{
        void mSetUserData(UserAuthModel userAuthModel);
    };

    public interface OnSignInListener {
        void onSignInSuccess(UserAuthModel userAuthModel);
    }
//
//    public interface OnSignInListener {
//        void onSignInSuccess(FirebaseUser user);
//    }


    public void mNotice(String title, String msg){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Action on positive button click
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    };

    public void mNotice(String title, String msg, Activity activity){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Action on positive button click
                dialog.dismiss();
                Intent intent = new Intent(context, activity.getClass());
                context.startActivity(intent);

            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    };

    public void mNotice(String title, String msg,String btn_text ,Activity activity){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton(btn_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Action on positive button click
                dialog.dismiss();
                Intent intent = new Intent(context, activity.getClass());
                context.startActivity(intent);

            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    };

    public void mGoToActivity(Activity activity){
        Intent intent = new Intent(context, activity.getClass());
        context.startActivity(intent);
    }
    public void mGoToActivity(Activity activity, Bundle data_to_send){
        Intent intent = new Intent(context, activity.getClass());
        intent.putExtras(data_to_send);
        context.startActivity(intent);
    }
    public void mNotice(String title, String msg, Activity activity, Bundle data_to_send){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Action on positive button click
                dialog.dismiss();
                Intent intent = new Intent(context, activity.getClass());
                intent.putExtras(data_to_send);
                context.startActivity(intent);

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    };

    public void mLogConCat(String tag, Object[] obj){
        String s = "";
        for (Object a: obj){
            s+=a+", ";
        }
        Log.d(tag, s);
    };



    /*
    List<Integer> cat_id_list = new ArrayList<>();
        reference = firebaseDatabase.getReference("list_category");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()){
                        cat_id_list.add(ds.getValue(Integer.class));
                    }
                }
                if (cat_id_list.size() > 0){

                    reference = firebaseDatabase.getReference("category");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snap) {
                            if (snap.exists()){
                                for (DataSnapshot data_sn: snap.getChildren()){
                                    Log.d("data3", data_sn.getKey()+", -  "+data_sn.getValue(CategoryModel.class).getCat_name());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    */


}
