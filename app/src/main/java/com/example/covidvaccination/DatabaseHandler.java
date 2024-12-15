package com.example.covidvaccination;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "CovidVaccinationDB";
    private static final int DB_VERSION = 3;

    private static final String TABLE_USERS = "Users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_STATE = "state";
    private static final String COLUMN_PHONE = "phone_no";
    private static final String COLUMN_STATUS = "vaccination_status";

    private static final String TABLE_EMPLOYEE = "employee";
    private static final String KEY_ID_EMPLOYEE = "id";
    private static final String KEY_EMPLOYEE_ID = "employeeId";
    private static final String KEY_USERNAME_EMPLOYEE = "username";
    private static final String KEY_PASSWORD_EMPLOYEE = "password";

    private static final String TABLE_SLOTS = "Slots";
    private static final String SLOT_HOSPITAL = "hospital";
    private static final String SLOT_HOSP_PHONE = "h_phone_no";
    private static final String AVAILABLE_SLOTS = "slots_avail";
    private static final String SLOT_DATE = "date";
    private static final String SLOT_CITY = "city";
    private static final String SLOT_STATE = "state";

    private static final String TABLE_BOOKED_SLOTS = "BookedSlots";
    private static final String BOOKED_SLOT_ID = "id";
    private static final String BOOKED_SLOT_USER_ID = "user_id";
    private static final String BOOKED_SLOT_SLOT_ID = "slot_id";
    private static final String BOOKED_SLOT_DATE = "booking_date";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_CITY + " TEXT, " +
                COLUMN_STATE + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_STATUS + " TEXT)";
        db.execSQL(createUsersTable);

        // Create Employee table
        String createEmployeeTable = "CREATE TABLE " + TABLE_EMPLOYEE + " (" +
                KEY_ID_EMPLOYEE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_EMPLOYEE_ID + " TEXT NOT NULL, " +
                KEY_USERNAME_EMPLOYEE + " TEXT NOT NULL UNIQUE, " +
                KEY_PASSWORD_EMPLOYEE + " TEXT NOT NULL)";
        db.execSQL(createEmployeeTable);

        // Create Slots table
        String createSlotsTable = "CREATE TABLE " + TABLE_SLOTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SLOT_HOSPITAL + " TEXT, " +
                SLOT_HOSP_PHONE + " TEXT, " +
                SLOT_DATE + " TEXT, " +
                AVAILABLE_SLOTS + " INTEGER, " +
                SLOT_CITY + " TEXT, " +
                SLOT_STATE + " TEXT)";
        db.execSQL(createSlotsTable);

        // Create BookedSlots table
        String createBookedSlotsTable = "CREATE TABLE " + TABLE_BOOKED_SLOTS + " (" +
                BOOKED_SLOT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BOOKED_SLOT_USER_ID + " INTEGER, " +
                BOOKED_SLOT_SLOT_ID + " INTEGER, " +
                BOOKED_SLOT_DATE + " TEXT, " +
                "FOREIGN KEY(" + BOOKED_SLOT_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY(" + BOOKED_SLOT_SLOT_ID + ") REFERENCES " + TABLE_SLOTS + "(" + COLUMN_ID + "))";
        db.execSQL(createBookedSlotsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SLOTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKED_SLOTS);
        onCreate(db);
    }


    // Add user method
    public boolean addUser(String name, String username, String password, String city, String state, String phoneNo, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_CITY, city);
        values.put(COLUMN_STATE, state);
        values.put(COLUMN_PHONE, phoneNo);
        values.put(COLUMN_STATUS, status);

        try {
            db.insertOrThrow(TABLE_USERS, null, values);
            db.close();
            return true; // Successful insertion
        } catch (android.database.sqlite.SQLiteConstraintException e) {
            db.close();
            return false; // Username already exists
        }

    }

    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=?", new String[]{username});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int userId = cursor.getInt(cursor.getColumnIndex("id")); // Assuming 'id' is the column name in your table
            cursor.close();
            db.close();
            return userId;
        } else {
            cursor.close();
            db.close();
            return -1; // Return -1 if user not found
        }
    }
    public int getEmpId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_EMPLOYEE + " WHERE " + COLUMN_USERNAME + "=?", new String[]{username});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int empId = cursor.getInt(cursor.getColumnIndex("id"));
            cursor.close();
            db.close();
            return empId;
        } else {
            cursor.close();
            db.close();
            return -1;
        }
    }

    // Add employee method
    public void addEmployee(String empId, String empName, String empPass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMPLOYEE_ID, empId);
        values.put(KEY_USERNAME_EMPLOYEE, empName);
        values.put(KEY_PASSWORD_EMPLOYEE, empPass);
        long result = db.insert(TABLE_EMPLOYEE, null, values);
        if (result == -1)
            System.out.println("DB insertion failed");
        else
            System.out.println("DB insertion success");
        db.close();
    }

    public void addSlots(String hospital, String h_phno, String date, String avail_slots, String city, String state) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SLOT_HOSPITAL, hospital);
        values.put(SLOT_HOSP_PHONE, h_phno);
        values.put(SLOT_DATE, date);
        values.put(AVAILABLE_SLOTS, avail_slots);
        values.put(SLOT_CITY, city);
        values.put(SLOT_STATE, state);


        long result = db.insert(TABLE_SLOTS, null, values);

        if (result == -1)
            System.out.println("DB insertion failed");
        else
            System.out.println("DB insertion success");
        db.close();
    }

    // Check user credentials
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password});
        boolean userExists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return userExists;
    }

    public boolean checkUserName(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=?",
                new String[]{username});
        boolean userExists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return userExists;
    }

    // Check employee credentials
    public boolean checkEmployeeCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EMPLOYEE + " WHERE " + KEY_USERNAME_EMPLOYEE + "=? AND " + KEY_PASSWORD_EMPLOYEE + "=?",
                new String[]{username, password});

        boolean employeeExists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return employeeExists;
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> getAvailableSlots() {
        ArrayList<HashMap<String, String>> slotsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Slots WHERE slots_avail > 0", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> slot = new HashMap<>();
                slot.put("id", cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                slot.put("hospital", cursor.getString(cursor.getColumnIndex(SLOT_HOSPITAL)));
                slot.put("h_phone_no", cursor.getString(cursor.getColumnIndex(SLOT_HOSP_PHONE)));
                slot.put("date", cursor.getString(cursor.getColumnIndex(SLOT_DATE)));
                slot.put("slots", cursor.getString(cursor.getColumnIndex(AVAILABLE_SLOTS))); // Ensure this is an integer in DB
                slot.put("city", cursor.getString(cursor.getColumnIndex(SLOT_CITY)));
                slot.put("state", cursor.getString(cursor.getColumnIndex(SLOT_STATE)));
                slotsList.add(slot);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return slotsList;
    }


    public void reduceAvailableSlot(int slotId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Fetch the current available slots
        Cursor cursor = db.rawQuery("SELECT slots_avail FROM Slots WHERE id = ?", new String[]{String.valueOf(slotId)});
        if (cursor.moveToFirst()) {
            int currentSlots = Integer.parseInt(cursor.getString(0)); // Parse to integer
            if (currentSlots > 0) {
                // Reduce slots by 1
                ContentValues values = new ContentValues();
                values.put(AVAILABLE_SLOTS, currentSlots - 1);
                db.update(TABLE_SLOTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(slotId)});
            }
        }
        cursor.close();
        db.close();
    }

    public void addBookedSlot(int userId, int slotId, String bookingDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BOOKED_SLOT_USER_ID, userId);
        values.put(BOOKED_SLOT_SLOT_ID, slotId);
        values.put(BOOKED_SLOT_DATE, bookingDate);

        long result = db.insert(TABLE_BOOKED_SLOTS, null, values);

        if (result != -1) {
            System.out.println("Booking successful for user ID: " + userId + ", slot ID: " + slotId);
            reduceAvailableSlot(slotId); // Reduce slot count only if booking is successful
        } else {
            System.out.println("Failed to book slot for user ID: " + userId);
        }
        db.close();
    }

    public boolean isSlotBookedByUserForDate(int userId, String bookingDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKED_SLOTS + " WHERE " + BOOKED_SLOT_USER_ID + "=? AND " + BOOKED_SLOT_DATE + "=?",
                new String[]{String.valueOf(userId), bookingDate});

        boolean isBooked = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isBooked;
    }
    @SuppressLint("Range")
    public HashMap<String, String> getUserDetails(int userId) {
        HashMap<String, String> userDetails = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            userDetails.put("name", cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            userDetails.put("username", cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
            userDetails.put("city", cursor.getString(cursor.getColumnIndex(COLUMN_CITY)));
            userDetails.put("state", cursor.getString(cursor.getColumnIndex(COLUMN_STATE)));
            userDetails.put("phone", cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
            userDetails.put("vaccination_status", cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
        }
        Log.d("Database", "User details fetched: " + userDetails.toString());

        cursor.close();
        db.close();
        return userDetails;
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> getSlotsByCityAndState(String city, String state) {
        ArrayList<HashMap<String, String>> filteredSlots = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM Slots WHERE city LIKE ? AND state LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + city + "%", "%" + state + "%"});

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> slot = new HashMap<>();
                slot.put("hospital", cursor.getString(cursor.getColumnIndex("hospital")));
                slot.put("date", cursor.getString(cursor.getColumnIndex("date")));
                slot.put("slots", cursor.getString(cursor.getColumnIndex("slots_avail")));
                slot.put("city", cursor.getString(cursor.getColumnIndex("city")));
                slot.put("state", cursor.getString(cursor.getColumnIndex("state")));
                slot.put("h_phone_no", cursor.getString(cursor.getColumnIndex("h_phone_no")));
                slot.put("id", cursor.getString(cursor.getColumnIndex("id")));

                filteredSlots.add(slot);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return filteredSlots;
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> getBookedSlotsByUser(int userId) {
        ArrayList<HashMap<String, String>> bookedSlotsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // SQL query to join BookedSlots and Slots tables
        String query = "SELECT s.hospital, s.h_phone_no, s.date, s.city, s.state " +
                "FROM BookedSlots bs " +
                "JOIN Slots s ON bs.slot_id = s.id " +
                "WHERE bs.user_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> slot = new HashMap<>();
                slot.put("hospital", cursor.getString(cursor.getColumnIndex("hospital")));
                slot.put("h_phone_no", cursor.getString(cursor.getColumnIndex("h_phone_no")));
                slot.put("date", cursor.getString(cursor.getColumnIndex("date")));
                slot.put("city", cursor.getString(cursor.getColumnIndex("city")));
                slot.put("state", cursor.getString(cursor.getColumnIndex("state")));
                bookedSlotsList.add(slot);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return bookedSlotsList;
    }
    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> getAllBookedSlotsWithName() {
        ArrayList<HashMap<String, String>> bookedSlots = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT u.username, s.hospital, s.h_phone_no, s.date, s.city, s.state " +
                "FROM BookedSlots AS b " +
                "JOIN Users AS u ON b.user_id = u.id " +
                "JOIN Slots AS s ON b.slot_id = s.id";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> slot = new HashMap<>();
                slot.put("username", cursor.getString(cursor.getColumnIndex("username")));
                slot.put("hospital", cursor.getString(cursor.getColumnIndex("hospital")));
                slot.put("h_phone_no", cursor.getString(cursor.getColumnIndex("h_phone_no")));
                slot.put("date", cursor.getString(cursor.getColumnIndex("date")));
                slot.put("city", cursor.getString(cursor.getColumnIndex("city")));
                slot.put("state", cursor.getString(cursor.getColumnIndex("state")));
                bookedSlots.add(slot);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return bookedSlots;
    }


    public boolean verifyPhoneNumber(String username, String phoneNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Users WHERE username = ? AND phone_no = ?",
                new String[]{username, phoneNumber});

        boolean isMatch = cursor.getCount() > 0; // Returns true if phone number matches
        cursor.close();
        db.close();
        return isMatch;
    }

    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword); // Assuming 'password' is the column name

        int rowsAffected = db.update("Users", values, "username = ?", new String[]{username});
        db.close();
        return rowsAffected > 0;
    }


}
