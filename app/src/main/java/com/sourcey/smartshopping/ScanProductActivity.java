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

public class ScanProductActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_product);
    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(ScanProductActivity.this, HomePageActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();  // optional depending on your needs
    }
    private ZXingScannerView scannerView;
    public void scanproduct (View view){
        scannerView= new ZXingScannerView(this);
        scannerView.setResultHandler(new ZXingScannerResultHandler());
        setContentView(scannerView);
        scannerView.startCamera();
    }
    public void OnPause(){
        super.onPause();
        scannerView.stopCamera();
    }
    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler
    {
        public String productid;
        @Override
        public void handleResult(Result result){
            setContentView(R.layout.activity_scan_product);
            scannerView.stopCamera();

            productid =  result.getText();
            Toast.makeText(ScanProductActivity.this, "ProductID:"+productid, Toast.LENGTH_LONG).show();
            SharedPreferences shop = getSharedPreferences("storeid", Context.MODE_PRIVATE);
            String shopid = shop.getString("shopid","");
            checkproduct(shopid,productid);
            Log.d(TAG, shopid);
            Log.d(TAG, productid);
        }
    }
    private void checkproduct(final String shopid, final String productid) {
        String tag_string_req = "req_product_details";
        Log.d(TAG, "Here");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://samystores.tk/get_product_details.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Product Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean Success = jObj.getBoolean("Success");
                    String pname = jObj.getString("ProductName");
                    String pid = jObj.getString("RequestedCode");
                    String pweight = jObj.getString("Weight_Volume");
                    String pwunit = jObj.getString("Weight_VolumeUnit");
                    String pprice = jObj.getString("Price");

                    String quantity = jObj.getString("Quantity");
                    Log.d("tag", jObj.toString(4));

                    SharedPreferences scan = getSharedPreferences("prodinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = scan.edit();
                    edit.putString("pid",pid);
                    edit.putString("pname",pname);
                    edit.putString("pweight",pweight);
                    edit.putString("pwunit",pwunit);
                    edit.putString("pprice",pprice);
                    edit.putString("avalquant",quantity);

                    edit.commit();
                    // Check for error node in json
                    if (Success) {
                        Intent pop = new Intent(ScanProductActivity.this, ProdPopup.class);
                        startActivity(pop);
                        finish();
                    } else {
                        String errorMsg = jObj.getString("Status");
                        Toast.makeText(getApplicationContext(),
                                "product not found, please contact store manager", Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Log.d(TAG, "Map start");
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                //params.put("mode",credentials);
                params.put("store_id", shopid);
                params.put("item_code", productid);
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
}
