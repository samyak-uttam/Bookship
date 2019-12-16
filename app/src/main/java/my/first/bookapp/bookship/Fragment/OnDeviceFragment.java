package my.first.bookapp.bookship.Fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import my.first.bookapp.bookship.Adapter.OnDeviceAdapter;
import com.first.bookapp.bookship.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;


public class OnDeviceFragment extends Fragment {

    private View rootView;
    private File dir;
    private static int REQUEST_PERMISSION = 1;

    private FloatingActionButton sortByButton;

    public static ArrayList<File> books, copy;
    private RecyclerView mRecyclerView;
    private OnDeviceAdapter mOnDeviceAdapter;
    private LinearLayout emptyLayout, searchLayout;
    private CardView clearResults;
    private FloatingActionMenu menuButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_on_device, container, false);

        final EditText dataSearch = rootView.findViewById(R.id.data_search_et);
        ImageButton searchButton = rootView.findViewById(R.id.search_button);
        clearResults = rootView.findViewById(R.id.clear_results);
        clearResults.setVisibility(View.GONE);

        menuButton = rootView.findViewById(R.id.menu_button);

        sortByButton = rootView.findViewById(R.id.sort_by);
        sortByButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuButton.close(true);
                SetUpSortBy();
            }
        });

        books = new ArrayList<>();
        searchLayout = rootView.findViewById(R.id.search_layout);
        emptyLayout = rootView.findViewById(R.id.empty_layout);

        dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                emptyLayout.setVisibility(View.GONE);

                String search = dataSearch.getText().toString().trim();
                if (search.isEmpty()) {
                    dataSearch.setError("Please enter a valid data name.");
                    dataSearch.requestFocus();
                    return;
                }
                search(search.toLowerCase());
            }
        });

        clearResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyLayout.setVisibility(View.GONE);
                books.clear();
                books.addAll(copy);
                mOnDeviceAdapter.notifyDataSetChanged();
                clearResults.setVisibility(View.GONE);
                dataSearch.setText("");
            }
        });

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
            menuButton.setVisibility(View.GONE);
            searchLayout.setVisibility(View.GONE);
        } else {
            initRecView();
        }

        return rootView;
    }

    private void initRecView() {
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchBooksAsyncTask task = new fetchBooksAsyncTask();
        task.execute();

    }

    private void SetUpSortBy() {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Sort By");

        String[] sortOptions = {"Name", "Date Modified"};
        builder.setItems(sortOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        SortByName();
                        Toast.makeText(getContext(), "Sorted by Name.", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        SortByDateModified();
                        Toast.makeText(getContext(), "Sorted by Date Modified.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        builder.create();
        builder.show();
    }

    private void SortByName() {
        Collections.sort(books, new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                return file1.getName().compareTo(file2.getName());
            }
        });
        mOnDeviceAdapter.notifyDataSetChanged();
    }

    private void SortByDateModified() {
        Collections.sort(books, new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                return (new Date(file2.lastModified())).compareTo(new Date(file1.lastModified()));
            }
        });
        mOnDeviceAdapter.notifyDataSetChanged();
    }

    private class fetchBooksAsyncTask extends AsyncTask<Void, Void, ArrayList<File>> {

        @Override
        protected ArrayList<File> doInBackground(Void... voids) {
            dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            return getFiles(dir);
        }

        protected void onPostExecute(ArrayList<File> result) {
            mOnDeviceAdapter = new OnDeviceAdapter(books, getContext());
            mRecyclerView.setAdapter(mOnDeviceAdapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initRecView();
                menuButton.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getContext(), "Please provide the permission.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ArrayList<File> getFiles(java.io.File dir) {
        final java.io.File[] files = dir.listFiles();

        if (files != null) {
            emptyLayout.setVisibility(View.GONE);
            for (java.io.File file : files) {
                if (file.isDirectory()) {
                    getFiles(file);
                } else {
                    if (file.getName().endsWith(".pdf")) {
                        books.add(file);
                    }
                }
            }
            copy = new ArrayList<>(books);
        }
        return books;
    }

    private void search(String search) {

        clearResults.setVisibility(View.VISIBLE);
        books.clear();
        books.addAll(copy);

        Iterator<File> itr = books.iterator();
        while (itr.hasNext()) {
            if (!itr.next().getName().split(".pdf")[0].toLowerCase().startsWith(search)) {
                itr.remove();
            }
        }

        if (books.size() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
        }
        mOnDeviceAdapter.notifyDataSetChanged();
    }
}
