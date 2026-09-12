package com.example.fooddonationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DonorActivity extends AppCompatActivity {

    private Button btnCreateDonation, btnMyDonations, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);

        btnCreateDonation = findViewById(R.id.btnCreateDonation);
        btnMyDonations = findViewById(R.id.btnMyDonations);
        btnBack = findViewById(R.id.btnBack);

        btnCreateDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonorActivity.this, DonationFormActivity.class);
                startActivity(intent);
            }
        });

        btnMyDonations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonorActivity.this, MyDonationsActivity.class);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Return to Home Page
            }
        });
    }
}