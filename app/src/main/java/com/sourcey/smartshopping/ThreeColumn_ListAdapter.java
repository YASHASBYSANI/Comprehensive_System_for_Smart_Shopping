package com.sourcey.smartshopping;

/**
 * Created by yashas on 06-06-2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ThreeColumn_ListAdapter extends ArrayAdapter<Prod> {
    private LayoutInflater mInflater;
    private ArrayList<Prod> pr;
    private int mViewResourceId;
    public dbHandler handler;
    static int priceamt;
    public ViewListContents totdis = new ViewListContents();
    static String amt;
    public ThreeColumn_ListAdapter(Context context, int textViewResourceId, ArrayList<Prod> pr) {
        super(context, textViewResourceId, pr);
        this.pr = pr;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);
        Prod prods = pr.get(position);
        String quant = prods.getProdquantity();
        String ppr= prods.getProdprice();
        int q = Integer.parseInt(quant);
        int p = Integer.parseInt(ppr);
        int totprice = q*p;
        String price = String.valueOf(totprice);
        if (pr != null) {
            TextView productname = (TextView) convertView.findViewById(R.id.productname);
            TextView productquantity = (TextView) convertView.findViewById(R.id.productquantity);
            TextView productprice = (TextView) convertView.findViewById(R.id.productprice);
            if (productname != null) {
                productname.setText(prods.getProdname());
            }
            if (productquantity != null) {
                productquantity.setText((prods.getProdquantity()));
            }
            if (productprice != null) {
                productprice.setText(price);
                priceamt = handler.total;
                priceamt += Integer.parseInt(price);
            }
        }

        return convertView;
    }
}