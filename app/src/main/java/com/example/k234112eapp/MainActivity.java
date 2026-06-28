package com.example.k234112eapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.UserAccount;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        addViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LinearLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        //step1: get intent
        Intent intent=getIntent();
        //step2: get data
        UserAccount uc = (UserAccount) intent.getSerializableExtra("USER_LOGIN");
        if (uc!=null)
        {
            String welcome="Welcome "+uc.toString();
            Toast.makeText(this,welcome,Toast.LENGTH_LONG).show();
            TextView txtWelcome=findViewById(R.id.txtWelcome);
            txtWelcome.setText(welcome);
        }
    }

    public void say_hello(View view) {
        Toast.makeText( this, "Hello K234112E", Toast.LENGTH_LONG).show();
    }

    public void close_app(View view) {
        finish();
    }

    public void click_say_hello(View view) {
        String hello=getString(R.string.atr_clickme_say_hello);
        Toast.makeText(this,hello,Toast.LENGTH_LONG).show();
    }

    public void openCalculatorApp(View view) {
        Intent intent=new Intent(MainActivity.this,CalculatorActivity.class);
        startActivity(intent);
    }
    public void openOrderManagement(View view) {
        Intent intent=new Intent(MainActivity.this,OrderManagementActivity.class);
        startActivity(intent);
    }
    public void openCategoryScreen(View view) {
        Intent intent=new Intent(MainActivity.this,CategoryActivity.class);
        startActivity(intent);
    }
    public void openProductView(View view) {
        Intent intent=new Intent(MainActivity.this,ProductActivity.class);
        startActivity(intent);
    }

    public void openSMSSypeware(View view) {
        Intent intent=new Intent(MainActivity.this,SMSSypewareActivity.class);
        startActivity(intent);
    }

    public void openMultiThreadingActivity(View view) {
        Intent intent=new Intent(MainActivity.this,MultiThreadingActivity.class);
        startActivity(intent);
    }
    public void openMultiThreadingObjectActivity(View view) {
        Intent intent=new Intent(MainActivity.this,MultiThreadingObjectActivity.class);
        startActivity(intent);
    }
    public void openPaperScreen(View view) {
        Intent intent=new Intent(MainActivity.this,PaperActivity.class);
        startActivity(intent);
    }

    public void openFontandMusic(View view) {
        Intent intent=new Intent(MainActivity.this,FontAndMusicActivity.class);
        startActivity(intent);
    }
    public void openMYUEL(View view) {
        Intent intent=new Intent(MainActivity.this,MyUELActivity.class);
        startActivity(intent);
    }

    public void openFirebaseContact(View view) {
        Intent intent=new Intent(MainActivity.this,FirebaseActivity.class);
        startActivity(intent);
    }

    public void openECommerceFirebase(View view) {
        Intent intent=new Intent(MainActivity.this,ECFirebaseActivity.class);
        startActivity(intent);
    }
}