package com.example.k234112eapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.adapters.PaperAdapter;
import com.example.models.PaperItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PaperActivity extends AppCompatActivity {

    ListView lvPaper;
    PaperAdapter paperAdapter;
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper);

        addControls();
        loadDataFromApi();
    }

    private void addControls() {
        lvPaper = findViewById(R.id.lvPaper);
        paperAdapter = new PaperAdapter(this, R.layout.item_paper);
        lvPaper.setAdapter(paperAdapter);
    }

    private void loadDataFromApi() {
        new Thread(() -> {
            try {
                // 1. Tạo kết nối đến API
                URL url = new URL("https://raovat.tuoitre.vn/api/list/list-top-ttorv");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // 2. Đọc dữ liệu trả về (JSON String)
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();

                // 3. Phân tích cấu trúc JSON
                // API trả về Object có chứa mảng "items"
                JSONObject jsonObject = new JSONObject(result.toString());
                String itemsJson = jsonObject.getJSONArray("items").toString();
                
                // Sử dụng Gson để chuyển từ String sang danh sách Object
                Gson gson = new Gson();
                List<PaperItem> items = gson.fromJson(itemsJson, new TypeToken<List<PaperItem>>(){}.getType());

                // 4. Đưa dữ liệu lên Giao diện (phải dùng Handler/runOnUiThread)
                handler.post(() -> {
                    paperAdapter.clear();
                    paperAdapter.addAll(items);
                    paperAdapter.notifyDataSetChanged();
                });

            } catch (Exception e) {
                Log.e("API_ERROR", "Lỗi khi lấy dữ liệu: " + e.getMessage());
            }
        }).start();
    }
}
