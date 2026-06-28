package com.example.k234112eapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertContactActivity extends AppCompatActivity {
    EditText edtContactId,edtName,edtPhone,edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insert_contact);
        addViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LinearLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        edtContactId=findViewById(R.id.edtContactId);
        edtName=findViewById(R.id.edtName);
        edtPhone=findViewById(R.id.edtPhone);
        edtEmail=findViewById(R.id.edtEmail);
    }
    public void processInsertContact(View view) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://k234112eapp-default-rtdb.asia-southeast1.firebasedatabase.app");
            DatabaseReference myRef = database.getReference("contacts");
            String contactId=edtContactId.getText().toString();
            String name = edtName.getText().toString();
            String phone = edtPhone.getText().toString();
            String email = edtEmail.getText().toString();
            myRef.child(contactId).child("phone").setValue(phone);
            myRef.child(contactId).child("email").setValue(email);
            myRef.child(contactId).child("name").setValue(name);
            finish();
        }
        catch (Exception ex) {
            Toast.makeText(this,"Error:"+ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
}