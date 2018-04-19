package com.sourcey.smartshopping;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

public class CardOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_options);

        RadioGroup rg = (RadioGroup) findViewById(R.id.radiogroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.debit:
                        Intent home1 = new Intent(CardOptions.this, CreditDebitCard.class);
                        startActivity(home1);
                        finish();
                        break;
                    case R.id.credit:
                        Intent home2 = new Intent(CardOptions.this, CreditDebitCard.class);
                        startActivity(home2);
                        finish();
                        break;
                }
            }
        });
    }
    @Override
    public void onBackPressed() {

        finish();
        Intent intent = new Intent(CardOptions.this, WalletActivity.class);
        startActivity(intent);
    }
}
