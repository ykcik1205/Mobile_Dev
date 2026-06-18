package com.example.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.k234112eapp.R;
import com.example.models.DataWareHouse;
import com.example.models.Order;
import com.example.models.OrderStatus;

import java.text.SimpleDateFormat;
import java.util.List;

public class OrderAdapter extends ArrayAdapter<Order> {
    Activity context;
    int resource;
    List<Order> objects;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public OrderAdapter(@NonNull Activity context, int resource, @NonNull List<Order> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View item = inflater.inflate(this.resource, null);

        TextView txtID = item.findViewById(R.id.txtID);
        TextView txtDate = item.findViewById(R.id.txtDate);
        TextView txtPrice = item.findViewById(R.id.txtPrice);
        TextView txtStatus = item.findViewById(R.id.txtStatus);

        Order order = this.objects.get(position);
        txtID.setText(order.getOrderId());
        txtDate.setText(sdf.format(order.getOrderDate()));
        txtPrice.setText(String.format("%,.0f VNĐ", DataWareHouse.sumOfMoney(order)));

        // Hiển thị trạng thái bằng TextView
        if (order.getOrderStatus() != null) {
            txtStatus.setText(order.getOrderStatus().toString());
            setStatusStyle(txtStatus, order.getOrderStatus());
        }

        return item;
    }

    private void setStatusStyle(TextView txtStatus, OrderStatus status) {
        switch (status) {
            case COMPLETED:
                txtStatus.setBackgroundColor(Color.parseColor("#4CAF50")); // Green
                break;
            case NOT_PAYMENT:
                txtStatus.setBackgroundColor(Color.parseColor("#F44336")); // Red
                break;
            case ON_LOGISTIC:
                txtStatus.setBackgroundColor(Color.parseColor("#2196F3")); // Blue
                break;
            case COMPLAIN:
                txtStatus.setBackgroundColor(Color.parseColor("#FF9800")); // Orange
                break;
            default:
                txtStatus.setBackgroundColor(Color.parseColor("#9E9E9E")); // Gray
                break;
        }
    }
}
