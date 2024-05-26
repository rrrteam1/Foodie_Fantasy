package com.raja.resturant.Controll.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.raja.resturant.Fragments.FragmentCategoryItem;
import com.raja.resturant.Model.CartItemModel;
import com.raja.resturant.Model.Category_List_Item_Model;
import com.raja.resturant.Model.MySingleListModel;
import com.raja.resturant.R;

import java.util.List;

public class FragmentHomeRecyclerAdapter extends RecyclerView.Adapter<FragmentHomeRecyclerAdapter.MyViewHolder> {

    private List<CartItemModel> cartList;
    private List<MySingleListModel> mySingleListModel;
    private FragmentActivity activity;

    public FragmentHomeRecyclerAdapter(FragmentActivity activity, List<MySingleListModel> mySingleListModelList) {
        this.mySingleListModel = mySingleListModelList;
        this.activity = activity;
    }



    public FragmentHomeRecyclerAdapter(FragmentActivity activity, List<MySingleListModel> mySingleListModelList, List<CartItemModel> cartList) {
        this.mySingleListModel = mySingleListModelList;
        this.activity = activity;
        this.cartList = cartList;
    }



    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public FragmentHomeRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_rv_fragment_demo, null, false);


        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FragmentHomeRecyclerAdapter.MyViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return mySingleListModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout fragmentContainer;
//        private TextView tvCatTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fragmentContainer = (LinearLayout) itemView.findViewById(R.id.main_home_fragment_container);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        int position = holder.getAdapterPosition();
        String title_of_current_list = mySingleListModel.get(position).getSingle_title();


        List<Category_List_Item_Model> categoryListOfList = mySingleListModel.get(position).getSingle_list();
        try{

        FragmentActivity activity = (FragmentActivity) holder.itemView.getContext();
        String fragmentTag = "child_frag_" + holder.getAdapterPosition();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        

            if (!fragmentManager.isStateSaved()){
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Fragment fragment = createFragment(categoryListOfList, title_of_current_list);

                transaction.add(holder.fragmentContainer.getId(), fragment);
                transaction.commit();
            }
        }catch (Exception e){
            Toast.makeText(activity, "Try Again. A problem occurs here...", Toast.LENGTH_SHORT).show();
        }


    }

    private Fragment createFragment(List<Category_List_Item_Model> categoryListItemList, String title_of_current_list) {
        FragmentCategoryItem fragmentCategoryItem = new FragmentCategoryItem(categoryListItemList, title_of_current_list, cartList);
        return fragmentCategoryItem;
    }
}




























//        Bundle args = new Bundle();
//        args.putString("title", item.getTitle()); // Pass data to fragment
//        fragmentCategoryItem.setArguments(args);