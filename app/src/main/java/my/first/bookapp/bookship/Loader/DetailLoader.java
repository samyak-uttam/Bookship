package my.first.bookapp.bookship.Loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import my.first.bookapp.bookship.Class.Details;
import my.first.bookapp.bookship.Queryclass;

public class DetailLoader extends AsyncTaskLoader<Details> {

    /** Query URL */
    private String murl;

    // constructor for DetailLoader
    public DetailLoader(Context context, String url) {
        super(context);
        murl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Details loadInBackground() {
        if(murl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract the details of book.
        Details bookDetails = Queryclass.fetchBookDetails(murl);
        return bookDetails;
    }
}
