package com.code.paridhan.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.code.paridhan.R;
import com.code.paridhan.RetrofitAPI.ApiService;
import com.code.paridhan.RetrofitAPI.ConnectToRetrofit;
import com.code.paridhan.RetrofitAPI.GlobalAppApis;
import com.code.paridhan.RetrofitAPI.RetrofitCallBackListenar;
import com.code.paridhan.adapter.BannerAddapter;
import com.code.paridhan.basic.CartActivity;
import com.code.paridhan.basic.NotificationList;
import com.code.paridhan.basic.ProductDetailsActivity;
import com.code.paridhan.basic.ProductListActivity;
import com.code.paridhan.basic.SerachActivity;
import com.code.paridhan.basic.SuCategoryListActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;

import static com.code.paridhan.RetrofitAPI.API_Config.getApiClient_ByPost;


public class HomeFragment extends Fragment {
    private static int currentPage = 0;

    ViewPager sliderView;
    LinearLayoutManager layoutManager,linearLayoutManager2;
    RecyclerView rcvCategoryList, rcvProductList,rcvOfferProductList;
    private ArrayList<HashMap<String, String>> topBannerList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> rootCategoryList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> arrdashboardlist = new ArrayList<>();
    private ArrayList<HashMap<String, String>> arrOfferlsit = new ArrayList<>();
    Handler mhandler;
    private SharedPreferences sharedPreferences;
    String username;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        findViewById(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.purple));
        }

        return view;
    }

    private void findViewById(View view) {
        sharedPreferences = getActivity().getSharedPreferences("login_preference",MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        sliderView = view.findViewById(R.id.imageSlider);

        mhandler = new Handler();
        rcvCategoryList = (RecyclerView) view.findViewById(R.id.rcvCategoryList);
        rcvProductList = (RecyclerView) view.findViewById(R.id.rcvProductList);
        rcvOfferProductList = (RecyclerView) view.findViewById(R.id.rcvOfferProductList);

        GetBanner();
        getrootcategorymaster();
        DashboardList();
    }
    public void GetBanner() {
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.GetBanner("");
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    topBannerList.clear();
                    Log.v("getStringgg", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("status");
                    if (Status.equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("Result");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("BannerImage", jsonObject2.getString("BannerImage"));
                            hm.put("SrNo", jsonObject2.getString("SrNo"));
                            topBannerList.add(hm);
                            sliderView.setPageMargin(20);
                            sliderView.setAdapter(new BannerAddapter(getActivity(), topBannerList));

                        }


                        // Auto start of viewpager
                        final Handler handler = new Handler();
                        final Runnable Update = new Runnable() {
                            public void run() {
                                if (currentPage == topBannerList.size()) {
                                    currentPage = 0;
                                }
                                sliderView.setCurrentItem(currentPage++, true);
                            }
                        };
                        Timer swipeTimer = new Timer();
                        swipeTimer.schedule(new TimerTask() {

                            @Override

                            public void run() {

                                handler.post(Update);
                            }
                        }, 5000, 3000);

                        // Pager listener over indicator

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getActivity(), call1, "", true);


    }
    public void getrootcategorymaster() {
        String otp1 = new GlobalAppApis().getrootcategorymaster();
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getrootcategorymaster(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    rootCategoryList.clear();
                    Log.v("getStringgg", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("status");
                    if (Status.equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("LoginRes");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("Rootcategoryname", jsonObject2.getString("Rootcategoryname"));
                            hm.put("image", jsonObject2.getString("image"));
                            hm.put("rootcategoryid", jsonObject2.getString("rootcategoryid"));
                            rootCategoryList.add(hm);
                        }
                        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        CategoryAdapter adapterr = new CategoryAdapter(getActivity(), rootCategoryList);
                        rcvCategoryList.setLayoutManager(layoutManager);
                        rcvCategoryList.setAdapter(adapterr);
                        // Pager listener over indicator
                    }
                } catch (JSONException e) {
                    e.printStackTrace(); }}
        }, getActivity(), call1, "", true);


    }
    public void DashboardList() {
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.DashboardList("");
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    arrdashboardlist.clear();
                    Log.v("getStringgg", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if (Status.equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("pres");
                        JSONArray OfferList = jsonObject.getJSONArray("OfferList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("CashBackAmt", jsonObject2.getString("CashBackAmt"));
                            hm.put("CatId", jsonObject2.getString("CatId"));
                            hm.put("IsMultiVariant", jsonObject2.getString("IsMultiVariant"));
                            hm.put("ProductCode", jsonObject2.getString("ProductCode"));
                            hm.put("RP", jsonObject2.getString("RP"));
                            hm.put("mainimgfile", jsonObject2.getString("mainimgfile"));
                            hm.put("productmrp", jsonObject2.getString("productmrp"));
                            hm.put("salerate", jsonObject2.getString("salerate"));
                            hm.put("ProductTitle", jsonObject2.getString("ProductTitle"));
                            hm.put("DiscountType", jsonObject2.getString("DiscountType"));
                            hm.put("ExtraDiscount", jsonObject2.getString("ExtraDiscount"));
                            hm.put("ExtraDiscountType", jsonObject2.getString("ExtraDiscountType"));
                            hm.put("Discount", jsonObject2.getString("Discount"));
                            hm.put("WishList", jsonObject2.getString("WishList"));
                            arrdashboardlist.add(hm);

                        }
                        for (int i = 0; i < OfferList.length(); i++) {
                            JSONObject jsonObject2 = OfferList.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("CashBackAmt", jsonObject2.getString("CashBackAmt"));
                            hm.put("CatId", jsonObject2.getString("CatId"));
                            hm.put("IsMultiVariant", jsonObject2.getString("IsMultiVariant"));
                            hm.put("ProductCode", jsonObject2.getString("ProductCode"));
                            hm.put("RP", jsonObject2.getString("RP"));
                            hm.put("mainimgfile", jsonObject2.getString("mainimgfile"));
                            hm.put("productmrp", jsonObject2.getString("productmrp"));
                            hm.put("salerate", jsonObject2.getString("salerate"));
                            hm.put("ProductTitle", jsonObject2.getString("ProductTitle"));
                            hm.put("DiscountType", jsonObject2.getString("DiscountType"));
                            hm.put("ExtraDiscount", jsonObject2.getString("ExtraDiscount"));
                            hm.put("ExtraDiscountType", jsonObject2.getString("ExtraDiscountType"));
                            hm.put("WishList", jsonObject2.getString("WishList"));
                            hm.put("Discount", jsonObject2.getString("Discount"));
                            arrOfferlsit.add(hm);

                        }

                        layoutManager = new GridLayoutManager(getActivity(), 2);
                        ProductAdapter productAdapter = new ProductAdapter(getActivity(),arrdashboardlist);
                        rcvProductList.setLayoutManager(layoutManager);
                        rcvProductList.setAdapter(productAdapter);


                        linearLayoutManager2 = new GridLayoutManager(getActivity(), 2);
                        OfferProductAdapter offerProductAdapter = new OfferProductAdapter(getActivity(),arrOfferlsit);
                        rcvOfferProductList.setLayoutManager(linearLayoutManager2);
                        rcvOfferProductList.setAdapter(offerProductAdapter);


                        // Pager listener over indicator

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getActivity(), call1, "", true);


    }
    private class CategoryAdapter extends RecyclerView.Adapter<Category> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public CategoryAdapter(FragmentActivity activity, ArrayList<HashMap<String, String>> rootCategoryList) {
            data = rootCategoryList;
        }
        public Category onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Category(LayoutInflater.from(parent.getContext()).inflate(R.layout.categorylist, parent, false));
        }
        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Category holder, final int position) {
            Picasso.get().load(data.get(position).get("image")).into(holder.catimg);
            holder.catname.setText(data.get(position).get("Rootcategoryname"));
            holder.llCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), SuCategoryListActivity.class);
                    intent.putExtra("rootcategoryid",data.get(position).get("rootcategoryid"));
                    startActivity(intent); }});
        }
        public int getItemCount() {
            return data.size();
        }
    }
    public class Category extends RecyclerView.ViewHolder {
        ImageView catimg;
        TextView catname;
        LinearLayout llCategory;

        public Category(View itemView) {
            super(itemView);
            catimg = itemView.findViewById(R.id.catimg);
            catname = itemView.findViewById(R.id.catname);
            llCategory = itemView.findViewById(R.id.llCategory);
        }
    }
    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public ProductAdapter(FragmentActivity activity, ArrayList<HashMap<String, String>> arrdashboardlist) {
        data=arrdashboardlist;
        }
        public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ProductHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_product, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final ProductHolder holder, final int position) {
            if (data.get(position).get("mainimgfile").equalsIgnoreCase("")){
                Picasso.get().load(R.drawable.ecommerce_logo).into(holder.img_product);
            }
            else {
                Picasso.get().load(data.get(position).get("mainimgfile")).into(holder.img_product);

            }

            holder.txt_Name.setText(data.get(position).get("ProductTitle"));

            if(data.get(position).get("WishList").equalsIgnoreCase("1"))
            {
                holder.ic_home_fav.setImageResource(R.drawable.ic_fav_hover);
            }
            else {
                holder.ic_home_fav.setImageResource(R.drawable.favorite_unlike);

            }

            if (data.get(position).get("ExtraDiscount").equalsIgnoreCase("0.00")) {
                holder.llOne.setVisibility(View.GONE);

            }
            else {
                if (data.get(position).get("ExtraDiscountType").equalsIgnoreCase("Percentage")) {

                    holder.tv_extraOff.setText("\u20b9" + data.get(position).get("ExtraDiscount")+"%");

                    holder.llOne.setVisibility(View.VISIBLE);
                }

                else {
                    holder.tv_extraOff.setText("\u20b9" + data.get(position).get("ExtraDiscount")+"Save");
                    holder.llOne.setVisibility(View.VISIBLE);
                }

            }
            if(data.get(position).get("DiscountType").equalsIgnoreCase("Percentage"))
            {
                if(data.get(position).get("productmrp").equalsIgnoreCase(data.get(position).get("salerate")))
                {
                    holder.tv_product_price.setText("\u20b9"+(data.get(position).get("salerate")));
                    holder.tv_actual_price.setVisibility(View.GONE);
                    holder.tv_offer_price.setVisibility(View.GONE);
                }
                else {
                    holder.tv_actual_price.setVisibility(View.VISIBLE);
                    holder.tv_offer_price.setVisibility(View.VISIBLE);
                    holder.tv_product_price.setText("\u20b9"+(data.get(position).get("salerate")));
                    holder.tv_actual_price.setText("MRP."+(data.get(position).get("productmrp")));
                    holder.tv_offer_price.setText(data.get(position).get("Discount")+"%");

                }
            }
            else {
                if(data.get(position).get("productmrp").equalsIgnoreCase(data.get(position).get("salerate")))
                {
                    holder.tv_product_price.setText("\u20b9"+(data.get(position).get("salerate")));
                    holder.tv_actual_price.setVisibility(View.GONE);
                    holder.tv_offer_price.setVisibility(View.GONE);
                }
                else {
                    holder.tv_actual_price.setVisibility(View.VISIBLE);
                    holder.tv_offer_price.setVisibility(View.VISIBLE);
                    holder.tv_product_price.setText("\u20b9"+(data.get(position).get("salerate")));
                    holder.tv_actual_price.setText("MRP."+(data.get(position).get("productmrp")));
                    holder.tv_offer_price.setText(data.get(position).get("Discount")+" Save");

                }
            }


            holder.ic_home_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddToFevorite(data.get(position).get("ProductCode"),holder.ic_home_fav);
                }
            });



            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), ProductDetailsActivity.class);
                    intent.putExtra("ProductCode",data.get(position).get("ProductCode"));
                    startActivity(intent);
                }
            });
        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class ProductHolder extends RecyclerView.ViewHolder {
        CardView cardview;
        ImageView img_product,ic_home_fav;
        TextView txt_Name;
        TextView tv_product_price, tv_actual_price, tv_offer_price,tv_extraOff;
        LinearLayout llOne;

        public ProductHolder(View itemView) {
            super(itemView);
            cardview = itemView.findViewById(R.id.cardview);
            img_product = itemView.findViewById(R.id.img_product);
            txt_Name = itemView.findViewById(R.id.txt_Name);
            tv_product_price =  itemView.findViewById(R.id.tv_product_price);
            tv_actual_price =  itemView.findViewById(R.id.tv_actual_price);
            tv_offer_price =  itemView.findViewById(R.id.tv_offer_price);
            ic_home_fav =  itemView.findViewById(R.id.ic_home_fav);
            tv_extraOff =  itemView.findViewById(R.id.tv_extraOff);
            llOne =  itemView.findViewById(R.id.llOne);



        }
    }
    private class OfferProductAdapter extends RecyclerView.Adapter<OfferProductHolder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public OfferProductAdapter(FragmentActivity activity, ArrayList<HashMap<String, String>> arrdashboardlist) {
            data=arrdashboardlist;
        }


        public OfferProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OfferProductHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_product, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final OfferProductHolder holder, final int position) {
            Picasso.get().load(data.get(position).get("mainimgfile")).into(holder.img_product);
            holder.txt_Name.setText(data.get(position).get("ProductTitle"));


            if(data.get(position).get("WishList").equalsIgnoreCase("1"))
            {
                holder.ic_home_fav.setImageResource(R.drawable.ic_fav_hover);
            }
            else {
                holder.ic_home_fav.setImageResource(R.drawable.favorite_unlike);

            }


            if (data.get(position).get("ExtraDiscount").equalsIgnoreCase("0.00")) {
                holder.llOne.setVisibility(View.GONE);

            }
            else {
                if (data.get(position).get("ExtraDiscountType").equalsIgnoreCase("Percentage")) {

                    holder.tv_extraOff.setText("\u20b9" + data.get(position).get("ExtraDiscount")+"%");

                    holder.llOne.setVisibility(View.VISIBLE);
                }

                else {
                    holder.tv_extraOff.setText("\u20b9" + data.get(position).get("ExtraDiscount")+"Save");
                    holder.llOne.setVisibility(View.VISIBLE);
                }

            }

            if(data.get(position).get("DiscountType").equalsIgnoreCase("Percentage"))
            {
                if(data.get(position).get("productmrp").equalsIgnoreCase(data.get(position).get("salerate")))
                {
                    holder.tv_product_price.setText("\u20b9"+(data.get(position).get("salerate")));
                    holder.tv_actual_price.setVisibility(View.GONE);
                    holder.tv_offer_price.setVisibility(View.GONE);
                }
                else {
                    holder.tv_actual_price.setVisibility(View.VISIBLE);
                    holder.tv_offer_price.setVisibility(View.VISIBLE);
                    holder.tv_product_price.setText("\u20b9"+(data.get(position).get("salerate")));
                    holder.tv_actual_price.setText("MRP."+(data.get(position).get("productmrp")));
                    holder.tv_offer_price.setText(data.get(position).get("Discount")+"%");

                }
            }
            else {
                if(data.get(position).get("productmrp").equalsIgnoreCase(data.get(position).get("salerate")))
                {
                    holder.tv_product_price.setText("\u20b9"+(data.get(position).get("salerate")));
                    holder.tv_actual_price.setVisibility(View.GONE);
                    holder.tv_offer_price.setVisibility(View.GONE);
                }
                else {
                    holder.tv_actual_price.setVisibility(View.VISIBLE);
                    holder.tv_offer_price.setVisibility(View.VISIBLE);
                    holder.tv_product_price.setText("\u20b9"+(data.get(position).get("salerate")));
                    holder.tv_actual_price.setText("MRP."+(data.get(position).get("productmrp")));
                    holder.tv_offer_price.setText(data.get(position).get("Discount")+" Save");

                }
            }


            holder.ic_home_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddToFevorite(data.get(position).get("ProductCode"),holder.ic_home_fav);
                }
            });



            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), ProductDetailsActivity.class);
                    intent.putExtra("ProductCode",data.get(position).get("ProductCode"));
                    startActivity(intent);
                }
            });
        }



        public int getItemCount() {
            return data.size();
        }
    }
    public class OfferProductHolder extends RecyclerView.ViewHolder {
        CardView cardview;
        ImageView img_product,ic_home_fav;
        TextView txt_Name;
        TextView tv_product_price, tv_actual_price, tv_offer_price,tv_extraOff;
        LinearLayout llOne;

        public OfferProductHolder(View itemView) {
            super(itemView);
            cardview = itemView.findViewById(R.id.cardview);
            img_product = itemView.findViewById(R.id.img_product);
            txt_Name = itemView.findViewById(R.id.txt_Name);
            tv_product_price =  itemView.findViewById(R.id.tv_product_price);
            tv_actual_price =  itemView.findViewById(R.id.tv_actual_price);
            tv_offer_price =  itemView.findViewById(R.id.tv_offer_price);
            ic_home_fav =  itemView.findViewById(R.id.ic_home_fav);
            tv_extraOff =  itemView.findViewById(R.id.tv_extraOff);
            llOne =  itemView.findViewById(R.id.llOne);


        }
    }

    public void AddToFevorite(String propetyId, ImageView ic_home_fav) {
        String otp1 = new GlobalAppApis().AddFav(propetyId,username);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.AddtowishList(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {

                    JSONObject object = new JSONObject(result);
                    String status = object.getString("Status");
                    String Message = object.getString("Msg");
                    if (status.equalsIgnoreCase("true")) {
                        if (Message.equalsIgnoreCase("Added Successfully.")) {
                            ic_home_fav.setImageResource(R.drawable.ic_fav_hover);

                            Toast.makeText(getContext(), "Added Wishlist Successfully", Toast.LENGTH_SHORT).show();

                        } else {

                            ic_home_fav.setImageResource(R.drawable.favorite_unlike);

                            Toast.makeText(getContext(), "Remove Wishlist Successfully", Toast.LENGTH_SHORT).show();

                        }


                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getActivity(), call1, "", true);


    }
}