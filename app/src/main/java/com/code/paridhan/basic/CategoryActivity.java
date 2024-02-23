package com.code.paridhan.basic;

import static com.code.paridhan.RetrofitAPI.API_Config.getApiClient_ByPost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.code.paridhan.R;
import com.code.paridhan.RetrofitAPI.ApiService;
import com.code.paridhan.RetrofitAPI.ConnectToRetrofit;
import com.code.paridhan.RetrofitAPI.GlobalAppApis;
import com.code.paridhan.RetrofitAPI.RetrofitCallBackListenar;
import com.code.paridhan.fragment.HomeFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

public class CategoryActivity extends AppCompatActivity {
    RecyclerView rcvCategoryList;
    RelativeLayout rlHeader;
    LinearLayoutManager layoutManager;
    private ArrayList<HashMap<String, String>> rootCategoryList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_category);
        rcvCategoryList=findViewById(R.id.rcvCategoryList);
        rlHeader=findViewById(R.id.rlHeader);
        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getrootcategorymaster();
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


                        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                        CategoryAdapter adapterr = new CategoryAdapter(getApplicationContext(), rootCategoryList);
                        rcvCategoryList.setLayoutManager(layoutManager);
                        rcvCategoryList.setAdapter(adapterr);
                        // Pager listener over indicator

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, CategoryActivity.this, call1, "", true);


    }
    private class CategoryAdapter extends RecyclerView.Adapter<Category> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public CategoryAdapter(Context applicationContext, ArrayList<HashMap<String, String>> rootCategoryList) {
            data = rootCategoryList;
        }


        public Category onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Category(LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategorylist, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Category holder, final int position) {
            Picasso.get().load(data.get(position).get("image")).into(holder.catimg);
            holder.catname.setText(data.get(position).get("Rootcategoryname"));
            holder.llCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), SuCategoryListActivity.class);
                    intent.putExtra("rootcategoryid",data.get(position).get("rootcategoryid"));
                    startActivity(intent);

                }
            });



        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class Category extends RecyclerView.ViewHolder {
        ImageView catimg;
        TextView catname;
        CardView llCategory;

        public Category(View itemView) {
            super(itemView);
            catimg = itemView.findViewById(R.id.catimg);
            catname = itemView.findViewById(R.id.catname);
            llCategory = itemView.findViewById(R.id.llCategory);


        }
    }
}