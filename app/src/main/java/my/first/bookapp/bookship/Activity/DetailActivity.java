package my.first.bookapp.bookship.Activity;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.first.bookapp.bookship.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import my.first.bookapp.bookship.Class.Details;
import my.first.bookapp.bookship.Data.BookContract.BookEntry;
import my.first.bookapp.bookship.Data.BookDbHelper;
import my.first.bookapp.bookship.Loader.DetailLoader;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements LoaderCallbacks<Details> {

    // URL for getting the details of the book
    private String BOOK_DETAILS_REQUEST_URL;

    /**
     * ImageView that is displayed when the list is empty
     */
    private ImageView mEmptyStateImageView;

    private LinearLayout MainDetail;

    private FloatingActionButton favButton;

    private String bookName, bookImageRequestUrl, selfLink;

    private BookDbHelper mDbHelper;

    private boolean isClicked = false;
    // List for authors
    final List<String> authors = new ArrayList<>();

    // new static class for holding all the views
    private static class ViewHolder {
        ImageView image;
        TextView title;
        TextView authors;
        TextView description;
        TextView pubDate_hardcoded;
        TextView pubDate;
        TextView pageCount_hardcoded;
        TextView pageCount;
        TextView infoLink;
        TextView sampleLink;
        RatingBar ratingBar;
    }

    // object of the class ViewHolder
    DetailActivity.ViewHolder viewHolder;
    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        viewHolder = new DetailActivity.ViewHolder();

        viewHolder.image = (ImageView) findViewById(R.id.book_image);
        viewHolder.title = (TextView) findViewById(R.id.book_title);
        viewHolder.authors = (TextView) findViewById(R.id.authors);
        viewHolder.description = (TextView) findViewById(R.id.book_desccription);
        viewHolder.pubDate_hardcoded = (TextView) findViewById(R.id.published_date_harcoded);
        viewHolder.pubDate = (TextView) findViewById(R.id.published_date);
        viewHolder.pageCount_hardcoded = (TextView) findViewById(R.id.page_count_harcoded);
        viewHolder.pageCount = (TextView) findViewById(R.id.page_count);
        viewHolder.infoLink = (TextView) findViewById(R.id.info_link);
        viewHolder.sampleLink = (TextView) findViewById(R.id.sample_link);
        viewHolder.ratingBar = (RatingBar) findViewById(R.id.book_rating);
        MainDetail = (LinearLayout) findViewById(R.id.scrollViewMain);

        favButton = (FloatingActionButton) findViewById(R.id.fav_button);

        BOOK_DETAILS_REQUEST_URL = getIntent().getExtras().getString("detailsURLLink");
        selfLink = BOOK_DETAILS_REQUEST_URL;

        mDbHelper = new BookDbHelper(this);

        mEmptyStateImageView = (ImageView) findViewById(R.id.empty_view);

        View loadingIndicator = findViewById(R.id.loading_spinner_detail);

        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get DetailActivity on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Start the AsyncTask to fetch the earthquake data
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            loadingIndicator.setVisibility(View.VISIBLE);

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible

            loadingIndicator.setVisibility(View.GONE);

            MainDetail.setVisibility(View.GONE);
            // Update empty state with no connection error message
            mEmptyStateImageView.setVisibility(View.VISIBLE);
            mEmptyStateImageView.setImageResource(R.drawable.nores);
        }

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClick();
            }
        });

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new DetailLoader(this, BOOK_DETAILS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader loader, Details details) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_spinner_detail);
        loadingIndicator.setVisibility(View.GONE);

        if (details != null) {
            mEmptyStateImageView.setVisibility(View.GONE);
            MainDetail.setVisibility(View.VISIBLE);
            set(details);
        } else {
            MainDetail.setVisibility(View.GONE);
            mEmptyStateImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        clear();
    }

    // function to set the values of each view
    public void set(final Details details) {

        bookName = details.getBookname();
        viewHolder.title.setText(bookName);

        bookImageRequestUrl = details.getBookImageRequestURL();
        if (bookImageRequestUrl != null) {
            Picasso.get().load(bookImageRequestUrl).into(viewHolder.image);
        } else
            viewHolder.image.setImageResource(R.drawable.download);

        // make sure that the image is visible
        viewHolder.image.setVisibility(View.VISIBLE);

        if (details.getAuthornames() == null)
            viewHolder.authors.setText("No Authors provided.");
        else {
            // Get the author name from the current book object and
            // set this text on the number TextView
            StringBuilder stringBuilder = new StringBuilder();
            for (String names : details.getAuthornames()) {
                stringBuilder.append(names + ",");
            }
            // Ti convert the stringBuilder to string
            String str = stringBuilder.toString();

            // To remove the last ','
            str = str.substring(0, str.length() - 1);

            viewHolder.authors.setText(str);
        }

        if (details.getBookDescription() == null)
            viewHolder.description.setText("No description provided.");
        else
            viewHolder.description.setText(Jsoup.parse(details.getBookDescription()).text());

        viewHolder.pubDate_hardcoded.setText("Published Date: ");

        viewHolder.pubDate.setText(details.getPublishedDate());

        viewHolder.pageCount_hardcoded.setText("Total Pages: ");

        viewHolder.pageCount.setText(details.getPageCount() + "");

        viewHolder.ratingBar.setRating((float) details.getRating());

        viewHolder.infoLink.setText("More info");

        viewHolder.infoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse(details.getInfoLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(intent);
            }
        });

        viewHolder.sampleLink.setText("Sample");

        viewHolder.sampleLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (details.getSampleLink() == null) {
                    Toast.makeText(getApplicationContext(), "Sorry, no sample is available.", Toast.LENGTH_SHORT).show();
                } else {
                    Uri webpage = Uri.parse(details.getSampleLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(intent);
                }
            }
        });

        View loadingIndicator = findViewById(R.id.loading_spinner_detail);
        loadingIndicator.setVisibility(View.GONE);
    }

    private void handleClick() {

        SQLiteDatabase readableDatabase = mDbHelper.getReadableDatabase();

        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME };

        Cursor cursor = readableDatabase.query(BookEntry.TABLE_NAME, projection, null,
                null, null, null, null);

        int check = 0;

        try {
            if(cursor.getCount() > 0) {

                int bookIdIndex = cursor.getColumnIndex(BookEntry._ID);
                int bookNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);

                while (cursor.moveToNext()) {
                    if(cursor.getString(bookNameColumnIndex).equals(bookName)) {
                        check = 1;
                        SQLiteDatabase writableDatabase = mDbHelper.getWritableDatabase();
                        writableDatabase.delete(BookEntry.TABLE_NAME, BookEntry._ID + "=" + cursor.getInt(bookIdIndex), null);
                        Toast.makeText(this, "Book removed from Favourites.",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
            if(cursor.getCount() == 0 || check == 0) {
                SQLiteDatabase writableDatabase = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(BookEntry.COLUMN_BOOK_NAME, bookName);
                values.put(BookEntry.COLUMN_BOOK_PIC_URL, bookImageRequestUrl);
                values.put(BookEntry.COLUMN_BOOK_SELF_LINK, selfLink);

                writableDatabase.insert(BookEntry.TABLE_NAME, null, values);

                Toast.makeText(this, "Book added to Favourites.",Toast.LENGTH_SHORT).show();
            }
        } finally {
            // finally close the cursor
            cursor.close();
        }
    }

    // function to clear all the values
    public void clear() {
        authors.clear();
        viewHolder.image.setVisibility(View.GONE);
        viewHolder.title.setText("");
        viewHolder.description.setText("");
        viewHolder.pubDate_hardcoded.setText("");
        viewHolder.pubDate.setText("");
        viewHolder.pageCount_hardcoded.setText("");
        viewHolder.pageCount.setText("");
        viewHolder.infoLink.setText("");
        viewHolder.sampleLink.setText("");
        viewHolder.ratingBar.setRating(0);
    }

    private void UpdateOperation() {
        clear();

        mEmptyStateImageView.setVisibility(View.GONE);
        MainDetail.setVisibility(View.GONE);

        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get DetailActivity on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Start the AsyncTask to fetch the earthquake data
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_spinner_detail);
            loadingIndicator.setVisibility(View.GONE);

            MainDetail.setVisibility(View.GONE);
            // Update empty state with no connection error message
            mEmptyStateImageView.setVisibility(View.VISIBLE);
            mEmptyStateImageView.setImageResource(R.drawable.nores);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public  boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            finish();
            return true;
        }
        UpdateOperation();
        return super.onOptionsItemSelected(item);
    }
}
