package com.example.mateusz.inteligentnywozek.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mateusz.inteligentnywozek.general.Product;
import com.example.mateusz.inteligentnywozek.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mateusz on 2015-11-28.
 */
public class ProductDBAdapter extends DBAdapter {

    protected SQLiteDatabase db = openDb();

    public ProductDBAdapter(Context context) {
        super(context);
    }

    void addProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(context.getString(R.string.key_name), product.getName());

        db.insert(context.getString(R.string.table_products), null, values);
    }

    public String getProductLastPurchaseDate(Long id) {
        Cursor cursor = db.query(context.getString(R.string.table_products), new String[]{context.getString(R.string.key_id),
                        context.getString(R.string.key_name), context.getString(R.string.key_star), context.getString(R.string.key_last_purchase_date), context.getString(R.string.key_next_purchase_date)}, context.getString(R.string.key_id) + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return cursor.getString(3);
    }

    public Cursor getAllProductsSortOnlyByStar(String begining) {
        String selectQuery;
        if (begining == "")
            selectQuery = "SELECT  * FROM " + context.getString(R.string.table_products) + " ORDER BY " + context.getString(R.string.key_star) + " DESC";
        else {
            selectQuery = "SELECT  * FROM " + context.getString(R.string.table_products) + " WHERE " + context.getString(R.string.key_name) + " LIKE '" + begining + "%' " + " ORDER BY " + context.getString(R.string.key_star) + " DESC";
        }
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor getAllProducts(String begining) {
        String selectQuery;
        if (begining == "")
            selectQuery = "SELECT  * FROM " + context.getString(R.string.table_products) + " ORDER BY CASE WHEN " + context.getString(R.string.key_next_purchase_date) + " IS NULL THEN 1 ELSE 0 END, " + context.getString(R.string.key_next_purchase_date) + "," + context.getString(R.string.key_star) + " DESC";
        else {
            selectQuery = "SELECT  * FROM " + context.getString(R.string.table_products) + " WHERE " + context.getString(R.string.key_name) + " LIKE '" + begining + "%' " + " ORDER BY CASE WHEN " + context.getString(R.string.key_next_purchase_date) + " IS NULL THEN 1 ELSE 0 END, " + context.getString(R.string.key_next_purchase_date) + "," + context.getString(R.string.key_star) + " DESC";
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

    public int updateProduct(Integer id, Date lastPurchaseDate, Date nextPurchaseDate) {
        ContentValues values = new ContentValues();
        values.put(context.getString(R.string.key_id), id);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        values.put(context.getString(R.string.key_last_purchase_date), dateFormat.format(lastPurchaseDate));
        values.put(context.getString(R.string.key_next_purchase_date), dateFormat.format(nextPurchaseDate));

        return db.update(context.getString(R.string.table_products), values, context.getString(R.string.key_id) + "= ?", new String[]{String.valueOf(id)});
    }

    public void deleteProduct(Product product) {

        db.delete(context.getString(R.string.table_products), context.getString(R.string.key_id) + " = ?",
                new String[]{String.valueOf(product.getId())});
    }
}
