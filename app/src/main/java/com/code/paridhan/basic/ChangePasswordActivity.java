package com.code.paridhan.basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.code.paridhan.R;
import com.code.paridhan.RetrofitAPI.ApiService;
import com.code.paridhan.RetrofitAPI.ConnectToRetrofit;
import com.code.paridhan.RetrofitAPI.GlobalAppApis;
import com.code.paridhan.RetrofitAPI.RetrofitCallBackListenar;
import com.code.paridhan.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;

import static com.code.paridhan.RetrofitAPI.API_Config.getApiClient_ByPost;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText edt_new_pass;
    private EditText edt_old_pass,edt_conf_pass;
    private Button btn_sendotp;
    SharedPreferences sharedPreferences;
    String UserId, loginchec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        findViewById();
    }

    private void findViewById() {
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("username", "");
        edt_new_pass=findViewById(R.id.edt_new_pass);
        edt_old_pass=findViewById(R.id.edt_old_pass);
        edt_conf_pass=findViewById(R.id.edt_conf_pass);
        btn_sendotp=findViewById(R.id.btn_sendotp);
        findViewById(R.id.back_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        btn_sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_old_pass.getText().toString().equalsIgnoreCase("")){
                    edt_old_pass.setError("Fields can't be blank!");
                    edt_old_pass.requestFocus();
                }
                else if (edt_new_pass.getText().toString().equalsIgnoreCase("")){
                    edt_new_pass.setError("Fields can't be blank!");
                    edt_new_pass.requestFocus();
                }
                else if (edt_new_pass.getText().toString().equalsIgnoreCase(edt_old_pass.getText().toString())){
                    Toast.makeText(ChangePasswordActivity.this, "Password not Match!", Toast.LENGTH_SHORT).show();
                }
                else if (edt_conf_pass.getText().toString().equalsIgnoreCase("")){
                    edt_conf_pass.setError("Fields can't be blank!");
                    edt_conf_pass.requestFocus();
                }
//               else if(!edt_new_pass.getText().toString().equals(edt_conf_pass.getText().toString())){
//                    //Toast is the pop up message
//                    Toast.makeText(getApplicationContext(), "Password Not matched!", Toast.LENGTH_LONG).show();
//                }
                else {
                    changePassword();
                    // Toast.makeText(getApplicationContext(),"Password Changed Successfully!",Toast.LENGTH_LONG).show();
                }
            }


        });

    }
    private void changePassword() {
        ApiService client1 = getApiClient_ByPost();
    String otp1 = new GlobalAppApis().ChangePassword(UserId,edt_new_pass.getText().toString(),edt_old_pass.getText().toString());

    Call<String> call1 = client1.NotificationList(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
        @Override
        public void RetrofitCallBackListenar(String result, String action) throws JSONException {
            try {
                JSONObject jsonObject = new JSONObject(result);
                Log.v("dadadaaaa", String.valueOf(jsonObject));

                String status = jsonObject.getString("Status");
                if (status.equalsIgnoreCase("true")) {
                    Toast.makeText(ChangePasswordActivity.this, "Password Change Successfully", Toast.LENGTH_SHORT).show();


                   startActivity(new Intent(getApplicationContext(), LoginActivity.class));


                } else {
                    Toast.makeText(getApplicationContext(), "No Data found", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    },ChangePasswordActivity.this, call1, "", true);
}




}