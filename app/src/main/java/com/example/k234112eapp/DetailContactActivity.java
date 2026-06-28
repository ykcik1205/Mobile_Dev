package com.example.k234112eapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DetailContactActivity extends AppCompatActivity {
    EditText edtContactId, edtName, edtEmail, edtPhone;
    Button btnBack, btnUpdate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_contact);
        
        addViews();
        addEvents();
        getContactDetail(); // Phải gọi hàm này để load dữ liệu khi mở màn hình
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        edtContactId = findViewById(R.id.edtContactId);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        btnBack = findViewById(R.id.btnBack);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
    }

    private void addEvents() {
        // Xử lý nút Back để quay lại màn hình trước
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processUpdateContact(view);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processDeleteContact(view);
            }
        });
    }

    private void getContactDetail() {
        Intent intent = getIntent();
        final String key = intent.getStringExtra("KEY");
        if (key == null) return;

        // Thêm URL của Database vì server đặt tại khu vực Đông Nam Á
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://k234112eapp-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("contacts");
        
        myRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    if (hashMap != null) {
                        edtContactId.setText(key);
                        if (hashMap.containsKey("name")) edtName.setText(hashMap.get("name").toString());
                        if (hashMap.containsKey("email")) edtEmail.setText(hashMap.get("email").toString());
                        if (hashMap.containsKey("phone")) edtPhone.setText(hashMap.get("phone").toString());
                    }
                } catch (Exception ex) {
                    Log.e("MY_ERROR", ex.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("MY_ERROR", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public void processUpdateContact(View view) {
        String key = edtContactId.getText().toString();
        String phone = edtPhone.getText().toString();
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://k234112eapp-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("contacts");
        
        myRef.child(key).child("phone").setValue(phone);
        myRef.child(key).child("email").setValue(email);
        myRef.child(key).child("name").setValue(name);
        finish();
    }

    public void processDeleteContact(View view) {
        String key = edtContactId.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://k234112eapp-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("contacts");

        myRef.child(key).removeValue();
        finish();
    }
}
