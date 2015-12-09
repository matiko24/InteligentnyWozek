package com.example.mateusz.inteligentnywozek;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
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
            selectQuery = "SELECT  * FROM " + context.getString(R.string.table_products) + " WHERE name LIKE '" + begining + "%' " + "ORDER BY " + context.getString(R.string.key_star) + " DESC";
            Log.d("SQL", selectQuery);
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

    public void deleteProduct(Product product) {

        db.delete(context.getString(R.string.table_products), context.getString(R.string.key_id) + " = ?",
                new String[]{String.valueOf(product.getId())});
    }
}
