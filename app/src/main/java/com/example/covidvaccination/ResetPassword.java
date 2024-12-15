package com.example.covidvaccination;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class ResetPassword extends AppCompatActivity {
    private TextInputEditText phoneNoInput, newPasswordInput;
    private Button resetPasswordButton;
    private DatabaseHandler db;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        ImageButton prev = findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        phoneNoInput = findViewById(R.id.phone_no);
        newPasswordInput = findViewById(R.id.new_pass);
        resetPasswordButton = findViewById(R.id.btn_reset_password);
        db = new DatabaseHandler(this);

        username = getIntent().getStringExtra("USERNAME");

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNoInput.getText().toString().trim();
                String newPassword = newPasswordInput.getText().toString().trim();

                if (phoneNumber.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(ResetPassword.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (db.verifyPhoneNumber(username, phoneNumber)) {
                    if (db.updatePassword(username, newPassword)) {
                        Toast.makeText(ResetPassword.this, "Password reset successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetPassword.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ResetPassword.this, "Error resetting password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ResetPassword.this, "Phone number does not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
