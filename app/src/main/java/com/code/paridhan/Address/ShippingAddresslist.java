package com.code.paridhan.Address;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
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
import com.code.paridhan.basic.CartActivity;
import com.code.paridhan.order.OrderSummary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

import static com.code.paridhan.RetrofitAPI.API_Config.getApiClient_ByPost;

public class ShippingAddresslist extends AppCompatActivity implements View.OnClickListener {
    //CustomTextView
    TextView tvaddnewaddress;
    JSONArray jsonArrayr;
    //RecyclerView
    RecyclerView recViewlistingAddress;

    //ImageView
    ImageView ivback, ivloader;

    //LinearLayout
    LinearLayout llproces;

    Boolean canProcced = false;
    RelativeLayout rlHeader;
    Adapter adapter;

    ///
    String customerName, flatHouseBulding, streetColony, landmark, pincode, city, phoneNumber, state, addressId;

    private ArrayList<HashMap<String, String>> ShippinAddresList = new ArrayList<>();
    public static final ArrayList<HashMap<String, String>> addressNewList = new ArrayList();

    private SharedPreferences sharedPreferences;
    String username;
    private SharedPreferences.Editor login_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_addresslist);
        findViewById();
        setListener();
    }

    private void setListener() {
        //textView
        tvaddnewaddress.setOnClickListener(this);

        //ImageView
        rlHeader.setOnClickListener(this);

        //LinearLayout
        llproces.setOnClickListener(this);
    }

    private void findViewById() {
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        //TextView
        tvaddnewaddress = findViewById(R.id.addnewaddress);

        //RecyclerView
        recViewlistingAddress = findViewById(R.id.listingaddress);

        //ImageView
        rlHeader = findViewById(R.id.rlHeader);

        //LinearLayout
        llproces = findViewById(R.id.proces);

        CustomerAddress();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addnewaddress:
                Intent intent = new Intent(getApplicationContext(), AddNewAddressActivity.class);
                intent.putExtra("id", "1");
                startActivity(intent);
                break;


            case R.id.rlHeader:
                finish();
                break;

            case R.id.proces:
                if(jsonArrayr==null)
                {
                 Toast.makeText(this, "Add New Address", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent I1 = new Intent(getApplicationContext(), OrderSummary.class);
                    startActivity(I1);
                }
               break;

        }
    }

    private class Adapter extends RecyclerView.Adapter<Holder> {

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public Adapter(Context applicationContext, ArrayList<HashMap<String, String>> shippinAddresList) {
            data = shippinAddresList;
        }


        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_address_list, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, final int position) {


            holder.tv_name.setText(data.get(position).get("CustomerName"));

            holder.tv_pincode.setText(data.get(position).get("Pincode"));

            holder.tv_number.setText(data.get(position).get("MobileNo"));

            String address = data.get(position).get("House") + ", " + data.get(position).get("Landmark") + ", " + data.get(position).get("Street") + ", " + data.get(position).get("StateName") + "," + data.get(position).get("CityName");

            holder.tv_addnewAddress.setText(address);


            if (data.get(position).get("primary").equals("1")) {
                holder.ivCheck.setImageResource(R.drawable.redioselect);
                sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
                login_editor = sharedPreferences.edit();
                login_editor.putString("customerName", data.get(position).get("CustomerName"));
                login_editor.putString("Pincode", data.get(position).get("Pincode"));
                login_editor.putString("MobileNo", data.get(position).get("MobileNo"));
                login_editor.putString("Landmark", data.get(position).get("Landmark"));
                login_editor.putString("Street", data.get(position).get("Street"));
                login_editor.putString("StateName", data.get(position).get("StateName"));
                login_editor.putString("CityName", data.get(position).get("CityName"));
                login_editor.putString("House", data.get(position).get("House"));
                login_editor.putString("srno", data.get(position).get("srno"));

                login_editor.commit();


            } else {
                holder.ivCheck.setImageResource(R.drawable.radio);
            }

            holder.tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), AddNewAddressActivity.class);
                    intent.putExtra("customerName", data.get(position).get("CustomerName"));
                    intent.putExtra("Pincode", data.get(position).get("Pincode"));
                    intent.putExtra("MobileNo", data.get(position).get("MobileNo"));
                    intent.putExtra("Landmark", data.get(position).get("Landmark"));
                    intent.putExtra("Street", data.get(position).get("Street"));
                    intent.putExtra("StateName", data.get(position).get("StateName"));
                    intent.putExtra("CityName", data.get(position).get("CityName"));
                    intent.putExtra("House", data.get(position).get("House"));
                    intent.putExtra("srno", data.get(position).get("srno"));
                    intent.putExtra("id", "2");

                    startActivity(intent);

                }
            });


            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShippingAddresslist.this);
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
                            DeleteCustomerAddress(data.get(position).get("srno"));
                            ShippinAddresList.remove(position);
                            notifyDataSetChanged();
                            dialog.dismiss();

                        }


                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                }
            });
            holder.ivCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    for (int i = 0; i < data.size(); i++) {

                        HashMap<String, String> addList = new HashMap();
                        addList.put("CustomerName", data.get(i).get("CustomerName"));
                        addList.put("Pincode", data.get(i).get("Pincode"));
                        addList.put("MobileNo", data.get(i).get("MobileNo"));
                        addList.put("Landmark", data.get(i).get("Landmark"));
                        addList.put("Street", data.get(i).get("Street"));
                        addList.put("House", data.get(i).get("House"));
                        addList.put("srno", data.get(i).get("srno"));
                        addList.put("StateName", data.get(i).get("StateName"));
                        addList.put("CityName", data.get(i).get("CityName"));
                        if (data.get(position).get("srno").equals(data.get(i).get("srno"))) {
                            if (data.get(i).get("primary").equals("1")) {
                                addList.put("primary", "0");
                            } else {
                                addList.put("primary", "1");

                                addressNewList.add(addList);
                            }
                        } else {
                            addList.put("primary", "0");
                        }

                        ShippinAddresList.set(i, addList);
                    }

                    adapter.notifyDataSetChanged();

                }

            });
        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class Holder extends RecyclerView.ViewHolder {

        //TextView
        TextView tv_name, tv_pincode, tv_number, tv_addnewAddress, tvEdit, tvDelete;

        //ImageView
        ImageView ivCheck;

        public Holder(View inflate) {

            super(inflate);
            tv_name = inflate.findViewById(R.id.textView4);
            tv_number = inflate.findViewById(R.id.textView3);
            tv_addnewAddress = inflate.findViewById(R.id.textView5);
            tv_pincode = inflate.findViewById(R.id.textView6);
            tvEdit = inflate.findViewById(R.id.tvEdit);
            tvDelete = inflate.findViewById(R.id.tvDelete);
            ivCheck = inflate.findViewById(R.id.imageView5);


        }
    }

    private void CustomerAddress() {

        ApiService client1 = getApiClient_ByPost();
        String otp1 = new GlobalAppApis().GetCustomerAddress(username);

        Call<String> call1 = client1.CustomerAddress(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    String status = jsonObject.getString("Status");
                     jsonArrayr = jsonObject.getJSONArray("AddressList");
                    if (status.equalsIgnoreCase("true")) {
                        for (int i = 0; i < jsonArrayr.length(); i++) {
                            JSONObject block_object = jsonArrayr.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("CityId", block_object.get("CityId").toString());
                            map.put("CityName", block_object.get("CityName").toString());
                            map.put("CustomerId", block_object.get("CustomerId").toString());
                            map.put("CustomerName", block_object.get("CustomerName").toString());
                            map.put("House", block_object.get("House").toString());
                            map.put("Landmark", block_object.get("Landmark").toString());
                            map.put("MobileNo", block_object.get("MobileNo").toString());
                            map.put("Pincode", block_object.get("Pincode").toString());
                            map.put("StateId", block_object.get("StateId").toString());
                            map.put("StateName", block_object.get("StateName").toString());
                            map.put("Street", block_object.get("Street").toString());
                            map.put("srno", block_object.get("srno").toString());

                            if (i == 0) {
                                map.put("primary", "1");
                                addressNewList.add(map);
                            } else {
                                map.put("primary", "0");
                            }
                            ShippinAddresList.add(map);


                        }

                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        adapter = new Adapter(getApplicationContext(), ShippinAddresList);
                        recViewlistingAddress.setLayoutManager(layoutManager);
                        recViewlistingAddress.setAdapter(adapter);
                    } else {
                        Toast.makeText(ShippingAddresslist.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, ShippingAddresslist.this, call1, "", true);
    }

    private void DeleteCustomerAddress(String srno) {

        ApiService client1 = getApiClient_ByPost();
        String otp1 = new GlobalAppApis().DeletCustomerAddress(username, srno);

        Call<String> call1 = client1.CustomerAddress(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    String status = jsonObject.getString("Status");
                    JSONArray jsonArrayr = jsonObject.getJSONArray("AddressList");
                    if (status.equalsIgnoreCase("true")) {
                        Toast.makeText(ShippingAddresslist.this, "Address Remove Succesfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ShippingAddresslist.this, "Something Wrong try again..", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, ShippingAddresslist.this, call1, "", true);
    }


}