package com.example.android.bookship.Fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.bookship.Adapter.OnDeviceAdapter;
import com.example.android.bookship.Class.LocalBook;
import com.example.android.bookship.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnDeviceFragment extends Fragment {

    private File dir;
    private static int REQUEST_PERMISSION = 1;

    public static ArrayList<File> bookFiles;
    private ArrayList<LocalBook> books;
    private RecyclerView mRecyclerView;
    private OnDeviceAdapter mOnDeviceAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_on_device, container, false);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_PERMISSION);

        books = new ArrayList<>();
        bookFiles = new ArrayList<>();

        dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchBooksAsyncTask task=new fetchBooksAsyncTask();
        task.execute();

        return rootView;
    }

    private class fetchBooksAsyncTask extends AsyncTask<Void,Void,ArrayList<File>>
    {

        @Override
        protected ArrayList<File> doInBackground(Void... voids) {
            dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            return getFiles(dir);
        }

        protected void onPostExecute(ArrayList<File> result)
        {
            mOnDeviceAdapter = new OnDeviceAdapter(books, getContext());
            mRecyclerView.setAdapter(mOnDeviceAdapter);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getFiles(dir);
            } else {
                Toast.makeText(getContext(), "Please provide the permission.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ArrayList<File> getFiles(java.io.File dir) {
        final java.io.File[] files = dir.listFiles();

        if(files != null) {
            for (java.io.File file : files) {
                if (file.isDirectory()) {
                    getFiles(file);
                } else {
                    if (file.getName().endsWith(".pdf")) {
                        bookFiles.add(file);
                        books.add(new LocalBook(file.getName().split(".pdf")[0],
                                R.drawable.fiction, new Date(file.lastModified())));
                    }
                }
            }
        }
        return bookFiles;
    }
}
