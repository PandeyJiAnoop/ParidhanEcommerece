
package com.code.paridhan.basic;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.code.paridhan.R;
import com.code.paridhan.RetrofitAPI.ApiService;
import com.code.paridhan.RetrofitAPI.ConnectToRetrofit;
import com.code.paridhan.RetrofitAPI.RetrofitCallBackListenar;
import com.code.paridhan.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;

import static com.code.paridhan.RetrofitAPI.API_Config.getApiClient_ByPost;

public class SplashActivity extends AppCompatActivity {
    private int DELAY=2000;
    private SharedPreferences sharedPreferences;
    String username;
    String versionName = "", versionCode = "";

     @Override
      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
         sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
         username = sharedPreferences.getString("username", "");
//         settingApi();
         checkLogin();
      }
      private void checkLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(username.equalsIgnoreCase(""))
                {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }}
        },DELAY);

    }
      @Override
      public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
      public void AlertVersion() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_ok);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        TextView btnSubmit = dialog.findViewById(R.id.btnSubmit);
        TextView btnCancel = dialog.findViewById(R.id.btnCancel);

        tvMessage.setText(getString(R.string.newVersion));
        btnSubmit.setText(getString(R.string.update));

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.equalsIgnoreCase(""))
                {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            } });
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                dialog.dismiss();
            }
        });
    }
      private void getVersionInfo() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = String.valueOf(packageInfo.versionCode);
            Log.v("vname", versionName + " ," + String.valueOf(versionCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
      public void settingApi()
      {
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.VersionList();
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Toast.makeText(getApplicationContext(),""+result, Toast.LENGTH_LONG).show();
                try {
                    Log.v("getStringgg",result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status=jsonObject.getString("status");
                    if(Status.equalsIgnoreCase("true"))
                    {
                        getVersionInfo();
                        if (versionName.equalsIgnoreCase(jsonObject.getString("Version").toString())) {
                            checkLogin();
                        } else {

                           AlertVersion();

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this, call1, "", true);


    }

}