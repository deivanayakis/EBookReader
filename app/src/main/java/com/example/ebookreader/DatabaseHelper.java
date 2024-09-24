package com.example.ebookreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String databaseName = "Book.db";

    public DatabaseHelper(@Nullable Context context)
    {
        super(context,databaseName,null,3);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table users(name TEXT not null,email TEXT primary key,password TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE books(" +
                "email TEXT NOT NULL, "+
                "book_name TEXT NOT NULL, " +
                "author_name TEXT NOT NULL, " +
                "genre TEXT NOT NULL, " +
                "publisher TEXT NOT NULL, " +
                "publication_year TEXT NOT NULL, " +
                "cover_image TEXT, " +  // Can store as URI or file path
                "pdf TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists users");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS books");
        onCreate(sqLiteDatabase);
    }

    public Boolean insertData(String name, String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("email",email);
        contentValues.put("password",password);
        long result = db.insert("users",null,contentValues);
        if(result==-1)
        {
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean checkEmail(String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where email = ?",new String[]{email});

        if (cursor.getCount()>0)
        {
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean checkEmailPassword(String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where email=? and password=?",new String[]{email,password});
        if (cursor.getCount()>0)
        {
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean insertBookData(String email,String bookName, String authorName, String genre, String publisher, String publicationYear, String coverImageUri, String pdfUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email",email);
        contentValues.put("book_name", bookName);
        contentValues.put("author_name", authorName);
        contentValues.put("genre", genre);
        contentValues.put("publisher", publisher);
        contentValues.put("publication_year", publicationYear);
        contentValues.put("cover_image", coverImageUri);
        contentValues.put("pdf", pdfUri);

        long result = db.insert("books", null, contentValues);
        return result != -1;  // Return true if insertion is successful
    }

    public Boolean checkBookExists(String bookName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM books WHERE book_name = ?", new String[]{bookName});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;  // Book already exists
        } else {
            cursor.close();
            return false;  // Book does not exist
        }
    }

    public String getUserNameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM users WHERE email = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            cursor.close();
            return name;  // Return the name if found
        } else {
            cursor.close();
            return null;  // Return null if no user found with that email
        }
    }


    public List<Book> getAllBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM books", null);

        List<Book> books = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String bookName = cursor.getString(cursor.getColumnIndex("book_name"));
                String authorName = cursor.getString(cursor.getColumnIndex("author_name"));
                String genre = cursor.getString(cursor.getColumnIndex("genre"));
                String publisher = cursor.getString(cursor.getColumnIndex("publisher"));
                String publicationYear = cursor.getString(cursor.getColumnIndex("publication_year"));
                String coverImage = cursor.getString(cursor.getColumnIndex("cover_image"));
                String pdf = cursor.getString(cursor.getColumnIndex("pdf"));

                // Create a Book object and add it to the list
                books.add(new Book(email, bookName, authorName, genre, publisher, publicationYear, coverImage, pdf));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return books;
    }

}
