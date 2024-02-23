package com.code.paridhan.basic;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.code.paridhan.R;
import com.code.paridhan.RetrofitAPI.ApiService;
import com.code.paridhan.RetrofitAPI.ConnectToRetrofit;
import com.code.paridhan.RetrofitAPI.GlobalAppApis;
import com.code.paridhan.RetrofitAPI.RetrofitCallBackListenar;
import com.google.gson.JsonArray;
import com.skyhope.showmoretextview.ShowMoreTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

import static com.code.paridhan.RetrofitAPI.API_Config.getApiClient_ByPost;


public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_add_to_cart;
    ImageView fragment_product_details_vpSlider, fourimage, threeimage, twoimage, iv_whatsapp, iv_wishlist, decrIvv, ivIncreIvv;
    TextView tv_extraOff,tvColor,tvSize,tv_product_title, tv_product_price, tv_actual_price, tv_offer_price, tv_grand_total;
    String ProductCode, Path;
    ProgressDialog mProgressDialog;
    TextView countTv;
    String value = String.valueOf(1);
    int n1 = 1;
    private SharedPreferences sharedPreferences;
    String username;
    RelativeLayout rlHeader;
    RecyclerView rcvVrirant,rcvSize;
    private ArrayList<HashMap<String, String>> arrayVarrint = new ArrayList<>();
    private ArrayList<HashMap<String, String>> arrayvarsize = new ArrayList<>();
    LinearLayoutManager layoutManager,layoutManager1;
    String color="0",Size="0";
    private static final int PERMISSION_REQUEST_CODE = 1;
    LinearLayout llOne;
    ShowMoreTextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        findViewById();
        setListNer();
        ProductDetails();
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission(); // Code for permission
            }
        }
        else
        {

            // Code for Below 23 API Oriented Device
            // Do next code
        }


    }

    private void setListNer() {
        btn_add_to_cart.setOnClickListener(this);
        iv_whatsapp.setOnClickListener(this);
        rlHeader.setOnClickListener(this);

    }

    private void findViewById() {
        fragment_product_details_vpSlider = findViewById(R.id.oneImage);
        twoimage = findViewById(R.id.twoimage);
        threeimage = findViewById(R.id.threeimage);
        fourimage = findViewById(R.id.fourimage);
        iv_whatsapp = findViewById(R.id.iv_whatsapp);
        iv_wishlist = findViewById(R.id.iv_wishlist);
        rlHeader = findViewById(R.id.rlHeader);
        btn_add_to_cart = findViewById(R.id.btn_add_to_cart);
        tv_product_title = findViewById(R.id.tv_product_title);
        tv_product_price = findViewById(R.id.tv_product_price);
        tv_actual_price = findViewById(R.id.tv_actual_price);
        tv_offer_price = findViewById(R.id.tv_offer_price);
        tvDescription = findViewById(R.id.tvDescription);
        tv_grand_total = findViewById(R.id.tv_grand_total);
        iv_whatsapp = findViewById(R.id.iv_whatsapp);
        ivIncreIvv = findViewById(R.id.increIv);
        decrIvv = findViewById(R.id.decrIv);
        countTv = findViewById(R.id.countTv);
        rcvVrirant = findViewById(R.id.rcvVrirant);
        tvColor = findViewById(R.id.tvColor);
        rcvSize = findViewById(R.id.rcvSize);
        tvSize = findViewById(R.id.tvSize);
        tv_extraOff = findViewById(R.id.tv_extraOff);
        llOne = findViewById(R.id.llOne);
        ProductCode = getIntent().getStringExtra("ProductCode");
        Log.v("dadada",ProductCode);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        decrIvv.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                n1 = Integer.parseInt(countTv.getText().toString());

                if (n1 > 1) {
                    n1 = n1 - 1;
                    value = String.valueOf(n1);
                    countTv.setText(value);

                }

            }
        });
        ivIncreIvv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                n1 = Integer.parseInt(countTv.getText().toString());

                n1 = n1 + 1;

                value = String.valueOf(n1);

                countTv.setText(value);

            }
        });
        fragment_product_details_vpSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ZiimActivity.class);
                intent.putExtra("Path", Path);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlHeader:
                finish();
                break;
            case R.id.btn_add_to_cart:
                if(Size.equalsIgnoreCase(""))
                {
                    Size="0";
                }
                else if(color.equalsIgnoreCase(""))
                    color="0";
                else {
                    AddToCard();
                }

                break;
            case R.id.iv_whatsapp:
                if (!Path.equalsIgnoreCase("")) {

                    new DownloadFromURL().execute(Path);
                }
                break;
        }
    }
    public void ProductDetails() {
        String otp1 = new GlobalAppApis().ProductDetails(ProductCode);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.ProductDetails(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {

                    Log.v("getStringgg", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");



                    if (Status.equalsIgnoreCase("true")) {


                        tvDescription.setText(Html.fromHtml(jsonObject.getString("ProductDescription")).toString().trim());

                        tvDescription.setShowingLine(10);
//                        textView.setShowingChar(30);
                        tvDescription.addShowMoreText("ReadMore");
                        tvDescription.addShowLessText("Less");
                        tvDescription.setShowMoreColor(Color.RED); // or other color
                        tvDescription.setShowLessTextColor(Color.RED); // or other color
                        tv_product_title.setText(jsonObject.getString("ProductTitle"));

                        if (jsonObject.getString("ExtraDiscount").equalsIgnoreCase("0.00")) {
                            llOne.setVisibility(View.GONE);

                        }
                        else {
                            if (jsonObject.getString("ExtraDiscountType").equalsIgnoreCase("Percentage")) {

                                tv_extraOff.setText("\u20b9" + jsonObject.getString("ExtraDiscount")+"%");

                                llOne.setVisibility(View.VISIBLE);
                            }
                            else {
                                tv_extraOff.setText("\u20b9" + jsonObject.getString("ExtraDiscount")+" Save");
                                llOne.setVisibility(View.VISIBLE);
                            }

                        }
                        if(!jsonObject.getString("Path").equalsIgnoreCase(""))
                        {
                            Picasso.get().load(jsonObject.getString("Path")).into(fragment_product_details_vpSlider);
                            Path = jsonObject.getString("Path");
                        }
                        if(!jsonObject.getString("Path1").equalsIgnoreCase(""))
                        {
                            Picasso.get().load(jsonObject.getString("Path1")).into(twoimage);

                        }
                        if(!jsonObject.getString("Path2").equalsIgnoreCase(""))
                        {
                            Picasso.get().load(jsonObject.getString("Path2")).into(threeimage);
                        }
                        if(!jsonObject.getString("Path3").equalsIgnoreCase(""))
                        {
                            Picasso.get().load(jsonObject.getString("Path3")).into(fourimage);
                        }
                        tv_grand_total.setText("\u20b9" + jsonObject.getString("salerate"));
                        if (jsonObject.getString("DiscountType").equalsIgnoreCase("Percentage")) {
                            if (jsonObject.getString("salerate").equalsIgnoreCase(jsonObject.getString("productmrp"))) {
                                tv_product_price.setText("\u20b9" + jsonObject.getString("salerate"));
                                tv_actual_price.setText("");
                                tv_offer_price.setText("");
                            } else {
                                tv_product_price.setText("\u20b9" + jsonObject.getString("salerate"));
                                tv_actual_price.setText("\u20b9" + jsonObject.getString("productmrp"));
                                tv_offer_price.setText(jsonObject.getString("Discount") + "%");

                            }
                        } else {



                            if (jsonObject.getString("salerate").equalsIgnoreCase(jsonObject.getString("productmrp"))) {
                                tv_product_price.setText("\u20b9" + jsonObject.getString("salerate"));
                                tv_actual_price.setText("");
                                tv_offer_price.setText("");
                            } else {
                                tv_product_price.setText("\u20b9" + jsonObject.getString("salerate"));
                                tv_actual_price.setText("\u20b9" + jsonObject.getString("productmrp"));
                                tv_offer_price.setText(jsonObject.getString("Discount") + " Save");

                            }
                        }

                        JSONArray varcolor=jsonObject.getJSONArray("varcolor");
                        JSONArray varsize=jsonObject.getJSONArray("varsize");
                      if(varcolor.length()==0)
                      {
                          tvColor.setVisibility(View.GONE);
                          }
                      else { tvColor.setVisibility(View.VISIBLE);
                                  for (int i = 0; i < varcolor.length(); i++) {
                                  JSONObject jsonObject2 = varcolor.getJSONObject(i);
                                  HashMap<String, String> hm = new HashMap<>();
                                  hm.put("VarName", jsonObject2.getString("VarName"));
                                  hm.put("SrNo", jsonObject2.getString("SrNo"));
                                  arrayVarrint.add(hm);
                            }
                          layoutManager = new LinearLayoutManager(ProductDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);

//                          layoutManager = new GridLayoutManager(getApplicationContext(),5);
                          VarrientAdapter adapterr = new VarrientAdapter(getApplicationContext(), arrayVarrint);
                          rcvVrirant.setLayoutManager(layoutManager);
                          rcvVrirant.setAdapter(adapterr);

                      }

                        if(varsize.length()==0)
                        {

                            tvSize.setVisibility(View.GONE);
                        }
                        else {
                            tvSize.setVisibility(View.VISIBLE);
                            for (int i = 0; i < varsize.length(); i++) {
                                JSONObject jsonObject2 = varsize.getJSONObject(i);
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("VarName", jsonObject2.getString("VarName"));
                                hm.put("SrNo", jsonObject2.getString("SrNo"));
                                arrayvarsize.add(hm);
                            }
                            layoutManager1 = new LinearLayoutManager(ProductDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
//                            layoutManager1 = new GridLayoutManager(getApplicationContext(), 5);
                            SizeAdapter adapterr = new SizeAdapter(getApplicationContext(), arrayvarsize);
                            rcvSize.setLayoutManager(layoutManager1);
                            rcvSize.setAdapter(adapterr);

                        }


                    }
                    else {
                        Toast.makeText(ProductDetailsActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, ProductDetailsActivity.this, call1, "", true);


    }
    class DownloadFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ProductDetailsActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle(getString(R.string.pleasewait));
            // Set progressdialog message
            mProgressDialog.setMessage(getString(R.string.loadingimage));
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();


        }

        @Override
        protected String doInBackground(String... fileUrl) {
            int count;
            try {
                File file1 = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "saved_images");
                String file = String.valueOf(new File(file1, "tvName" + ".jpeg"));
                file1.mkdirs();
                URL url = new URL(fileUrl[0]);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                // show progress bar 0-100%
                int fileLength = urlConnection.getContentLength();
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                OutputStream outputStream = new FileOutputStream(file);

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / fileLength));
                    outputStream.write(data, 0, count);
                }
                // flushing output
                outputStream.flush();
                // closing streams
                outputStream.close();
                inputStream.close();

            } catch (Exception e) {
                // Log.e("Error: ", e.getMessage());
                //  kProgressHUD.dismiss();
                System.out.println(e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            File dir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "saved_images");
            Uri uri = Uri.fromFile(dir);
            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);


                    Log.v("urlimage", String.valueOf(uri));
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setType("image/jpg");
                    startActivity(Intent.createChooser(intent, "send"));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), getString(R.string.sharingimage), Toast.LENGTH_SHORT).show();
        }


    }
    public void AddToCard() {
        String otp1 = new GlobalAppApis().Addtocart(username, ProductCode, countTv.getText().toString(),color,Size);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.Addtocart(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {

                    Log.v("getResponsedata", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if (Status.equalsIgnoreCase("true")) {

                        Toast.makeText(ProductDetailsActivity.this, "Product added successfully!!!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), CartActivity.class));
                    } else {
                        Toast.makeText(ProductDetailsActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } }, ProductDetailsActivity.this, call1, "", true);

    }
    private class VarrientAdapter extends RecyclerView.Adapter<Varrient> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        private int selectedPosition = -1;
        public VarrientAdapter(Context applicationContext, ArrayList<HashMap<String, String>> rootCategoryList) {
            data = rootCategoryList;
        }
     public Varrient onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Varrient(LayoutInflater.from(parent.getContext()).inflate(R.layout.varient, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Varrient holder, final int position) {
            holder.ckbName.setText(data.get(position).get("VarName"));
            holder.ckbName.setOnClickListener(view -> {
                selectedPosition = holder.getAdapterPosition();
                color=   data.get(position).get("SrNo");
                notifyDataSetChanged();
            });
            if (selectedPosition==position){

                holder.ckbName.setChecked(true);
            }
            else {
                holder.ckbName.setChecked(false);
            }
        }
        public int getItemCount() {
            return data.size();
        }
    }
    public class Varrient extends RecyclerView.ViewHolder {
        CheckBox ckbName;


        public Varrient(View itemView) {
            super(itemView);
            ckbName = itemView.findViewById(R.id.ckbName);
        }
    }
    private class SizeAdapter extends RecyclerView.Adapter<Size> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        private int selectedPosition = -1;
        public SizeAdapter(Context applicationContext, ArrayList<HashMap<String, String>> rootCategoryList) {
            data = rootCategoryList;
        }
        public Size onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Size(LayoutInflater.from(parent.getContext()).inflate(R.layout.varient, parent, false));
        }
        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Size holder, final int position) {
            holder.ckbName.setText(data.get(position).get("VarName"));
            holder.ckbName.setOnClickListener(view -> {
                selectedPosition = holder.getAdapterPosition();
                Size=   data.get(position).get("SrNo");
                notifyDataSetChanged();
            });
            if (selectedPosition==position){
                holder.ckbName.setChecked(true);
            }
            else {
                holder.ckbName.setChecked(false);
            }
        }
        public int getItemCount() {
            return data.size();
        }
    }
    public class Size extends RecyclerView.ViewHolder {
        CheckBox ckbName;

        public Size(View itemView) {
            super(itemView);
            ckbName = itemView.findViewById(R.id.ckbName);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ProductDetailsActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ProductDetailsActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(ProductDetailsActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(ProductDetailsActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break; }
    }
}