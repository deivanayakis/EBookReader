package com.example.ebookreader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class UploadBook extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_PDF_REQUEST = 2;

    String email;
    private EditText editTextBookName, editTextAuthorName, editTextGenre, editTextPublisher;
    private Spinner spinnerPublicationYear;
    private Button buttonUploadCover, buttonAttachPdf, buttonSubmit;
    private ImageView imageViewCoverPreview;

    private Uri pdfUri;
    private Uri imageUri;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_book);

        databaseHelper = new DatabaseHelper(this);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        // Create a list of years for the spinner
        ArrayList<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for (int year = 1950; year <= currentYear; year++) {
            years.add(String.valueOf(year));
        }

        // Initialize views
        editTextBookName = findViewById(R.id.editTextBookName);
        editTextAuthorName = findViewById(R.id.editTextAuthorName);
        editTextGenre = findViewById(R.id.editTextGenre);
        editTextPublisher = findViewById(R.id.editTextPublisher);
        spinnerPublicationYear = findViewById(R.id.spinnerPublicationYear);
        buttonUploadCover = findViewById(R.id.buttonUploadCover);
        buttonAttachPdf = findViewById(R.id.buttonAttachPdf);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        imageViewCoverPreview = findViewById(R.id.imageViewCoverPreview);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPublicationYear.setAdapter(yearAdapter);

        // Set onClickListeners
        buttonUploadCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        buttonAttachPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePdf();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void choosePdf() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    imageViewCoverPreview.setImageBitmap(bitmap);
                    imageViewCoverPreview.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == PICK_PDF_REQUEST) {
                pdfUri = data.getData();
                Toast.makeText(this, "PDF Attached: " + pdfUri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void submitForm() {
        String bookName = editTextBookName.getText().toString();
        String authorName = editTextAuthorName.getText().toString();
        String genre = editTextGenre.getText().toString();
        String publisher = editTextPublisher.getText().toString();
        String publicationYear = spinnerPublicationYear.getSelectedItem().toString();
        String coverImageUri = (imageUri != null) ? imageUri.toString() : null;
        String pdfUriString = (pdfUri != null) ? pdfUri.toString() : null;

        // Simple validation check
        if (bookName.isEmpty() || authorName.isEmpty() || genre.isEmpty() || publisher.isEmpty() || imageUri == null || pdfUri == null) {
            Toast.makeText(this, "Please fill all the fields and attach files", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the book already exists
        Boolean bookExists = databaseHelper.checkBookExists(bookName);

        Log.d("MyTag", "Checked");

        if (bookExists) {
            Toast.makeText(this, "Book with this name already exists", Toast.LENGTH_SHORT).show();
        } else {
            // Insert book data
            Boolean insertSuccess = databaseHelper.insertBookData(email,bookName, authorName, genre, publisher, publicationYear, coverImageUri, pdfUriString);

            if (insertSuccess) {
                Toast.makeText(this, "Book Submitted Successfully", Toast.LENGTH_SHORT).show();
                clearForm();
            } else {
                Toast.makeText(this, "Failed to Submit Book", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Clear the form after successful submission
    private void clearForm() {
        editTextBookName.setText("");
        editTextAuthorName.setText("");
        editTextGenre.setText("");
        editTextPublisher.setText("");
        spinnerPublicationYear.setSelection(0);
        imageViewCoverPreview.setVisibility(View.GONE);
        imageUri = null;
        pdfUri = null;
    }
}
