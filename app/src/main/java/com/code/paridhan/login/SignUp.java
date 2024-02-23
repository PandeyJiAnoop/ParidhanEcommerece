package com.code.paridhan.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.code.paridhan.R;
import com.code.paridhan.RetrofitAPI.ApiService;
import com.code.paridhan.RetrofitAPI.ConnectToRetrofit;
import com.code.paridhan.RetrofitAPI.GlobalAppApis;
import com.code.paridhan.RetrofitAPI.RetrofitCallBackListenar;
import com.code.paridhan.basic.SuCategoryListActivity;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;

import static com.code.paridhan.RetrofitAPI.API_Config.getApiClient_ByPost;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    Button signup_button;
    EditText edt_name, edt_mobile, edt_email, edt_password, edt_confirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViewById();
        setlistner();
    }

    private void setlistner() {
        signup_button.setOnClickListener(this);
    }

    private void findViewById() {
        signup_button = findViewById(R.id.btn_signUp);
        edt_name = findViewById(R.id.edt_name);

        edt_mobile = findViewById(R.id.edt_mobile);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        edt_confirmpassword = findViewById(R.id.edt_confirmpassword);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signUp:


                if (edt_name.getText().toString().equalsIgnoreCase("")) {
                    edt_name.setError("Fields can't be blank!");
                    edt_name.requestFocus();
                } else if (edt_mobile.getText().toString().equalsIgnoreCase("")) {
                    edt_mobile.setError("Fields can't be blank!");
                    edt_mobile.requestFocus();
                } else if (edt_email.getText().toString().equalsIgnoreCase("")) {
                    edt_email.setError("Fields can't be blank!");
                    edt_email.requestFocus();
                } else if (edt_password.getText().toString().equalsIgnoreCase("")) {
                    edt_password.setError("Fields can't be blank!");
                    edt_password.requestFocus();
                } else if (edt_confirmpassword.getText().toString().equalsIgnoreCase("")) {
                    edt_confirmpassword.setError("Fields can't be blank!");
                    edt_confirmpassword.requestFocus();
                } else if (!edt_password.getText().toString().equalsIgnoreCase(edt_confirmpassword.getText().toString())) {
                    Toast.makeText(this, "Please Enter Correct Password", Toast.LENGTH_SHORT).show();
                } else {
                    NewRegistration(edt_name.getText().toString(), edt_mobile.getText().toString()
                            , edt_email.getText().toString(), edt_confirmpassword.getText().toString());
                }
                break;

        }

    }

    private void NewRegistration(String name, String mobile, String email, String confirm) {
        String otp1 = new GlobalAppApis().CustomerRegistration(name,mobile,email,confirm);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.CustomerRegistration(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    Log.v("getStringgg", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if (Status.equalsIgnoreCase("true")) {
                        Toast.makeText(SignUp.this, "Registration Successfully ", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));




                        // Pager listener over indicator

                    }
                    else {
                        Toast.makeText(SignUp.this, "Something Wrong try again ", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, SignUp.this, call1, "", true);


    }
}