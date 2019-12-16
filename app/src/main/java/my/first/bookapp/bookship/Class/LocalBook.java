package my.first.bookapp.bookship.Class;

import java.util.Date;

public class LocalBook {

    private String bookName;

    private Date lastModified;

    private int pageCount;


    public LocalBook(String bookName, Date lastModified, int pageCount) {
        this.bookName = bookName;
        this.lastModified = lastModified;
        this.pageCount = pageCount;
    }

    public String getBookName() {
        return bookName;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public int getPageCount() {
        return pageCount;
    }
}
