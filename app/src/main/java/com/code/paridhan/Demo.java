//package com.code.paridhan;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//
//import com.phonepe.intent.sdk.api.B2BPGRequest;
//import com.phonepe.intent.sdk.api.B2BPGRequestBuilder;
//import com.phonepe.intent.sdk.api.PhonePe;
//import com.phonepe.intent.sdk.api.PhonePeInitException;
//import com.phonepe.intent.sdk.api.UPIApplicationInfo;
//import com.phonepe.intent.sdk.api.models.PhonePeEnvironment;
//
//import java.util.List;
//
//public class Demo extends AppCompatActivity {
//    private static int B2B_PG_REQUEST_CODE = 777;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_demo);
//
//        B2BPGRequest b2BPGRequest = new B2BPGRequestBuilder()
//                .setData(base64Body)
//                .setChecksum(checksum)
//                .setUrl(apiEndPoint)
//                .build();
//        try {
//            startActivityForResult(PhonePe.getImplicitIntent(
//                    /* Context */ this, b2BPGRequest, "com.phonepe.app"),B2B_PG_REQUEST_CODE);
//        } catch(PhonePeInitException e){
//        }
//
////        // Initialize PhonePe SDK
////        PhonePe.init(this, PhonePeEnvironment.valueOf("PhonePeEnvironment.PRODUCTION"),"M1P3YN3GKX58", "M1P3YN3GKX58");
////        try {
////            PhonePe.setFlowId("Unique Id of the user"); // Recommended, not mandatory , An alphanumeric string without any special character
////            List<com.phonepe.intent.sdk.api.UPIApplicationInfo> upiApps = PhonePe.getUpiApps();
////        } catch (PhonePeInitException exception) {
////            exception.printStackTrace();
////        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == B2B_PG_REQUEST_CODE) {
//            //This is UI callback and indicates only about completion of UI flow.
//
//            if (resultCode != Activity.RESULT_CANCELED)
//                Log.i("onActivityResult", "Result Cancelled");
//                //Inform your server to make the transaction status call to get the status. Update your app with the success/failure/pending status.
//
//            else {
//                if (data != null && data.getExtras() != null && data.getExtras().keySet().size() > 0)
//                    for (String key : data.getExtras().keySet())
//                        result.append(key).append(" = ").append(data.getExtras().get(key)).append("\n");
//                Log.i("onActivityResult", "result: " + result);
//                //Inform your server to make the transaction status call to get the status. Update your app with the success/failure/pending status.
//            }
//        }
//    }
//}