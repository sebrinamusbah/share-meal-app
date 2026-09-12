package com.example.fooddonationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private Button btnStart;
    private TextView tvAppInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btnStart = findViewById(R.id.btnStart);
        tvAppInfo = findViewById(R.id.tvAppInfo);

        // Set app information
        tvAppInfo.setText("Donate food, reduce waste, help your community!\n\n" +
                "Connect donors with receivers to prevent food waste " +
                "and support those in need.");

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}