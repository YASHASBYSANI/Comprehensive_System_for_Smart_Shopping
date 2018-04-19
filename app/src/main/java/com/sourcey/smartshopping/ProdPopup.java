package com.sourcey.smartshopping;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yashas on 31-05-2017.
 */

public class ProdPopup extends Activity {

    private SharedPreferences prefs;
    private String prefName = "spinner_value";
    public String quant;
    int id=0;
    public ProductDataSource newproduct;
    public dbHandler handle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        newproduct = new ProductDataSource(this);
        newproduct.open();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int i;
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        SharedPreferences info = getSharedPreferences("prodinfo", Context.MODE_PRIVATE);
        String quant12 = info.getString("avalquant","");
        int quant122 = Integer.parseInt(quant12);
        getWindow().setLayout((int) (width * .8), (int) (height * .7));

        List<String> list = new ArrayList<String>();
        for(i=1;i<=quant122;i++)
        {
            String q = String.valueOf(i);
           list.add(q);
        }
//        list.add("1");
//        list.add("2");
//        list.add("3");
//        list.add("4");
//        list.add("5");
//        list.add("6");
//        list.add("7");
//        list.add("8");
//        list.add("9");
//        list.add("10");

        final Spinner quantity = (Spinner) findViewById(R.id.quantity);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantity.setAdapter(adp);

        prefs = getSharedPreferences(prefName, MODE_PRIVATE);
        id = prefs.getInt("last_val", 1);
        quantity.setSelection(id);

        final Spinner finalSp1 = quantity;
        quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1, int pos, long arg3) {

                prefs = getSharedPreferences(prefName, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                //---save the values in the EditText view to preferences---
                editor.putInt("last_val", 0);

                editor.commit();

                //Toast.makeText(getBaseContext(),
                        //finalSp1.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                quant = ((TextView) arg1).getText().toString();
            }
            //String state = state1.getSelectedItem().toString();

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        handle = new dbHandler(this);

        final SharedPreferences quant1 = getSharedPreferences("prodinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = quant1.edit();
        edit.putString("pquant",quant);

        SharedPreferences prod = getSharedPreferences("prodinfo", Context.MODE_PRIVATE);
        final String pname = prod.getString("pname","");
        final String pweight = prod.getString("pweight","");
        final String pwunit = prod.getString("pwunit","");
        final String pprice = prod.getString("pprice","");

        final String weight = pweight+pwunit;

        TextView prod_name = (TextView) findViewById(R.id.pname);
        prod_name.setText(pname);

        TextView prod_weight = (TextView) findViewById(R.id.pweight);
        prod_weight.setText(weight);

        TextView prod_price = (TextView) findViewById(R.id.pprice);
        prod_price.setText(pprice);

        Button popcan = (Button)findViewById(R.id.popcancel);

        popcan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProdPopup.this, ScanProductActivity.class);
                startActivity(intent);
            }
        });
        SharedPreferences pid1 = getSharedPreferences("prodinfo", Context.MODE_PRIVATE);
        final String pid = prod.getString("pid","");
        Button popadd = (Button)findViewById(R.id.add);
        popadd.setOnClickListener(new View.OnClickListener() {

            //add button
            @Override
            public void onClick(View v) {
                boolean check = handle.CheckIsDataAlreadyInDBorNot(pid);
                if(check)
                {
                    handle.updatequant(pid,quant);
                    Intent added = new Intent(ProdPopup.this, ScanProductActivity.class);
                    startActivity(added);
                    finish();
                }
                else {
                    newproduct.addproduct(pid, pname, quant, pprice, weight);
                    Toast.makeText(getApplicationContext(),
                            "Product added to cart", Toast.LENGTH_SHORT).show();
                    Intent added = new Intent(ProdPopup.this, ScanProductActivity.class);
                    startActivity(added);
                    finish();
                }
            }
        });
    }
}
