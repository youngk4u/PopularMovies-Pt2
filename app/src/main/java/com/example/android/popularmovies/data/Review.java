package com.example.android.popularmovies.data;

public class Review {
    private String author;
    private String comment;

    public Review(String author, String comment) {
        this.author = author;
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public String getComment() {
        return comment;
    }
}
