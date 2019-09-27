package com.example.android.bookship.Class;

import java.util.List;

public class Books {

    private String mselfLink;

    /** the name of the particular book **/
    private String mbookname;

    /** the name of the author of the book **/
    private List<String> mauthornames ;

    /** the Image Request URL of the book **/
    private String mbookImageRequestURL;

    /** the description of the book **/
    private String mbookDescription;

    /** the rating of the book **/
    private double mrating;

    // constructor function for the class.
    public Books(String selfLink, String bookname,List<String> authornames,String bookImageRequestURL,String bookDescription,double rating) {
        mbookname = bookname;
        mauthornames = authornames;
        mbookImageRequestURL = bookImageRequestURL;
        mbookDescription = bookDescription;
        mrating = rating;
        mselfLink = selfLink;
    }

    public String getSelfLink (){
        return mselfLink;
    }

    public String getbookname(){
        return mbookname;
    }

    public List<String> getAuthornames(){
        return mauthornames;
    }

    public String getbookImageRequestURL(){
        return mbookImageRequestURL;
    }

    public String getbookDescription(){
        return mbookDescription;
    }

    public double getRating(){
        return mrating;
    }
}
