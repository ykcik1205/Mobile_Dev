package com.example.k234112eapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adapters.OrderAdapter;
import com.example.models.DataWareHouse;
import com.example.models.Order;
import com.example.models.OrderStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OrderManagementActivity extends AppCompatActivity {
    ImageView imgFromDate, imgToDate, imgClearFilter, imgFilter;
    ListView lvOrder;
    TextView txtFromDate, txtToDate;
    ArrayList<Order> orders;
    OrderAdapter orderAdapter;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Calendar calFromDate = Calendar.getInstance();
    Calendar calToDate = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateFromListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            calFromDate.set(Calendar.YEAR, i);
            calFromDate.set(Calendar.MONTH, i1);
            calFromDate.set(Calendar.DAY_OF_MONTH, i2);
            txtFromDate.setText(sdf.format(calFromDate.getTime()));
        }
    };
    DatePickerDialog.OnDateSetListener dateToListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            calToDate.set(Calendar.YEAR, i);
            calToDate.set(Calendar.MONTH, i1);
            calToDate.set(Calendar.DAY_OF_MONTH, i2);
            txtToDate.setText(sdf.format(calToDate.getTime()));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_management);
        addViews();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LinearLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        imgFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFromDate();
            }
        });
        imgToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectToDate();
            }
        });
        imgClearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders=DataWareHouse.getOrders();
                orderAdapter.clear();
                orderAdapter.addAll(orders);
                orderAdapter.notifyDataSetChanged();
            }
        });
        imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date fromDate = calFromDate.getTime();
                Date toDate = calToDate.getTime();
                orders=DataWareHouse.filterOrdersByDate(fromDate,toDate);
                orderAdapter.clear();
                orderAdapter.addAll(orders);
                orderAdapter.notifyDataSetChanged();
            }
        });
    }

    private void selectToDate() {
        DatePickerDialog picker = new DatePickerDialog(
                this, dateToListener,
                calToDate.get(Calendar.YEAR),
                calToDate.get(Calendar.MONTH),
                calToDate.get(Calendar.DAY_OF_MONTH)
        );
        picker.show();
    }

    private void selectFromDate() {
        DatePickerDialog picker = new DatePickerDialog(
                this, dateFromListener,
                calFromDate.get(Calendar.YEAR),
                calFromDate.get(Calendar.MONTH),
                calFromDate.get(Calendar.DAY_OF_MONTH)
        );
        picker.show();
    }

    private void addViews() {
        txtFromDate = findViewById(R.id.txtFromDate);
        txtToDate = findViewById(R.id.txtToDate);
        imgFromDate = findViewById(R.id.imgFromDate);
        imgToDate = findViewById(R.id.imgToDate);
        imgClearFilter = findViewById(R.id.imgClearFilter);
        imgFilter = findViewById(R.id.imgFilter);

        lvOrder = findViewById(R.id.lvOrder);
        orders = DataWareHouse.getOrders();
        orderAdapter = new OrderAdapter(this, R.layout.order_custom_item, orders);
        lvOrder.setAdapter(orderAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_status, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.order_status_all) {
            orders = DataWareHouse.getOrders();
        } else if (item.getItemId() == R.id.order_status_completed) {
            orders = DataWareHouse.filterOrdersByStatus(OrderStatus.COMPLETED);
        } else if (item.getItemId() == R.id.order_status_unpay) {
            orders = DataWareHouse.filterOrdersByStatus(OrderStatus.NOT_PAYMENT);
        } else if (item.getItemId() == R.id.order_status_log) {
            orders = DataWareHouse.filterOrdersByStatus(OrderStatus.ON_LOGISTIC);
        } else if (item.getItemId() == R.id.order_status_complain) {
            orders = DataWareHouse.filterOrdersByStatus(OrderStatus.COMPLAIN);
        }

        orderAdapter.clear();
        orderAdapter.addAll(orders);
        orderAdapter.notifyDataSetChanged();

        return super.onOptionsItemSelected(item);
    }
}