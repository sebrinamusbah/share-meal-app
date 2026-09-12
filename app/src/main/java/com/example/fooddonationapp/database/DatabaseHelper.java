package com.example.fooddonationapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.fooddonationapp.models.Donation;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FoodDonationDB";
    private static final int DATABASE_VERSION = 1;

    // Donations table
    private static final String TABLE_DONATIONS = "donations";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DONOR_NAME = "donor_name";
    private static final String COLUMN_DONOR_PHONE = "donor_phone";
    private static final String COLUMN_FOOD_NAME = "food_name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_PICKUP_LOCATION = "pickup_location";
    private static final String COLUMN_PICKUP_TIME = "pickup_time";
    private static final String COLUMN_EXPIRY_DATE = "expiry_date";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_REQUESTED_BY = "requested_by";
    private static final String COLUMN_REQUESTED_PHONE = "requested_phone";
    private static final String COLUMN_CREATED_AT = "created_at";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DONATIONS_TABLE = "CREATE TABLE " + TABLE_DONATIONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DONOR_NAME + " TEXT,"
                + COLUMN_DONOR_PHONE + " TEXT,"
                + COLUMN_FOOD_NAME + " TEXT,"
                + COLUMN_QUANTITY + " TEXT,"
                + COLUMN_PICKUP_LOCATION + " TEXT,"
                + COLUMN_PICKUP_TIME + " TEXT,"
                + COLUMN_EXPIRY_DATE + " TEXT,"
                + COLUMN_STATUS + " TEXT DEFAULT 'Available',"
                + COLUMN_REQUESTED_BY + " TEXT,"
                + COLUMN_REQUESTED_PHONE + " TEXT,"
                + COLUMN_CREATED_AT + " TEXT"
                + ")";
        db.execSQL(CREATE_DONATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DONATIONS);
        onCreate(db);
    }

    // Add new donation
    public long addDonation(Donation donation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_DONOR_NAME, donation.getDonorName());
        values.put(COLUMN_DONOR_PHONE, donation.getDonorPhone());
        values.put(COLUMN_FOOD_NAME, donation.getFoodName());
        values.put(COLUMN_QUANTITY, donation.getQuantity());
        values.put(COLUMN_PICKUP_LOCATION, donation.getPickupLocation());
        values.put(COLUMN_PICKUP_TIME, donation.getPickupTime());
        values.put(COLUMN_EXPIRY_DATE, donation.getExpiryDate());
        values.put(COLUMN_STATUS, donation.getStatus());
        values.put(COLUMN_CREATED_AT, donation.getCreatedAt());

        long id = db.insert(TABLE_DONATIONS, null, values);
        db.close();
        return id;
    }

    // Get all donations for a donor (by phone number)
    public List<Donation> getDonationsByDonor(String donorPhone) {
        List<Donation> donations = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_DONATIONS +
                " WHERE " + COLUMN_DONOR_PHONE + " = '" + donorPhone + "'" +
                " ORDER BY " + COLUMN_CREATED_AT + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Donation donation = cursorToDonation(cursor);
                donations.add(donation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return donations;
    }

    // Get all available donations for receivers
    public List<Donation> getAvailableDonations() {
        List<Donation> donations = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_DONATIONS +
                " WHERE " + COLUMN_STATUS + " = 'Available'" +
                " AND date(" + COLUMN_EXPIRY_DATE + ") >= date('now')" +
                " ORDER BY " + COLUMN_CREATED_AT + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Donation donation = cursorToDonation(cursor);
                donations.add(donation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return donations;
    }

    // Update donation status
    public int updateDonationStatus(int id, String status, String requestedBy, String requestedPhone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_STATUS, status);
        if (requestedBy != null) {
            values.put(COLUMN_REQUESTED_BY, requestedBy);
        }
        if (requestedPhone != null) {
            values.put(COLUMN_REQUESTED_PHONE, requestedPhone);
        }

        return db.update(TABLE_DONATIONS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // Mark donation as expired
    public int markDonationExpired(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, "Expired");
        return db.update(TABLE_DONATIONS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // Check for expired donations
    public void checkAndMarkExpiredDonations() {
        String updateQuery = "UPDATE " + TABLE_DONATIONS +
                " SET " + COLUMN_STATUS + " = 'Expired'" +
                " WHERE " + COLUMN_STATUS + " IN ('Available', 'Waiting')" +
                " AND date(" + COLUMN_EXPIRY_DATE + ") < date('now')";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(updateQuery);
        db.close();
    }

    // Helper method to convert cursor to Donation object
    private Donation cursorToDonation(Cursor cursor) {
        Donation donation = new Donation();
        donation.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        donation.setDonorName(cursor.getString(cursor.getColumnIndex(COLUMN_DONOR_NAME)));
        donation.setDonorPhone(cursor.getString(cursor.getColumnIndex(COLUMN_DONOR_PHONE)));
        donation.setFoodName(cursor.getString(cursor.getColumnIndex(COLUMN_FOOD_NAME)));
        donation.setQuantity(cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY)));
        donation.setPickupLocation(cursor.getString(cursor.getColumnIndex(COLUMN_PICKUP_LOCATION)));
        donation.setPickupTime(cursor.getString(cursor.getColumnIndex(COLUMN_PICKUP_TIME)));
        donation.setExpiryDate(cursor.getString(cursor.getColumnIndex(COLUMN_EXPIRY_DATE)));
        donation.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
        donation.setRequestedBy(cursor.getString(cursor.getColumnIndex(COLUMN_REQUESTED_BY)));
        donation.setRequestedPhone(cursor.getString(cursor.getColumnIndex(COLUMN_REQUESTED_PHONE)));
        donation.setCreatedAt(cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT)));
        return donation;
    }
}