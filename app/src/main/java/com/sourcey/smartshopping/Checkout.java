package com.sourcey.smartshopping;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by yashas on 06-06-2017.
 */

public class Checkout extends Activity {
    private ImageView imageView;
    dbHandler myDB;
    private ProgressDialog pDialog;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        myDB = new dbHandler(this);
        Button can = (Button)findViewById(R.id.pay_can);
        Button add = (Button)findViewById(R.id.pay_samy);
        Button pay = (Button)findViewById(R.id.finish);
        TextView user = (TextView)findViewById(R.id.userid);
        TextView total = (TextView)findViewById(R.id.total1);

        imageView = (ImageView) this.findViewById(R.id.qrcode);
        Bitmap bitmap = getIntent().getParcelableExtra("pic");
        imageView.setImageBitmap(bitmap);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(Checkout.this,HomePageActivity.class);
                finish();
                startActivity(back);

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(Checkout.this,WalletActivity.class);
                finish();
                startActivity(add);

            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences userinfo = getSharedPreferences("logintime", Context.MODE_PRIVATE);
                String email = userinfo.getString("email", "");
                String mode = "check";
                String addamt = "0";
                getbalance(mode, email, addamt);
                String storeid = "0";
                String checkin = "0";
                SharedPreferences check = getSharedPreferences("checkin", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = check.edit();
                edit.putString("ckeckin", checkin);
                edit.commit();
                SharedPreferences store = getSharedPreferences("storeid", Context.MODE_PRIVATE);
                edit = store.edit();
                edit.putString("shopid", storeid);
                edit.commit();
                Log.d("storeid",storeid);
                Log.d("checkin",checkin);
                myDB.deleteall();
//                Intent thank = new Intent(Checkout.this, ThankYouActivity.class);
//                finish();
//                startActivity(thank);
            }
        });
        SharedPreferences info = getSharedPreferences("logintime", Context.MODE_PRIVATE);
        String email = info.getString("email","");
        user.setText(email);

        String total1 = myDB.caltotal();
        total.setText(total1);

        SharedPreferences bal = getSharedPreferences("samycash", Context.MODE_PRIVATE);
        String samy_balance = bal.getString("samybal","");

        TextView balance = (TextView)findViewById(R.id.avl_balance);
        balance.setText(samy_balance);
    }
    @Override
    public void onBackPressed() {

        finish();
        Intent intent = new Intent(Checkout.this, ViewListContents.class);
        startActivity(intent);
    }

    private void getbalance(final String mode, final String email, final String addamt) {
        // Tag used to cancel the request
        String tag_string_req = "get_samy_balance";
        pDialog.setMessage("Finishing Please Wait ...");
        showDialog();
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
                        Intent thank = new Intent(Checkout.this, ThankYouActivity.class);
                        finish();
                        startActivity(thank);
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
                //params.put("mode",credentials);
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
        private void showDialog() {
            if (!pDialog.isShowing())
                pDialog.show();
        }

    }



