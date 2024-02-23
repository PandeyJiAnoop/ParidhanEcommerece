package com.code.paridhan.Address;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.code.paridhan.R;
import com.code.paridhan.RetrofitAPI.ApiService;
import com.code.paridhan.RetrofitAPI.ConnectToRetrofit;
import com.code.paridhan.RetrofitAPI.GlobalAppApis;
import com.code.paridhan.RetrofitAPI.RetrofitCallBackListenar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

import static com.code.paridhan.RetrofitAPI.API_Config.getApiClient_ByPost;

public class AddNewAddressActivity extends AppCompatActivity implements View.OnClickListener {
   RelativeLayout rlHeader;
    TextInputEditText edtCutomerName, edt_phonenumber, edt_flat, edt_colony, edtLandmark, edt_pincode,edtStreet;

    CitySpinner spinnerr;
    //ArrayList
    //ArrayList
    ArrayList<String> StateList;
    ArrayList<String> StateListID;
    ArrayList<HashMap<String, String>> arrStatelist;
    String address="";
    //////////////////
    //ArrayList
    ArrayList<String> CityList;
    ArrayList<String> CityListID;
    ArrayList<HashMap<String, String>> arrCitylist;
    //////////////////
    String city="";

    Spinner sp_state, sp_city;
    StateAdapter stateAdapter;
    LinearLayout llAddSave;
    private SharedPreferences sharedPreferences;
    String username;
    String customername="",phonenumber="",flat="",Landmark="",pincode="",Street="",srno="",id="1";
    TextView tvAddressss,save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);
        findViewById();
        setListner();
        State_CityList();
    }

    private void setListner() {
        rlHeader.setOnClickListener(this);
        llAddSave.setOnClickListener(this);
    }

    private void findViewById() {
        sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        rlHeader=findViewById(R.id.rlHeader);
        //EditText
        edtCutomerName = findViewById( R.id.edtCutomerName );
        edt_phonenumber = findViewById( R.id.edtPhoneNymber );
        edt_flat = findViewById( R.id.edt_busniess );
        edtStreet = findViewById( R.id.edtStreet );
        edtLandmark = findViewById( R.id.edtLandmark );
        edt_pincode = findViewById( R.id.edt_pincode );
        llAddSave = findViewById( R.id.llAddSave );
        save = findViewById( R.id.save );
        tvAddressss = findViewById( R.id.tvAddressss );

        //Spinner
        sp_city = findViewById( R.id.sp_city );
        sp_state = findViewById( R.id.sp_state );

        StateList = new ArrayList<>();
        StateListID = new ArrayList<>();
        arrStatelist = new ArrayList<>();

        CityList = new ArrayList<>();
        CityListID = new ArrayList<>();
        arrCitylist = new ArrayList<>();
        customername=getIntent().getStringExtra("customerName");
        pincode=getIntent().getStringExtra("Pincode");
        phonenumber=getIntent().getStringExtra("MobileNo");
        Landmark=getIntent().getStringExtra("Landmark");
        Street=getIntent().getStringExtra("Street");
        srno=getIntent().getStringExtra("srno");
        id=getIntent().getStringExtra("id");
        flat=getIntent().getStringExtra("House");

        if(id.equalsIgnoreCase("1"))
        {
            tvAddressss.setText("Add New Address");
            save.setText("Save Address");

        }
        else {

            edtCutomerName.setText(customername);
            edt_flat.setText(flat);
            edt_phonenumber.setText(phonenumber);
            edtStreet.setText(Street);
            edt_pincode.setText(pincode);
            edtLandmark.setText(Landmark);
            tvAddressss.setText("Update Address");
            save.setText("Update Address");

        }




        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_city.getSelectedItem().toString().trim() == "Select City") {
                    city="";
                } else {
                    city = CityListID.get(sp_city.getSelectedItemPosition());

                    Log.v("easd", city);


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (parent.getItemAtPosition(position).toString() == "Select State") {
                    address="";
                } else {
                    address = StateListID.get(sp_state.getSelectedItemPosition());

                    State_CityList(address);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rlHeader:
                onBackPressed();
                break;

            case R.id.llAddSave:
                if (edtCutomerName.getText().toString().equalsIgnoreCase("")) {
                    edtCutomerName.setError("Fields can't be blank!");
                    edtCutomerName.requestFocus();
                } else if (edt_phonenumber.getText().toString().length() != 10) {
                    edt_phonenumber.setError("Fields can't be blank!");
                    edt_phonenumber.requestFocus();
                } else if (edt_flat.getText().toString().equalsIgnoreCase("")) {
                    edt_flat.setError("Fields can't be blank!");
                    edt_flat.requestFocus();
                }
                else if (edtStreet.getText().toString().equalsIgnoreCase("")) {
                    edtStreet.setError("Fields can't be blank!");
                    edtStreet.requestFocus();
                }
                else if (edtLandmark.getText().toString().equalsIgnoreCase("")) {
                    edtLandmark.setError("Fields can't be blank!");
                    edtLandmark.requestFocus();
                }
                else if (edt_pincode.getText().toString().equalsIgnoreCase("")) {
                    edt_pincode.setError("Fields can't be blank!");
                    edt_pincode.requestFocus();
                }
                else if (address.equalsIgnoreCase("Select State")) {
                    Toast.makeText(this, "Please Select State", Toast.LENGTH_SHORT).show();
                }
                else if (city.equalsIgnoreCase("Select City")) {
                    Toast.makeText(this, "Please Select City", Toast.LENGTH_SHORT).show();
                }
                else {

                    if(id.equalsIgnoreCase("1"))
                    {
                        CustomerAddress(username,edtCutomerName.getText().toString(),edt_phonenumber.getText().toString(),edt_flat.getText().toString(),edtStreet.getText().toString(),edtLandmark.getText().toString(),edt_pincode.getText().toString(),address,city);

                    }
                    else {
                        UpdateCustomerAddress(username,edtCutomerName.getText().toString(),edt_phonenumber.getText().toString(),edt_flat.getText().toString(),edtStreet.getText().toString(),edtLandmark.getText().toString(),edt_pincode.getText().toString(),address,city,srno);

                    }


                }

                break;
        }
    }



    public static class StateAdapter extends ArrayAdapter<String> {

        ArrayList<String> data;

        public StateAdapter(Context context, int textViewResourceId, ArrayList<String> arraySpinner_time) {

            super( context, textViewResourceId, arraySpinner_time );

            this.data = arraySpinner_time;

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView( position, convertView, parent );
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView( position, convertView, parent );
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
            View row = inflater.inflate( R.layout.spinner, parent, false );
            TextView label = (TextView) row.findViewById( R.id.tvName );
            label.setText( data.get( position ) );
            return row;
        }
    }

    public static class CitySpinner extends ArrayAdapter<String> {

        ArrayList<String> data;

        public CitySpinner(Context context, int textViewResourceId, ArrayList<String> arraySpinner_time) {

            super( context, textViewResourceId, arraySpinner_time );

            this.data = arraySpinner_time;

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView( position, convertView, parent );
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView( position, convertView, parent );
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
            View row = inflater.inflate( R.layout.spinner, parent, false );
            TextView labell = (TextView) row.findViewById( R.id.tvName );
            labell.setText( data.get( position ) );
            return row;
        }
    }

    public void State_CityList() {

        ApiService client1 = getApiClient_ByPost();
        String otp1 = new GlobalAppApis().State_CityList("1","");

        Call<String> call1 = client1.State_CityList(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {

                    arrStatelist.clear();
                    StateListID.clear();
                    StateList.clear();

                    StateListID.add("Select State");
                    StateList.add("Select State");


                    JSONObject jsonObject = new JSONObject(result);

                    String status = jsonObject.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray jsonArrayr = jsonObject.getJSONArray("DetailList");
                        for (int i = 0; i < jsonArrayr.length(); i++) {
                            JSONObject block_object = jsonArrayr.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("Name", block_object.get("Name").toString());
                            map.put("Id", block_object.get("Id").toString());
                            arrStatelist.add(map);
                            StateListID.add(arrStatelist.get(i).get("Id"));
                            StateList.add(arrStatelist.get(i).get("Name"));


                        }
                        stateAdapter = new StateAdapter(getApplicationContext(), R.layout.spinner, (ArrayList<String>) StateList);
                        //  LocationSpinner.setAdapter(new spinnerAdapter(getApplicationContext(), R.layout.spinner_layout, (ArrayList<String>) Locationlist));
                        sp_state.setAdapter(stateAdapter);

                    } else {
                        Toast.makeText(AddNewAddressActivity.this, "Something Wrong...", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, AddNewAddressActivity.this, call1, "", true);
    }


    public void State_CityList(String state) {

        ApiService client1 = getApiClient_ByPost();
        String otp1 = new GlobalAppApis().State_CityList("2",state);

        Call<String> call1 = client1.State_CityList(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {

                //Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();

                try {

                    CityList.clear();
                    CityListID.clear();
                    arrCitylist.clear();
                    CityListID.add("Select City");
                    CityList.add("Select City");
                    JSONObject jsonObject = new JSONObject(result);

                    String status = jsonObject.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray jsonArrayr = jsonObject.getJSONArray("DetailList");
                        for (int i = 0; i < jsonArrayr.length(); i++) {
                            JSONObject block_object = jsonArrayr.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("Name", block_object.get("Name").toString());
                            map.put("Id", block_object.get("Id").toString());
                            arrCitylist.add(map);
                            CityListID.add(arrCitylist.get(i).get("Id"));
                            CityList.add(arrCitylist.get(i).get("Name"));


                        }
                        spinnerr = new CitySpinner(getApplicationContext(), R.layout.spinner, (ArrayList<String>) CityList);
                        //  LocationSpinner.setAdapter(new spinnerAdapter(getApplicationContext(), R.layout.spinner_layout, (ArrayList<String>) Locationlist));
                        sp_city.setAdapter(spinnerr);

                    } else {
                        Toast.makeText(AddNewAddressActivity.this, "Something Wrong...", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, AddNewAddressActivity.this, call1, "", true);
    }

    private void CustomerAddress(String username,String CustomerAddress,String phonenumber,String flat,String Street,String landmark,String Pincode,String state,String city) {

        ApiService client1 = getApiClient_ByPost();
        String otp1 = new GlobalAppApis().CustomerAddress(username,CustomerAddress,phonenumber,flat,Street,landmark,Pincode,state,city);

        Call<String> call1 = client1.CustomerAddress(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {




                    JSONObject jsonObject = new JSONObject(result);

                    String status = jsonObject.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        Toast.makeText(AddNewAddressActivity.this, "Address Add Successfully", Toast.LENGTH_SHORT).show();
                         startActivity(new Intent(getApplicationContext(),ShippingAddresslist.class));
                    } else {
                        Toast.makeText(AddNewAddressActivity.this, "Something Wrong...", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, AddNewAddressActivity.this, call1, "", true);
    }


    private void UpdateCustomerAddress(String username,String CustomerAddress,String phonenumber,String flat,String Street,String landmark,String Pincode,String state,String city,String srno) {

        ApiService client1 = getApiClient_ByPost();
        String otp1 = new GlobalAppApis().UpdateCustomerAddress(username,CustomerAddress,phonenumber,flat,Street,landmark,Pincode,state,city,srno);

        Call<String> call1 = client1.CustomerAddress(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    String status = jsonObject.getString("Status");

                    if (status.equalsIgnoreCase("true")) {
                        Toast.makeText(AddNewAddressActivity.this, "Address Update Succesfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),ShippingAddresslist.class));
                    } else {
                        Toast.makeText(AddNewAddressActivity.this, "Something Wrong try again..", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, AddNewAddressActivity.this, call1, "", true);
    }
}