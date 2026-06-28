package com.example.k234112eapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import com.example.models.Customer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ECCustomerActivity extends AppCompatActivity {

    ListView lvCustomer;
    ArrayList<Customer> customerList = new ArrayList<>();
    ArrayAdapter<Customer> customerAdapter;

    String firebaseURL = "https://e-commerce-f17c0-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eccustomer);
        
        addViews();
        loadDataFromFirebase();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        lvCustomer = findViewById(R.id.lvCustomer);
        customerAdapter = new ArrayAdapter<Customer>(this, R.layout.ec_customer_view, customerList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.ec_customer_view, parent, false);
                }

                Customer customer = getItem(position);
                if (customer != null) {
                    TextView txtID = convertView.findViewById(R.id.txtCustomerID);
                    TextView txtName = convertView.findViewById(R.id.txtCustomerName);
                    TextView txtEmail = convertView.findViewById(R.id.txtCustomerEmail);
                    TextView txtPhone = convertView.findViewById(R.id.txtCustomerPhone);
                    TextView txtAddress = convertView.findViewById(R.id.txtCustomerAddress);

                    txtID.setText("ID: " + customer.getCustomerId());
                    txtName.setText(customer.getCustomerName());
                    txtEmail.setText(customer.getEmail());
                    txtPhone.setText(customer.getPhone());
                    txtAddress.setText(customer.getAddress());
                }
                return convertView;
            }
        };
        lvCustomer.setAdapter(customerAdapter);
    }

    private void loadDataFromFirebase() {
        FirebaseDatabase.getInstance(firebaseURL).getReference("customers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        customerList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Customer customer = data.getValue(Customer.class);
                            if (customer != null) {
                                // Nếu ID chưa có trong data, lấy từ key của Firebase
                                if (customer.getCustomerId() == null) {
                                    customer.setCustomerId(data.getKey());
                                }
                                customerList.add(customer);
                            }
                        }
                        customerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ECCustomerActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
