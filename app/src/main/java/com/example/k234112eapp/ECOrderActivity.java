package com.example.k234112eapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ECOrderActivity extends AppCompatActivity {

    TextView txtFromDate, txtToDate;
    ImageView imgFromDate, imgToDate, imgClearFilter, imgFilter;
    ListView lvOrder;

    ArrayList<Order> allOrdersList = new ArrayList<>();
    ArrayList<Order> displayList = new ArrayList<>();
    
    // Maps để lưu dữ liệu mở rộng từ Firebase
    HashMap<String, Double> orderPrices = new HashMap<>();
    HashMap<String, String> orderStatusStrings = new HashMap<>();
    
    ArrayAdapter<Order> adapter;
    Calendar calFrom = Calendar.getInstance();
    Calendar calTo = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    String firebaseURL = "https://e-commerce-f17c0-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ecorder);

        addViews();
        addEvents();
        loadDataFromFirebase();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LinearLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        txtFromDate = findViewById(R.id.txtFromDate);
        txtToDate = findViewById(R.id.txtToDate);
        imgFromDate = findViewById(R.id.imgFromDate);
        imgToDate = findViewById(R.id.imgToDate);
        imgClearFilter = findViewById(R.id.imgClearFilter);
        imgFilter = findViewById(R.id.imgFilter);
        lvOrder = findViewById(R.id.lvOrder);

        calFrom.set(2023, 0, 1);
        calTo.set(2026, 11, 31);
        txtFromDate.setText(sdf.format(calFrom.getTime()));
        txtToDate.setText(sdf.format(calTo.getTime()));

        adapter = new ArrayAdapter<Order>(this, R.layout.order_custom_item, displayList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_custom_item, parent, false);
                }

                Order order = getItem(position);
                if (order != null) {
                    TextView txtID = convertView.findViewById(R.id.txtID);
                    TextView txtDate = convertView.findViewById(R.id.txtDate);
                    TextView txtStatus = convertView.findViewById(R.id.txtStatus);
                    TextView txtPrice = convertView.findViewById(R.id.txtPrice);

                    txtID.setText("ID: " + order.getOrderId());
                    txtDate.setText(order.getOrderDate() != null ? sdfFull.format(order.getOrderDate()) : "No Date");
                    
                    String status = orderStatusStrings.get(order.getOrderId());
                    if (status == null) status = "Pending";
                    txtStatus.setText(status);
                    
                    // Cập nhật màu sắc theo yêu cầu
                    int color = 0xFF9E9E9E; // Default Gray
                    if (status.equalsIgnoreCase("Completed")) color = 0xFF4CAF50;
                    else if (status.equalsIgnoreCase("Shipping")) color = 0xFF2196F3;
                    else if (status.equalsIgnoreCase("Processing")) color = 0xFFFF9800;
                    
                    txtStatus.setBackgroundColor(color);

                    Double price = orderPrices.get(order.getOrderId());
                    txtPrice.setText(String.format(Locale.getDefault(), "%,.0f VNĐ", price != null ? price : 0.0));
                }
                return convertView;
            }
        };
        lvOrder.setAdapter(adapter);
    }

    private void addEvents() {
        imgFromDate.setOnClickListener(v -> showDatePicker(true));
        imgToDate.setOnClickListener(v -> showDatePicker(false));
        imgFilter.setOnClickListener(v -> applyFilter());
        imgClearFilter.setOnClickListener(v -> {
            displayList.clear();
            displayList.addAll(allOrdersList);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Show all orders", Toast.LENGTH_SHORT).show();
        });
    }

    private void showDatePicker(boolean isFrom) {
        Calendar cal = isFrom ? calFrom : calTo;
        new DatePickerDialog(this, (view, year, month, day) -> {
            cal.set(year, month, day);
            if (isFrom) txtFromDate.setText(sdf.format(cal.getTime()));
            else txtToDate.setText(sdf.format(cal.getTime()));
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void loadDataFromFirebase() {
        FirebaseDatabase.getInstance(firebaseURL).getReference("orders")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        allOrdersList.clear();
                        orderPrices.clear();
                        orderStatusStrings.clear();
                        
                        // Định dạng ISO 8601 từ Firebase (UTC)
                        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                        isoFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
                        
                        for (DataSnapshot data : snapshot.getChildren()) {
                            String orderId = data.getKey();
                            Order order = new Order();
                            order.setOrderId(orderId);
                            
                            // 1. Xử lý ngày tháng (Linh hoạt nhiều định dạng)
                            Object dateVal = data.child("orderDate").getValue();
                            if (dateVal instanceof Long) {
                                order.setOrderDate(new Date((Long) dateVal));
                            } else if (dateVal instanceof String) {
                                String s = (String) dateVal;
                                try {
                                    if (s.contains("T")) {
                                        // Xử lý ISO: "2026-06-15T08:30:00Z" hoặc có millisecond
                                        String clean = s.split("\\.")[0].replace("Z", "");
                                        order.setOrderDate(isoFormat.parse(clean));
                                    } else if (s.contains(":")) {
                                        order.setOrderDate(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse(s));
                                    } else {
                                        order.setOrderDate(sdf.parse(s));
                                    }
                                } catch (Exception e) {
                                    android.util.Log.e("ECOrder", "Parse error for " + orderId + ": " + s);
                                }
                            }

                            // 2. Xử lý trạng thái (Ưu tiên field "status" mới)
                            String st = "Pending";
                            DataSnapshot stSnap = data.child("status");
                            if (!stSnap.exists()) stSnap = data.child("orderStatus");
                            if (stSnap.exists()) {
                                Object v = stSnap.getValue();
                                if (v instanceof String) st = (String) v;
                                else if (v instanceof Long) {
                                    int i = ((Long) v).intValue();
                                    if (i == 1) st = "Completed";
                                    else if (i == 2) st = "Processing";
                                    else if (i == 3) st = "Shipping";
                                }
                            }
                            orderStatusStrings.put(orderId, st);

                            // 3. Xử lý tổng tiền (Ưu tiên field "totalAmount" mới)
                            Double price = 0.0;
                            DataSnapshot prSnap = data.child("totalAmount");
                            if (!prSnap.exists()) prSnap = data.child("totalPrice");
                            if (!prSnap.exists()) prSnap = data.child("total");
                            if (!prSnap.exists()) prSnap = data.child("price");
                            if (prSnap.exists()) {
                                Object p = prSnap.getValue();
                                if (p instanceof Number) price = ((Number) p).doubleValue();
                                else if (p instanceof String) {
                                    try { price = Double.parseDouble((String) p); } catch (Exception e) {}
                                }
                            }
                            orderPrices.put(orderId, price);

                            allOrdersList.add(order);
                        }
                        applyFilter();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void applyFilter() {
        long fromTs = getStartOfDay(calFrom.getTime());
        long toTs = getEndOfDay(calTo.getTime());

        displayList.clear();
        for (Order o : allOrdersList) {
            if (o.getOrderDate() != null) {
                long orderTs = o.getOrderDate().getTime();
                if (orderTs >= fromTs && orderTs <= toTs) {
                    displayList.add(o);
                }
            }
            // Khi lọc, ta bỏ qua các đơn hàng không có ngày hợp lệ
        }
        adapter.notifyDataSetChanged();
        if (displayList.isEmpty() && !allOrdersList.isEmpty()) {
            Toast.makeText(this, "No orders found in this range", Toast.LENGTH_SHORT).show();
        }
    }

    private long getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    private long getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }
}
