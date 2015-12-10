package com.example.mateusz.inteligentnywozek;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Mateusz on 2015-11-28.
 */
public class ProductsDBAdapter extends DBAdapter {

    protected SQLiteDatabase db = openDb();

    public ProductsDBAdapter(Context context) {
        super(context);
    }

    void addProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(context.getString(R.string.key_name), product.getName());

        db.insert(context.getString(R.string.table_products), null, values);
    }

    public Long getProductId(String name) {
        Cursor cursor = db.query(context.getString(R.string.table_products), new String[]{context.getString(R.string.key_id),
                        context.getString(R.string.key_name)}, context.getString(R.string.key_name) + "=?",
                new String[]{String.valueOf(name)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return Long.parseLong(cursor.getString(0));
    }

    public Cursor getAllProducts(String begining) {
        String selectQuery;
        if (begining == "")
            selectQuery = "SELECT  * FROM " + context.getString(R.string.table_products) + " ORDER BY " + context.getString(R.string.key_star) + " DESC";
        else {
            selectQuery = "SELECT  * FROM " + context.getString(R.string.table_products) + " WHERE " + context.getString(R.string.key_name) + " LIKE '" + begining + "%' " + "ORDER BY " + context.getString(R.string.key_star) + " DESC";
        }
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public int updateProduct(Product product, Integer stars) {

        ContentValues values = new ContentValues();
        values.put(context.getString(R.string.key_id), product.getId());
        values.put(context.getString(R.string.key_name), product.getName());
        values.put(context.getString(R.string.key_star), stars);

        return db.update(context.getString(R.string.table_products), values, context.getString(R.string.key_id) + "= ?", new String[]{String.valueOf(product.getId())});
    }

    public int updateProduct(Integer id, Date date){
        ContentValues values = new ContentValues();
        values.put(context.getString(R.string.key_id), id);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Log.d("updateProduct DBADAPTER", dateFormat.format(date));
        values.put(context.getString(R.string.last_purchase_date), dateFormat.format(date));

        return db.update(context.getString(R.string.table_products), values, context.getString(R.string.key_id) + "= ?", new String[]{String.valueOf(id)});
    }

    public void deleteProduct(Product product) {

        db.delete(context.getString(R.string.table_products), context.getString(R.string.key_id) + " = ?",
                new String[]{String.valueOf(product.getId())});
    }
}
