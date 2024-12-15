package com.example.covidvaccination;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class Register extends AppCompatActivity {
    protected EditText name,username,password,city,state,phone_no;
    private TextInputLayout usernameLayout, passwordLayout;
    private Spinner stateSpinner;
    private String VacStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        DatabaseHandler db = new DatabaseHandler(this);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        city = findViewById(R.id.city);
        state = findViewById(R.id.State);
        phone_no = findViewById(R.id.phone_no);
        usernameLayout = findViewById(R.id.textInputLayout1);

        stateSpinner = findViewById(R.id.vaccination_status);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.state_options, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(adapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                VacStatus = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                VacStatus = "Not Vaccinated";
            }
        });

        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameInput = name.getText().toString().trim();
                String usernameInput = username.getText().toString().trim();
                String passwordInput = password.getText().toString().trim();
                String cityInput = city.getText().toString().trim();
                String stateInput = state.getText().toString().trim();
                String phoneInput = phone_no.getText().toString().trim();

                if (nameInput.isEmpty() ||usernameInput.isEmpty() || passwordInput.isEmpty() ||
                        cityInput.isEmpty() || stateInput.isEmpty() || phoneInput.isEmpty() || VacStatus.isEmpty()) {
                    Toast.makeText(Register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (db.checkUserName(usernameInput)) {
                    usernameLayout.setErrorEnabled(true);
                    usernameLayout.setError("Username already exists");
                    return;
                }
                usernameLayout.setErrorEnabled(false);
                db.addUser(nameInput,usernameInput,passwordInput,cityInput,stateInput,phoneInput,VacStatus);
                Toast.makeText(Register.this, "User registered successfully ",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
