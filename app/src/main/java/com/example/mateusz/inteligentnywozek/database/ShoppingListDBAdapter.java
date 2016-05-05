package com.example.mateusz.inteligentnywozek.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mateusz.inteligentnywozek.R;
import com.example.mateusz.inteligentnywozek.general.ShoppingList;

/**
 * Created by Mateusz on 2015-11-29.
 */
public class ShoppingListDBAdapter extends DBAdapter {

    protected SQLiteDatabase db = openDb();

    public ShoppingListDBAdapter(Context context) {
        super(context);
    }

    public void addList(ShoppingList list) {
        ContentValues values = new ContentValues();
        values.put(context.getString(R.string.key_name), list.getName());

        db.insert(context.getString(R.string.table_shopping_list), null, values);
    }

    public Long getListId(String name) {
        Cursor cursor = db.query(context.getString(R.string.table_shopping_list), new String[]{context.getString(R.string.key_id), context.getString(R.string.key_name)}, context.getString(R.string.key_name) + "=?", new String[]{name}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor.getLong(0);
    }

    public Cursor getAllLists() {
        String selectQuery = "SELECT  * FROM " + context.getString(R.string.table_shopping_list);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

    public void deleteList(ShoppingList shoppingList) {

        db.delete(context.getString(R.string.table_shopping_list), context.getString(R.string.key_id) + " = ?",
                new String[]{String.valueOf(shoppingList.getId())});
    }
}
