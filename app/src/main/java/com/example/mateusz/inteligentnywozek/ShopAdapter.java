package com.example.mateusz.inteligentnywozek;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Mateusz on 2015-12-09.
 */
public class ShopAdapter extends CursorAdapter{
    public ShopAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_product, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView shopName = (TextView) view.findViewById(R.id.productName);
        shopName.setText(cursor.getString(1));
    }


}
