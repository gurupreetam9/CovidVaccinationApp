package com.example.covidvaccination;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CheckUsers extends AppCompatActivity {

    ListView listview;
    ArrayList<String> users;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_users);
        ImageButton prev = findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listview = findViewById(R.id.listview);
        users = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
        listview.setAdapter(adapter);
        loadUsers();
    }
    private void loadUsers() {
        DatabaseHandler db = new DatabaseHandler(this);
        Cursor cursor = db.getReadableDatabase().rawQuery("SELECT * FROM Users", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                //String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                String city = cursor.getString(cursor.getColumnIndexOrThrow("city"));
                String state = cursor.getString(cursor.getColumnIndexOrThrow("state"));
                String phone_no = cursor.getString(cursor.getColumnIndexOrThrow("phone_no"));
                String vaccination_status = cursor.getString(cursor.getColumnIndexOrThrow("vaccination_status"));

                String userInfo = "Name " + name + "\n"
                        + "Username: " + username + "\n"
                        //+ "Password: " + password + "\n"
                        + "City: " + city + "\n"
                        + "State: " + state + "\n"
                        + "Phone: " + phone_no + "\n"
                        + "Vaccination Status: " + vaccination_status;

                users.add(userInfo);
            } while (cursor.moveToNext());
            cursor.close();
        }
        else {
            users.add("No users registered");
        }
        adapter.notifyDataSetChanged();
    }
}
