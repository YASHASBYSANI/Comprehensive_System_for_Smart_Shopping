package com.sourcey.smartshopping;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WalletActivity extends AppCompatActivity {
    EditText add2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        Button cancel = (Button)findViewById(R.id.addcancel);
        final Button add = (Button)findViewById(R.id.addcash);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent home = new Intent(WalletActivity.this, HomePageActivity.class);
                startActivity(home);
            }
        });
        add2 = (EditText)findViewById(R.id.addamt);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    String newbal = add2.getText().toString();
                    SharedPreferences id = getSharedPreferences("newbal", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = id.edit();
                    edit.putString("addamount", newbal);
                    edit.commit();
                    Intent next = new Intent(WalletActivity.this, CardOptions.class);
                    finish();
                    startActivity(next);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Enter Amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
        SharedPreferences bal = getSharedPreferences("samycash", Context.MODE_PRIVATE);
        String samy_balance1 = bal.getString("samybal","");
        TextView balance = (TextView)findViewById(R.id.textView11);
        balance.setText(samy_balance1);
    }
    public boolean validate()
    {
        boolean valid = true;
        String amount = add2.getText().toString();
        if (amount.matches("")) {
            add2.setError("Enter Amount");
            valid = false;
        } else {
            add2.setError(null);
        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(WalletActivity.this, HomePageActivity.class);
        finish();
        startActivity(home);
    }
}
