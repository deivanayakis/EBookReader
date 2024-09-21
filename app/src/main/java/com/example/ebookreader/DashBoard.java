package com.example.ebookreader;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DashBoard extends AppCompatActivity {

    DatabaseHelper dbhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbhelper = new DatabaseHelper(this);


        setContentView(R.layout.activity_dash_board);
        Intent intent = getIntent();

        String email = intent.getStringExtra("email");

        String name = dbhelper.getUserNameByEmail(email);

        if (name != null && !name.isEmpty()) {
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }

        TextView emailTextView = findViewById(R.id.textView);
        emailTextView.setText("Hi "+name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    // Step 2: Handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upload_book:
                // Handle Upload Book click
                Toast.makeText(this, "Upload Book clicked", Toast.LENGTH_SHORT).show();
                // You can add an intent to open a new activity for book upload
                // Intent uploadIntent = new Intent(this, UploadBookActivity.class);
                // startActivity(uploadIntent);
                return true;

            case R.id.logout:
                // Handle Logout click
                Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
                // Here you can log the user out and redirect to login activity
                // Intent logoutIntent = new Intent(this, LoginActivity.class);
                // startActivity(logoutIntent);
                // finish();  // Close the current activity
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}