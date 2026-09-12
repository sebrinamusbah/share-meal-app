package com.example.fooddonationapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddonationapp.database.DatabaseHelper;
import com.example.fooddonationapp.models.Donation;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
public class MyDonationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayout layoutEmpty;
    private Button btnBack, btnCreateDonation;
    private DonationAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Donation> donations;
    private String donorPhone = "+1234567890"; // In real app, get from shared preferences or intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donations); // Make sure this matches XML filename

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        btnBack = findViewById(R.id.btnBack);
        btnCreateDonation = findViewById(R.id.btnCreateDonation);

        dbHelper = new DatabaseHelper(this);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        donations = new ArrayList<>();
        adapter = new DonationAdapter(donations);
        recyclerView.setAdapter(adapter);

        // Set click listeners
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to DonorActivity
            }
        });

        btnCreateDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyDonationsActivity.this, DonationFormActivity.class);
                startActivity(intent);
            }
        });

        loadDonations();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDonations();
    }

    private void loadDonations() {
        // Check for expired donations first
        dbHelper.checkAndMarkExpiredDonations();

        donations.clear();
        donations.addAll(dbHelper.getDonationsByDonor(donorPhone));
        adapter.notifyDataSetChanged();

        if (donations.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
        }
    }

    // Inner Adapter Class
    private class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.ViewHolder> {
        private List<Donation> items;

        public DonationAdapter(List<Donation> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_donation, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Donation donation = items.get(position);

            holder.textFoodName.setText(donation.getFoodName());
            holder.textQuantity.setText("Quantity: " + donation.getQuantity());
            holder.textLocation.setText("Location: " + donation.getPickupLocation());
            holder.textPickupTime.setText("Pickup: " + donation.getPickupTime());
            holder.textExpiry.setText("Expires: " + donation.getExpiryDate());
            holder.textStatus.setText("Status: " + donation.getStatus());

            if (donation.getRequestedBy() != null && !donation.getRequestedBy().isEmpty()) {
                holder.textRequestedBy.setText("Requested by: " + donation.getRequestedBy());
                holder.textRequestedBy.setVisibility(View.VISIBLE);
            } else {
                holder.textRequestedBy.setVisibility(View.GONE);
            }

            // Set status color
            switch (donation.getStatus()) {
                case "Available":
                    holder.textStatus.setTextColor(getResources().getColor(R.color.available_color));
                    break;
                case "Waiting":
                    holder.textStatus.setTextColor(getResources().getColor(R.color.waiting_color));
                    break;
                case "Completed":
                    holder.textStatus.setTextColor(getResources().getColor(R.color.completed_color));
                    break;
                case "Expired":
                    holder.textStatus.setTextColor(getResources().getColor(R.color.expired_color));
                    break;
            }

            // Setup action buttons based on status
            setupActionButtons(holder, donation, position);
        }

        private void setupActionButtons(ViewHolder holder, Donation donation, int position) {
            // Make action buttons layout visible
            holder.layoutActions.setVisibility(View.VISIBLE);
            holder.btnAction1.setVisibility(View.GONE);
            holder.btnAction2.setVisibility(View.GONE);

            switch (donation.getStatus()) {
                case "Available":
                    holder.btnAction1.setText("Mark as Expired");
                    holder.btnAction1.setVisibility(View.VISIBLE);
                    holder.btnAction1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            markAsExpired(donation, position);
                        }
                    });
                    break;

                case "Waiting":
                    holder.btnAction1.setText("Mark as Completed");
                    holder.btnAction2.setText("Reopen");

                    holder.btnAction1.setVisibility(View.VISIBLE);
                    holder.btnAction2.setVisibility(View.VISIBLE);

                    holder.btnAction1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            markAsCompleted(donation, position);
                        }
                    });

                    holder.btnAction2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reopenDonation(donation, position);
                        }
                    });
                    break;

                case "Completed":
                case "Expired":
                    // Hide action buttons for completed/expired donations
                    holder.layoutActions.setVisibility(View.GONE);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textFoodName, textQuantity, textLocation, textPickupTime;
            public TextView textExpiry, textStatus, textRequestedBy;
            public Button btnAction1, btnAction2;
            public LinearLayout layoutActions;

            public ViewHolder(View itemView) {
                super(itemView);
                textFoodName = itemView.findViewById(R.id.textFoodName);
                textQuantity = itemView.findViewById(R.id.textQuantity);
                textLocation = itemView.findViewById(R.id.textLocation);
                textPickupTime = itemView.findViewById(R.id.textPickupTime);
                textExpiry = itemView.findViewById(R.id.textExpiry);
                textStatus = itemView.findViewById(R.id.textStatus);
                textRequestedBy = itemView.findViewById(R.id.textRequestedBy);
                btnAction1 = itemView.findViewById(R.id.btnAction1);
                btnAction2 = itemView.findViewById(R.id.btnAction2);

                // Find the actions layout - add this ID to your item_donation.xml
                layoutActions = itemView.findViewById(R.id.layoutActions);

                // If layoutActions ID doesn't exist, create it in item_donation.xml:
                // <LinearLayout android:id="@+id/layoutActions" ...>
            }
        }
    }

    private void markAsExpired(Donation donation, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Mark as Expired")
                .setMessage("Are you sure you want to mark this donation as expired?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.markDonationExpired(donation.getId());
                        loadDonations();
                        Toast.makeText(MyDonationsActivity.this,
                                "Donation marked as expired", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void markAsCompleted(Donation donation, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Mark as Completed")
                .setMessage("Did the receiver pick up the food?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.updateDonationStatus(donation.getId(), "Completed", null, null);
                        loadDonations();
                        Toast.makeText(MyDonationsActivity.this,
                                "Donation completed successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void reopenDonation(Donation donation, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Reopen Donation")
                .setMessage("Reopen this donation for other receivers?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.updateDonationStatus(donation.getId(), "Available", null, null);
                        loadDonations();
                        Toast.makeText(MyDonationsActivity.this,
                                "Donation reopened", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}