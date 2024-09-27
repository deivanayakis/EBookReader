package com.example.ebookreader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PdfViewer extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        //imageView = findViewById(R.id.imageView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            displayPdf();
        }
    }

    private void displayPdf() {
        Uri pdfUri = Uri.parse(getIntent().getStringExtra("pdfUri")); // Retrieve the PDF URI
        if (pdfUri != null) {
            try {
                ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(pdfUri, "r");
                if (parcelFileDescriptor != null) {
                    PdfRenderer pdfRenderer = new PdfRenderer(parcelFileDescriptor);
                    PdfRenderer.Page page = pdfRenderer.openPage(0); // Open the first page

                    Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                    imageView.setImageBitmap(bitmap);

                    page.close();
                    pdfRenderer.close();
                } else {
                    Toast.makeText(this, "Failed to open PDF.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("PdfViewerActivity", "Error displaying PDF: " + e.getMessage());
                Toast.makeText(this, "Error displaying PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("PdfViewerActivity", "PDF URI is null.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayPdf();
            } else {
                Toast.makeText(this, "Permission denied to read the book.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
