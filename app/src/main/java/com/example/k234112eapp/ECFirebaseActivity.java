package com.example.k234112eapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ECFirebaseActivity extends AppCompatActivity {
    Button btnCategory, btnCustomer, btnEmployee, btnOrder, btnProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ecfirebase);
        addViews();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ECFirebaseActivity.this,ECCategoryActivity.class);
                startActivity(intent);
            }
        });
        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ECFirebaseActivity.this,ECCustomerActivity.class);
                startActivity(intent);
            }
        });
        btnEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ECFirebaseActivity.this,ECEmployeeActivity.class);
                startActivity(intent);
            }
        });
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ECFirebaseActivity.this,ECOrderActivity.class);
                startActivity(intent);
            }
        });
        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ECFirebaseActivity.this,ECProductActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addViews() {
        btnCategory = findViewById(R.id.btnCategory);
        btnCustomer = findViewById(R.id.btnCustomer);
        btnEmployee = findViewById(R.id.btnEmployee);
        btnOrder = findViewById(R.id.btnOrder);
        btnProduct = findViewById(R.id.btnProduct);
    }
}