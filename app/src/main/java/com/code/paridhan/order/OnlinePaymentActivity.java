package com.code.paridhan.order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.cashfree.pg.CFPaymentService;
import com.cashfree.pg.ui.gpay.GooglePayStatusListener;
import com.code.paridhan.R;
import com.code.paridhan.RetrofitAPI.ApiService;
import com.code.paridhan.RetrofitAPI.ConnectToRetrofit;
import com.code.paridhan.RetrofitAPI.GlobalAppApis;
import com.code.paridhan.RetrofitAPI.RetrofitCallBackListenar;
import com.code.paridhan.basic.MainActivity;
import com.code.paridhan.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_BANK_CODE;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_CVV;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_HOLDER;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_MM;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_NUMBER;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_YYYY;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_NOTE;
import static com.cashfree.pg.CFPaymentService.PARAM_PAYMENT_OPTION;
import static com.cashfree.pg.CFPaymentService.PARAM_UPI_VPA;
import static com.cashfree.pg.CFPaymentService.PARAM_WALLET_CODE;
import static com.code.paridhan.RetrofitAPI.API_Config.getApiClient_ByPost;

public class OnlinePaymentActivity extends AppCompatActivity {
    String token;
    String get_amount, get_token, userName, orderid, phonenumber;
    SeamlessMode currentMode = SeamlessMode.CARD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_payment);
        get_amount = getIntent().getStringExtra("PayableAmt");
        get_token = getIntent().getStringExtra("token_gen");
        orderid = getIntent().getStringExtra("orderid");
        userName = getIntent().getStringExtra("userName");
        phonenumber = getIntent().getStringExtra("phonenumber");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null)
                for (String key : bundle.keySet()) {
                    if (bundle.getString(key) != null) {
                        if (bundle.getString(key).equalsIgnoreCase("CANCELLED")) {
                            Toast.makeText(getApplicationContext(), "Transaction CANCELLED", Toast.LENGTH_LONG).show();
                        } else if (bundle.getString(key).equalsIgnoreCase("FAILED")) {
                            Toast.makeText(getApplicationContext(), "Transaction FAILED", Toast.LENGTH_LONG).show();
                            SuccesAppi("FAILED");
                        } else if (bundle.getString(key).equalsIgnoreCase("SUCCESS")) {
                            Toast.makeText(getApplicationContext(), "Transaction SUCCESS!", Toast.LENGTH_LONG).show();
                            SuccesAppi("SUCCESS");
                        }
                    }
                }
        }
    }
    public void onClick(View view) {
        /*
         * stage allows you to switch between sandboxed and production servers
         * for CashFree Payment Gateway. The possible values are
         *
         * 1. TEST: Use the Test server. You can use this service while integrating
         *      and testing the CashFree PG. No real money will be deducted from the
         *      cards and bank accounts you use this stage. This mode is thus ideal
         *      for use during the development. You can use the cards provided here
         *      while in this stage: https://docs.cashfree.com/docs/resources/#test-data
         *
         * 2. PROD: Once you have completed the testing and integration and successfully
         *      integrated the CashFree PG, use this value for stage variable. This will
         *      enable live transactions
         */
        String stage = "PROD";
        //Show the UI for doGPayPayment and phonePePayment only after checking if the apps are ready for payment
        if (view.getId() == R.id.phonePe_exists) {
            Toast.makeText(
                    OnlinePaymentActivity.this,
                    CFPaymentService.getCFPaymentServiceInstance().doesPhonePeExist(OnlinePaymentActivity.this, stage) + "",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (view.getId() == R.id.gpay_ready) {
            CFPaymentService.getCFPaymentServiceInstance().isGPayReadyForPayment(OnlinePaymentActivity.this, new GooglePayStatusListener() {
                @Override
                public void isReady() {
                    Toast.makeText(OnlinePaymentActivity.this, "Ready", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void isNotReady() {
                    Toast.makeText(OnlinePaymentActivity.this, "Not Ready", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        /*
         * token can be generated from your backend by calling cashfree servers. Please
         * check the documentation for details on generating the token.
         * READ THIS TO GENERATE TOKEN: https://bit.ly/2RGV3Pp
         */
        token = get_token;
        Log.v("dadadada", token);
        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);
        switch (view.getId()) {

            /***
             * This method handles the payment gateway invocation (web flow).
             *
             * @param context Android context of the calling activity
             * @param params HashMap containing all the parameters required for creating a payment order
             * @param token Provide the token for the transaction
             * @param stage Identifies if test or production service needs to be invoked. Possible values:
             *              PROD for production, TEST for testing.
             * @param color1 Background color of the toolbar
             * @param color2 text color and icon color of toolbar
             * @param hideOrderId If true hides order Id from the toolbar
             */
            case R.id.web: {
                cfPaymentService.doPayment(OnlinePaymentActivity.this, getInputParams(), token, stage, "#784BD2", "#FFFFFF", false);
//                 cfPaymentService.doPayment(OnlinePaymentActivity.this, params, token, stage);
                break;
            }
            /***
             * Same for all payment modes below.
             *
             * @param context Android context of the calling activity
             * @param params HashMap containing all the parameters required for creating a payment order
             * @param token Provide the token for the transaction
             * @param stage Identifies if test or production service needs to be invoked. Possible values:
             *              PROD for production, TEST for testing.
             */
            case R.id.upi: {
//                                cfPaymentService.selectUpiClient("com.google.android.apps.nbu.paisa.user");
                cfPaymentService.upiPayment(OnlinePaymentActivity.this, getInputParams(), token, stage);
                break;
            }
            case R.id.amazon: {
                cfPaymentService.doAmazonPayment(OnlinePaymentActivity.this, getInputParams(), token, stage);
                break;
            }
            case R.id.gpay: {
                cfPaymentService.gPayPayment(OnlinePaymentActivity.this, getInputParams(), token, stage);
                break;
            }
            case R.id.phonePe: {
                cfPaymentService.phonePePayment(OnlinePaymentActivity.this, getInputParams(), token, stage);
                break;
            }
            case R.id.web_seamless: {
                cfPaymentService.doPayment(OnlinePaymentActivity.this, getSeamlessCheckoutParams(), token, stage);
                break;
            }
        }
    }
    private Map<String, String> getInputParams() {

        /*
         * appId will be available to you at CashFree Dashboard. This is a unique
         * identifier for your app. Please replace this appId with your appId.
         * Also, as explained below you will need to change your appId to prod
         * credentials before publishing your app.
         */
        String appId = "187673e3942a4570272aa5ce45376781";
        String orderId = orderid;
        String orderAmount = get_amount;
        String orderNote = "Subhparidhan";
        String customerName = "Subhparidhan";
        String customerPhone = phonenumber;
        String customerEmail = "Subhparidhan@gmail.com";

        Map<String, String> params = new HashMap<>();

        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderId);
        params.put(PARAM_ORDER_AMOUNT, orderAmount);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
        params.put(PARAM_ORDER_CURRENCY, "INR");
        return params;
    }
    private Map<String, String> getSeamlessCheckoutParams() {
        Map<String, String> params = getInputParams();
        switch (currentMode) {
            case CARD:
                params.put(PARAM_PAYMENT_OPTION, "card");
                params.put(PARAM_CARD_NUMBER, "VALID_CARD_NUMBER");
                params.put(PARAM_CARD_YYYY, "YYYY");
                params.put(PARAM_CARD_MM, "MM");
                params.put(PARAM_CARD_HOLDER, "CARD_HOLDER_NAME");
                params.put(PARAM_CARD_CVV, "CVV");
                break;
            case WALLET:
                params.put(PARAM_PAYMENT_OPTION, "wallet");
                params.put(PARAM_WALLET_CODE, "4007"); // Put one of the wallet codes mentioned here https://dev.cashfree.com/payment-gateway/payments/wallets
                break;
            case NET_BANKING:
                params.put(PARAM_PAYMENT_OPTION, "nb");
                params.put(PARAM_BANK_CODE, "3333"); // Put one of the bank codes mentioned here https://dev.cashfree.com/payment-gateway/payments/netbanking
                break;
            case UPI_COLLECT:
                params.put(PARAM_PAYMENT_OPTION, "upi");
                params.put(PARAM_UPI_VPA, "VALID_VPA");
                break;
            case PAY_PAL:
                params.put(PARAM_PAYMENT_OPTION, "paypal");
                break;
        }
        return params;
    }
    private void SuccesAppi(String status) {
        String otp1 = new GlobalAppApis().OrderSuccess(userName, status, token, orderid);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.OrderSuccess(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                //Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    Log.v("getString", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if (Status.equalsIgnoreCase("true")) {
                        Toast.makeText(OnlinePaymentActivity.this, "Order Place Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), OnlinePaymentActivity.class));
                        // Pager listener over indicator

                    } else {
                        Toast.makeText(OnlinePaymentActivity.this, "Something Wrong try again ", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, OnlinePaymentActivity.this, call1, "", true);


    }
    enum SeamlessMode {
        CARD, WALLET, NET_BANKING, UPI_COLLECT, PAY_PAL
    }


}

