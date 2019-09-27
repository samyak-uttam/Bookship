package com.example.android.bookship.Class;

public class Genres {

    /** Name of the current genre **/
    private String genreName;

    /** Image resource ID for the word */
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    /** Constant value that represents no image was provided for this word */
    private static final int NO_IMAGE_PROVIDED = -1;

    public Genres(String genreName, int mImageResourceId) {
        this.genreName = genreName;
        this.mImageResourceId = mImageResourceId;
    }

    /**
     * Return the name of the genre.
     */
    public String getGenreName() {
        return genreName;
    }

    /**
     * Return the image resource ID of the genre.
     */
    public int getmImageResourceId() {
        return mImageResourceId;
    }

    /**
     * Returns whether or not there is an image for this word.
     */
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
}
