package com.example.ebookreader;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class DashBoard extends AppCompatActivity {

    DatabaseHelper dbHelper;
    private LinearLayout bookContainer;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(this);


        setContentView(R.layout.activity_dash_board);
        Intent intent = getIntent();

        email = intent.getStringExtra("email");

        String name = dbHelper.getUserNameByEmail(email);

        if (name != null && !name.isEmpty()) {
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }

        TextView emailTextView = findViewById(R.id.textView);
        emailTextView.setText("Hi "+name);

        ImageView menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(v -> showMenu());


        bookContainer = findViewById(R.id.bookContainer);  // Reference the container in activity_dashboard.xml
        dbHelper = new DatabaseHelper(this);

        // Fetch books from the database
        List<Book> books = dbHelper.getAllBooks();

        // Inflate and add each book to the container
        for (Book book : books) {
            addBookToContainer(book);
        }
    }

    private void showMenu() {
        // Create a PopupMenu
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.menu_icon));
        popupMenu.getMenuInflater().inflate(R.menu.dashboard_menu, popupMenu.getMenu());

        // Set click listener for menu items
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.upload_book) {  // Replace with your actual ID from R.java
                Toast.makeText(this, "Let's start to upload your book", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DashBoard.this, UploadBook.class);
                intent.putExtra("email", email);
                startActivity(intent);
                return true;
            } else if (id == R.id.logout) {  // Replace with your actual ID from R.java
                Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent);
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        });

        // Show the menu
        popupMenu.show();
    }


    private void addBookToContainer(Book book) {
        // Inflate bookitem.xml
        LayoutInflater inflater = LayoutInflater.from(this);
        View bookItemView = inflater.inflate(R.layout.book_item, bookContainer, false);

        // Set book details to views in bookitem.xml
        TextView bookNameTextView = bookItemView.findViewById(R.id.textView6);  // Book name
        TextView authorNameTextView = bookItemView.findViewById(R.id.textView7);  // Author name
        TextView genreTextView = bookItemView.findViewById(R.id.textView8);  // Genre
        TextView publisherTextView = bookItemView.findViewById(R.id.textView9);  // Publisher

        ImageView coverImage = bookItemView.findViewById(R.id.imageView8);



        bookNameTextView.setText(book.getBookName());
        authorNameTextView.setText(book.getAuthorName());
        genreTextView.setText(book.getGenre());
        publisherTextView.setText(book.getPublisher());

        // Add the inflated view to the container
        bookContainer.addView(bookItemView);
    }


}