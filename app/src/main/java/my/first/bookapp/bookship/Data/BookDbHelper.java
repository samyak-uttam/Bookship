package my.first.bookapp.bookship.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import my.first.bookapp.bookship.Data.BookContract.BookEntry;

public class BookDbHelper extends SQLiteOpenHelper {


    public static final String LOG_TAG = BookDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "books.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    public BookDbHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + " ("
                + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookEntry.COLUMN_BOOK_NAME + " TEXT NOT NULL, "
                + BookEntry.COLUMN_BOOK_PIC_URL + " TEXT, "
                + BookEntry.COLUMN_BOOK_SELF_LINK + " TEXT NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
