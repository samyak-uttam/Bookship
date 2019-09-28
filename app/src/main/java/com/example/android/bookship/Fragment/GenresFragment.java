package com.example.android.bookship.Fragment;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.bookship.Adapter.GenresAdapter;
import com.example.android.bookship.Class.Genres;
import com.example.android.bookship.R;
import com.example.android.bookship.Results;

import java.util.ArrayList;

public class GenresFragment extends Fragment {


    public GenresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_genres, container, false);

        // Create a list of genres
        final ArrayList<Genres> genres = new ArrayList<Genres>();

        genres.add(new Genres("ROMANCE", R.drawable.romance));
        genres.add(new Genres("ADVENTURE", R.drawable.adventure));
        genres.add(new Genres("FANTASY", R.drawable.fantasy));
        genres.add(new Genres("DRAMA", R.drawable.drama));
        genres.add(new Genres("PHILOSOPHY", R.drawable.philosophy));
        genres.add(new Genres("FICTION", R.drawable.fiction));
        genres.add(new Genres("COMICS", R.drawable.comics));
        genres.add(new Genres("EDUCATION", R.drawable.education));
        genres.add(new Genres("HORROR", R.drawable.horror));
        genres.add(new Genres("MUSIC", R.drawable.music));
        genres.add(new Genres("SCIENCE", R.drawable.science));
        genres.add(new Genres("HUMOUR", R.drawable.funny));
        genres.add(new Genres("HISTORY", R.drawable.history));
        genres.add(new Genres("RELIGION", R.drawable.religion));
        genres.add(new Genres("WAR", R.drawable.war));
        genres.add(new Genres("POLITICS", R.drawable.politics));
        genres.add(new Genres("POETRY", R.drawable.poetry));
        genres.add(new Genres("TRAVEL", R.drawable.travel));

        final GenresAdapter adapter = new GenresAdapter(getActivity(), genres);

        // Find the {@link GridView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link GridView} with the view ID called list, which is declared in the
        // fragment_genres.xml layout file.
        GridView gridView = (GridView) rootView.findViewById(R.id.grid);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ConnectivityManager connMgr =
                        (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    Intent intent = new Intent(getActivity(), Results.class);

                    intent.putExtra("genreName", genres.get(position).getGenreName());
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }


            }
        });

        return rootView;
    }

}
