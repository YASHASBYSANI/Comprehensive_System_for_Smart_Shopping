package com.sourcey.smartshopping;

/**
 * Created by yashas on 06-06-2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProductDataSource {

    // Database fields

    public SQLiteDatabase database;
    private dbHandler dbHelper;
    private String[] allColumns = { dbHandler.COLUMN_ID,
            dbHandler.COLUMN_PRODUCTID,dbHandler.COLUMN_PRODUCTNAME,dbHandler.COLUMN_QUANTITY,dbHandler.COULMN_PRICE,dbHandler.COULMN_PWEIGHT};

    public ProductDataSource(Context context) {
        dbHelper = new dbHandler(context);
//        database = new SQLiteDatabase();
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Products addproduct(String pid, String pname, String pquantity, String pprice, String pweight) {
        ContentValues values = new ContentValues();
        values.put(dbHandler.COLUMN_PRODUCTID, pid);
        values.put(dbHandler.COLUMN_PRODUCTNAME, pname);
        values.put(dbHandler.COLUMN_QUANTITY, pquantity);
        values.put(dbHandler.COULMN_PRICE, pprice);
        values.put(dbHandler.COULMN_PWEIGHT, pweight);
        long insertId = database.insert(dbHandler.TABLE_PRODUCTS, null,
                values);
        Cursor cursor = database.query(dbHandler.TABLE_PRODUCTS,
                allColumns, dbHandler.COLUMN_ID + " = " + insertId, null,
                null, null, null,null);
        cursor.moveToFirst();
        Products newProduct = cursorToProduct(cursor);
        cursor.close();
        return newProduct;
    }

    public List<Products> getAllProducts() {
        List<Products> products = new ArrayList<Products>();

        Cursor cursor = database.query(dbHandler.TABLE_PRODUCTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Products product = cursorToProduct(cursor);
            products.add(product);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return products;
    }

    private Products cursorToProduct(Cursor cursor) {
        Products product = new Products();
        product.setId((int) cursor.getLong(0));
        product.setPid(cursor.getString(1));
        product.setPname(cursor.getString(2));
        product.setPquantity(cursor.getString(3));
        product.setPprice(cursor.getString(4));
        product.setPweight(cursor.getString(5));
        return product;
    }
}
