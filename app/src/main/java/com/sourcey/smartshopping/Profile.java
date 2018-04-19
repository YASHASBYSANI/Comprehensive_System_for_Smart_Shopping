package com.sourcey.smartshopping;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences info = getSharedPreferences("logintime", Context.MODE_PRIVATE);
        String name = info.getString("name","");
        String address = info.getString("address","");
        String state = info.getString("state","");
        String city = info.getString("city","");
        String zip = info.getString("zip","");
        String mobile = info.getString("mobile","");
        String email = info.getString("email","");

        TextView user_name = (TextView) findViewById(R.id.user_profile_name);
        user_name.setText(name);

        TextView user_add = (TextView) findViewById(R.id.user_address);
        user_add.setText(address);

        TextView user_state = (TextView) findViewById(R.id.user_state);
        user_state.setText(state);

        TextView user_city = (TextView) findViewById(R.id.user_city);
        user_city.setText(city);

        TextView user_zip = (TextView) findViewById(R.id.user_zip);
        user_zip.setText(zip);

        TextView user_mobile = (TextView) findViewById(R.id.user_mobile);
        user_mobile.setText(mobile);

        TextView user_email = (TextView) findViewById(R.id.user_email);
        user_email.setText(email);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent hom = new Intent(Profile.this, HomePageActivity.class);
        finish();
        startActivity(hom);
    }
}
