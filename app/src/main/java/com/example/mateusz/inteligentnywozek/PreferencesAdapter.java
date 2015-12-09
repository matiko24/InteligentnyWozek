package com.example.mateusz.inteligentnywozek;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Mateusz on 2015-11-30.
 */
public class PreferencesAdapter extends CursorAdapter {
    ProductsDBAdapter db;
    public PreferencesAdapter(Context context, Cursor c) {

        super(context, c, 0);
        db = new ProductsDBAdapter(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_preference, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView productNameTextView = (TextView) view.findViewById(R.id.item_name);
        productNameTextView.setText(cursor.getString(1));

        final Long productId = cursor.getLong(0);
        final String productName = cursor.getString(1);
        final Integer star = cursor.getInt(2);
        final ImageView image = (ImageView) view.findViewById(R.id.star);
        if(star==0)
            image.setImageResource(R.drawable.ic_star_outline);
        else
            image.setImageResource(R.drawable.ic_action_star_10);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image.getTag().toString().equals("out")){
                    image.setImageResource(R.drawable.ic_action_star_10);
                    image.setTag("full");
                    db.updateProduct(new Product(productId,productName),1);
                }
                else {
                    image.setImageResource(R.drawable.ic_star_outline);
                    image.setTag("out");
                    db.updateProduct(new Product(productId, productName), 0);
                }
            }
        });

    }
}