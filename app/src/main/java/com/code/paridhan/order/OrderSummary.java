package com.code.paridhan.order;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.code.paridhan.Address.AddNewAddressActivity;
import com.code.paridhan.Address.ShippingAddresslist;
import com.code.paridhan.R;
import com.code.paridhan.RetrofitAPI.ApiService;
import com.code.paridhan.RetrofitAPI.ConnectToRetrofit;
import com.code.paridhan.RetrofitAPI.GlobalAppApis;
import com.code.paridhan.RetrofitAPI.RetrofitCallBackListenar;
import com.code.paridhan.basic.CartActivity;
import com.code.paridhan.basic.MainActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

import static com.code.paridhan.RetrofitAPI.API_Config.getApiClient_ByPost;

public class OrderSummary extends AppCompatActivity implements View.OnClickListener {
    TextView tv_other,tvAddress;
    private SharedPreferences sharedPreferences;
    String username;
    RecyclerView rv_cart_list;
    private ArrayList<HashMap<String, String>> arrCartlist = new ArrayList<>();

      //TextView
      TextView txt_TotalAmount,tvPayAbleAmmount,tv_name, tv_pincode, tv_number, tv_addnewAddress;
     String customername="",phonenumber="",flat="",Landmark="",pincode="",Street="",srno="",id="1",StateName="",CityName="";
     RadioButton rb_COD,rb_payNow;
     String PaymentMethod="1",paymentmode="Cash";
     Button btn_CreateOrder;
     String PayableAmt="",PaymentUrl="";
     String genrated_token,ProductId;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        findViewById();
        ViewCartList();
        listnere();

    }

    private void listnere() {
        tv_other.setOnClickListener(this);
        rb_COD.setOnClickListener(this);
        rb_payNow.setOnClickListener(this);
        btn_CreateOrder.setOnClickListener(this);
    }
    private void findViewById() {
        tv_other=findViewById(R.id.tv_other);
        tvAddress=findViewById(R.id.tvAddress);
        rv_cart_list=findViewById(R.id.rv_cart_list);
        tv_name = findViewById(R.id.textView4);
        tv_number = findViewById(R.id.textView3);
        tv_addnewAddress = findViewById(R.id.textView5);
        tv_pincode = findViewById(R.id.textView6);
        tvPayAbleAmmount = findViewById(R.id.tvPayAbleAmmount);
        txt_TotalAmount = findViewById(R.id.txt_TotalAmount);
        rb_payNow = findViewById(R.id.rb_payNow);
        rb_COD = findViewById(R.id.rb_COD);
        btn_CreateOrder = findViewById(R.id.btn_CreateOrder);

        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        customername = sharedPreferences.getString("customerName", "");
        pincode = sharedPreferences.getString("Pincode", "");
        phonenumber = sharedPreferences.getString("MobileNo", "");
        Landmark = sharedPreferences.getString("Landmark", "");
        Street = sharedPreferences.getString("Street", "");
        StateName = sharedPreferences.getString("StateName", "");
        CityName = sharedPreferences.getString("CityName", "");
        flat = sharedPreferences.getString("House", "");
        srno = sharedPreferences.getString("srno", "");

       tv_name.setText(customername);

       tv_pincode.setText(pincode);

       tv_number.setText(phonenumber);

        String address = flat + ", " +Landmark + ", " + Street + ", " + StateName + "," + CityName;

        tv_addnewAddress.setText(address);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_other:
                Intent intent = new Intent(getApplicationContext(), ShippingAddresslist.class);
                startActivity(intent);
                break;

            case R.id.rb_COD:
                PaymentMethod="1";
                paymentmode="Cash";
                break;

            case R.id.rb_payNow:
                PaymentMethod="2";
                paymentmode="Online";

                Intent intent1=new Intent(getApplicationContext(),OnlinePaymentActivity.class);
                intent1.putExtra("PayableAmt",PayableAmt);
                intent1.putExtra("token_gen",genrated_token);
                intent1.putExtra("orderid",ProductId);
                intent1.putExtra("userName",username);
                intent1.putExtra("phonenumber",phonenumber);
                startActivity(intent1);
                break;


            case R.id.btn_CreateOrder:
                CustomerAddress(PaymentMethod,paymentmode,username,srno,PayableAmt);
                break;

        }

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
            Picasso.get().load(data.get(position).get("Path")).into(holder.iv_product_image);
            holder.tv_qty.setText(data.get(position).get("Quantity"));
            holder.tv_price.setText(data.get(position).get("SaleRate"));
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
    public void ViewCartList() {
        String otp1 = new GlobalAppApis().ViewCartList(username);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.ViewCartList(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    arrCartlist.clear();
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                     PayableAmt = jsonObject.getString("PayableAmt");
                    tvPayAbleAmmount.setText("\u20b9" + PayableAmt);
                    txt_TotalAmount.setText("\u20b9" + PayableAmt);
                    if (Status.equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("objcart");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("Path", jsonObject2.getString("Path"));
                            hm.put("ProductId", jsonObject2.getString("ProductId"));
                            ProductId=jsonObject2.getString("ProductId");
                            hm.put("ProductTitle", jsonObject2.getString("ProductTitle"));
                            hm.put("Quantity", jsonObject2.getString("Quantity"));
                            hm.put("SaleRate", jsonObject2.getString("SaleRate"));
                            hm.put("UnitRate", jsonObject2.getString("UnitRate"));
                            hm.put("VarName", jsonObject2.getString("VarName"));
                            arrCartlist.add(hm);
                            GenerateOrderToken(PayableAmt,ProductId);

                        }


                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        CartAdapter productAdapter = new CartAdapter(getApplicationContext(), arrCartlist);
                        rv_cart_list.setLayoutManager(layoutManager);
                        rv_cart_list.setAdapter(productAdapter);
                        // Pager listener over indicator

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, OrderSummary.this, call1, "", true);


    }
    private void CustomerAddress(String paymentMethod, String paymentmode, String username, String srno,String PayableAmt) {

        ApiService client1 = getApiClient_ByPost();
        String otp1 = new GlobalAppApis().OrderPlaced(paymentMethod,paymentmode,username,srno,PayableAmt);

        Call<String> call1 = client1.OrderPlaced(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                   JSONObject jsonObject = new JSONObject(result);
                   String status = jsonObject.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        Toast.makeText(OrderSummary.this, "Order Book Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(OrderSummary.this, "Something Wrong...", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, OrderSummary.this, call1, "", true);
    }
    private void GenerateOrderToken(String amount,String ProductId) {

        ApiService client1 = getApiClient_ByPost();
        String otp1 = new GlobalAppApis().GenerateOrderToken(username, amount,ProductId);

        Call<String> call1 = client1.GenerateOrderToken(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.v("respnsedata", String.valueOf(jsonObject));
                    String status = jsonObject.getString("Status");
                    genrated_token = jsonObject.getString("TokenNo");
                    if (status.equalsIgnoreCase("true")) {
                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, OrderSummary.this, call1, "", true);
    }

}