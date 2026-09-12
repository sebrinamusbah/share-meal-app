package com.example.fooddonationapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fooddonationapp.database.DatabaseHelper;
import com.example.fooddonationapp.models.Donation;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DonationFormActivity extends AppCompatActivity {

    private EditText etFullName, etPhone, etFoodName, etQuantity,
            etPickupLocation, etExpiryDate, etPickupTime;
    private Button btnSubmit, btnBack, btnExpiryDate, btnPickupTime;
    private Calendar calendar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_form);

        dbHelper = new DatabaseHelper(this);
        calendar = Calendar.getInstance();

        // Initialize views
        initializeViews();

        // Set date and time pickers
        setupDateAndTimePickers();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDonation();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Return to Donor Page
            }
        });
    }

    private void initializeViews() {
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etFoodName = findViewById(R.id.etFoodName);
        etQuantity = findViewById(R.id.etQuantity);
        etPickupLocation = findViewById(R.id.etPickupLocation);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        etPickupTime = findViewById(R.id.etPickupTime);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);
        btnExpiryDate = findViewById(R.id.btnExpiryDate);
        btnPickupTime = findViewById(R.id.btnPickupTime);
    }

    private void setupDateAndTimePickers() {
        btnExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(etExpiryDate);
            }
        });

        btnPickupTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(etPickupTime);
            }
        });
    }

    private void showDatePickerDialog(final EditText editText) {
        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);

                        String dateFormat = "yyyy-MM-dd";
                        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
                        editText.setText(sdf.format(calendar.getTime()));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
        datePicker.show();
    }

    private void showTimePickerDialog(final EditText editText) {
        TimePickerDialog timePicker = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        editText.setText(time);
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePicker.show();
    }

    private void submitDonation() {
        // Validate all fields
        if (TextUtils.isEmpty(etFullName.getText().toString())) {
            etFullName.setError("Full name is required");
            return;
        }

        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            etPhone.setError("Phone number is required");
            return;
        }

        if (TextUtils.isEmpty(etFoodName.getText().toString())) {
            etFoodName.setError("Food name is required");
            return;
        }

        if (TextUtils.isEmpty(etQuantity.getText().toString())) {
            etQuantity.setError("Quantity is required");
            return;
        }

        if (TextUtils.isEmpty(etPickupLocation.getText().toString())) {
            etPickupLocation.setError("Pickup location is required");
            return;
        }

        if (TextUtils.isEmpty(etExpiryDate.getText().toString())) {
            etExpiryDate.setError("Expiry date is required");
            return;
        }

        if (TextUtils.isEmpty(etPickupTime.getText().toString())) {
            etPickupTime.setError("Pickup time is required");
            return;
        }

        // Create Donation object
        Donation donation = new Donation();
        donation.setDonorName(etFullName.getText().toString());
        donation.setDonorPhone(etPhone.getText().toString());
        donation.setFoodName(etFoodName.getText().toString());
        donation.setQuantity(etQuantity.getText().toString());
        donation.setPickupLocation(etPickupLocation.getText().toString());
        donation.setExpiryDate(etExpiryDate.getText().toString());
        donation.setPickupTime(etPickupTime.getText().toString());
        donation.setStatus("Available");
        donation.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(Calendar.getInstance().getTime()));

        // Save to database
        long id = dbHelper.addDonation(donation);

        if (id > 0) {
            // Show confirmation
            Toast.makeText(this, "Thank you for your donation!", Toast.LENGTH_LONG).show();

            // Clear form
            clearForm();

            // Return to Donor Page after delay
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            finish();
                        }
                    },
                    2000
            );
        } else {
            Toast.makeText(this, "Failed to save donation. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        etFullName.setText("");
        etPhone.setText("");
        etFoodName.setText("");
        etQuantity.setText("");
        etPickupLocation.setText("");
        etExpiryDate.setText("");
        etPickupTime.setText("");
    }
}