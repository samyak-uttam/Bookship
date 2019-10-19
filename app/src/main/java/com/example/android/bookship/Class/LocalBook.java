package com.example.android.bookship.Class;

import android.net.Uri;

import java.util.Date;

public class LocalBook {

    private String bookName;

    private Date lastModified;


    public LocalBook(String bookName, Date lastModified) {
        this.bookName = bookName;
        this.lastModified = lastModified;
    }

    public String getBookName() {
        return bookName;
    }

    public Date getLastModified() {
        return lastModified;
    }
}
