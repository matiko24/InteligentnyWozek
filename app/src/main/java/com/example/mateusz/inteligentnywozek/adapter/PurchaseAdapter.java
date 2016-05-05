package com.example.mateusz.inteligentnywozek.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mateusz.inteligentnywozek.general.Purchase;
import com.example.mateusz.inteligentnywozek.database.PurchaseDBAdapter;
import com.example.mateusz.inteligentnywozek.R;

/**
 * Created by Mateusz on 2015-11-30.
 */
public class PurchaseAdapter extends CursorAdapter{
    public PurchaseAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_list_view_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView itemName = (TextView) view.findViewById(R.id.item_name);

        final Long list_id = cursor.getLong(0);
        final Long product_id = cursor.getLong(1);
        String text = cursor.getString(2);
        itemName.setText(text);

        ImageButton delete = (ImageButton) view.findViewById(R.id.action_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PurchaseDBAdapter db = new PurchaseDBAdapter(context);
                Purchase purchase = new Purchase(list_id,product_id);
                db.deletePurchase(purchase);
                changeCursor(db.getAllPurchases(list_id));
            }
        });

    }
}
