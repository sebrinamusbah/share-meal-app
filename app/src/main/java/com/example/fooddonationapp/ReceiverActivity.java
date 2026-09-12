package com.example.fooddonationapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddonationapp.database.DatabaseHelper;
import com.example.fooddonationapp.models.Donation;
import java.util.List;
import android.content.DialogInterface; // Add this line

public class ReceiverActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DonationAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Donation> availableDonations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        dbHelper = new DatabaseHelper(this);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Return to Home Page
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadAvailableDonations();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAvailableDonations();
    }

    private void loadAvailableDonations() {
        // Check for expired donations
        dbHelper.checkAndMarkExpiredDonations();

        availableDonations = dbHelper.getAvailableDonations();
        adapter = new DonationAdapter(availableDonations);
        recyclerView.setAdapter(adapter);

        if (availableDonations.isEmpty()) {
            Toast.makeText(this, "No available donations at the moment", Toast.LENGTH_SHORT).show();
        }
    }

    private class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.ViewHolder> {
        private List<Donation> items;

        public DonationAdapter(List<Donation> items) {
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_donation_receiver, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Donation donation = items.get(position);

            holder.textFoodName.setText(donation.getFoodName());
            holder.textQuantity.setText("Quantity: " + donation.getQuantity());
            holder.textExpiry.setText("Expires: " + donation.getExpiryDate());
            holder.textPickupTime.setText("Pickup Time: " + donation.getPickupTime());
            holder.textLocation.setText("Location: " + donation.getPickupLocation());
            holder.textDonorInfo.setText("Donor: " + donation.getDonorName() + " - " + donation.getDonorPhone());

            holder.btnRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRequestDialog(donation);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textFoodName, textQuantity, textExpiry, textPickupTime, textLocation, textDonorInfo;
            public Button btnRequest;

            public ViewHolder(View itemView) {
                super(itemView);
                textFoodName = itemView.findViewById(R.id.textFoodName);
                textQuantity = itemView.findViewById(R.id.textQuantity);
                textExpiry = itemView.findViewById(R.id.textExpiry);
                textPickupTime = itemView.findViewById(R.id.textPickupTime);
                textLocation = itemView.findViewById(R.id.textLocation);
                textDonorInfo = itemView.findViewById(R.id.textDonorInfo);
                btnRequest = itemView.findViewById(R.id.btnRequest);
            }
        }
    }

    private void showRequestDialog(Donation donation) {
        new AlertDialog.Builder(this)
                .setTitle("Request Food")
                .setMessage("Do you want to request " + donation.getFoodName() + "?\n\n" +
                        "You will need to call the donor to arrange pickup.")
                .setPositiveButton("Call Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Update donation status to "Waiting"
                        dbHelper.updateDonationStatus(donation.getId(), "Waiting",
                                "Receiver", "0000000000"); // In real app, get receiver info

                        // Open phone dialer
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + donation.getDonorPhone()));
                        startActivity(intent);

                        Toast.makeText(ReceiverActivity.this,
                                "Status updated to: Waiting for Pickup", Toast.LENGTH_SHORT).show();

                        // Refresh list
                        loadAvailableDonations();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}