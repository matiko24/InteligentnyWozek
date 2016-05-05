package com.example.mateusz.inteligentnywozek.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mateusz.inteligentnywozek.R;
import com.example.mateusz.inteligentnywozek.general.ShoppingList;
import com.example.mateusz.inteligentnywozek.database.ShoppingListDBAdapter;
import com.example.mateusz.inteligentnywozek.database.PurchaseDBAdapter;

/**
 * Created by Mateusz on 2015-11-30.
 */
public class ShoppingListAdapter extends CursorAdapter{
    public ShoppingListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_list_view_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView itemName = (TextView) view.findViewById(R.id.item_name);

        final Long listId = cursor.getLong(0);
        final String listName = cursor.getString(1);
        itemName.setText(listName);

        ImageButton delete = (ImageButton) view.findViewById(R.id.action_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingListDBAdapter db = new ShoppingListDBAdapter(context);
                ShoppingList shoppingList = new ShoppingList(listId,listName);
                db.deleteList(shoppingList);
                PurchaseDBAdapter pdb = new PurchaseDBAdapter(context);
                pdb.deletePurchases(shoppingList);
                db = new ShoppingListDBAdapter(context);
                changeCursor(db.getAllLists());
            }
        });

    }
}
