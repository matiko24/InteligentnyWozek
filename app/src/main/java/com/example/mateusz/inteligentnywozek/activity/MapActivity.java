package com.example.mateusz.inteligentnywozek.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mateusz.inteligentnywozek.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Mateusz on 2017-02-15.
 */

public class MapActivity extends AppCompatActivity {
    TouchImageView drawingImageView;
    ListView productList;
    ArrayAdapter<String> adapter;
    Canvas canvas;
    Handler handler;
    int screenSizeWidth;
    int screenSizeHight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenSizeHight = size.y;
        screenSizeWidth = size.x;

        final Long shopId = getIntent().getExtras().getLong(getBaseContext().getString(R.string.extra_shop_id));
        final int columnNumber = getIntent().getExtras().getInt(getBaseContext().getString(R.string.extra_column_number));
        final int rowNumber = getIntent().getExtras().getInt(getBaseContext().getString(R.string.extra_row_number));
        final Long listId = getIntent().getExtras().getLong(getBaseContext().getString(R.string.extra_list_id));
        final String listName = getIntent().getExtras().getString(getBaseContext().getString(R.string.extra_list_name));
        final String productsIds = getIntent().getExtras().getString(getBaseContext().getString(R.string.extra_products_ids));

        drawingImageView = (TouchImageView) this.findViewById(R.id.mapImageView);
        productList = (ListView) this.findViewById(R.id.productListMap);
        String[] array = {"Start"};
        ArrayList<String> lst = new ArrayList<String>(Arrays.asList(array));
        adapter = new ArrayAdapter<String>(this, R.layout.row_product_on_map, lst);
        productList.setAdapter(adapter);
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parent.getChildAt(position).setBackgroundColor(Color.GREEN);
            }
        });

        drawShelfs(columnNumber, rowNumber);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                String results = (String) msg.obj;
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(results);

                    Paint paint = new Paint();
                    paint.setStrokeWidth(5);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(Color.GREEN);

                    Paint circlePaint = new Paint();
                    circlePaint.setStrokeWidth(5);
                    circlePaint.setStyle(Paint.Style.STROKE);
                    circlePaint.setColor(Color.RED);

                    Path p = new Path();
                    System.out.println(canvas.getHeight());
                    int elementNumber = (6 + ((rowNumber -1) *4));
                    int zeroHigh = screenSizeHight - (screenSizeHight / elementNumber);
                    p.moveTo(0, zeroHigh);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        int x = Integer.valueOf(jsonArray.getJSONObject(i).getString("columnNumber"));
                        int y = Integer.valueOf(jsonArray.getJSONObject(i).getString("rowNumber"));
                        p.lineTo((x + 1) * (screenSizeWidth) / (2 * columnNumber), zeroHigh - (y * screenSizeHight/elementNumber));
                        if (jsonArray.getJSONObject(i).getBoolean("containsRequiredProduct")) {
                            addListViewItem(jsonArray.getJSONObject(i).getString("id"));
                            canvas.drawCircle((x + 1) * (screenSizeWidth) / (2 * columnNumber), zeroHigh - (y * screenSizeHight/elementNumber), 10, circlePaint);
                        }
                    }
                    canvas.drawPath(p, paint);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        askServer(productsIds, shopId);
    }

    public void addListViewItem(String productName) {
        adapter.add("Product_" + productName);
        adapter.notifyDataSetChanged();
        ViewGroup.LayoutParams params = productList.getLayoutParams();
        params.height = adapter.getCount() * 40;
        productList.setLayoutParams(params);
        productList.requestLayout();
    }

    public void drawShelfs(int x, int y) {

        Bitmap bitmap = Bitmap.createBitmap(screenSizeWidth, screenSizeHight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        drawingImageView.setImageBitmap(bitmap);

        Paint rectanglePaint = new Paint();
        rectanglePaint.setARGB(255, 0, 0, 0);
        rectanglePaint.setStrokeWidth(20 - x);
        rectanglePaint.setColor(Color.BLACK);
        rectanglePaint.setStyle(Paint.Style.STROKE);

        int numberToScreenDivideX = (screenSizeWidth) / (2 * x);
        int numberToScreenDivideY = (screenSizeHight) / (6 + ((y -1) *4));

        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                Rect rectangle = new Rect(numberToScreenDivideX * (i + 1) + numberToScreenDivideX * i, numberToScreenDivideY * (2+(4*j)), numberToScreenDivideX * (i + 1) + numberToScreenDivideX * (i + 1), numberToScreenDivideY * (4+(4*j)) );
                canvas.drawRect(rectangle, rectanglePaint);
                /*rectangle = new Rect(numberToScreenDivideX * (i + 1) + numberToScreenDivideX * i, numberToScreenDivideY * 5, numberToScreenDivideX * (i + 1) + numberToScreenDivideX * (i + 1), numberToScreenDivideY * 8);
                canvas.drawRect(rectangle, rectanglePaint);*/
            }
        }
    }

    public String askServer(String productsIds, Long shopId) {
        final String[] returnString = new String[1];
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://10.0.3.2:8080/Serwer/map/getShortestPath?shop=" + shopId + "&" + productsIds)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //System.out.println("ERROR BŁAD W ZAPYTANIU DO SERWERA");
                //String responseData = "[{\"columnNumber\":0,\"id\":302,\"shopId\":1,\"rowNumber\":1,\"containsRequiredProduct\":true},{\"columnNumber\":0,\"id\":303,\"shopId\":1,\"rowNumber\":2,\"containsRequiredProduct\":false},{\"columnNumber\":0,\"id\":304,\"shopId\":1,\"rowNumber\":3,\"containsRequiredProduct\":false},{\"columnNumber\":0,\"id\":305,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":1,\"id\":318,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":1,\"id\":319,\"shopId\":1,\"rowNumber\":5,\"containsRequiredProduct\":true},{\"columnNumber\":1,\"id\":319,\"shopId\":1,\"rowNumber\":5,\"containsRequiredProduct\":true}]";
                String responseData = "[{\"columnNumber\":0,\"id\":3,\"shopId\":1,\"rowNumber\":2,\"containsRequiredProduct\":true},{\"columnNumber\":0,\"id\":4,\"shopId\":1,\"rowNumber\":3,\"containsRequiredProduct\":true},{\"columnNumber\":0,\"id\":4,\"shopId\":1,\"rowNumber\":3,\"containsRequiredProduct\":true},{\"columnNumber\":0,\"id\":5,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":0,\"id\":6,\"shopId\":1,\"rowNumber\":5,\"containsRequiredProduct\":true},{\"columnNumber\":0,\"id\":6,\"shopId\":1,\"rowNumber\":5,\"containsRequiredProduct\":true},{\"columnNumber\":0,\"id\":7,\"shopId\":1,\"rowNumber\":6,\"containsRequiredProduct\":true},{\"columnNumber\":0,\"id\":7,\"shopId\":1,\"rowNumber\":6,\"containsRequiredProduct\":true},{\"columnNumber\":0,\"id\":6,\"shopId\":1,\"rowNumber\":5,\"containsRequiredProduct\":true},{\"columnNumber\":0,\"id\":5,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":1,\"id\":14,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":1,\"id\":13,\"shopId\":1,\"rowNumber\":3,\"containsRequiredProduct\":false},{\"columnNumber\":1,\"id\":12,\"shopId\":1,\"rowNumber\":2,\"containsRequiredProduct\":false},{\"columnNumber\":2,\"id\":21,\"shopId\":1,\"rowNumber\":2,\"containsRequiredProduct\":true},{\"columnNumber\":2,\"id\":21,\"shopId\":1,\"rowNumber\":2,\"containsRequiredProduct\":true},{\"columnNumber\":2,\"id\":22,\"shopId\":1,\"rowNumber\":3,\"containsRequiredProduct\":false},{\"columnNumber\":2,\"id\":23,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":3,\"id\":32,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":4,\"id\":41,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":5,\"id\":50,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":5,\"id\":49,\"shopId\":1,\"rowNumber\":3,\"containsRequiredProduct\":true},{\"columnNumber\":5,\"id\":49,\"shopId\":1,\"rowNumber\":3,\"containsRequiredProduct\":true},{\"columnNumber\":5,\"id\":50,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":6,\"id\":59,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":7,\"id\":68,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":7,\"id\":67,\"shopId\":1,\"rowNumber\":3,\"containsRequiredProduct\":true},{\"columnNumber\":7,\"id\":67,\"shopId\":1,\"rowNumber\":3,\"containsRequiredProduct\":true},{\"columnNumber\":8,\"id\":76,\"shopId\":1,\"rowNumber\":3,\"containsRequiredProduct\":false},{\"columnNumber\":8,\"id\":77,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":9,\"id\":86,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":9,\"id\":85,\"shopId\":1,\"rowNumber\":3,\"containsRequiredProduct\":false},{\"columnNumber\":9,\"id\":84,\"shopId\":1,\"rowNumber\":2,\"containsRequiredProduct\":false},{\"columnNumber\":10,\"id\":93,\"shopId\":1,\"rowNumber\":2,\"containsRequiredProduct\":true},{\"columnNumber\":10,\"id\":93,\"shopId\":1,\"rowNumber\":2,\"containsRequiredProduct\":true},{\"columnNumber\":10,\"id\":92,\"shopId\":1,\"rowNumber\":1,\"containsRequiredProduct\":false},{\"columnNumber\":10,\"id\":91,\"shopId\":1,\"rowNumber\":0,\"containsRequiredProduct\":false},{\"columnNumber\":11,\"id\":100,\"shopId\":1,\"rowNumber\":0,\"containsRequiredProduct\":false},{\"columnNumber\":12,\"id\":109,\"shopId\":1,\"rowNumber\":0,\"containsRequiredProduct\":false},{\"columnNumber\":13,\"id\":118,\"shopId\":1,\"rowNumber\":0,\"containsRequiredProduct\":false},{\"columnNumber\":14,\"id\":127,\"shopId\":1,\"rowNumber\":0,\"containsRequiredProduct\":false},{\"columnNumber\":15,\"id\":136,\"shopId\":1,\"rowNumber\":0,\"containsRequiredProduct\":false},{\"columnNumber\":16,\"id\":145,\"shopId\":1,\"rowNumber\":0,\"containsRequiredProduct\":false},{\"columnNumber\":16,\"id\":146,\"shopId\":1,\"rowNumber\":1,\"containsRequiredProduct\":true}]";
                returnString[0] = responseData;
                Message message = new Message();
                message.obj = responseData;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(final Response response) throws IOException {

                String responseData = response.body().string();
                //responseData = "[{\"columnNumber\":0,\"id\":302,\"shopId\":1,\"rowNumber\":1,\"containsRequiredProduct\":true},{\"columnNumber\":0,\"id\":303,\"shopId\":1,\"rowNumber\":2,\"containsRequiredProduct\":false},{\"columnNumber\":0,\"id\":304,\"shopId\":1,\"rowNumber\":3,\"containsRequiredProduct\":false},{\"columnNumber\":0,\"id\":305,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":1,\"id\":318,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":1,\"id\":319,\"shopId\":1,\"rowNumber\":5,\"containsRequiredProduct\":true},{\"columnNumber\":1,\"id\":319,\"shopId\":1,\"rowNumber\":5,\"containsRequiredProduct\":true}]";
                returnString[0] = responseData;
                Message message = new Message();
                message.obj = responseData;
                handler.sendMessage(message);
            }
        });
        return returnString[0];
    }
}

