package com.example.k234112eapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.Employee;


import java.util.ArrayList;

public class AddEmployeeActivity extends AppCompatActivity {

    EditText edtID,edtName,edtPhone;
    AutoCompleteTextView actBirthplace;
    ArrayList<String> listBirthplace;
    ArrayAdapter<String> adapterBirthplace;
    ImageView imgSave,imgCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_employee);
        addViews();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LinearLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processAddNewEmployee();
            }
        });
    }

    private void processAddNewEmployee() {
        String id=edtID.getText().toString();
        String name=edtName.getText().toString();
        String phone=edtPhone.getText().toString();
        String birthplace=actBirthplace.getText().toString();

        Employee emp=new Employee(id,name,phone,birthplace);
        //step1: get intent
        Intent intent=getIntent();
        //step2: set data
        intent.putExtra("NEW_EMPLOYEE",emp);
        setResult(125,intent);
        finish();
    }

    private void addViews() {
        edtID=findViewById(R.id.edtID);
        edtName=findViewById(R.id.edtName);
        edtPhone=findViewById(R.id.edtPhone);
        actBirthplace=findViewById(R.id.actBirthplace);
        imgSave=findViewById(R.id.imgSave);
        imgCancel=findViewById(R.id.imgCancel);

        String []arrBirthplace=getResources().getStringArray(R.array.array_birthplace);
        adapterBirthplace=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                arrBirthplace);
        actBirthplace.setAdapter(adapterBirthplace);
    }
}