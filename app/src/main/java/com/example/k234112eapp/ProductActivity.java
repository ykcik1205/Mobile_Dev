package com.example.k234112eapp;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adapters.ProductAdapter;
import com.example.dals.ProductDAO;
import com.example.models.Product;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    ListView lvProduct;
    ArrayList<Product> products;
    ProductAdapter adapterProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        addViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LinearLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        lvProduct = findViewById(R.id.lvProduct);
        products = ProductDAO.getProducts(this);
        if (products != null && !products.isEmpty()) {
            adapterProduct = new ProductAdapter(this, R.layout.product_view);
            adapterProduct.addAll(products);
            lvProduct.setAdapter(adapterProduct);
        } else {
            Toast.makeText(this, "No products found!", Toast.LENGTH_SHORT).show();
        }
    }
}