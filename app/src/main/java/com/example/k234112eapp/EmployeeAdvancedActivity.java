package com.example.k234112eapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adapters.EmployeeAdapter;
import com.example.models.Department;
import com.example.models.Employee;

import java.util.ArrayList;

public class EmployeeAdvancedActivity extends AppCompatActivity {
    ListView lvEmployee;
    ArrayList<Employee> listOfEmployee;
    EmployeeAdapter adapterEmployee;
    Spinner spDepartment;
    ArrayList<Department> listOfDepartment;
    ArrayAdapter<Department> adapterDepartment;
    ImageView imgAddEmployee, imgEditEmployee, imgDeleteEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_advanced);
        addViews();
        sampleData();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                adapterEmployee.clear();
                if (i == 0) {
                    // Xem toàn bộ Employee của tất cả các Department
                    for (Department dept : listOfDepartment) {
                        adapterEmployee.addAll(dept.getListOfEmployee());
                    }
                } else {
                    // Hiển thị Employee của Department đã chọn
                    Department selectedDepartment = listOfDepartment.get(i);
                    adapterEmployee.addAll(selectedDepartment.getListOfEmployee());
                }
                adapterEmployee.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        imgAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy vị trí phòng ban đang được chọn trong Spinner
                int selectedPosition = spDepartment.getSelectedItemPosition();
                Department selectedDept = listOfDepartment.get(selectedPosition);

                // Kiểm tra nếu đang ở "All Departments" (vị trí 0 hoặc id là "all")
                if (selectedPosition == 0 || selectedDept.getId().equals("all")) {
                    // Yêu cầu chọn phòng ban cụ thể.
                    // Mẹo để không bị Hardcode: Bạn nên khai báo chuỗi này vào file res/values/strings.xml (ví dụ: R.string.msg_select_department)
                    android.widget.Toast.makeText(EmployeeAdvancedActivity.this,
                            getString(R.string.str_require_department),
                            android.widget.Toast.LENGTH_SHORT).show();
                } else {
                    // Nếu đã chọn phòng ban hợp lệ thì mới mở màn hình thêm
                    Intent intent = new Intent(EmployeeAdvancedActivity.this, AddEmployeeActivity.class);
                    startActivityForResult(intent, 512);
                }
            }
        });
    }

    private void sampleData() {
        listOfDepartment.add(new Department("all", "All Departments"));
        Department d1=new Department("d1","Administrative Department");
        Department d2=new Department("d2","Financial Department");
        Department d3=new Department("d3","Technology Department");
        Department d4=new Department("d4","Human Resources Department");
        Department d5=new Department("d5","Marketing Department");
        listOfDepartment.add(d1);
        listOfDepartment.add(d2);
        listOfDepartment.add(d3);
        listOfDepartment.add(d4);
        listOfDepartment.add(d5);
        adapterDepartment.notifyDataSetChanged();

        d1.addEmployee(new Employee("1", "Nguyễn Văn A", "0912679685"));
        d1.addEmployee(new Employee("2", "Nguyễn Văn B", "0912679685"));
        d1.addEmployee(new Employee("3", "Nguyễn Văn C", "0912679685"));
        d2.addEmployee(new Employee("4", "Nguyễn Văn D", "0912679685"));
        d2.addEmployee(new Employee("5", "Nguyễn Văn E", "0912679685"));
        d2.addEmployee(new Employee("6", "Nguyễn Văn F", "0912679685"));
        d3.addEmployee(new Employee("7", "Nguyễn Văn G", "0912679685"));
        d3.addEmployee(new Employee("8", "Nguyễn Văn H", "0912679685"));
        d3.addEmployee(new Employee("9", "Nguyễn Văn I", "0912679685"));
        d5.addEmployee(new Employee("10", "Nguyễn Văn K", "0912679685"));
        d5.addEmployee(new Employee("11", "Nguyễn Văn L", "0912679685"));
        d5.addEmployee(new Employee("12", "Nguyễn Văn M", "0912679685"));

        ArrayList<Employee> listOfEmp4=new ArrayList<>();
        listOfEmp4.add(new Employee("11", "Nguyễn Văn A", "0912679685"));
        listOfEmp4.add(new Employee("12", "Nguyễn Văn B", "0912679685"));
        listOfEmp4.add(new Employee("13", "Nguyễn Văn C", "0912679685"));
        listOfEmp4.add(new Employee("14", "Nguyễn Văn D", "0912679685"));
        listOfEmp4.add(new Employee("15", "Nguyễn Văn E", "0912679685"));
        listOfEmp4.add(new Employee("16", "Nguyễn Văn F", "0912679685"));
        d4.addListEmployee(listOfEmp4);

    }

    private void addViews() {
        lvEmployee = findViewById(R.id.lvEmployee);
        listOfEmployee = new ArrayList<>();
        adapterEmployee = new EmployeeAdapter(this, R.layout.item_custom_employee);
        listOfEmployee.add(new Employee("e1", "Tèo", "09134579685"));
        listOfEmployee.add(new Employee("e2", "Tí", "09134534385"));
        listOfEmployee.add(new Employee("e3", "Bin", "09134523485"));
        listOfEmployee.add(new Employee("e4", "Bo", "09789579685"));
        listOfEmployee.add(new Employee("e5", "Tin", "09267579685"));
        adapterEmployee.addAll(listOfEmployee);
        lvEmployee.setAdapter(adapterEmployee);
        adapterEmployee.notifyDataSetChanged();


        spDepartment = findViewById(R.id.spDepartment);
        listOfDepartment = new ArrayList<>();
        adapterDepartment = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                listOfDepartment);
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepartment.setAdapter(adapterDepartment);
        imgAddEmployee = findViewById(R.id.imgAddEmployee);
        imgEditEmployee = findViewById(R.id.imgEditEmployee);
        imgDeleteEmployee = findViewById(R.id.imgDeleteEmployee);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==512 && resultCode==125)
        {
            Employee emp= (Employee) data.getSerializableExtra("NEW_EMPLOYEE");
            // Lấy phòng ban ĐANG ĐƯỢC CHỌN trên Spinner thay vì hardcode index 4
            int selectedPosition = spDepartment.getSelectedItemPosition();
            Department selectedDept = listOfDepartment.get(selectedPosition);

            // Thêm nhân viên vào đúng phòng ban đó
            selectedDept.addEmployee(emp);

            // Cập nhật lại ListView để hiển thị ngay nhân viên mới thêm
            adapterEmployee.clear();
            adapterEmployee.addAll(selectedDept.getListOfEmployee());
            adapterEmployee.notifyDataSetChanged();
        }
    }
}