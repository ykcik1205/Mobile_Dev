package com.example.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.k234112eapp.R;
import com.example.models.Product;

public class ProductAdapter extends ArrayAdapter<Product> {
    Activity context;
    int resource;
    public ProductAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View custom = inflater.inflate(resource, null);
        Product p = getItem(position);

        TextView txtProductID = custom.findViewById(R.id.txtProductID);
        TextView txtProductName = custom.findViewById(R.id.txtProductName);
        TextView txtPrice = custom.findViewById(R.id.txtPrice);
        TextView txtQuantity = custom.findViewById(R.id.txtQuantity);
        if (p!=null) {
            txtProductID.setText(p.getProductId());
            txtProductName.setText(p.getProductName());
            txtPrice.setText(String.format("%,.0f VNĐ", p.getPrice()));
            txtQuantity.setText("Quantity: " + p.getQuantity());
        }
        return custom;
    }
}
