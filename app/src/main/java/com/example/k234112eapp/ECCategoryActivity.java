package com.example.k234112eapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ECCategoryActivity extends AppCompatActivity {
    ListView lvCategory;
    ArrayList<Category> categoryList = new ArrayList<>();
    ArrayAdapter<Category> categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eccategory);
        addViews();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
    }

    private void addViews() {
        lvCategory = findViewById(R.id.lvCategory);
        
        categoryAdapter = new ArrayAdapter<Category>(this, R.layout.category_view, categoryList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_view, parent, false);
                }
                
                Category c = getItem(position);
                if (c != null) {
                    TextView txtcateId = convertView.findViewById(R.id.txtcateId);
                    TextView txtcateName = convertView.findViewById(R.id.txtcateName);
                    TextView txtcateDesc = convertView.findViewById(R.id.txtcateDesc);
                    
                    txtcateId.setText("ID: " + c.getCateId());
                    txtcateName.setText(c.getCateName());
                    txtcateDesc.setText(c.getCateDesc());
                }
                return convertView;
            }
        };
        
        lvCategory.setAdapter(categoryAdapter);
        loadData();
    }

    private void loadData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://e-commerce-f17c0-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("categories");
        
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    // Trích xuất thủ công để đảm bảo không bị null do mismatch field name
                    String id = data.child("cateId").getValue(String.class);
                    if (id == null) id = data.getKey();
                    
                    String name = data.child("cateName").getValue(String.class);
                    if (name == null) name = data.child("name").getValue(String.class);
                    
                    String desc = data.child("cateDesc").getValue(String.class);
                    if (desc == null) desc = data.child("description").getValue(String.class);
                    
                    Category c = new Category(id, name, desc);
                    categoryList.add(c);
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}
