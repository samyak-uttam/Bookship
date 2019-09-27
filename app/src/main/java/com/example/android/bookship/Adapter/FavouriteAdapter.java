package com.example.android.bookship.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bookship.Class.Favourites;
import com.example.android.bookship.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavouriteAdapter extends ArrayAdapter<Favourites> {

    public FavouriteAdapter(Context context, ArrayList<Favourites> favourites) {
        super(context, 0, favourites);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View gridItemView = convertView;
        if (gridItemView == null) {
            gridItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.fav_item, parent, false);
        }

        Favourites currentFav = getItem(position);

        TextView nameTextView = (TextView) gridItemView.findViewById(R.id.fav_book_name);
        // Get the book Name from the currentFav object and set this text on
        // the nameTextView
        nameTextView.setText(currentFav.getMbookName());

        ImageView favImage = (ImageView) gridItemView.findViewById(R.id.fav_book_image);

        if (currentFav.getMbookImageRequestURL() != null) {
            Picasso.get().load(currentFav.getMbookImageRequestURL()).into(favImage);
        } else {
            favImage.setImageResource(R.drawable.download);
        }

        return gridItemView;
    }
}
