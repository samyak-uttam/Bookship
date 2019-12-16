package my.first.bookapp.bookship.Data;

import android.provider.BaseColumns;

public class BookContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private BookContract() {}

    public static final class BookEntry implements BaseColumns {

        public final static String TABLE_NAME = "books";

        /**
         * Unique ID number for the book (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the book.
         *
         * Type: TEXT
         */
        public final static String COLUMN_BOOK_NAME = "name";

        /**
         * URL of the book picture.
         *
         * Type: TEXT
         */
        public final static String COLUMN_BOOK_PIC_URL = "picurl";

        /**
         * Self Link of the book.
         *
         * Type: TEXT
         */
        public final static String COLUMN_BOOK_SELF_LINK = "selfLink";
    }
}
