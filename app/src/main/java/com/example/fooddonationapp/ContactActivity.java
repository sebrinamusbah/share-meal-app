package com.example.fooddonationapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        TextView tvContactInfo = findViewById(R.id.tvContactInfo);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnEmail = findViewById(R.id.btnEmail);
        Button btnCall = findViewById(R.id.btnCall);

        String contactInfo = "Contact Support Team\n\n" +
                "Email: support@fooddonationapp.com\n" +
                "Phone: +1 (234) 567-8900\n\n" +
                "Office Hours:\n" +
                "Monday-Friday: 9AM-5PM\n" +
                "Saturday: 10AM-2PM\n" +
                "Sunday: Closed";

        tvContactInfo.setText(contactInfo);

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:support@fooddonationapp.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Food Donation App Support");
                startActivity(intent);
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+12345678900"));
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}