package com.example.k234112eapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adapters.CategoryAdapter;
import com.example.dals.CategoryDAO;
import com.example.models.Category;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    ListView lvCategory;
    ArrayList<Category> categories;
    CategoryAdapter adapterCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);
        addViews();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        lvCategory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
                processRemoveCategory(i);
                return false;
            }
        });
    }
    private void processRemoveCategory(int i) {
        Category category = categories.get(i);
        long result=CategoryDAO.removeCategory(this,category);
        if (result>0)
        {
            categories = CategoryDAO.getCategories(this);
            //Xoá data trên apdater đi
            adapterCategory.clear();
            //Thêm mới lại dữ liệu mới hoàn toàn
            adapterCategory.addAll(categories);
            //Thông báo cho apdater để cập nhật lại giao diện
            adapterCategory.notifyDataSetChanged();
        }
    }


    private void addViews() {
        lvCategory = findViewById(R.id.lvCategory);
        categories = CategoryDAO.getCategories(this);
        adapterCategory = new CategoryAdapter(this, R.layout.category_view);
        adapterCategory.addAll(categories);
        lvCategory.setAdapter(adapterCategory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.mnu_category_new)
        {
            //openCategory new activity
            Intent intent = new Intent(this, CategoryNewActivity.class);
            startActivityForResult(intent, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==2)
        {
            //press cancel..nothing to do
        }
        else if(requestCode==1 && resultCode==3)
        {
            categories = CategoryDAO.getCategories(this);
            adapterCategory.clear();
            adapterCategory.addAll(categories);
            adapterCategory.notifyDataSetChanged();
        }
    }
}