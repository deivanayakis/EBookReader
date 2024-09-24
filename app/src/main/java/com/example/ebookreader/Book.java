package com.example.ebookreader;

import android.net.Uri;

public class Book {

    private String email;
    private String bookName;
    private String authorName;
    private String genre;
    private String publisher;
    private String publicationYear;
    private String coverImage;
    private String pdf;

    public Book(String email, String bookName, String authorName, String genre, String publisher, String publicationYear, String coverImage, String pdf) {
        this.email = email;
        this.bookName = bookName;
        this.authorName = authorName;
        this.genre = genre;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.coverImage = coverImage;
        this.pdf = pdf;
    }

    // Getters
    public String getBookName() {
        return bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getGenre() {
        return genre;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getCoverImage(){
        return coverImage;
    }
}
