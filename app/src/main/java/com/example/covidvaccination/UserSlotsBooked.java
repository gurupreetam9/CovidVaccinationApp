package com.example.covidvaccination;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class UserSlotsBooked extends AppCompatActivity {
    ListView listView;
    DatabaseHandler dbHandler;
    ArrayList<String> bookedSlotsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_user_slots_booked);
        ImageButton prev = findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = findViewById(R.id.listview);
        dbHandler = new DatabaseHandler(this);
        bookedSlotsList = new ArrayList<>();

        fetchBookedSlots();

        // Display the list using an ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookedSlotsList);
        listView.setAdapter(adapter);
    }

    private void fetchBookedSlots() {
        // Fetch all booked slots from the database
        ArrayList<HashMap<String, String>> bookedSlotsData = dbHandler.getAllBookedSlotsWithName();

        for (HashMap<String, String> slot : bookedSlotsData) {
            String displayText = "Username: " + slot.get("username") + "\n"
                    + "Hospital: " + slot.get("hospital") + "\n"
                    + "Phone: " + slot.get("h_phone_no") + "\n"
                    + "Date: " + slot.get("date") + "\n"
                    + "City: " + slot.get("city") + ", State: " + slot.get("state");
            bookedSlotsList.add(displayText);
        }
    }
}
