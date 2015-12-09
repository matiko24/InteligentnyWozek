package com.example.mateusz.inteligentnywozek;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateusz on 2015-11-29.
 */
public class ShoppingListDBAdapter extends DBAdapter {

    protected SQLiteDatabase db = openDb();

    public ShoppingListDBAdapter(Context context) {
        super(context);
    }

    void addList(ShoppingList list) {
        ContentValues values = new ContentValues();
        values.put(context.getString(R.string.key_name), list.getName());

        db.insert(context.getString(R.string.table_shopping_list), null, values);
    }

    public ShoppingList getList(String name) {
        String query = "SELECT  * FROM " + context.getString(R.string.table_shopping_list) + " WHERE " + context.getString(R.string.key_name) + "=" + name;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null)
            cursor.moveToFirst();

        ShoppingList shoppingList = new ShoppingList(cursor.getInt(0), cursor.getString(1));
        return shoppingList;
    }

    public Long getListId(String name) {
        String query = "SELECT  * FROM " + context.getString(R.string.table_shopping_list) + " WHERE " + context.getString(R.string.key_name) + "=" + name;
        Cursor cursor = db.query(context.getString(R.string.table_shopping_list), new String[]{context.getString(R.string.key_id), context.getString(R.string.key_name)}, context.getString(R.string.key_name) + "=?", new String[]{name}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor.getLong(0);
    }

    public Cursor getAllLists() {
        List<ShoppingList> shoppingLists = new ArrayList<ShoppingList>();
        String selectQuery = "SELECT  * FROM " + context.getString(R.string.table_shopping_list);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

    public int updateList(ShoppingList shoppingList) {

        ContentValues values = new ContentValues();
        values.put(context.getString(R.string.key_name), shoppingList.getName());

        return db.update(context.getString(R.string.table_shopping_list), values, context.getString(R.string.key_id) + " = ?",
                new String[]{String.valueOf(shoppingList.getId())});
    }

    public void deleteList(ShoppingList shoppingList) {

        db.delete(context.getString(R.string.table_shopping_list), context.getString(R.string.key_id) + " = ?",
                new String[]{String.valueOf(shoppingList.getId())});
    }
}
