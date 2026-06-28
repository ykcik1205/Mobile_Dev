package com.example.k234112eapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MyUELActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;

    private EditText edtInput;
    private ImageButton btnVoice;
    private TextView txtSelectedMajor;
    private Button btnGetData;
    private WebView wvContent;
    private TextView txtStatus;

    // 1. Danh sách 3 ngành đào tạo
    private final String[] majors = {
            "Thương mại điện tử",
            "Hệ thống thông tin quản lý",
            "Kinh doanh số và trí tuệ nhân tạo"
    };

    private final String[] urls = {
            "https://myuel.uel.edu.vn/Default.aspx?ModuleId=f92f39b2-dea3-4185-8cbb-56c1c49c5226&OlogyID=411&DepartmentID=05&GraduateLevelID=DH&StudyTypeID=CQ",
            "https://myuel.uel.edu.vn/Default.aspx?ModuleId=f92f39b2-dea3-4185-8cbb-56c1c49c5226&OlogyID=7340405&DepartmentID=05&GraduateLevelID=DH&StudyTypeID=CQ",
            "https://myuel.uel.edu.vn/Default.aspx?ModuleId=f92f39b2-dea3-4185-8cbb-56c1c49c5226&OlogyID=416&DepartmentID=05&GraduateLevelID=DH&StudyTypeID=CQ"
    };

    // 2. Vocabulary (Từ điển từ khóa để Vector hóa) - Bao gồm cả từ viết tắt
    private final String[] vocabulary = {
            "thương", "mại", "điện", "tử", "tmdt", "tmđt",
            "hệ", "thống", "thông", "tin", "quản", "lý", "httt",
            "kinh", "doanh", "số", "trí", "tuệ", "nhân", "tạo", "ai", "kds"
    };

    private int selectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_uelactivity);
        initViews();
        setupEvents();
    }

    private void initViews() {
        edtInput = findViewById(R.id.edtInput);
        btnVoice = findViewById(R.id.btnVoice);
        txtSelectedMajor = findViewById(R.id.txtSelectedMajor);
        btnGetData = findViewById(R.id.btnGetData);
        wvContent = findViewById(R.id.wvContent);
        txtStatus = findViewById(R.id.txtStatus);

        WebSettings webSettings = wvContent.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wvContent.setWebViewClient(new WebViewClient());
    }

    private void setupEvents() {
        btnVoice.setOnClickListener(v -> startVoiceRecognition());
        btnGetData.setOnClickListener(v -> {
            String input = edtInput.getText().toString().trim();
            if (!input.isEmpty()) {
                processVectorMatching(input);
                loadMajorData();
            } else {
                Toast.makeText(this, "Vui lòng nhập từ khóa!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Logic xử lý chính: Vector hóa + So sánh khoảng cách
    private void processVectorMatching(String text) {
        String input = text.toLowerCase().trim();
        
        // Bước 1: Vector hóa nội dung người dùng nhập
        double[] inputVector = vectorize(input);
        
        double minDistance = Double.MAX_VALUE;
        int bestMatchIndex = 0;
        StringBuilder debugInfo = new StringBuilder("Kết quả phân tích Vector:\n");

        // Bước 2: So sánh với Vector của từng ngành
        for (int i = 0; i < majors.length; i++) {
            double[] majorVector = vectorize(majors[i].toLowerCase());
            
            // Tính khoảng cách Euclidean
            double distance = calculateEuclideanDistance(inputVector, majorVector);
            
            // Lưu thông tin để hiển thị (chứng minh với giảng viên)
            String majorNameShort = majors[i].split(" ")[0];
            debugInfo.append("- ").append(majorNameShort).append("...: d=").append(String.format("%.2f", distance)).append("\n");

            if (distance < minDistance) {
                minDistance = distance;
                bestMatchIndex = i;
            }
        }

        selectedIndex = bestMatchIndex;
        txtSelectedMajor.setText(majors[selectedIndex]);
        txtStatus.setText(debugInfo.toString()); // Hiển thị các khoảng cách đã tính được
    }

    // Hàm chuyển đổi văn bản thành Vector dựa trên Vocabulary
    private double[] vectorize(String text) {
        double[] vector = new double[vocabulary.length];
        String[] words = text.split("\\s+");

        for (int i = 0; i < vocabulary.length; i++) {
            for (String word : words) {
                // Tính độ tương đồng Levenshtein (cho phép sai sót chính tả)
                double similarity = calculateSimilarity(vocabulary[i], word);
                
                // Nếu từ nhập vào giống > 75% từ khóa trong từ điển thì ghi nhận vào Vector
                if (similarity > 0.75) {
                    vector[i] += 1.0; 
                }
            }
        }
        return vector;
    }

    private double calculateSimilarity(String s1, String s2) {
        if (s1.length() < 3 || s2.length() < 3) return s1.equals(s2) ? 1.0 : 0.0;
        int distance = calculateLevenshteinDistance(s1, s2);
        return 1.0 - ((double) distance / Math.max(s1.length(), s2.length()));
    }

    private double calculateEuclideanDistance(double[] v1, double[] v2) {
        double sum = 0;
        for (int i = 0; i < v1.length; i++) {
            sum += Math.pow(v1[i] - v2[i], 2);
        }
        return Math.sqrt(sum);
    }

    private int calculateLevenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) dp[i][j] = j;
                else if (j == 0) dp[i][j] = i;
                else {
                    dp[i][j] = Math.min(Math.min(
                                    dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1),
                                    dp[i - 1][j] + 1),
                            dp[i][j - 1] + 1);
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }

    private void loadMajorData() {
        txtStatus.setVisibility(View.VISIBLE); // Giữ hiển thị thông số phân tích
        wvContent.setVisibility(View.VISIBLE);
        wvContent.loadUrl(urls[selectedIndex]);
        Toast.makeText(this, "Đang tải: " + majors[selectedIndex], Toast.LENGTH_SHORT).show();
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Bạn muốn tìm ngành nào?");
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi nhận diện giọng nói", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String voiceText = result.get(0);
                edtInput.setText(voiceText);
                processVectorMatching(voiceText);
                loadMajorData();
            }
        }
    }
}
