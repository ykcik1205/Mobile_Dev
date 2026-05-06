package com.example.k234112eapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class CalculatorActivity extends AppCompatActivity {

    EditText edtFormula;
    Button btnDel,btnEqual,btnC, btnPercent, btnCE, btn1_x, btnx_2, btnSQRT, btnPM;
    TextView txtMC,txtMR, txtMPlus, txtMMinus,txtMS, txtM;
    View.OnClickListener m_onclick;
    View.OnClickListener advanced_onclick;
    // THÊM DÒNG NÀY NÈ: Tạo "cái túi" bộ nhớ, mặc định ban đầu bằng 0
    double memoryValue = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculator);
        addViews();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get current data
                String current_data=edtFormula.getText().toString();
                //remove last character
                String new_value="";
                if(current_data.length()>1) {
                    new_value=current_data.substring(0,current_data.length()-1);
                }
                //set new value
                edtFormula.setText(new_value);
            }
        });
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtFormula.setText(""); // Đơn giản là set cho chữ trống trơn thôi!
            }
        });
        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //step1: get data (formula)
                    String formula=edtFormula.getText().toString();
                    // Step 2: Xử lý thay thế dấu
                    String fixedFormula = formula.replace("x", "*").replace(":", "/");
                    //step3: invoke library for formula (find internet...)
                    Expression expression = new ExpressionBuilder(fixedFormula).build();
                    double result = expression.evaluate();
                    //result=library nào đó(formula)
                    //step4: Show on screen
                    if (result == (long) result) {
                        edtFormula.setText(String.valueOf((long) result));
                    } else {
                        edtFormula.setText(String.valueOf(result));
                    }
                } catch (Exception e) {
                    // Step 5: Bắt lỗi nếu nhập sai
                    edtFormula.setText(getString(R.string.str_error));
                }
            }
        });
        m_onclick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentText = edtFormula.getText().toString();

                // NHÓM 1: Các nút KHÔNG cần tính toán phép tính trên màn hình
                if (view.equals(txtMC)) {
                    memoryValue = 0; // Xoá túi
                    Toast.makeText(CalculatorActivity.this, getString(R.string.str_memory_deleted), Toast.LENGTH_SHORT).show();
                    return;

                } else if (view.equals(txtMR)) {
                    // Gọi bộ nhớ ra, ghép thêm vào sau màn hình hiện tại
                    String memStr = (memoryValue == (long) memoryValue) ?
                            String.valueOf((long) memoryValue) : String.valueOf(memoryValue);
                    edtFormula.setText(currentText + memStr);
                    return;

                } else if (view.equals(txtM)) {
                    // Xem túi
                    // Hơi đặc biệt xíu vì có nối biến số vào:
                    String memStr = (memoryValue == (long) memoryValue) ? String.valueOf((long) memoryValue) : String.valueOf(memoryValue);
                    // Dùng getString() ghép biến memStr vào thẳng luôn:
                    String message = getString(R.string.str_memory_saving, memStr);
                    Toast.makeText(CalculatorActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }

                // NHÓM 2: Các nút CẦN tính kết quả trên màn hình (MS, M+, M-)
                if (currentText.isEmpty()) return; // Màn hình trống thì bỏ qua

                double currentValue = 0;
                try {
                    // Bắt buộc phải tính ra một con số cụ thể trước
                    String fixedFormula = currentText.replace("x", "*").replace(":", "/");
                    Expression expression = new ExpressionBuilder(fixedFormula).build();
                    currentValue = expression.evaluate();
                } catch (Exception e) {
                    // Nếu nhập dở dang (ví dụ 8+) mà ráng bấm MS, M+, M- thì mới báo lỗi
                    edtFormula.setText(R.string.str_error);
                    return;
                }

                // Tính được currentValue rồi thì mới xử lý tiếp
                if (view.equals(txtMS)) {
                    memoryValue = currentValue;
                    Toast.makeText(CalculatorActivity.this, getString(R.string.str_memory_saved), Toast.LENGTH_SHORT).show();

                } else if (view.equals(txtMPlus)) {
                    memoryValue += currentValue;
                    Toast.makeText(CalculatorActivity.this, getString(R.string.str_memory_added), Toast.LENGTH_SHORT).show();

                } else if (view.equals(txtMMinus)) {
                    memoryValue -= currentValue;
                    Toast.makeText(CalculatorActivity.this, getString(R.string.str_memory_subtracted), Toast.LENGTH_SHORT).show();
                }
            }
        };
        //m_onclick là biến có khả năng sinh ra sự kiện (variable as listener)
        //thường dùng để sharing sự kiện (từ 2 views trở lên)
        txtM.setOnClickListener(m_onclick);
        txtMMinus.setOnClickListener(m_onclick);
        txtMC.setOnClickListener(m_onclick);
        txtMR.setOnClickListener(m_onclick);
        txtMPlus.setOnClickListener(m_onclick);
        txtMS.setOnClickListener(m_onclick);

        advanced_onclick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Nút CE (Clear Entry): Xoá số vừa nhập gần nhất
                if (view.equals(btnCE)) {
                    String currentText = edtFormula.getText().toString();

                    // "[0-9.]+$" là một "câu thần chú" (Regex). Nó có nghĩa là:
                    // Tìm một cụm bao gồm các chữ số (0-9) và dấu chấm (.) nằm ở tận cùng ($) của chuỗi.
                    // replaceFirst sẽ thay thế cụm đó thành chuỗi rỗng (tức là xoá nó đi).
                    String newText = currentText.replaceFirst("[0-9.]+$", "");

                    edtFormula.setText(newText);
                    return;
                }

                String currentText = edtFormula.getText().toString();
                if (currentText.isEmpty()) return; // Màn hình trống thì nghỉ khoẻ

                double currentValue = 0;
                try {
                    // Giải quyết phép tính trên màn hình trước để ra 1 con số cụ thể
                    String fixedFormula = currentText.replace("x", "*").replace(":", "/");
                    Expression expression = new ExpressionBuilder(fixedFormula).build();
                    currentValue = expression.evaluate();
                } catch (Exception e) {
                    edtFormula.setText(R.string.str_error);
                    return;
                }

                double finalResult = 0;

                // Xử lý logic cho từng nút
                if (view.equals(btnPercent)) {
                    finalResult = currentValue / 100.0; // Phần trăm

                } else if (view.equals(btn1_x)) {
                    if (currentValue == 0) {
                        edtFormula.setText(R.string.str_error); // Bắt lỗi chia cho 0
                        return;
                    }
                    finalResult = 1.0 / currentValue; // Nghịch đảo 1/x

                } else if (view.equals(btnx_2)) {
                    finalResult = Math.pow(currentValue, 2); // Bình phương x^2

                } else if (view.equals(btnSQRT)) {
                    if (currentValue < 0) {
                        edtFormula.setText(R.string.str_error); // Căn bậc 2 của số âm là lỗi
                        return;
                    }
                    finalResult = Math.sqrt(currentValue); // Căn bậc 2

                } else if (view.equals(btnPM)) {
                    finalResult = currentValue * -1; // Đảo dấu +/-
                }

                // Cắt đuôi .0 nếu kết quả là số chẵn
                String resultStr = (finalResult % 1 == 0) ?
                        String.valueOf((long) finalResult) : String.valueOf(finalResult);

                // Hiển thị ra màn hình
                edtFormula.setText(resultStr);
            }
        };
        // GẮN SỰ KIỆN CHO 6 NÚT
        btnPercent.setOnClickListener(advanced_onclick);
        btnCE.setOnClickListener(advanced_onclick);
        btn1_x.setOnClickListener(advanced_onclick);
        btnx_2.setOnClickListener(advanced_onclick);
        btnSQRT.setOnClickListener(advanced_onclick);
        btnPM.setOnClickListener(advanced_onclick);
    }

    private void addViews() {
        edtFormula=findViewById(R.id.edtFormula);
        btnDel=findViewById(R.id.btnDel);
        btnEqual=findViewById(R.id.btnEqual);
        btnC=findViewById(R.id.btnC);
        btnPercent = findViewById(R.id.btnPercent);
        btnCE = findViewById(R.id.btnCE);
        btn1_x = findViewById(R.id.btn1_x);
        btnx_2 = findViewById(R.id.btnx_2);
        btnSQRT = findViewById(R.id.btnSQRT);
        btnPM = findViewById(R.id.btnPM);

        txtMC=findViewById(R.id.txtMC);
        txtMR=findViewById(R.id.txtMR);
        txtMPlus=findViewById(R.id.txtMPlus);
        txtMMinus=findViewById(R.id.txtMMinus);
        txtMS=findViewById(R.id.txtMS);
        txtM=findViewById(R.id.txtM);
    }
    public void processInputData(View view) {
        Button btn_clicked= (Button) view;
        //old value;
        String old_value=edtFormula.getText().toString();
        //input value;
        String input_value=btn_clicked.getText().toString();
        //new value (lasted value)
        String new_value=old_value+input_value;
        //show new value for customer;
        edtFormula.setText(new_value);
    }
}