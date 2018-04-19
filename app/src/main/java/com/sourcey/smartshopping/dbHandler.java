package com.sourcey.smartshopping;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by yashas on 06-06-2017.
 */

public class dbHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cart";
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTID = "pid";
    public static final String COLUMN_PRODUCTNAME = "pname";
    public static final String COLUMN_QUANTITY = "pquantity";
    public static final String COULMN_PRICE = "pprice";
    public static final String COULMN_PWEIGHT = "pweight";

    private static SQLiteDatabase db;
    public ThreeColumn_ListAdapter amt12;
    public ViewListContents disp;
    public dbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    static int total = 0;
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PRODUCTS +
                " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCTID + " TEXT, " +
                COLUMN_PRODUCTNAME + " TEXT, " +
                COLUMN_QUANTITY + " INTEGER, " +
                COULMN_PRICE + " INTEGER, " +
                COULMN_PWEIGHT + " TEXT " + ");");
    }
    public boolean CheckIsDataAlreadyInDBorNot(String pid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_PRODUCTS + " where " + COLUMN_PRODUCTID + " = '" + pid + "';";
        Cursor cursor = db.rawQuery(Query, null);
        Log.d("TAG", String.valueOf(cursor.getCount()));
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
    public String caltotal ()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " AS CNT";
        Cursor cursor = db.rawQuery(query, null);
        String total = "";
        if(cursor.getCount() <= 0)
            total = "0";
        else
        {
            query = "SELECT SUM(" + COLUMN_QUANTITY + "*" + COULMN_PRICE + ") AS TOTAL_PRICE FROM " + TABLE_PRODUCTS;
            Cursor cur = db.rawQuery(query, null);
            cur.moveToFirst();
            total = cur.getString(cur.getColumnIndex("TOTAL_PRICE"));
            cur.close();
        }
        cursor.close();
        return  total;
    }
    public boolean updatequant (String pid, String quant)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String up = "SELECT " + COLUMN_QUANTITY  +" FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTID + " = " + pid + ";";
        Cursor cursor = db.rawQuery(up,null);
        cursor.moveToFirst();

        String currQuant = cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY));
        int up1 = Integer.parseInt(currQuant);
        int quan = Integer.parseInt(quant);
        int add = up1 + quan;
        String updated = String.valueOf(add);
        String newquant = "UPDATE " + TABLE_PRODUCTS +" SET "+ COLUMN_QUANTITY+" = "+updated+" WHERE " + COLUMN_PRODUCTID + " = " + pid + ";";
        db.execSQL(newquant);
        return true;
    }
    public void deleteall(){
        SQLiteDatabase db = this.getWritableDatabase();
        total = 0;
        db.execSQL("DELETE FROM " + TABLE_PRODUCTS);
    }
    public void deleteData (String pos) {
        SQLiteDatabase db = this.getWritableDatabase();
        String up = "SELECT " + COLUMN_QUANTITY  + " , " + COULMN_PRICE + " FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTID + " = " + pos + ";";
        Cursor cursor = db.rawQuery(up,null);
        cursor.moveToFirst();
        String currQuant = cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY));
        String currprice = cursor.getString(cursor.getColumnIndex(COULMN_PRICE));
        Log.d("TAG",currQuant);
        Log.d("TAG",currprice);
        db.execSQL("DELETE FROM products WHERE " + COLUMN_PRODUCTID + " = "+ pos +";" );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_PRODUCTS);
        onCreate(db);
    }
    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
        return data;
    }
    public String getObjectsJson() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String jsonStr = "[";
        do {
            jsonStr += "{\"item_code\":\"" + cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCTID)) + "\",\"quantity\":\"" + cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY)) + "\"},";
            //jsonStr += "{\"item_code\":\"" + cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCTID)).substring(0, cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCTID)).length()) + "\",\"quantity\":\"" + cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY)) + "\"},";
        } while (cursor.moveToNext());
        jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
        jsonStr += "]";
        Log.d("TAG",jsonStr);
        return jsonStr;
    }
}

