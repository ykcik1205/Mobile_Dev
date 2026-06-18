package com.example.k234112eapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dals.MyContactDAO;
import com.example.models.MyContact;

import java.util.ArrayList;

public class MyContactActivity extends AppCompatActivity {
    ListView lvMyContact;
    ArrayList<MyContact> contacts;
    ArrayAdapter<MyContact> adapterMyContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_contact);
        addViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        lvMyContact=findViewById(R.id.lvMyContact);
        contacts= MyContactDAO.getContacts(this);
        adapterMyContact=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,contacts);
        lvMyContact.setAdapter(adapterMyContact);
    }
}