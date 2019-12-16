package my.first.bookapp.bookship.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import my.first.bookapp.bookship.Class.Genres;
import com.first.bookapp.bookship.R;

import java.util.ArrayList;

public class GenresAdapter extends ArrayAdapter<Genres> {

    public GenresAdapter(Context context, ArrayList<Genres> genres) {
        super(context, 0, genres);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View gridItemView = convertView;
        if (gridItemView == null) {
            gridItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_item, parent, false);
        }

        Genres currentGenre = getItem(position);

        TextView nameTextView = (TextView) gridItemView.findViewById(R.id.genre_name);
        // Get the genre Name from the currentGenre object and set this text on
        // the nameTextView
        nameTextView.setText(currentGenre.getGenreName());

        ImageView genreImage = (ImageView) gridItemView.findViewById(R.id.genre_image);

        if (currentGenre.hasImage()) {
            genreImage.setImageResource(currentGenre.getmImageResourceId());
            // Make sure the view is visible
            genreImage.setVisibility(View.VISIBLE);
        } else {
            // Otherwise hide the ImageView (set visibility to GONE)
            genreImage.setVisibility(View.GONE);
        }

        return gridItemView;
    }
}
