package com.example.ebookreader;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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


        List<Book> books = dbHelper.getAllBooks();

        for (Book book : books) {
            addBookToContainer(book);
        }
    }

    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.menu_icon));
        popupMenu.getMenuInflater().inflate(R.menu.dashboard_menu, popupMenu.getMenu());

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

        popupMenu.show();
    }


    private void addBookToContainer(Book book) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View bookItemView = inflater.inflate(R.layout.book_item, bookContainer, false);

        TextView bookNameTextView = bookItemView.findViewById(R.id.textView6);
        TextView authorNameTextView = bookItemView.findViewById(R.id.textView7);
        TextView genreTextView = bookItemView.findViewById(R.id.textView8);
        TextView publisherTextView = bookItemView.findViewById(R.id.textView9);
        ImageView coverImageView = bookItemView.findViewById(R.id.imageView8);

        if (book.getCoverImage() != null && !book.getCoverImage().isEmpty()) {
            Bitmap coverImageBitmap = decodeBase64(book.getCoverImage());
            coverImageView.setImageBitmap(coverImageBitmap);
        } else {
            coverImageView.setImageResource(R.drawable.uploadmenu);
        }


        bookNameTextView.setText(book.getBookName());
        authorNameTextView.setText(book.getAuthorName());
        genreTextView.setText(book.getGenre());
        publisherTextView.setText(book.getPublisher());

        bookContainer.addView(bookItemView);
        Button readNowButton = bookItemView.findViewById(R.id.button3);
        readNowButton.setOnClickListener(v -> {
            String pdfUriString = book.getPdf();
            Uri pdfUri = Uri.parse(pdfUriString);

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(pdfUri, "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NO_HISTORY);

                PackageManager packageManager = getPackageManager();
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "No PDF reader found. Please install one.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error opening the PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private Bitmap decodeBase64(String encodedImage) {
        byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}