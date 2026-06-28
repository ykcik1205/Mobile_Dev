package com.example.k234112eapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class EmployeeManagementActivity extends AppCompatActivity {

    Button btnExit;
    ListView lvEmployee;
    ArrayList<String>listEmployee;
    ArrayAdapter<String>adapterEmployee;
    EditText edtID,edtName,edtPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_management);
        addViews();
        addEvents();
        loadData();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LinearLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadData() {
        listEmployee.add("e1-Tèo-09134579685");
        listEmployee.add("e2-Tí-09134534385");
        listEmployee.add("e3-Bin-09134523485");
        listEmployee.add("e4-Bo-09789579685");
        listEmployee.add("e5-Tin-09267579685");
        adapterEmployee.notifyDataSetChanged();
    }

    private void addEvents() {
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processExit();
            }
        });

        lvEmployee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayEmployeeInfor(position);
            }
        });
    }

    private void displayEmployeeInfor(int position) {
        String data=listEmployee.get(position);
        String[]items=data.split("-");
        //hiển thị items[0]-->id, items[2]-->name, ietms[3]-->phone
        edtID.setText(items[0]);
        edtName.setText(items[1]);
        edtPhone.setText(items[2]);

    }

    private void processExit() {
        Dialog custom=new Dialog(this);
        custom.setContentView(R.layout.custom_dialog);
        ImageView imgSave=custom.findViewById(R.id.imgYes);
        ImageView imgCancel=custom.findViewById(R.id.imgCancel);
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom.dismiss();
            }
        });
        custom.show();
    }

    private void addViews() {
        btnExit=findViewById(R.id.btnExit);
        lvEmployee=findViewById(R.id.lvEmployee);
        listEmployee=new ArrayList<>();
        adapterEmployee=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listEmployee);
        lvEmployee.setAdapter(adapterEmployee);
        edtID=findViewById(R.id.edtID);
        edtName=findViewById(R.id.edtName);
        edtPhone=findViewById(R.id.edtPhone);
    }

    public void saveEmployee(View view) {
        //1. Lấy dữ liệu từ EditText
        String id = edtID.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();

        // Kiểm tra rỗng (không hardcode string thông báo)
        if (id.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, R.string.str_empty_error, Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Tạo chuỗi dữ liệu mới theo định dạng id-name-phone
        String newData = id + "-" + name + "-" + phone;

        // 3. Kiểm tra xem ID đã tồn tại trong danh sách chưa
        int position = -1;
        for (int i = 0; i < listEmployee.size(); i++) {
            String item = listEmployee.get(i);
            // Tách ID cũ để so sánh chính xác
            if (item.startsWith(id + "-")) {
                position = i;
                break;
            }
        }

        if (position == -1) {
            // Trường hợp: THÊM MỚI
            listEmployee.add(newData);
            Toast.makeText(this, R.string.str_save_success, Toast.LENGTH_SHORT).show();
        } else {
            // Trường hợp: CẬP NHẬT
            listEmployee.set(position, newData);
            Toast.makeText(this, R.string.str_update_success, Toast.LENGTH_SHORT).show();
        }

        // 4. Cập nhật giao diện ListView
        adapterEmployee.notifyDataSetChanged();

        // 5. Xóa trống các ô nhập liệu và cho con trỏ về ô ID để nhập tiếp (tùy chọn)
        edtID.setText("");
        edtName.setText("");
        edtPhone.setText("");
        edtID.requestFocus();
    }
    public void removeEmployee(View view) {
        // 1. Lấy ID từ EditText để xác định nhân viên cần xóa
        String id = edtID.getText().toString().trim();

        // 2. Nếu ID trống, thông báo yêu cầu chọn nhân viên
        if (id.isEmpty()) {
            Toast.makeText(this, R.string.str_select_to_delete, Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Tìm vị trí của nhân viên trong danh sách
        int position = -1;
        for (int i = 0; i < listEmployee.size(); i++) {
            if (listEmployee.get(i).startsWith(id + "-")) {
                position = i;
                break;
            }
        }
        // 4. Nếu tìm thấy nhân viên, hiển thị AlertDialog xác nhận xóa
        if (position != -1) {
            final int finalPosition = position;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.str_confirm_delete_title);
            builder.setMessage(R.string.str_confirm_delete_msg);

            // Nút "Có" (Đồng ý xóa)
            builder.setPositiveButton(R.string.str_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Xóa khỏi list và cập nhật giao diện
                    listEmployee.remove(finalPosition);
                    adapterEmployee.notifyDataSetChanged();

                    // Xóa trống các ô nhập liệu
                    edtID.setText("");
                    edtName.setText("");
                    edtPhone.setText("");

                    Toast.makeText(EmployeeManagementActivity.this,
                            R.string.str_delete_success, Toast.LENGTH_SHORT).show();
                }
            });
            // Nút "Không" (Hủy bỏ)
            builder.setNegativeButton(R.string.str_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            // Hiển thị dialog
            builder.show();
        } else {
            // Trường hợp ID nhập vào không khớp với nhân viên nào trong list
            Toast.makeText(this, R.string.str_select_to_delete, Toast.LENGTH_SHORT).show();
        }
    }
}