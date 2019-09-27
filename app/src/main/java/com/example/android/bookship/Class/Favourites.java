package com.example.android.bookship.Class;

public class Favourites {

    /** the name of the particular book **/
    private String mbookName;

    /** the Image Request URL of the book **/
    private String mbookImageRequestURL;

    /** the Self Link of the book **/
    private String mselfLink;

    public Favourites(String mbookName, String mbookImageRequestURL, String mselfLink) {
        this.mbookName = mbookName;
        this.mbookImageRequestURL = mbookImageRequestURL;
        this.mselfLink = mselfLink;
    }

    public String getMbookName() {
        return mbookName;
    }

    public String getMbookImageRequestURL() {
        return mbookImageRequestURL;
    }

    public String getMselfLink() {
        return mselfLink;
    }
}
