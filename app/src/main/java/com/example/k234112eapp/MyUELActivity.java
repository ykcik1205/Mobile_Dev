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

    // Data for 3 majors
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

    private int selectedIndex = 0; // Default to TMĐT

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

        // WebView configuration
        WebSettings webSettings = wvContent.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wvContent.setWebViewClient(new WebViewClient());
    }

    private void setupEvents() {
        btnVoice.setOnClickListener(v -> startVoiceRecognition());

        btnGetData.setOnClickListener(v -> {
            String input = edtInput.getText().toString().trim();
            if (!input.isEmpty()) {
                processInput(input);
            }
            loadMajorData();
        });
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Đang nghe...");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Thiết bị không hỗ trợ nhận diện giọng nói", Toast.LENGTH_SHORT).show();
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
                processInput(voiceText);
            }
        }
    }

    private void processInput(String text) {
        // Simple "Vectorization" simulation using Levenshtein distance
        int bestMatchIndex = 0;
        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < majors.length; i++) {
            int distance = calculateLevenshteinDistance(text.toLowerCase(), majors[i].toLowerCase());
            if (distance < minDistance) {
                minDistance = distance;
                bestMatchIndex = i;
            }
        }

        selectedIndex = bestMatchIndex;
        txtSelectedMajor.setText(majors[selectedIndex]);
    }

    private void loadMajorData() {
        txtStatus.setVisibility(View.GONE);
        wvContent.setVisibility(View.VISIBLE);
        wvContent.loadUrl(urls[selectedIndex]);
    }

    // Simple algorithm to calculate similarity distance
    private int calculateLevenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(Math.min(
                                    dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1),
                                    dp[i - 1][j] + 1),
                            dp[i][j - 1] + 1);
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }
}
