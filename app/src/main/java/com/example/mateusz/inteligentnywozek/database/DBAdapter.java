package com.example.mateusz.inteligentnywozek.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mateusz.inteligentnywozek.R;

/**
 * Created by Mateusz on 2015-11-28.
 */
public class DBAdapter {
    protected static DatabaseHelper DBHelper;
    protected final Context context;

    public DBAdapter(Context ctx) {
        this.context = ctx;
    }

    public SQLiteDatabase openDb() {
        if (DBHelper == null) {
            DBHelper = new DatabaseHelper(context);
        }
        return DBHelper.getWritableDatabase();
    }

    public void closeDb() {
        DBHelper.close();
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, "DataBase", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String CREATE_PRODUCTS_TABLE =
                    "CREATE TABLE " + context.getString(R.string.table_products) + "(" +
                            context.getString(R.string.key_id) + " INTEGER NOT NULL PRIMARY KEY," +
                            context.getString(R.string.key_name) + " TEXT," +
                            context.getString(R.string.key_star) + " INTEGER DEFAULT 0," +
                            context.getString(R.string.key_last_purchase_date)+ " DATE DEFAULT '2015-12-01', " +
                            context.getString(R.string.key_next_purchase_date)+ " DATE)";
            db.execSQL(CREATE_PRODUCTS_TABLE);

            String CREATE_SHOPPING_LIST_TABLE =
                    "CREATE TABLE " + context.getString(R.string.table_shopping_list) + "(" +
                            context.getString(R.string.key_id) + " INTEGER NOT NULL PRIMARY KEY," +
                            context.getString(R.string.key_name) + " TEXT)";
            db.execSQL(CREATE_SHOPPING_LIST_TABLE);

            String CREATE_PURCHASE_TABLE =
                    "CREATE TABLE " + context.getString(R.string.table_purchase) + "(" +
                            context.getString(R.string.key_id) + " INTEGER NOT NULL PRIMARY KEY," +
                            context.getString(R.string.key_list_id) + " INTEGER NOT NULL," +
                            context.getString(R.string.key_product_id) + " INTEGER NOT NULL, " +
                            "FOREIGN KEY (" + context.getString(R.string.key_list_id) + ") REFERENCES " + context.getString(R.string.table_shopping_list) + "(" + context.getString(R.string.key_id) + "), " +
                            "FOREIGN KEY (" + context.getString(R.string.key_product_id) + ") REFERENCES " + context.getString(R.string.table_products) + "(" + context.getString(R.string.key_id) + "))";
            db.execSQL(CREATE_PURCHASE_TABLE);

            String CREATE_SHOPS_TABLE =
                    "CREATE TABLE " + context.getString(R.string.table_shops) + " ("+
                            context.getString(R.string.key_id) + " INTEGER NOT NULL PRIMARY KEY, "+
                            context.getString(R.string.key_name) + " TEXT, " +
                            context.getString(R.string.key_number_of_purchases) +" INTEGER DEFAULT 0," +
                            "column_number INTEGER NOT NULL," +
                            "row_number INTEGER NOT NULL)";
            db.execSQL(CREATE_SHOPS_TABLE);

            /*String products[] = new String[]{"chleb", "mleko", "woda", "jabłko", "jogurt", "bułka", "musli",
                    "banan", "jajka", "kiełbasa", "makaron", "musztarda", "sok",
                    "ryba", "mąka", "ser", "kefir", "orzeszki", "pomidory", "ogórki"};*/

            String products[] = new String[200];
            for (int i = 1; i <= products.length; i++) {
                products[i - 1] = "Product_"+ String.valueOf(i);
            }

            for (String p : products) {
                ContentValues values = new ContentValues();
                values.put(context.getString(R.string.key_name), p.toString());

                db.insert(context.getString(R.string.table_products), null, values);
            }

            ContentValues values = new ContentValues();
            values.put(context.getString(R.string.key_name), "Tesco 2x20");
            values.put("column_number","20");
            values.put("row_number","2");
            db.insert(context.getString(R.string.table_shops), null, values);

            values = new ContentValues();
            values.put(context.getString(R.string.key_name), "Lewiatan 2x10");
            values.put("column_number","10");
            values.put("row_number","2");
            db.insert(context.getString(R.string.table_shops), null, values);


            values = new ContentValues();
            ContentValues values1 = new ContentValues();
            ContentValues values2 = new ContentValues();
            ContentValues values3 = new ContentValues();

            values.put(context.getString(R.string.key_list_id), 1);
            values.put(context.getString(R.string.key_product_id), 1);

            values1.put(context.getString(R.string.key_list_id), 1);
            values1.put(context.getString(R.string.key_product_id), 2);

            values2.put(context.getString(R.string.key_list_id), 1);
            values2.put(context.getString(R.string.key_product_id), 3);

            values3.put(context.getString(R.string.key_list_id), 1);
            values3.put(context.getString(R.string.key_product_id), 4);

            db.insert(context.getString(R.string.table_purchase), null, values);
            db.insert(context.getString(R.string.table_purchase), null, values1);
            db.insert(context.getString(R.string.table_purchase), null, values2);
            db.insert(context.getString(R.string.table_purchase), null, values3);


            String lists[] = new String[]{"lista1"};

            for (String p : lists) {
                ContentValues values4 = new ContentValues();
                values4.put(context.getString(R.string.key_id), 1);
                values4.put(context.getString(R.string.key_name), p.toString());

                db.insert(context.getString(R.string.table_shopping_list), null, values4);
            }


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
