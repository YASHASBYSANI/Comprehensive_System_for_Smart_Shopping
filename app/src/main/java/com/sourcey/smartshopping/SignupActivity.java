package com.sourcey.smartshopping;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {

    public String name;
    public String email;
    public String address;
    public String state;
    public String city;
    public String zip;
    public String mobile;
    public String password;
    public String reEnterPassword;
    private ProgressDialog pDialog;

    private static final String TAG = "SignupActivity";
    private SharedPreferences prefs;
    private String prefName = "spinner_value";
    int id=0;

    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_address) EditText _addressText;
    @Bind(R.id.input_zip) EditText _zipText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_mobile) EditText _mobileText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //Spinner for state
        List<String> list=new ArrayList<String>();
        list.add("Andhra Pradesh");
        list.add("Delhi");
        list.add("Maharashtra");
        list.add("Karnataka");
        list.add("West Bengal");


        final Spinner state1=(Spinner) findViewById(R.id.select_state);
        state1.setPrompt("Select-State");
        ArrayAdapter<String> adp= new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,list);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state1.setAdapter(adp);

        prefs = getSharedPreferences(prefName, MODE_PRIVATE);
        id=prefs.getInt("last_val",0);
        state1.setSelection(id);

        final Spinner finalSp1 = state1;
        state1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1,int pos, long arg3) {

                prefs = getSharedPreferences(prefName, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("last_val", pos);
                editor.commit();
                state = ((TextView)arg1).getText().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        //Spinner for city
        list = new ArrayList<String>();
        list.add("Bangalore");
        list.add("Delhi");
        list.add("Hyderabad");
        list.add("Pune");
        list.add("Mumbai");
        list.add("Kolkata");
        final Spinner city1 = (Spinner) findViewById(R.id.select_city);
        adp = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city1.setAdapter(adp);
        prefs = getSharedPreferences(prefName, MODE_PRIVATE);
        id=prefs.getInt("last_val",0);
        city1.setSelection(id);

        final Spinner finalSp = city1;
        city1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1,int pos, long arg3) {

                prefs = getSharedPreferences(prefName, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("last_val", pos);
                editor.commit();
                city = ((TextView)arg1).getText().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
                checkSignup(name,address,state,city,zip,mobile,email,password);
            }
        });
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }
    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }
        _signupButton.setEnabled(false);

        name = _nameText.getText().toString();
        address = _addressText.getText().toString();
        zip = _zipText.getText().toString();
        email = _emailText.getText().toString();
        mobile = _mobileText.getText().toString();
        password = _passwordText.getText().toString();
        reEnterPassword = _reEnterPasswordText.getText().toString();
    }
    public void onSignupFailed() {

        _signupButton.setEnabled(true);
    }
    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String zip = _zipText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }

        if (zip.isEmpty() || zip.length()!=6){
            _zipText.setError("Enter Valid Pin Code");
            valid = false;
        } else {
            _zipText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }


    private void checkSignup(final String name, final String address, final String state, final String city,
                             final String zip,final String mobile, final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_signin";
        pDialog.setMessage("Signing up ...");
        showDialog();

        Log.d(TAG, "Here");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://samystores.tk/register.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Signup Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean Success = jObj.getBoolean("Success");
                    Log.d("tag", jObj.toString(4));

                    // Check for error node in json
                    if (Success) {
                        // user successfully logged in
                        // Create login session
                        //session.setLogin(true);

                        // store user details
                        SharedPreferences store = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = store.edit();
                        edit.putString("name",name);
                        edit.putString("address",address);
                        edit.putString("city",city);
                        edit.putString("state",state);
                        edit.putString("zip",zip);
                        edit.putString("mobile",mobile);
                        edit.putString("email",email);
                        edit.putString("password",password);
                        edit.commit();
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(),
                                 "Account Created, Please Login!", Toast.LENGTH_LONG).show();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("Status");
                        Toast.makeText(getApplicationContext(),
                                "Unable To Signup, Please Check Input Details!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    //e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Log.d(TAG, "Map start");
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("address", address);
                params.put("city", city);
                params.put("state", state);
                params.put("zip", zip);
                params.put("email", email);
                params.put("mobile", mobile);
                params.put("password", password);

                Log.d(TAG, "Map end");
                return params;
            }

        };

        // Adding request to request queue
        Log.d(TAG, "Here2"); //tag_string_req
        AppController c = new AppController();
        c.setContext(getApplicationContext());
        if(c != null){
            Log.d(TAG, "notnull");
            c.addToRequestQueue(strReq, tag_string_req);
        }
        else {
            Log.d(TAG, "null");
        }

    }
    private void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(SignupActivity.this, LoginActivity.class);
        finish();
        startActivity(back);
    }
}