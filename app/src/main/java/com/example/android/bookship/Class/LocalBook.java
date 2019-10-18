package com.example.android.bookship.Class;

public class LocalBook {
    private String bookName;

    private int bookImage;

    public LocalBook(String bookName, int book_image) {
        this.bookName = bookName;
        this.bookImage = book_image;
    }

    public String getBookName() {
        return bookName;
    }

    public int getBookImage() {
        return bookImage;
    }
}
