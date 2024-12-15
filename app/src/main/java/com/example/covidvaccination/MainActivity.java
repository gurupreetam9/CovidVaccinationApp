package com.example.covidvaccination;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    protected EditText username, password;
    protected RadioGroup r_group;
    protected String selected_radio_btn;
    protected RadioButton r_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHandler db1 = new DatabaseHandler(this);
        db1.addEmployee("E00","admin","admin");
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        String userType = sharedPreferences.getString("userType", "");

        if (isLoggedIn) {
            if ("Employee".equals(userType)) {
                Intent intent = new Intent(MainActivity.this, EmpHome.class);
                int empId = sharedPreferences.getInt("EmpId",-1);
                intent.putExtra("EmpId",empId);
                startActivity(intent);
                finish();
            } else if ("User".equals(userType)) {
                Toast.makeText(this, "Redirecting to User Home Screen", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, UserHome.class);
                int userId = sharedPreferences.getInt("USER_ID", -1); // Retrieve userId from SharedPreferences
                intent.putExtra("USER_ID", userId); // Pass userId
                startActivity(intent);
                finish();
            }
        } else {
            setContentView(R.layout.activity_main);

            DatabaseHandler db = new DatabaseHandler(this);
            username = findViewById(R.id.username);
            password = findViewById(R.id.password);
            r_btn = findViewById(R.id.rbEmployee);
            r_group = findViewById(R.id.radioGroup);

            r_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton selectedRadioButton = findViewById(checkedId);
                    if (selectedRadioButton != null) {
                        selected_radio_btn = selectedRadioButton.getText().toString();
                        Toast.makeText(MainActivity.this, selected_radio_btn, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            TextView forgot_pass = findViewById(R.id.forgot_pass);
            forgot_pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String usernameInput = username.getText().toString().trim();

                    if (usernameInput.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Please enter Username", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Check if the username exists in the database
                    if (db.checkUserName(usernameInput)) { // Implement this method in DatabaseHandler
                        Intent intent = new Intent(MainActivity.this, ResetPassword.class);
                        intent.putExtra("USERNAME", usernameInput); // Pass the username to the next activity
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Username does not exist", Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, "If you are a Employee contact administration.", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            Button login = findViewById(R.id.login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String usernameInput = username.getText().toString().trim();
                    String passwordInput = password.getText().toString().trim();

                    if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    if ("User".equals(selected_radio_btn)) {
                        if (db.checkUser(usernameInput, passwordInput)) {
                            Toast.makeText(MainActivity.this, "Success login", Toast.LENGTH_SHORT).show();
                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("userType", "User");

                            // Assuming getUserId returns the user's ID
                            int userId = db.getUserId(usernameInput); // This should be defined in your DatabaseHandler class
                            editor.putInt("USER_ID", userId); // Store userId in SharedPreferences
                            editor.apply();

                            Intent intent = new Intent(MainActivity.this, UserHome.class);
                            intent.putExtra("USER_ID", userId); // Pass userId
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    } else if ("Employee".equals(selected_radio_btn)) {
                        if (db.checkEmployeeCredentials(usernameInput, passwordInput)) {
                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("userType", "Employee");

                            int empId = db.getEmpId(usernameInput);
                            editor.putInt("EmpId",empId);
                            editor.apply();

                            Intent intent = new Intent(MainActivity.this, EmpHome.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            TextView register = findViewById(R.id.register);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, Register.class);
                    startActivity(intent);
                }
            });
        }
    }
}
