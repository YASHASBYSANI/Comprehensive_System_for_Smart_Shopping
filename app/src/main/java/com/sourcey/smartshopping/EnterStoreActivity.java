package com.sourcey.smartshopping;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.content.ContentValues.TAG;

public class EnterStoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterstore);


    }

    private ZXingScannerView scannerView;

    public void scanCode(View view) {
        scannerView = new ZXingScannerView(this);
        scannerView.setResultHandler(new ZXingScannerResultHandler());

        setContentView(scannerView);
        scannerView.startCamera();

    }

    public void OnPause() {
        super.onPause();
        scannerView.stopCamera();

    }

    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler {
        String shopid;

        @Override
        public void handleResult(Result result) {
            setContentView(R.layout.activity_enterstore);
            scannerView.stopCamera();

            shopid = result.getText();
            Toast.makeText(EnterStoreActivity.this, "ShopID:" + shopid, Toast.LENGTH_LONG).show();
            Intent scandone = new Intent(EnterStoreActivity.this, HomePageActivity.class);
            startActivity(scandone);
            finish();
            //to store shopid for future use
            SharedPreferences store = getSharedPreferences("storeid", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = store.edit();
            edit.putString("shopid", shopid);
            edit.commit();

            //fetching email of user
            SharedPreferences userinfo = getSharedPreferences("logintime", Context.MODE_PRIVATE);
            String email = userinfo.getString("email", "");
            String mode = "check";
            String addamt = "0";
            getbalance(mode,email,addamt);
            checkin(shopid, email);
            Log.d(TAG,mode);
            Log.d(TAG,addamt);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homepage = new Intent(EnterStoreActivity.this, HomePageActivity.class);
        finish();
        startActivity(homepage);
    }

    private void checkin(final String shopid, final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_product_details";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://samystores.tk/checkin.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Product Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean Success = jObj.getBoolean("Success");
                    String checkin_id = jObj.getString("Checkin_Id");
                    SharedPreferences id = getSharedPreferences("checkin", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = id.edit();
                    edit.putString("ckeckin", checkin_id);
                    edit.commit();
                    Log.d("TAG",checkin_id);
                    Log.d("tag", jObj.toString(4));

                    // Check for error node in json
                    if (Success) {

                        Intent intent = new Intent(EnterStoreActivity.this, HomePageActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        Toast.makeText(getApplicationContext(),
                                "unable to check in", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "checkin Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("store_id", shopid);
                params.put("username", email);
                Log.d(TAG, "Map end");
                return params;
            }

        };
        // Adding request to request queue
        AppController c = new AppController();
        c.setContext(getApplicationContext());
        if (c != null) {
            Log.d(TAG, "notnull");
            c.addToRequestQueue(strReq, tag_string_req);
        } else {
            Log.d(TAG, "null");
        }
    }

    private void getbalance(final String mode, final String email, final String addamt) {
        // Tag used to cancel the request
        String tag_string_req = "get_samy_balance";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://samystores.tk/samycash.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Product Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean Success = jObj.getBoolean("success");
                    String samy_bal = jObj.getString("samycash");
                    SharedPreferences id = getSharedPreferences("samycash", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = id.edit();
                    edit.putString("samybal", samy_bal);
                    edit.commit();
                    Log.d("tag", jObj.toString(4));
                    // Check for error node in json
                    if (Success) {

                        Toast.makeText(getApplicationContext(),
                                "Balance Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        // Error in balance retrieval. Get the error message
                        Toast.makeText(getApplicationContext(),
                                "unable to get balance", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "checkin Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Log.d(TAG, "Map start");
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("mode", mode);
                params.put("username", email);
                params.put("add_amt", addamt);
                Log.d(TAG, "Map end");
                return params;
            }

        };

        // Adding request to request queue
        Log.d(TAG, "Here2"); //tag_string_req
        AppController c = new AppController();
        c.setContext(getApplicationContext());
        if (c != null) {
            Log.d(TAG, "notnull");
            c.addToRequestQueue(strReq, tag_string_req);
        } else {
            Log.d(TAG, "null");
        }

    }
}
