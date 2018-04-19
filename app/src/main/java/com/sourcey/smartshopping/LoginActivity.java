package com.sourcey.smartshopping;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends Activity {
    private static final String TAG = SignupActivity.class.getSimpleName();
    private Button btnLogin;
    private TextView btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    public static SessionManager session;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MainActivity m_activity = MainActivity.getInstance();
        if(m_activity != null)
            m_activity.finish();

        HomePageActivity activity = HomePageActivity.getInstance();
        if(activity != null)
            activity.finish();

        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLinkToRegister = (TextView) findViewById(R.id.link_signup);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // Session manager
        session = new SessionManager(getApplicationContext());
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        }
        // Login button_style Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
             if(validate())
             {
                 String email = inputEmail.getText().toString();
                 String password = inputPassword.getText().toString();
                 checkLogin(email, password);
             }
             else
             {
                 AlertDialog.Builder builder;
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                     builder = new AlertDialog.Builder(LoginActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                 } else {
                     builder = new AlertDialog.Builder(LoginActivity.this);
                 }
                 builder.setTitle("Invalid Credentials")
                         .setMessage("Wrong UserName or Password, Try Again! ")
                         .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 dialog.dismiss();
                             }
                         });
                 Toast.makeText(getApplicationContext(),
                         "Invalid Login Credentials", Toast.LENGTH_LONG).show();
             }
            }

        });
        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        SignupActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    public boolean validate()
    {
        boolean valid = true;
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("enter a valid email address");
            valid = false;
        } else {
            inputEmail.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            inputPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            inputPassword.setError(null);
        }
        return valid;
    }
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        pDialog.setMessage("Logging in ...");
        showDialog();
        StringRequest strReq = new StringRequest(Method.POST,
                "http://samystores.tk/login.php?mode=credentials", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String nam = jObj.getString("Name");
                    String address = jObj.getString("ResidentAddress");
                    String city = jObj.getString("ResidentCity");
                    String state = jObj.getString("ResidentState");
                    String zip = jObj.getString("ResidentZip");
                    String mobile = jObj.getString("MobileNum");
                    String email = jObj.getString("EmailUsername");
                    String samy_bal = jObj.getString("samycash");

                    SharedPreferences logintime = getSharedPreferences("logintime", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = logintime.edit();
                    edit.putString("name",nam);
                    edit.putString("address",address);
                    edit.putString("city",city);
                    edit.putString("state",state);
                    edit.putString("zip",zip);
                    edit.putString("mobile",mobile);
                    edit.putString("email",email);
                    edit.putString("password",password);
                    edit.commit();
                    SharedPreferences id = getSharedPreferences("samycash", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit1 = id.edit();
                    edit1.putString("samybal", samy_bal);
                    edit1.commit();
                    boolean Auth = jObj.getBoolean("Auth");
                    Log.d("tag", jObj.toString(4));
                    // Check for error node in json
                    if (Auth) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);
                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_LONG).show();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("Status");
                        Toast.makeText(getApplicationContext(),
                                "Invalid Login Credentials", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Invalid Login Credentials", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Log.d(TAG, "Map start");
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("username", email);
                params.put("password", password);
                return params;
            }
        };
        // Adding request to request queue
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
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}