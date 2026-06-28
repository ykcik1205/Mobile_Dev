package com.example.k234112eapp;

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

import com.bumptech.glide.Glide;
import com.example.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class ECProductActivity extends AppCompatActivity {

    ListView lvProduct;
    ArrayList<Product> productList = new ArrayList<>();
    ArrayAdapter<Product> productAdapter;
    
    // Lưu URL ảnh từ Firebase vì Model Product không có trường image
    java.util.HashMap<String, String> productImages = new java.util.HashMap<>();

    String firebaseURL = "https://e-commerce-f17c0-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ecproduct);
        
        addViews();
        loadDataFromFirebase();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        lvProduct = findViewById(R.id.lvProduct);
        productAdapter = new ArrayAdapter<Product>(this, R.layout.ec_product_view, productList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.ec_product_view, parent, false);
                }

                Product p = getItem(position);
                if (p != null) {
                    ImageView imgProduct = convertView.findViewById(R.id.imgProduct);
                    TextView txtProductID = convertView.findViewById(R.id.txtProductID);
                    TextView txtProductName = convertView.findViewById(R.id.txtProductName);
                    TextView txtProductPrice = convertView.findViewById(R.id.txtProductPrice);
                    TextView txtProductQuantity = convertView.findViewById(R.id.txtProductQuantity);

                    txtProductID.setText("ID: " + p.getProductId());
                    txtProductName.setText(p.getProductName());
                    txtProductPrice.setText(String.format(Locale.getDefault(), "Price: %,.0f VNĐ", p.getPrice()));
                    txtProductQuantity.setText("In stock: " + p.getQuantity());

                    // Load ảnh bằng Glide từ Map đã lưu
                    String imgUrl = productImages.get(p.getProductId());
                    if (imgUrl != null && !imgUrl.isEmpty()) {
                        Glide.with(ECProductActivity.this)
                                .load(imgUrl)
                                .placeholder(android.R.drawable.ic_menu_report_image)
                                .error(android.R.drawable.ic_menu_report_image)
                                .fitCenter()
                                .into(imgProduct);
                    } else {
                        imgProduct.setImageResource(android.R.drawable.ic_menu_report_image);
                    }
                }
                return convertView;
            }
        };
        lvProduct.setAdapter(productAdapter);
    }

    private void loadDataFromFirebase() {
        FirebaseDatabase.getInstance(firebaseURL).getReference("products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productList.clear();
                        productImages.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            // Trích xuất thủ công các trường quan trọng để đảm bảo tính chính xác
                            String id = data.child("productId").getValue(String.class);
                            if (id == null) id = data.getKey();

                            String name = data.child("productName").getValue(String.class);
                            if (name == null) name = data.child("name").getValue(String.class);

                            Double price = 0.0;
                            Object pVal = data.child("price").getValue();
                            if (pVal instanceof Long) price = ((Long) pVal).doubleValue();
                            else if (pVal instanceof Double) price = (Double) pVal;

                            Integer quantity = 0;
                            DataSnapshot qSnap = data.child("quantity");
                            if (!qSnap.exists()) qSnap = data.child("stock");
                            if (!qSnap.exists()) qSnap = data.child("qty");
                            
                            if (qSnap.exists()) {
                                Object qVal = qSnap.getValue();
                                if (qVal instanceof Long) quantity = ((Long) qVal).intValue();
                                else if (qVal instanceof String) {
                                    try { quantity = Integer.parseInt((String) qVal); } catch (Exception e) {}
                                } else if (qVal instanceof Double) quantity = ((Double) qVal).intValue();
                            }

                            // Trích xuất ảnh linh hoạt hơn bằng cách duyệt qua các trường
                            String imgUrl = null;
                            for (DataSnapshot child : data.getChildren()) {
                                String key = child.getKey();
                                if (key != null) {
                                    String lowerKey = key.toLowerCase();
                                    if (lowerKey.contains("image") || lowerKey.contains("img") || lowerKey.contains("thumb") || lowerKey.contains("url") || lowerKey.contains("photo")) {
                                        Object val = child.getValue();
                                        if (val instanceof String && ((String) val).startsWith("http")) {
                                            imgUrl = (String) val;
                                            break;
                                        }
                                    }
                                }
                            }

                            Product p = new Product();
                            p.setProductId(id);
                            p.setProductName(name);
                            p.setPrice(price);
                            p.setQuantity(quantity);

                            if (imgUrl != null) {
                                productImages.put(id, imgUrl);
                            } else {
                                // Log để debug nếu cần
                                android.util.Log.d("ECProduct", "No image found for product: " + id);
                            }
                            
                            productList.add(p);
                        }
                        productAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ECProductActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
