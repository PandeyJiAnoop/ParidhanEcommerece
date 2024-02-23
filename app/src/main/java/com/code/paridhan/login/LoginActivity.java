package com.code.paridhan.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.code.paridhan.R;
import com.code.paridhan.RetrofitAPI.ApiService;
import com.code.paridhan.RetrofitAPI.ConnectToRetrofit;
import com.code.paridhan.RetrofitAPI.GlobalAppApis;
import com.code.paridhan.RetrofitAPI.RetrofitCallBackListenar;
import com.code.paridhan.basic.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;

import static com.code.paridhan.RetrofitAPI.API_Config.getApiClient_ByPost;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    ConstraintLayout signin_page, signup_page;
    Button continue_btn;
    TextView signin, signup;
    TextInputEditText edt_email, edt_password;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById();
        setlistner();
    }

    private void setlistner() {
        continue_btn.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    private void findViewById() {
        continue_btn = findViewById(R.id.continue_button);
        signup = findViewById(R.id.signup);
        edt_email = findViewById(R.id.email);
        edt_password = findViewById(R.id.password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_button:
                if (edt_email.getText().toString().equalsIgnoreCase("")) {
                    edt_email.setError("Fields can't be blank!");
                    edt_email.requestFocus();
                } else if (edt_password.getText().toString().equalsIgnoreCase("")) {
                    edt_password.setError("Fields can't be blank!");
                    edt_password.requestFocus();
                } else {
                    if(edt_email.getText().toString().equalsIgnoreCase("admin"))

                    {
                        Toast.makeText(this, "Invalid Userid and Password", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        login(edt_email.getText().toString(),edt_password.getText().toString());
                    }

                }

                break;
            case R.id.signup:
                startActivity(new Intent(getApplicationContext(), SignUp.class));

                break;
        }

    }

    private void login(String email, String password) {
        String otp1 = new GlobalAppApis().LoginCustomer(email,password);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.LoginCustomer(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    Log.v("getStringgg", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if (Status.equalsIgnoreCase("true")) {
                        Toast.makeText(LoginActivity.this, "Login Successfully ", Toast.LENGTH_SHORT).show();
                        login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
                        login_editor = login_preference.edit();
                        login_editor.putString("username",jsonObject.getString("CustomerCode"));
                        login_editor.commit();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();


                        // Pager listener over indicator

                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Something Wrong try again ", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, LoginActivity.this, call1, "", true);


    }
    public void onBackPressed() {
        finishAffinity();
    }
}