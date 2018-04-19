package com.sourcey.smartshopping;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreditDebitCard extends AppCompatActivity {
    EditText cardnumber;
    EditText expiremm;
    EditText expireyy;
    EditText cardcvv;
    EditText namecard;
    private static final String TAG = "CreditDebitCard";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_debit_card);

        Button cann= (Button)findViewById(R.id.can);
        final Button pay = (Button)findViewById(R.id.button3);

        cann.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent page = new Intent(CreditDebitCard.this, HomePageActivity.class);
                startActivity(page);
                finish();
            }

        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    String mode = "add";
                    SharedPreferences id = getSharedPreferences("newbal", Context.MODE_PRIVATE);
                    String addamt1 = id.getString("addamount","");
                    SharedPreferences info = getSharedPreferences("logintime", Context.MODE_PRIVATE);
                    String email = info.getString("email","");
                    addbalance(mode,email,addamt1);
                    Intent home = new Intent(CreditDebitCard.this, HomePageActivity.class);
                    finish();
                    startActivity(home);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Check Input Information", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public void onBackPressed() {

        finish();
        Intent intent = new Intent(CreditDebitCard.this, CardOptions.class);
        startActivity(intent);
    }
    public boolean validate()
    {
        boolean valid = true;
        cardnumber = (EditText) findViewById(R.id.cardno);
        expiremm = (EditText) findViewById(R.id.mm);
        expireyy = (EditText) findViewById(R.id.yy);
        cardcvv = (EditText) findViewById(R.id.cvv);
        namecard = (EditText) findViewById(R.id.name);

        String cardno = cardnumber.getText().toString();
        String expmm = expiremm.getText().toString();
        String expyy = expireyy.getText().toString();
        String cvv = cardcvv.getText().toString();
        String cardname = namecard.getText().toString();

        if (cardno.isEmpty() || cardno.length() < 16) {
            cardnumber.setError("enter vaild card number");
            valid = false;
        } else {
            cardnumber.setError(null);
        }
        if (expmm.isEmpty() || expmm.length() < 2 ) {
            expiremm.setError("enter vaild expiry month");
            valid = false;
        } else {
            expiremm.setError(null);
        }
        if (expyy.isEmpty() || expyy.length() < 2) {
            expireyy.setError("enter vaild expiry year");
            valid = false;
        } else {
            expireyy.setError(null);
        }
        if (cvv.isEmpty() || cvv.length() < 3) {
            cardcvv.setError("enter vaild cvv");
            valid = false;
        } else {
            cardcvv.setError(null);
        }
        if (cardname.isEmpty() || cardname.length() < 3) {
            namecard.setError("enter vaild name");
            valid = false;
        } else {
            namecard.setError(null);
        }

        return valid;
    }
    private void addbalance(final String mode, final String email, final String addamt) {
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
        //AppController c = AppController.getInstance();
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
