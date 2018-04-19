package com.sourcey.smartshopping;

/**
 * Created by yashas on 06-06-2017.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ViewListContents extends AppCompatActivity {
    private static final String TAG = SignupActivity.class.getSimpleName();
    dbHandler myDB;
    ArrayList<Prod> prodList;
    ListView listView;
    Prod prods;
    private ProductDataSource datasource;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcontents_layout);
        final Context context = this;
        datasource = new ProductDataSource(this);
        datasource.open();
        myDB = new dbHandler(this);
        prodList = new ArrayList<>();
        Cursor data = myDB.getListContents();
        int numRows = data.getCount();
        Button checkout = (Button)findViewById(R.id.bcheckout);
        if (numRows == 0) {
            Toast.makeText(ViewListContents.this, " The Cart is empty! ", Toast.LENGTH_SHORT).show();
            checkout.setEnabled(false);
        } else {
            while (data.moveToNext()) {
                prods = new Prod(data.getString(2), data.getString(3), data.getString(4),data.getString(1));
                prodList.add(prods);
                checkout.setEnabled(true);
            }
        }
        final ThreeColumn_ListAdapter adapter = new ThreeColumn_ListAdapter(this, R.layout.list_adapter_view, prodList);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(ViewListContents.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(ViewListContents.this);
                }
                builder.setTitle("Complete Shopping")
                        .setMessage("Are you sure you want to Finish Shopping?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String listprods = myDB.getObjectsJson();
                                SharedPreferences info = getSharedPreferences("logintime", Context.MODE_PRIVATE);
                                String email = info.getString("email","");
                                SharedPreferences check = getSharedPreferences("checkin", Context.MODE_PRIVATE);
                                String checkin = check.getString("ckeckin","");
                                SharedPreferences store = getSharedPreferences("storeid", Context.MODE_PRIVATE);
                                String storeid = store.getString("shopid","");
                                productlist(email,storeid,checkin,listprods);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        final ProductDataSource delete = new ProductDataSource(ViewListContents.this);
        final Products product = new Products();
        Button clear = (Button)findViewById(R.id.bClear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(ViewListContents.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(ViewListContents.this);
                }
                builder.setTitle("Clear Cart")
                        .setMessage("Are you sure you want to Clear the Cart?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                myDB.deleteall();
                                Intent clear = new Intent(ViewListContents.this,ViewListContents.class);
                                startActivity(clear);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                // TODO Auto-generated method stub
                final String iid = prodList.get(pos).getProdid();
                String id1= String.valueOf(pos);
                Log.v("long clicked","pos: " + iid);
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(ViewListContents.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(ViewListContents.this);
                }
                builder.setTitle("Delete Product")
                        .setMessage("Are you sure you want to delete this Product?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                myDB.deleteData(iid);
                                Intent refresh = new Intent(ViewListContents.this,ViewListContents.class);
                                finish();
                                startActivity(refresh);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });
        String tot1 = myDB.caltotal();
        TextView total = (TextView)findViewById(R.id.totprice);
        total.setText(tot1);
    }
    private void productlist(final String username, final String store_id , final String checkin_id , final String listprods) {
        // Tag used to cancel the request
        String tag_string_req = "get_samy_balance";
        final String requestBody = listprods.toString();
        String url = "http://samystores.tk/checkout.php?username="+username+"&store_id="+store_id+"&checkin_id="+checkin_id+"";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "user activity_checkout: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean Success = jObj.getBoolean("Success");
                    if (Success) {

                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        try {
                            SharedPreferences info = getSharedPreferences("logintime", Context.MODE_PRIVATE);
                            String email = info.getString("email","");
                            BitMatrix bitMatrix = multiFormatWriter.encode(email, BarcodeFormat.QR_CODE,160,160);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                            Intent intent = new Intent(ViewListContents.this, Checkout.class);
                            intent.putExtra("pic",bitmap);
                            startActivity(intent);
                            finish();
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Error in balance retrieval
                        String errorMsg = jObj.getString("Message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
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
            }

        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }
        };

        //tag_string_req
        AppController c = new AppController();
        c.setContext(getApplicationContext());
        if (c != null) {
            Log.d(TAG, "notnull");
            c.addToRequestQueue(strReq, tag_string_req);
        } else {
            Log.d(TAG, "null");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(ViewListContents.this, HomePageActivity.class);
        finish();
        startActivity(home);
    }
}
