package com.example.k234112eapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ECEmployeeActivity extends AppCompatActivity {

    Spinner spDepartment;
    ListView lvEmployee;
    ArrayList<Employee> employeeList = new ArrayList<>();
    ArrayAdapter<Employee> employeeAdapter;
    
    String firebaseURL = "https://e-commerce-f17c0-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ecemployee);
        
        addViews();
        setupSpinner();
        addEvents();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LinearLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        spDepartment = findViewById(R.id.spDepartment);
        lvEmployee = findViewById(R.id.lvEmployee);
        
        employeeAdapter = new ArrayAdapter<Employee>(this, R.layout.ec_employee_view, employeeList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.ec_employee_view, parent, false);
                }
                
                Employee emp = getItem(position);
                if (emp != null) {
                    TextView txtID = convertView.findViewById(R.id.txtEmployeeID);
                    TextView txtName = convertView.findViewById(R.id.txtEmployeeName);
                    TextView txtDept = convertView.findViewById(R.id.txtEmployeeDept);
                    TextView txtPos = convertView.findViewById(R.id.txtEmployeePos);
                    TextView txtEmail = convertView.findViewById(R.id.txtEmployeeEmail);

                    txtID.setText(emp.getId());
                    txtName.setText(emp.getName());
                    txtEmail.setText(emp.getPhone()); // Đã map email vào trường phone

                    String bp = emp.getBirthplace(); // Đã map Dept|Pos vào birthplace
                    if (bp != null && bp.contains("|")) {
                        String[] parts = bp.split("\\|");
                        txtDept.setText(parts[0]);
                        txtPos.setText(parts.length > 1 ? parts[1] : "");
                    } else {
                        txtDept.setText(bp != null ? bp : "N/A");
                        txtPos.setText("");
                    }

                    // Thêm sự kiện click để thực hiện hành động nếu cần (như cũ)
                    convertView.setOnClickListener(v -> {
                        // Ví dụ: Mở trình quay số nếu cần, hoặc làm gì đó khác
                    });
                }
                return convertView;
            }
        };
        lvEmployee.setAdapter(employeeAdapter);
    }

    private void setupSpinner() {
        // Thêm "All Departments" vào đầu danh sách
        String[] departments = {"All Departments", "Sales Team A", "Sales Management", "Sales Team B", "Customer Support"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepartment.setAdapter(adapter);
        // Mặc định chọn All Departments
        spDepartment.setSelection(0);
    }

    private void addEvents() {
        spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDept = parent.getItemAtPosition(position).toString();
                if (selectedDept.equals("All Departments")) {
                    loadAllEmployees();
                } else {
                    loadEmployeesByDepartment(selectedDept);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadAllEmployees() {
        FirebaseDatabase.getInstance(firebaseURL).getReference("employees")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        employeeList.clear();
                        processSnapshotRecursive(snapshot, null);
                        employeeAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void loadEmployeesByDepartment(String department) {
        FirebaseDatabase.getInstance(firebaseURL).getReference("employees")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        employeeList.clear();
                        processSnapshotRecursive(snapshot, department);
                        employeeAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void processSnapshotRecursive(DataSnapshot snapshot, String targetDept) {
        for (DataSnapshot child : snapshot.getChildren()) {
            boolean isEmployee = child.hasChild("fullName") || child.hasChild("name") || 
                               child.hasChild("email") || child.hasChild("phone");
            
            if (isEmployee) {
                if (targetDept == null) {
                    addEmployeeFromSnapshot(child);
                } else {
                    String empDept = child.child("department").getValue(String.class);
                    // Check if the department field matches OR if the parent node name matches the department
                    if (targetDept.equalsIgnoreCase(empDept) || (snapshot.getKey() != null && snapshot.getKey().equalsIgnoreCase(targetDept))) {
                        addEmployeeFromSnapshot(child);
                    }
                }
            } else {
                // If it's a department node (like employees/Sales Team A/)
                if (targetDept != null && child.getKey() != null && child.getKey().equalsIgnoreCase(targetDept)) {
                    // Match found, add all employees under this node
                    for (DataSnapshot emp : child.getChildren()) {
                        addEmployeeFromSnapshot(emp);
                    }
                } else {
                    // Continue searching deeper (recursive traversal)
                    processSnapshotRecursive(child, targetDept);
                }
            }
        }
    }

    private void addEmployeeFromSnapshot(DataSnapshot snapshot) {
        try {
            // Lấy ID
            String id = snapshot.child("id").getValue(String.class);
            if (id == null) id = snapshot.getKey();
            
            // Lấy Tên (ưu tiên fullName từ firebase mới)
            String name = snapshot.child("fullName").getValue(String.class);
            if (name == null) name = snapshot.child("name").getValue(String.class);
            if (name == null) name = "Unknown Employee";
            
            // Lấy Email/Phone (ưu tiên email cho view mới)
            String email = snapshot.child("email").getValue(String.class);
            if (email == null) email = snapshot.child("phone").getValue(String.class);
            if (email == null) email = "No Contact";

            // Lấy Dept và Position
            String dept = snapshot.child("department").getValue(String.class);
            String pos = snapshot.child("position").getValue(String.class);
            
            // Ép dữ liệu vào model Employee hiện có (id, name, phone, birthPlace)
            Employee emp = new Employee();
            emp.setId(id);
            emp.setName(name);
            emp.setPhone(email); // Gán email vào phone để hiển thị
            emp.setBirthplace((dept != null ? dept : "N/A") + "|" + (pos != null ? pos : "N/A"));

            employeeList.add(emp);
        } catch (Exception e) {
            android.util.Log.e("ECEmployee", "Error parsing employee: " + e.getMessage());
        }
    }
}
