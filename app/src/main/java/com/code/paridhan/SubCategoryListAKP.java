package com.code.paridhan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.code.paridhan.RetrofitAPI.ApiService;
import com.code.paridhan.RetrofitAPI.ConnectToRetrofit;
import com.code.paridhan.RetrofitAPI.GlobalAppApis;
import com.code.paridhan.RetrofitAPI.RetrofitCallBackListenar;
import com.code.paridhan.basic.ProductListActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

import static com.code.paridhan.RetrofitAPI.API_Config.getApiClient_ByPost;

public class SubCategoryListAKP extends AppCompatActivity {
    LinearLayoutManager layoutManager;
    private ArrayList<HashMap<String, String>> arrSubCategoryList = new ArrayList<>();
    RelativeLayout rlHeader;
    RecyclerView rcvSubCategoryList;
    String rootcategoryid,rootcategoryname;
    TextView title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_list_a_k_p);
        findViewById();
        getCategorymaster(rootcategoryid);

    }

    private void findViewById() {
        title_tv=findViewById(R.id.title_tv);
        rlHeader=findViewById(R.id.rlHeader);
        rcvSubCategoryList=findViewById(R.id.rcvSubCategoryList);
        rootcategoryid=getIntent().getStringExtra("CatId");
        rootcategoryname=getIntent().getStringExtra("CatName");
//        Toast.makeText(getApplicationContext(),"id="+rootcategoryid, Toast.LENGTH_LONG).show();

        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_tv.setText(rootcategoryname);
    }

    public void getCategorymaster(String rootcategoryid) {
        String otp1 = new GlobalAppApis().getSubCategorymasteraa(rootcategoryid);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getSubCategorymasterAPI(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result, Toast.LENGTH_LONG).show();
                try {
                    arrSubCategoryList.clear();
                    Log.v("getStringgg123", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("status");
                    if (Status.equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("LoginRes");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("SubCategoryName", jsonObject2.getString("SubCategoryName"));
                            hm.put("SubCategoryImagePath", jsonObject2.getString("SubCategoryImagePath"));
                            hm.put("CategoryId", jsonObject2.getString("CategoryId"));
                            hm.put("SubCategoryId", jsonObject2.getString("SubCategoryId"));
                            arrSubCategoryList.add(hm);
                        }
                        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                        SubCategoryAdapterAKP adapterr = new SubCategoryAdapterAKP(getApplicationContext(), arrSubCategoryList);
                        rcvSubCategoryList.setLayoutManager(layoutManager);
                        rcvSubCategoryList.setAdapter(adapterr);
                        // Pager listener over indicator

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, SubCategoryListAKP.this, call1, "", true);


    }

    private class SubCategoryAdapterAKP extends RecyclerView.Adapter<SubCategoryListAKP.Category> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public SubCategoryAdapterAKP(Context applicationContext, ArrayList<HashMap<String, String>> arrSubCategoryList) {
            data = arrSubCategoryList;
        }


       /* public SubCategoryAdapterAKP( activity, ArrayList<HashMap<String, String>> rootCategoryList) {
            data = rootCategoryList;
        }*/


        public SubCategoryListAKP.Category onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SubCategoryListAKP.Category(LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategorylistakp, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final SubCategoryListAKP.Category holder, final int position) {
            Picasso.get().load(data.get(position).get("SubCategoryImagePath")).into(holder.catimg);
            holder.catname.setText(data.get(position).get("SubCategoryName"));

            holder.llCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), ProductListActivity.class);
                    intent.putExtra("CatId",data.get(position).get("SubCategoryId"));
                    intent.putExtra("SubCategoryName",data.get(position).get("SubCategoryName"));
                    startActivity(intent);                }
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