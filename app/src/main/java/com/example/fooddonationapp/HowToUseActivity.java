package com.example.fooddonationapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HowToUseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);

        TextView tvInstructions = findViewById(R.id.tvInstructions);
        Button btnBack = findViewById(R.id.btnBack);

        String instructions = "📱 HOW TO USE THIS APP\n\n" +
                "FOR DONORS:\n" +
                "1. Tap DONOR on Home Page\n" +
                "2. Tap 'Create Donation'\n" +
                "3. Fill in all details about the food\n" +
                "4. Submit the donation\n" +
                "5. Track your donations in 'My Donations'\n" +
                "6. Update status when receiver picks up\n\n" +
                "FOR RECEIVERS:\n" +
                "1. Tap RECEIVER on Home Page\n" +
                "2. Browse available food donations\n" +
                "3. Tap 'Request' on desired food\n" +
                "4. Call the donor to arrange pickup\n" +
                "5. Pick up the food at agreed time/location\n\n" +
                "STATUS MEANINGS:\n" +
                "✅ Available - Food is ready for pickup\n" +
                "⏳ Waiting - Someone requested, awaiting pickup\n" +
                "✅ Completed - Food was picked up\n" +
                "❌ Expired - Food past expiry date";

        tvInstructions.setText(instructions);

        btnBack.setOnClickListener(v -> finish());
    }
}