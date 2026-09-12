package com.example.fooddonationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Button btnDonor, btnReceiver, btnHowToUse, btnContactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize buttons
        btnDonor = findViewById(R.id.btnDonor);
        btnReceiver = findViewById(R.id.btnReceiver);
        btnHowToUse = findViewById(R.id.btnHowToUse);
        btnContactUs = findViewById(R.id.btnContactUs);

        // Set click listeners
        btnDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DonorActivity.class);
                startActivity(intent);
            }
        });

        btnReceiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ReceiverActivity.class);
                startActivity(intent);
            }
        });

        btnHowToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HowToUseActivity.class);
                startActivity(intent);
            }
        });

        btnContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });
    }
}