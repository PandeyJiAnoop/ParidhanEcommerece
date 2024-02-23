package com.code.paridhan.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.code.paridhan.R;
import com.code.paridhan.RetrofitAPI.ApiService;
import com.code.paridhan.RetrofitAPI.ConnectToRetrofit;
import com.code.paridhan.RetrofitAPI.GlobalAppApis;
import com.code.paridhan.RetrofitAPI.RetrofitCallBackListenar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

import static com.code.paridhan.RetrofitAPI.API_Config.getApiClient_ByPost;

public class OrderDetailsActivity extends AppCompatActivity {
    RelativeLayout rlHeader;
    private ArrayList<HashMap<String, String>> arrOrderItemDetails = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    String username, OrderId;
    TextView total_items_price, total_price, tv_name, tv_pincode, tv_number, tv_addnewAddress,delivery_price,tvGstt;
    RecyclerView rcv_orderitem_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        findViewById();
        OrderItemDetails(getIntent().getStringExtra("OrderId"));

    }
    private void findViewById() {
        rlHeader = findViewById(R.id.rlHeader);
        total_items_price = findViewById(R.id.total_items_price);
        total_price = findViewById(R.id.total_price);
        tv_name = findViewById(R.id.textView4);
        tv_number = findViewById(R.id.textView3);
        tv_addnewAddress = findViewById(R.id.textView5);
        tv_pincode = findViewById(R.id.textView6);
        rcv_orderitem_details = findViewById(R.id.happy_shopping_image);
        delivery_price = findViewById(R.id.delivery_price);
        tvGstt = findViewById(R.id.tvGstt);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void OrderItemDetails(String orderId) {
        String otp1 = new GlobalAppApis().OrderItemDetails(username, orderId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.OrderItemDetails(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    arrOrderItemDetails.clear();
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    String FinalAmount = jsonObject.getString("FinalAmount");
                    if (Status.equalsIgnoreCase("true")) {
                        total_items_price.setText("\u20b9" + FinalAmount + "/-");
                        total_price.setText("\u20b9" + FinalAmount + "/-");
                        String gstamt  =jsonObject.getString("gstAmt");
                        String Shipping =jsonObject.getString("DeliveryCharge");
                        delivery_price.setText("\u20b9" + Shipping+ "/-");
                        tvGstt.setText("\u20b9" + gstamt+ "/-");
                        JSONArray jsonArray = jsonObject.getJSONArray("objItem");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("ProductTitle", jsonObject2.getString("ProductTitle"));
                            hm.put("Quantity", jsonObject2.getString("Quantity"));
                            hm.put("TotalPrice", jsonObject2.getString("TotalPrice"));
                            hm.put("ImgPath", jsonObject2.getString("ImgPath"));
                            hm.put("OrderStatus", jsonObject2.getString("OrderStatus"));
                            arrOrderItemDetails.add(hm);
                        }
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        CartAdapter productAdapter = new CartAdapter(getApplicationContext(), arrOrderItemDetails);
                        rcv_orderitem_details.setLayoutManager(layoutManager);
                        rcv_orderitem_details.setAdapter(productAdapter);

                        JSONArray jsonArrayy = jsonObject.getJSONArray("AddressList");
                        for (int i = 0; i < jsonArrayy.length(); i++) {
                            JSONObject jsonObject2 = jsonArrayy.getJSONObject(i);
                            String Address = jsonObject2.getString("Address");
                            String CityName = jsonObject2.getString("CityName");
                            String StateName = jsonObject2.getString("StateName");
                            String MobileNo = jsonObject2.getString("MobileNo");
                            String CustomerName = jsonObject2.getString("CustomerName");
                            String Pincode = jsonObject2.getString("Pincode");
                            tv_name.setText(CustomerName);
                            tv_pincode.setText(Pincode);
                            tv_number.setText(MobileNo);
                            String address = Address + ", " + StateName + "," + CityName;
                            tv_addnewAddress.setText(address);


                        }


                    } else {

                        Toast.makeText(OrderDetailsActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, OrderDetailsActivity.this, call1, "", true);


    }
    private class CartAdapter extends RecyclerView.Adapter<ProductHolder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public CartAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrCartlist) {
            data = arrCartlist;
        }


        public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ProductHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflateorder_summary, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final ProductHolder holder, final int position) {
            Picasso.get().load(data.get(position).get("ImgPath")).into(holder.iv_product_image);
            holder.tv_qty.setText("Quantity "+data.get(position).get("Quantity"));
            holder.tv_price.setText("Price "+data.get(position).get("TotalPrice"));
            holder.tv_title.setText(data.get(position).get("ProductTitle"));
        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class ProductHolder extends RecyclerView.ViewHolder {
        ImageView iv_product_image;
        TextView tv_title, tv_price, tv_qty;

        public ProductHolder(View itemView) {
            super(itemView);
            iv_product_image = itemView.findViewById(R.id.iv_product_image);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_qty = itemView.findViewById(R.id.tv_qty);


        }
    }

}