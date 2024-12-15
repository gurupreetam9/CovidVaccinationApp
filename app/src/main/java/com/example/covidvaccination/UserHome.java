package com.example.covidvaccination;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;

public class UserHome extends AppCompatActivity {

    DatabaseHandler db;
    ListView listView;
    ArrayList<HashMap<String, String>> slotsList;
    SlotAdapter adapter;
    RelativeLayout sidebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_page);

        db = new DatabaseHandler(this);
        listView = findViewById(R.id.listView);
        sidebar = findViewById(R.id.sidebar);

        slotsList = db.getAvailableSlots(); // Fetch all slots initially
        adapter = new SlotAdapter();
        listView.setAdapter(adapter);

        // User profile icon and sidebar logic
        ImageView userProfileIcon = findViewById(R.id.userProfileIcon);
        userProfileIcon.setOnClickListener(v -> toggleSidebar());
        findViewById(R.id.checkUserDetails).setOnClickListener(v -> checkUserDetails());
        findViewById(R.id.checkBookedSlots).setOnClickListener(v -> checkBookedSlots());
        findViewById(R.id.logout).setOnClickListener(v -> logoutUser());

        // Search functionality
        Button searchButton = findViewById(R.id.search_button);
        TextInputEditText cityEditText = findViewById(R.id.city);
        TextInputEditText stateEditText = findViewById(R.id.state);

        searchButton.setOnClickListener(v -> {
            String city = cityEditText.getText().toString().trim();
            String state = stateEditText.getText().toString().trim();

            // Filter slots based on city and state
            slotsList = db.getSlotsByCityAndState(city, state);
            adapter.notifyDataSetChanged();

            if (slotsList.isEmpty()) {
                Toast.makeText(this, "No slots found for the specified city and state.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void toggleSidebar() {
        if (sidebar.getVisibility() == View.GONE) {
            sidebar.setVisibility(View.VISIBLE);
        } else {
            sidebar.setVisibility(View.GONE);
        }
    }
    private void checkUserDetails() {
        int userId = getIntent().getIntExtra("USER_ID", -1);
        Intent intent = new Intent(UserHome.this, UserDetails.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void checkBookedSlots() {
        int userId = getIntent().getIntExtra("USER_ID",-1);
        Intent intent = new Intent(UserHome.this, CheckBookedSlots.class);
        intent.putExtra("USER_ID",userId);
        startActivity(intent);
    }
    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent logoutIntent = new Intent(UserHome.this, MainActivity.class);
        startActivity(logoutIntent);
        finish();
    }

    class SlotAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return slotsList.size();
        }

        @Override
        public Object getItem(int position) {
            return slotsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(UserHome.this).inflate(R.layout.slot_item, parent, false);
            }

            TextView hospitalName = convertView.findViewById(R.id.hospitalNameValue);
            TextView date = convertView.findViewById(R.id.dateValue);
            TextView availableSlots = convertView.findViewById(R.id.availableSlotsValue);
            TextView city = convertView.findViewById(R.id.cityValue);
            TextView state = convertView.findViewById(R.id.stateValue);
            TextView h_phone_no = convertView.findViewById(R.id.phone_noValue);
            Button bookButton = convertView.findViewById(R.id.bookButton);

            HashMap<String, String> slot = slotsList.get(position);

            hospitalName.setText(slot.get("hospital"));
            date.setText(slot.get("date"));
            availableSlots.setText(slot.get("slots"));
            city.setText(slot.get("city"));
            state.setText(slot.get("state"));
            h_phone_no.setText(slot.get("h_phone_no"));

            String idString = slot.get("id");
            if (idString == null || idString.isEmpty()) {
                bookButton.setEnabled(false);
                Toast.makeText(UserHome.this, "Invalid slot data.", Toast.LENGTH_SHORT).show();
                return convertView;
            }

            int slotId = Integer.parseInt(idString);

            bookButton.setOnClickListener(v -> {
                int userId = getIntent().getIntExtra("USER_ID", -1);
                String selectedDate = slot.get("date");

                if (userId != -1) {
                    if (db.isSlotBookedByUserForDate(userId, selectedDate)) {
                        Toast.makeText(UserHome.this, "You have already booked a slot for this date!", Toast.LENGTH_SHORT).show();
                    } else {
                        int currentSlots = Integer.parseInt(slot.get("slots"));

                        if (currentSlots > 0) {
                            db.addBookedSlot(userId, slotId, selectedDate);
                            slot.put("slots", String.valueOf(currentSlots - 1));
                            notifyDataSetChanged();
                            Toast.makeText(UserHome.this, "Slot booked successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserHome.this, "No slots available!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(UserHome.this, "Failed to book slot. Invalid user.", Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }
    }
        private void refreshSlotList() {
        slotsList = db.getAvailableSlots();
        adapter.notifyDataSetChanged();
    }
}
