package com.code.paridhan.basic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.code.paridhan.Address.ShippingAddresslist;
import com.code.paridhan.R;
import com.code.paridhan.RetrofitAPI.ApiService;
import com.code.paridhan.RetrofitAPI.ConnectToRetrofit;
import com.code.paridhan.RetrofitAPI.GlobalAppApis;
import com.code.paridhan.RetrofitAPI.RetrofitCallBackListenar;
import com.code.paridhan.order.OrderListActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

import static com.code.paridhan.RetrofitAPI.API_Config.getApiClient_ByPost;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rv_cart_list;
    Button btn_checkout;
    private ArrayList<HashMap<String, String>> arrCartlist = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    String username;
    TextView tv_grand_total,tvShippinCharj,tvgst;
    String value = String.valueOf(1);
    int n1 = 1;
    RelativeLayout rlHeader;
    String Status="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById();
        ViewCartList();
    }

    private void findViewById() {
        rv_cart_list = findViewById(R.id.rv_cart_list);
        btn_checkout = findViewById(R.id.btn_checkout);
        tv_grand_total = findViewById(R.id.tv_grand_total);
        rlHeader = findViewById(R.id.rlHeader);
        tvShippinCharj = findViewById(R.id.tvShippinCharj);
        tvgst = findViewById(R.id.tvgst);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Status.equalsIgnoreCase("false"))
                {
                    Toast.makeText(CartActivity.this, "Your Cart Is Empty Please Add to card", Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(getApplicationContext(), ShippingAddresslist.class));
                }

            }
        });
        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    private class CartAdapter extends RecyclerView.Adapter<ProductHolder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public CartAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrCartlist) {
            data = arrCartlist;
        }


        public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ProductHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_cart_list, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final ProductHolder holder, final int position) {
            Picasso.get().load(data.get(position).get("Path")).into(holder.iv_product_image);
            holder.tv_qty.setText(data.get(position).get("Quantity"));
            holder.tv_price.setText("\u20b9" +data.get(position).get("SaleRate"));
            holder.tv_title.setText(data.get(position).get("ProductTitle"));
            holder.iv_minus.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    n1 = Integer.parseInt(holder.tv_qty.getText().toString());

                    if (n1 > 1) {
                        n1 = n1 - 1;
                        value = String.valueOf(n1);
                        holder.tv_qty.setText(value);
                        UpdateCartQuantity(value, data.get(position).get("ProductId"));
                    }

                }
            });
            holder.iv_add.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    n1 = Integer.parseInt(holder.tv_qty.getText().toString());
                    n1 = n1 + 1;
                    value = String.valueOf(n1);
                    holder.tv_qty.setText(value);
                    UpdateCartQuantity(value, data.get(position).get("ProductId"));


                }


            });
            holder.iv_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CartActivity.this);
                    alertDialogBuilder.setMessage(getString(R.string.remove));
                    alertDialogBuilder.setPositiveButton(getString(R.string.cancele),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {
                                    dialog.dismiss();

                                }
                            });

                    alertDialogBuilder.setNegativeButton(getString(R.string.removee), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           RemoveItemFromCart(holder.tv_qty.getText().toString(),data.get(position).get("ProductId"));
                            arrCartlist.remove(position);
                            notifyDataSetChanged();
                            dialog.dismiss();

                        }


                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                }
            });

        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        ImageView iv_product_image, iv_clear, iv_minus, iv_add;
        TextView tv_title, tv_price, tv_qty;

        public ProductHolder(View itemView) {
            super(itemView);
            iv_product_image = itemView.findViewById(R.id.iv_product_image);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_qty = itemView.findViewById(R.id.tv_qty);
            iv_clear = itemView.findViewById(R.id.iv_clear);
            iv_minus = itemView.findViewById(R.id.iv_minus);
            iv_add = itemView.findViewById(R.id.iv_add);


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
                     Status = jsonObject.getString("Status");
                    String PayableAmt = jsonObject.getString("PayableAmt");

                    String gstamt  =jsonObject.getString("TotalGst");
                    String Shipping =jsonObject.getString("TotalShiping");

                    tv_grand_total.setText("\u20b9" + PayableAmt);
                    tvShippinCharj.setText("\u20b9" + Shipping);
                    tvgst.setText("\u20b9" + gstamt);
                    if (Status.equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("objcart");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("Path", jsonObject2.getString("Path"));
                            hm.put("ProductId", jsonObject2.getString("ProductId"));
                            hm.put("ProductTitle", jsonObject2.getString("ProductTitle"));
                            hm.put("Quantity", jsonObject2.getString("Quantity"));
                            hm.put("SaleRate", jsonObject2.getString("SaleRate"));
                            hm.put("UnitRate", jsonObject2.getString("UnitRate"));
                            hm.put("VarName", jsonObject2.getString("VarName"));
                            hm.put("MRP", jsonObject2.getString("MRP"));
                            hm.put("shippingCharge", jsonObject2.getString("shippingCharge"));

                            arrCartlist.add(hm);

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
        }, CartActivity.this, call1, "", true);


    }

    private void UpdateCartQuantity(String value, String productId) {
        String otp1 = new GlobalAppApis().UpdateCartdetail(username, value, productId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.UpdateCartdetail(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    String Msg = jsonObject.getString("Msg");
                    if (Status.equalsIgnoreCase("true")) {
                        arrCartlist.clear();
                        ViewCartList();
                        Toast.makeText(CartActivity.this, "Product Update Successfully", Toast.LENGTH_SHORT).show();


                    } else {

                        Toast.makeText(CartActivity.this, Msg, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, CartActivity.this, call1, "", true);


    }
    private void RemoveItemFromCart(String value,String entryId) {

        String otp1 = new GlobalAppApis().DeleteCartdetail(username, value, entryId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.DeleteCartdetail(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    String Msg = jsonObject.getString("Msg");
                    if (Status.equalsIgnoreCase("true")) {
                        arrCartlist.clear();
                        ViewCartList();
                        Toast.makeText(CartActivity.this, "Remove Card Item Successfully", Toast.LENGTH_SHORT).show();


                    } else {

                        Toast.makeText(CartActivity.this, Msg, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, CartActivity.this, call1, "", true);


    }
}