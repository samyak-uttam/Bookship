package my.first.bookapp.bookship.Fragment;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import my.first.bookapp.bookship.Adapter.FavouriteAdapter;
import my.first.bookapp.bookship.Class.Favourites;
import my.first.bookapp.bookship.Data.BookContract.BookEntry;
import my.first.bookapp.bookship.Data.BookDbHelper;
import my.first.bookapp.bookship.Activity.DetailActivity;
import com.first.bookapp.bookship.R;

import java.util.ArrayList;

public class FavouriteFragment extends Fragment {

    private BookDbHelper mDbHelper;

    private TextView emptyStateTextView;

    private View rootView;

    FavouriteAdapter adapter;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_favourite, container, false);

        emptyStateTextView = (TextView) rootView.findViewById(R.id.empty_list_view);

        mDbHelper = new BookDbHelper(getContext());

        ConnectivityManager connMgr =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get DetailActivity on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            loadFav();
        } else {
            // Update empty state with no connection error message
            emptyStateTextView.setText(R.string.no_internet_connection);
            emptyStateTextView.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ConnectivityManager connMgr =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get DetailActivity on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            loadFav();
        } else {
            // Update empty state with no connection error message
            emptyStateTextView.setText(R.string.no_internet_connection);
            emptyStateTextView.setVisibility(View.VISIBLE);
        }
    }

    private void loadFav() {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_PIC_URL,
                BookEntry.COLUMN_BOOK_SELF_LINK};

        Cursor cursor = database.query(BookEntry.TABLE_NAME, projection, null,
                null, null, null, null);

        try {

            if (cursor.getCount() == 0) {
                emptyStateTextView.setText(R.string.empty_fav);
                emptyStateTextView.setVisibility(View.VISIBLE);
            } else {
                emptyStateTextView.setVisibility(View.GONE);

                int bookNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
                int imageUrlColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PIC_URL);
                int selfLinkIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SELF_LINK);

                final ArrayList<Favourites> favourites = new ArrayList<>();

                while (cursor.moveToNext()) {
                    String currentBookName = cursor.getString(bookNameColumnIndex);
                    String currentBookImageUrl = cursor.getString(imageUrlColumnIndex);
                    String currentBookSelfLink = cursor.getString(selfLinkIndex);

                    favourites.add(new Favourites(currentBookName, currentBookImageUrl, currentBookSelfLink));
                }

                adapter = new FavouriteAdapter(getActivity(), favourites);

                // Find the {@link GridView} object in the view hierarchy of the {@link Activity}.
                // There should be a {@link GridView} with the view ID called list, which is declared in the
                // fragment_favourites.xml layout file.
                GridView gridView = (GridView) rootView.findViewById(R.id.fav_grid_view);

                gridView.setAdapter(adapter);

                registerForContextMenu(gridView);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), DetailActivity.class);

                        intent.putExtra("detailsURLLink", favourites.get(position).getMselfLink());
                        startActivity(intent);
                    }
                });
            }

        } finally {
            // finally close the cursor
            cursor.close();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getActivity().getMenuInflater().inflate(R.menu.fav_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        String bookName = adapter.getItem(index).getMbookName();
        switch (item.getItemId()) {
            case R.id.delte:
                deleteFav(bookName);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteFav(String bookName) {

        SQLiteDatabase readableDatabase = mDbHelper.getReadableDatabase();

        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME};

        Cursor cursor = readableDatabase.query(BookEntry.TABLE_NAME, projection, null,
                null, null, null, null);

        try {
            int bookIdIndex = cursor.getColumnIndex(BookEntry._ID);
            int bookNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);

            while (cursor.moveToNext()) {
                if (cursor.getString(bookNameColumnIndex).equals(bookName)) {
                    SQLiteDatabase writableDatabase = mDbHelper.getWritableDatabase();
                    writableDatabase.delete(BookEntry.TABLE_NAME, BookEntry._ID + "=" + cursor.getInt(bookIdIndex), null);
                    Toast.makeText(getActivity(), "Book removed from Favourites.", Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        } finally {
            // finally close the cursor
            cursor.close();
            loadFav();
        }
    }
}
