package com.example.ebookreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String databaseName = "Book.db";

    public DatabaseHelper(@Nullable Context context)
    {
        super(context,databaseName,null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table users(name TEXT not null,email TEXT primary key,password TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE books(" +
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

    public Boolean insertBookData(String bookName, String authorName, String genre, String publisher, String publicationYear, String coverImageUri, String pdfUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
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



}
