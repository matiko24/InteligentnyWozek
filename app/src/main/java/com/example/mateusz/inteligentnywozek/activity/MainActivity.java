package com.example.mateusz.inteligentnywozek.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mateusz.inteligentnywozek.R;
import com.example.mateusz.inteligentnywozek.general.ShoppingList;
import com.example.mateusz.inteligentnywozek.adapter.ShoppingListAdapter;
import com.example.mateusz.inteligentnywozek.database.ShoppingListDBAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //getBaseContext().deleteDatabase(getBaseContext().getString(R.string.database_name));
        final ShoppingListDBAdapter db = new ShoppingListDBAdapter(getBaseContext());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddProduct);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.closeDb();
                showInputDialog();
            }
        });

        lists = (ListView) findViewById(R.id.listsView);
        final ShoppingListAdapter adapter = new ShoppingListAdapter(this,db.getAllLists());
        lists.setAdapter(adapter);
        lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ShoppingListsActivity.class);
                Cursor cursor = adapter.getCursor();
                Long listId = cursor.getLong(0);
                intent.putExtra(getBaseContext().getString(R.string.extra_list_id), listId);
                intent.putExtra(getBaseContext().getString(R.string.extra_list_name), cursor.getString(1));
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Toast.makeText(this, "Jesteś w oknie startowym", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_shop_list) {
            // Handle the camera action
        } else if (id == R.id.nav_products) {
            Intent intent = new Intent(MainActivity.this,ProductsPreferencesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_shops) {
            Intent intent = new Intent(MainActivity.this,ShopsPreferencesActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void showInputDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        alertDialogBuilder.setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final ShoppingListDBAdapter db = new ShoppingListDBAdapter(getBaseContext());
                        String listName = editText.getText().toString();
                        ShoppingList shoppingList = new ShoppingList(listName);
                        db.addList(shoppingList);
                        Intent intent = new Intent(MainActivity.this, ShoppingListsActivity.class);
                        intent.putExtra(getBaseContext().getString(R.string.extra_list_id), db.getListId(shoppingList.getName()));
                        intent.putExtra(getBaseContext().getString(R.string.extra_list_name), listName);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}
