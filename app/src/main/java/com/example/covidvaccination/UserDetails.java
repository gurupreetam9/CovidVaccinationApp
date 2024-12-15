package com.example.covidvaccination;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class UserDetails extends AppCompatActivity {
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details);
        ImageButton prev = findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        db = new DatabaseHandler(this);

        int userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId != -1) {
            // Fetch user details from the database
            HashMap<String, String> userDetails = db.getUserDetails(userId); // Ensure getUserDetails is implemented
            if (userDetails != null) {
                // Populate the TextViews with user details
                ((TextView) findViewById(R.id.NameValue)).setText(userDetails.get("name"));
                ((TextView) findViewById(R.id.usernameValue)).setText(userDetails.get("username"));
                ((TextView) findViewById(R.id.cityValue)).setText(userDetails.get("city"));
                ((TextView) findViewById(R.id.stateValue)).setText(userDetails.get("state"));
                ((TextView) findViewById(R.id.phone_noValue)).setText(userDetails.get("phone"));
                ((TextView) findViewById(R.id.VaccinationStatusValue)).setText(userDetails.get("vaccination_status"));
            } else {
                Toast.makeText(this, "User details not found!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Invalid user ID!", Toast.LENGTH_SHORT).show();
        }
    }
}
