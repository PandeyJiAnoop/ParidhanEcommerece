package com.code.paridhan.basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

import static com.code.paridhan.RetrofitAPI.API_Config.getApiClient_ByPost;

public class NotificationList extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rcvReminderList;
    SharedPreferences sharedPreferences;
    String UserId, loginchec;
    private ArrayList<HashMap<String, String>> arrNotificationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list2);
        findViewById();
        setListner();
        notification(UserId);

    }

    private void setListner() {

    }

    private void findViewById( ) {
        rcvReminderList = findViewById(R.id.rcvReminderList);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("username", "");
    }

    @Override
    public void onClick(View v) {


    }

    public void notification(String userId) {

        ApiService client1 = getApiClient_ByPost();
        String otp1 = new GlobalAppApis().NotificationList(userId);

        Call<String> call1 = client1.NotificationList(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.v("dadadaaaa", String.valueOf(jsonObject));

                    String status = jsonObject.getString("Status");
                    if (status.equalsIgnoreCase("true")) {

                        JSONArray jsonArrayr = jsonObject.getJSONArray("NotificationList");
                        for (int i = 0; i < jsonArrayr.length(); i++) {
                            JSONObject block_object = jsonArrayr.getJSONObject(i);

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("Title", block_object.get("Title").toString());
                            map.put("Description", block_object.get("Description").toString());
                            map.put("NotificationDate", block_object.get("Ondate").toString());
                            arrNotificationList.add(map);


                        }
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        NotificationAdapter adapter = new NotificationAdapter(getApplicationContext(), arrNotificationList);
                        rcvReminderList.setLayoutManager(layoutManager);
                        rcvReminderList.setAdapter(adapter);

                    } else {
                        Toast.makeText(getApplicationContext(), "No Data found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },NotificationList.this, call1, "", true);
    }

    private class NotificationAdapter extends RecyclerView.Adapter<Holder> {

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public NotificationAdapter(Context applicationContext, ArrayList<HashMap<String, String>> favList) {
            data = favList;
        }

        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list, parent, false));
        }


        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Holder holder, final int position) {

            holder.msg_tv.setText(data.get(position).get("Title"));
            holder.date_tv.setText(data.get(position).get("Description"));
            holder.credit_amount_tv.setText(data.get(position).get("NotificationDate"));
        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView msg_tv, date_tv, credit_amount_tv;

        public Holder(View inflate) {
            super(inflate);
            msg_tv = inflate.findViewById(R.id.msg_tv);
            date_tv = inflate.findViewById(R.id.date_tv);
            credit_amount_tv = inflate.findViewById(R.id.credit_amount_tv);


        }
    }
}