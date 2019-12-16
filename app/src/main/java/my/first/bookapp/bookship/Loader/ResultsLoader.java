package my.first.bookapp.bookship.Loader;

import android.content.Context;
import android.content.AsyncTaskLoader;

import my.first.bookapp.bookship.Class.Books;
import my.first.bookapp.bookship.Queryclass;

import java.util.List;

/**
 * Loads a list of books by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class ResultsLoader extends AsyncTaskLoader<List<Books>> {

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link BookLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public ResultsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Books> loadInBackground() {
        if(mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of books.
        List<Books> books = Queryclass.fetchBookData(mUrl);
        return books;
    }
}
