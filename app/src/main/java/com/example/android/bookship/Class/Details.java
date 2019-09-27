package com.example.android.bookship.Class;

import java.util.List;

public class Details {

    /**
     * the name of the particular book
     **/
    private String mbookname;

    /**
     * the name of the author of the book
     **/
    private List<String> mauthornames;

    /**
     * the Image Request URL of the book
     **/
    private String mbookImageRequestURL;

    /**
     * the description of the book
     **/
    private String mbookDescription;

    /**
     * the rating of the book
     **/
    private double mrating;

    /**
     * the pageCount of the book
     **/
    private int mpageCount;

    /**
     * the publishing date of the book
     **/
    private String mpublishedDate;

    /**
     * the info Link of the book
     **/
    private String minfoLink;

    private String msampleLink;

    public Details(String mbookname, List<String> mauthornames, String mbookImageRequestURL, String mbookDescription, double mrating, int mpageCount, String mpublishedDate, String minfoLink, String msampleLink) {
        this.mbookname = mbookname;
        this.mauthornames = mauthornames;
        this.mbookImageRequestURL = mbookImageRequestURL;
        this.mbookDescription = mbookDescription;
        this.mrating = mrating;
        this.mpageCount = mpageCount;
        this.mpublishedDate = mpublishedDate;
        this.minfoLink = minfoLink;
        this.msampleLink = msampleLink;
    }

    public String getBookname() {
        return mbookname;
    }

    public List<String> getAuthornames() {
        return mauthornames;
    }

    public String getBookImageRequestURL() {
        return mbookImageRequestURL;
    }

    public String getBookDescription() {
        return mbookDescription;
    }

    public double getRating() {
        return mrating;
    }

    public int getPageCount() {
        return mpageCount;
    }

    public String getPublishedDate() {
        return mpublishedDate;
    }

    public String getInfoLink() {
        return minfoLink;
    }

    public String getSampleLink() {
        return msampleLink;
    }
}
