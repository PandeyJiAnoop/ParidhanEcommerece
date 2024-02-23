package com.code.paridhan.order;

import static com.code.paridhan.RetrofitAPI.API_Config.getApiClient_ByPost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.code.paridhan.R;
import com.code.paridhan.RetrofitAPI.ApiService;
import com.code.paridhan.RetrofitAPI.ConnectToRetrofit;
import com.code.paridhan.RetrofitAPI.GlobalAppApis;
import com.code.paridhan.RetrofitAPI.RetrofitCallBackListenar;
import com.code.paridhan.basic.ProductDetailsActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

public class OfferActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rcvProductlist;
    LinearLayoutManager layoutManager;
    RelativeLayout rlHeader;
    private ArrayList<HashMap<String, String>> arrProductlist = new ArrayList<>();
    String CatId;
    private SharedPreferences sharedPreferences;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        findViewById();
        setListner();
    }
    private void setListner() {
        rlHeader.setOnClickListener(this);
    }
    private void findViewById() {
        rcvProductlist = findViewById(R.id.rcvProductList);
        rlHeader = findViewById(R.id.rlHeader);
        sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        CatId = getIntent().getStringExtra("CatId");
        ProductListbycategoryid();


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlHeader:
                finish();
                break;
        }

    }
    public void ProductListbycategoryid() {
        String otp1 = new GlobalAppApis().OfferDetails(username);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.OfferDetails(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    arrProductlist.clear();
                    Log.v("getStringgg", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");

                    if (Status.equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("ProductList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("Discount", jsonObject2.getString("Discount"));
                            hm.put("IsMultiVariant", jsonObject2.getString("IsMultiVariant"));
                            hm.put("ProductCode", jsonObject2.getString("ProductCode"));
                            hm.put("mainimgfile", jsonObject2.getString("Path"));
                            hm.put("productmrp", jsonObject2.getString("productmrp"));
                            hm.put("ProductTitle", jsonObject2.getString("ProductTitle"));
                            hm.put("salerate", jsonObject2.getString("salerate"));
                            hm.put("DiscountType", jsonObject2.getString("DiscountType"));
                            hm.put("ExtraDiscount", jsonObject2.getString("ExtraDiscount"));
                            hm.put("ExtraDiscountType", jsonObject2.getString("ExtraDiscountType"));
                            arrProductlist.add(hm);

                        }
                        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                        OfferActivity.ProductAdapter productAdapter = new OfferActivity.ProductAdapter(getApplicationContext(), arrProductlist);
                        rcvProductlist.setLayoutManager(layoutManager);
                        rcvProductlist.setAdapter(productAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, OfferActivity.this, call1, "", true);
    }
    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public ProductAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrProductlist) {
            data = arrProductlist;
        }
        public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ProductHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_product, parent, false));
        }

        @SuppressLint("SetTextI18n")
           public void onBindViewHolder(final ProductHolder holder, final int position) {
            Picasso.get().load(data.get(position).get("mainimgfile")).into(holder.img_product);
            holder.txt_Name.setText(data.get(position).get("ProductTitle"));
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
                    holder.tv_offer_price.setText(data.get(position).get("Discount")+" %");

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
            holder.ic_home_fav.setVisibility(View.GONE);
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), ProductDetailsActivity.class);
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
}