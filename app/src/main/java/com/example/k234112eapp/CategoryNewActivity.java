package com.example.k234112eapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dals.CategoryDAO;
import com.example.models.Category;

public class CategoryNewActivity extends AppCompatActivity {
    EditText edtCategoryId, edtCategoryName, edtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_new);
        addViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LinearLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        edtCategoryId = findViewById(R.id.edtCategoryId);
        edtCategoryName = findViewById(R.id.edtCategoryName);
        edtDescription = findViewById(R.id.edtDescription);
    }

    public void processSaveCategory(View view) {
        String cateId=edtCategoryId.getText().toString();
        String cateName=edtCategoryName.getText().toString();
        String cateDesc=edtDescription.getText().toString();

        Category category = new Category(cateId,cateName,cateDesc);
        long result = CategoryDAO.saveCategory(this, category);
        if(result>0)
        {
            Intent intent = getIntent();
            //assume 3 is saved OK
            setResult(3, intent);
            finish();
        }
        else
        {
            finish();
        }
    }
    public void processCancel(View view) {
        Intent intent=new Intent(this, CategoryActivity.class);
    }
}