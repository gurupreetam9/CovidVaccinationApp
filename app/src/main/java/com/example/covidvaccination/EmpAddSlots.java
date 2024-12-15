package com.example.covidvaccination;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class EmpAddSlots extends AppCompatActivity {
    protected EditText hospital,h_phno,date,avail_slots,city,state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_add_slots);
        ImageButton prev = findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DatabaseHandler db = new DatabaseHandler(this);
        hospital = findViewById(R.id.hospital);
        h_phno = findViewById(R.id.hospital_phn);
        date = findViewById(R.id.date);
        avail_slots = findViewById(R.id.available);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        Button add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hospital_input = hospital.getText().toString().trim();
                String phno_input = h_phno.getText().toString().trim();
                String date_input = date.getText().toString().trim();
                String avail_input = avail_slots.getText().toString().trim();
                String city_input = city.getText().toString().trim();
                String state_input = state.getText().toString().trim();
                if (hospital_input.isEmpty() || phno_input.isEmpty() || date_input.isEmpty() || avail_input.isEmpty()
                    || city_input.isEmpty() || state_input.isEmpty()) {
                    Toast.makeText(EmpAddSlots.this,"Fill all details",Toast.LENGTH_SHORT).show();
                    return;
                }
                db.addSlots(hospital_input,phno_input,date_input,avail_input,city_input,state_input);
                Toast.makeText(EmpAddSlots.this, "Added successfully ",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
