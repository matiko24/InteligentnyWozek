package com.example.mateusz.inteligentnywozek.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mateusz.inteligentnywozek.general.Purchase;
import com.example.mateusz.inteligentnywozek.R;
import com.example.mateusz.inteligentnywozek.general.ShoppingList;

/**
 * Created by Mateusz on 2015-11-29.
 */
public class PurchaseDBAdapter extends DBAdapter {
    protected SQLiteDatabase db = openDb();

    public PurchaseDBAdapter(Context context) {
        super(context);
    }

    public void addPurchase(Purchase purchase) {
        ContentValues values = new ContentValues();
        values.put(context.getString(R.string.key_list_id), purchase.getList_id());
        values.put(context.getString(R.string.key_product_id), purchase.getProduct_id());

        db.insert(context.getString(R.string.table_purchase), null, values);
    }

    public Cursor getAllPurchases(Long listId) {
        String selectQuery = "SELECT pu." + context.getString(R.string.key_list_id) + ", pr." + context.getString(R.string.key_id) + ", pr." + context.getString(R.string.key_name) + " FROM " + context.getString(R.string.table_purchase) + " pu INNER JOIN " + context.getString(R.string.table_products) +
                " pr ON pu." + context.getString(R.string.key_product_id) + "=pr." + context.getString(R.string.key_id) +
                " WHERE " + context.getString(R.string.key_list_id) + "=" + listId;
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor;
    }

    public String getAllPurchasesString(Long listId) {
        String selectQuery = "SELECT pu." + context.getString(R.string.key_list_id) + ", pr." + context.getString(R.string.key_id) + ", pr." + context.getString(R.string.key_name) + " FROM " + context.getString(R.string.table_purchase) + " pu INNER JOIN " + context.getString(R.string.table_products) +
                " pr ON pu." + context.getString(R.string.key_product_id) + "=pr." + context.getString(R.string.key_id) +
                " WHERE " + context.getString(R.string.key_list_id) + "=" + listId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        String produstsIds = "";
        while (cursor.moveToNext()) {
            produstsIds += "productList=" + cursor.getString(2) + "&";
        }
        produstsIds = produstsIds.substring(0, produstsIds.length()-1);
        return produstsIds;
    }

    public void deletePurchase(Purchase purchase) {
        db.delete(context.getString(R.string.table_purchase), context.getString(R.string.key_list_id) + "=? AND " + context.getString(R.string.key_product_id) + "=?",
                new String[]{String.valueOf(purchase.getList_id()), String.valueOf(purchase.getProduct_id())});
    }

    public void deletePurchases(ShoppingList shoppingList) {

        db.delete(context.getString(R.string.table_purchase), context.getString(R.string.key_list_id) + "=?",
                new String[]{String.valueOf(shoppingList.getId())});
    }
}
