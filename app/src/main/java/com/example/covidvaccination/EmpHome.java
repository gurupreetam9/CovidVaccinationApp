package com.example.covidvaccination;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EmpHome extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_home_page);
        Button add_slots = findViewById(R.id.add_slots);
        add_slots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmpHome.this, EmpAddSlots.class);
                startActivity(intent);
            }
        });
        Button check_users = findViewById(R.id.check_users);
        check_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmpHome.this, CheckUsers.class);
                startActivity(intent);
            }
        });

        Button slots_booked = findViewById(R.id.slots_booked);
        slots_booked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmpHome.this, UserSlotsBooked.class);
                startActivity(intent);
            }
        });

        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(EmpHome.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }
}
