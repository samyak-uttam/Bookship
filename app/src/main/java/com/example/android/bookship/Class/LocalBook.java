package com.example.android.bookship.Class;

import android.net.Uri;

import java.util.Date;

public class LocalBook {
    private String bookName;

    private int bookImage;

    private Date lastModified;


    public LocalBook(String bookName, int book_image, Date lastModified) {
        this.bookName = bookName;
        this.bookImage = book_image;
        this.lastModified = lastModified;
    }

    public String getBookName() {
        return bookName;
    }

    public int getBookImage() {
        return bookImage;
    }

    public Date getLastModified() {
        return lastModified;
    }
}
