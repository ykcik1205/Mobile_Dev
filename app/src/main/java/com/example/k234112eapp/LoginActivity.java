package com.example.k234112eapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    /*
    Declare all variables for interactive views
    **/
    EditText edtUserName;
    EditText edtPassword;
    TextView txtMessage;
    CheckBox chkSaveLogin;
    String name_share_pref="LoginInfo";
    RadioButton radAdmin,radEmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        addViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        edtUserName=findViewById(R.id.edtUserName);
        edtPassword=findViewById(R.id.edtPassword);
        txtMessage=findViewById(R.id.txtMessage);
        chkSaveLogin=findViewById(R.id.chkSaveLogin);
        radAdmin=findViewById(R.id.radAdmin);
        radEmp=findViewById(R.id.radEmp);
    }

    public void loginsystem(View view) {
        String username=edtUserName.getText().toString();
        String password=edtPassword.getText().toString();
        if(username.equalsIgnoreCase("admin") && password.equals("123")) {
            if (radAdmin.isChecked() || radEmp.isChecked()) {
                boolean saved = chkSaveLogin.isChecked();
                SharedPreferences preferences = getSharedPreferences(name_share_pref, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Username", username);
                editor.putString("Password", password);
                editor.putBoolean("Saved", saved);
                editor.commit();

                txtMessage.setText(getString(R.string.str_login_success));
                // Kiểm tra xem RadioButton nào đang được tick
                if (radAdmin.isChecked()) {
                    // Nếu chọn Admin -> Chuyển qua giao diện quản lý nhân viên
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (radEmp.isChecked()) {
                    // Nếu chọn Employee -> Chuyển qua giao diện chính
                    Intent intent = new Intent(LoginActivity.this, EmployeeManagementActivity.class);
                    startActivity(intent);
                } else {
                    // Bắt lỗi: Lỡ người dùng quên chưa tick vô ô nào mà lo bấm Login
                    Toast.makeText(LoginActivity.this, getString(R.string.str_select_role), Toast.LENGTH_SHORT).show();
                    txtMessage.setText("");
                }
            } else {
                txtMessage.setText(getString(R.string.str_login_failed));
            }
        }
    }
    public void exitsystem(View view) {
        //finish();
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(getString(R.string.str_confirm_exit_title));
        builder.setMessage(getString(R.string.str_confirm_exit));
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(getString(R.string.str_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.str_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences=getSharedPreferences(name_share_pref,MODE_PRIVATE);
        String username=preferences.getString("Username","");
        String password=preferences.getString("Password","");
        boolean saved=preferences.getBoolean("Saved",false);
        if(saved)
        {
            edtUserName.setText(username);
            edtPassword.setText(password);
        }
        chkSaveLogin.setChecked(saved);
    }

}