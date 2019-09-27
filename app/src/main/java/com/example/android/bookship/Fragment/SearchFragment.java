package com.example.android.bookship.Fragment;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.bookship.Adapter.BookAdapter;
import com.example.android.bookship.Loader.BookLoader;
import com.example.android.bookship.Class.Books;
import com.example.android.bookship.DetailActivity;
import com.example.android.bookship.R;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<Books>> {

    private View rootView;

    private String BOOK_REQUEST_URL;

    private static String prevSearch;

    private int check = 0, n = 0;

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    /**
     * Adapter for the list of books
     */
    private BookAdapter mAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    private Context mcontext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        final EditText bookSearch = (EditText) rootView.findViewById(R.id.book_search_editText);

        ImageButton searchButton = (ImageButton) rootView.findViewById(R.id.search_button);

        // Find a reference to the {@link ListView} in the layout
        final ListView bookListView = (ListView) rootView.findViewById(R.id.list);

        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_list_view);
        bookListView.setEmptyView(mEmptyStateTextView);
        mEmptyStateTextView.setText(R.string.search_above);

        // Initially don't show the loading Indicator
        View loadingIndicator = rootView.findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);

        // Create a new adapter that takes an empty list of books as input
        mAdapter = new BookAdapter(getActivity(), new ArrayList<Books>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        // On clicking each item move to Detail Activity of that class
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), DetailActivity.class);

                // getting the book at current position to get the detailURLLink
                Books currentBook = (Books) bookListView.getItemAtPosition(position);

                intent.putExtra("detailsURLLink", currentBook.getSelfLink());
                startActivity(intent);
            }
        });

        ConnectivityManager connMgr =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get DetailActivity on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        prevSearch = "";
        if (networkInfo != null && networkInfo.isConnected()) {
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String search = bookSearch.getText().toString().trim();
                    if(!prevSearch.equals(search) && n > 0)
                        check = 1;
                    if (search.isEmpty()) {
                        bookSearch.setError("Please enter a valid book name.");
                        bookSearch.requestFocus();
                        return;
                    }
                    search(search);
                }
            });
        } else {
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }

        // apply SwipeRefresh feature to the searchFragment
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                UpdateOperation();
            }
        });

        return rootView;
    }

    private void search(String search) {

        mEmptyStateTextView.setText("");
        View loadingIndicator = rootView.findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.VISIBLE);

        BOOK_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=" + search + "&maxResults=25";

        if (check == 1) {
            getLoaderManager().restartLoader(BOOK_LOADER_ID, null, this);
            mAdapter.clear();
            prevSearch = search;
        } else {
            // Start the AsyncTask to fetch the earthquake data
            // Get a reference to the LoaderManager, in order to interact with loaders.
            android.support.v4.app.LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        }

        n++;
    }

    @Override
    public android.support.v4.content.Loader<List<Books>> onCreateLoader(int id, Bundle urls) {
        // Create a new loader for the given URL
        return new BookLoader(getActivity(), BOOK_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<Books>> loader, List<Books> books) {

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's.
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        } else {
            // Set empty state text to display "No books found."
            mEmptyStateTextView.setText(R.string.no_books);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = rootView.findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<Books>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    private void UpdateOperation() {
        mAdapter.clear();
        mEmptyStateTextView.setVisibility(View.GONE);

        ConnectivityManager connMgr =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get DetailActivity on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Start the AsyncTask to fetch the earthquake data
            // Get a reference to the LoaderManager, in order to interact with loaders.
            android.support.v4.app.LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = rootView.findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);

            // make the TextView visible
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
    }
}
