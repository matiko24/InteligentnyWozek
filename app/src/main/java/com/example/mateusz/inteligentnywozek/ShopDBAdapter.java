package com.example.mateusz.inteligentnywozek;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Mateusz on 2015-12-09.
 */
public class ShopDBAdapter extends DBAdapter {

    SQLiteDatabase db = openDb();

    public ShopDBAdapter(Context ctx) {
        super(ctx);
    }

    public Shop getShop(String shopName) {
        Cursor cursor = db.query(context.getString(R.string.table_shops), new String[]{context.getString(R.string.key_id), context.getString(R.string.key_name), context.getString(R.string.key_number_of_purchases)}, context.getString(R.string.key_name) + "=?", new String[]{shopName}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Shop shop = new Shop(cursor.getLong(0), cursor.getString(1), cursor.getInt(2));
        return shop;
    }

    public Cursor getShops(String begining) {
        String selectQuery;
        if (begining == "")
            selectQuery = "SELECT * FROM " + context.getString(R.string.table_shops) + " ORDER BY " + context.getString(R.string.key_number_of_purchases) + " DESC";
        else
            selectQuery = "SELECT * FROM " + context.getString(R.string.table_shops) + " WHERE " + context.getString(R.string.key_name) + " LIKE '" + begining + "%' ORDER BY " + context.getString(R.string.key_number_of_purchases) + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor;
    }

    public void updateShop(String shopName) {
        Shop shop = getShop(shopName);
        ContentValues values = new ContentValues();
        values.put(context.getString(R.string.key_id), shop.getId());
        values.put(context.getString(R.string.key_name), shop.getName());
        values.put(context.getString(R.string.key_number_of_purchases), shop.getNumberOfPurchases() + 1);

        db.update(context.getString(R.string.table_shops), values, context.getString(R.string.key_id) + "=?", new String[]{String.valueOf(shop.getId())});

    }
}
