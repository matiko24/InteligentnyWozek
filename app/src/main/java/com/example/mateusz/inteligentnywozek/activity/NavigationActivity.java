package com.example.mateusz.inteligentnywozek.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mateusz.inteligentnywozek.R;
import com.example.mateusz.inteligentnywozek.general.ShoppingList;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NavigationActivity extends AppCompatActivity {

    Handler handler;
    ListView listView;
    long listId;
    String productsIds;
    String listName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        listId = getIntent().getExtras().getLong(getBaseContext().getString(R.string.extra_list_id));
        productsIds = getIntent().getExtras().getString(getBaseContext().getString(R.string.extra_products_ids));
        listName = getIntent().getExtras().getString(getBaseContext().getString(R.string.extra_list_name));

        listView = (ListView) findViewById(R.id.navigationTipListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parent.getChildAt(position).setBackgroundColor(Color.GREEN);
            }
        });

        Button button_koniec = (Button) findViewById(R.id.button_koniec);
        button_koniec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                String results = (String) msg.obj;
                String[] tips = results.split(";");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(NavigationActivity.this, R.layout.row_navigation_tip, R.id.navigation_tip, navigateTips(tips));
                listView.setAdapter(adapter);
            }
        };

        askServer(productsIds);

    }

    public String askServer(String productsIds) {
        final String[] returnString = new String[1];
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://10.0.3.2:8080/Serwer/map/products?shop=1&ids=" + productsIds)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("ERROR BŁAD W ZAPYTANIU DO SERWERA");
            }

            @Override
            public void onResponse(final Response response) throws IOException {

                //TODO Usunąć zwracane przez serwer "a" przed JSONem
                String responseData = response.body().string().substring(1);
                JSONObject json = null;
                try {
                    json = new JSONObject(responseData);
                    String result = json.getString("Result");
                    returnString[0] = result;
                    Message message = new Message();
                    message.obj = result;

                    handler.sendMessage(message);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return returnString[0];
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ShoppingListsActivity.class);
        intent.putExtra(getBaseContext().getString(R.string.extra_list_id), listId);
        intent.putExtra(getBaseContext().getString(R.string.extra_list_name), listName);
        startActivity(intent);
    }

    public String[] navigateTips(String[] tips) {

        String[] newTips = tips.clone();

        for (int i = 0; i < tips.length; i++) {
            if (i == 0) {
                if (tips[i].equals("L"))
                    newTips[i] = "Skręć w lewo";
                else if (tips[i].equals("P"))
                    newTips[i] = "Skręć w prawo";
                else if (tips[i].equals("S"))
                    newTips[i] = "Idź prosto";
            } else if (tips[i].substring(1).equals("WP") || tips[i].substring(1).equals("WL")) {
                if (tips[i + 1].length() == 2) {
                    if (tips[i].substring(2).equals("L")) {
                        newTips[i] = "Skręć w " + tips[i].substring(0, 1) + " alejkę w lewo";
                    } else {
                        newTips[i] = "Skręć w " + tips[i].substring(0, 1) + " alejkę w prawo";
                    }
                } else {
                    if (tips[i].substring(2).equals("L")) {
                        newTips[i] = "Idź " + tips[i].substring(0, 1) + " alejek prosto";
                    } else {
                        newTips[i] = "Idź " + tips[i].substring(0, 1) + " alejek prosto";
                    }
                }
            } else if (tips[i].substring(0, 1).equals("S") || tips[i].substring(0, 1).equals("Z")) {
                if (tips[i].equals("SL"))
                    newTips[i] = "Następnie skręć w lewo";
                else if (tips[i].equals("SP"))
                    newTips[i] = "Następnie skręć w prawo";
                else if (tips[i].equals("ZP"))
                    newTips[i] = "Zawróc i skręć w prawo";
                else if (tips[i].equals("ZL"))
                    newTips[i] = "Zawróc i skręć w lewo";
            } else if (tips[i].equals("M")) {
                newTips[i] = "KONIEC";
            } else if (tips[i].length() > 2) {
                if (tips[i].substring(1, 2).equals("L")) {
                    if (tips[i].substring(2).equals("U")) {
                        if (tips[i].substring(0, 1).equals("1"))
                            newTips[i] = " W prawo, na początku regału po lewej stronie ";
                        else if (tips[i].substring(0, 1).equals("2"))
                            newTips[i] = " W prawo, w środku regału po lewej stronie ";
                        else
                            newTips[i] = " W prawo, na końcu regału po lewej stronie ";
                    } else {
                        if (tips[i].substring(0, 1).equals("1"))
                            newTips[i] = " W lewo, na początku regału po lewej stronie ";
                        else if (tips[i].substring(0, 1).equals("2"))
                            newTips[i] = " W lewo, w środku regału po lewej stronie ";
                        else
                            newTips[i] = " W lewo, na końcu regału po lewej stronie ";
                    }
                } else {
                    if (tips[i].substring(2).equals("U")) {
                        if (tips[i].substring(0, 1).equals("1"))
                            newTips[i] = " W prawo, na początku regału po prawej stronie ";
                        else if (tips[i].substring(0, 1).equals("2"))
                            newTips[i] = " W prawo, w środku regału po prawej stronie ";
                        else
                            newTips[i] = " W prawo, na końcu regału po prawej stronie ";
                    } else {
                        if (tips[i].substring(0, 1).equals("1"))
                            newTips[i] = " W lewo, na początku regału po prawej stronie ";
                        else if (tips[i].substring(0, 1).equals("2"))
                            newTips[i] = " W lewo, w środku regału po prawej stronie ";
                        else
                            newTips[i] = " W lewo, na końcu regału po prawej stronie ";
                    }
                }
            } else {
                if (tips[i].substring(1).equals("L")) {
                    if (tips[i].substring(0, 1).equals("1"))
                        newTips[i] = "Na początku regału po lewej";
                    else if (tips[i].substring(0, 1).equals("2"))
                        newTips[i] = "W środku regału po lewej";
                    else
                        newTips[i] = "Na końcu regału po lewej";
                } else {
                    if (tips[i].substring(0, 1).equals("1"))
                        newTips[i] = "Na początku regału po prawej";
                    else if (tips[i].substring(0, 1).equals("2"))
                        newTips[i] = "W środku regału po prawej";
                    else
                        newTips[i] = "Na końcu regału po prawej";
                }
            }


        }
        return newTips;
    }
}
