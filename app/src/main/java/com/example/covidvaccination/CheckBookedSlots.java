package com.example.covidvaccination;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;

public class CheckBookedSlots extends AppCompatActivity {

    ListView listView;
    DatabaseHandler dbHandler;
    ArrayList<String> bookedSlotsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_booked_slots);
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
        int loggedInUserId = getIntent().getIntExtra("USER_ID", -1);

        fetchBookedSlots(loggedInUserId);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookedSlotsList);
        listView.setAdapter(adapter);
    }

    private void fetchBookedSlots(int userId) {
        ArrayList<HashMap<String, String>> bookedSlotsData = dbHandler.getBookedSlotsByUser(userId);

        for (HashMap<String, String> slot : bookedSlotsData) {
            String displayText = "Hospital: " + slot.get("hospital") + "\n"
                    + "Phone: " + slot.get("h_phone_no") + "\n"
                    + "Date: " + slot.get("date") + "\n"
                    + "City: " + slot.get("city") + ", State: " + slot.get("state");
            bookedSlotsList.add(displayText);
        }
    }
}
