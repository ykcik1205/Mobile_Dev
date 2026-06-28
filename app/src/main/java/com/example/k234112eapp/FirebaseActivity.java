package com.example.k234112eapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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

public class FirebaseActivity extends AppCompatActivity {
    ListView lvContact;
    ArrayAdapter<String> contactAdapter;
    String TAG="FIREBASE";
    Button btnInsertContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_firebase);
        addViews();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LinearLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        btnInsertContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FirebaseActivity.this,InsertContactActivity.class);
                startActivity(intent);
            }
        });
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data=contactAdapter.getItem(position);
                String key=data.split("\n")[0];
                Intent intent=new Intent(FirebaseActivity.this,DetailContactActivity.class);
                intent.putExtra("KEY",key);
                startActivity(intent);
            }
        });
    }

    private void addViews() {
        btnInsertContact = findViewById(R.id.btnInsertContact);
        lvContact=findViewById(R.id.lvContact);
        contactAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        lvContact.setAdapter(contactAdapter);
        loadData();
    }

    private void loadData() {
        // Cần truyền URL của Database vì server đặt tại Asia-Southeast1
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://k234112eapp-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("contacts");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactAdapter.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    String key=data.getKey();
                    Object valueObj = data.getValue();
                    String value = (valueObj != null) ? valueObj.toString() : "";
                    contactAdapter.add(key+"\n"+value);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

}