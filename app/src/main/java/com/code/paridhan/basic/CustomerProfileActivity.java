package com.code.paridhan.basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.code.paridhan.R;
import com.code.paridhan.RetrofitAPI.ApiService;
import com.code.paridhan.RetrofitAPI.ConnectToRetrofit;
import com.code.paridhan.RetrofitAPI.GlobalAppApis;
import com.code.paridhan.RetrofitAPI.RetrofitCallBackListenar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

import static com.code.paridhan.RetrofitAPI.API_Config.getApiClient_ByPost;

public class CustomerProfileActivity extends AppCompatActivity implements View.OnClickListener {
    EditText tvName, tvContact, tvAddress;
    private SharedPreferences sharedPreferences;
    String username;
    Button btn_Update;
    CircleImageView tv_first_letter_text;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String imgString = "";
    RelativeLayout rlHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        findViewById();
        setListner();
        CustomerProfile();
    }

    private void setListner() {
        btn_Update.setOnClickListener(this);
        tv_first_letter_text.setOnClickListener(this);
        rlHeader.setOnClickListener(this);
    }

    private void findViewById() {
        tvName = findViewById(R.id.tvName);
        tvContact = findViewById(R.id.tvContact);
        tvAddress = findViewById(R.id.tvAddress);
        btn_Update = findViewById(R.id.btn_Update);
        tv_first_letter_text = findViewById(R.id.tv_first_letter_text);
        rlHeader = findViewById(R.id.rlHeader);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_Update:
                if (tvName.getText().toString().equalsIgnoreCase("")) {
                    tvName.setError("Fields can't be blank!");
                    tvName.requestFocus();
                } else if (tvContact.getText().toString().equalsIgnoreCase("")) {
                    tvContact.setError("Fields can't be blank!");
                    tvContact.requestFocus();
                }

                else if (tvAddress.getText().toString().equalsIgnoreCase("")) {
                    tvAddress.setError("Fields can't be blank!");
                    tvAddress.requestFocus();
                }
                else {
                    UpdateCustomerProfile(username,tvName.getText().toString(),tvContact.getText().toString(),tvAddress.getText().toString());
                }


                break;

            case R.id.tv_first_letter_text:
                selectImage();
                break;

            case R.id.rlHeader:
                onBackPressed();
                break;
        }

    }

    private void UpdateCustomerProfile(String username, String name, String contact, String email) {
        String otp1 = new GlobalAppApis().UpdateCustomerProfile(username,name,contact,email,imgString);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.UpdateCustomerProfile(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {

                    Log.v("getStringgg", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");

                    if (Status.equalsIgnoreCase("true")) {
                        Toast.makeText(CustomerProfileActivity.this, "Profile Update Succesfully", Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, CustomerProfileActivity.this, call1, "", true);


    }


    public void CustomerProfile() {
        String otp1 = new GlobalAppApis().CustomerProfile(username);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.CustomerProfile(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {

                    Log.v("getStringgg", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");

                    if (Status.equalsIgnoreCase("true")) {
                        tvName.setText(jsonObject.getString("CustomerName"));
                        tvAddress.setText(jsonObject.getString("EmailAddress"));
                        tvContact.setText(jsonObject.getString("MobileNo"));



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, CustomerProfileActivity.this, call1, "", true);


    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CustomerProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(CustomerProfileActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tv_first_letter_text.setImageBitmap(thumbnail);

        getEncoded64ImageStringFromBitmap(thumbnail);


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        tv_first_letter_text.setImageBitmap(bm);


    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        imgString = Base64.encodeToString(byteFormat, Base64.DEFAULT);

        Log.v("dadadadadaaaa", imgString);


        imgString = Base64.encodeToString(byteFormat, Base64.DEFAULT);


        return imgString;
    }
}