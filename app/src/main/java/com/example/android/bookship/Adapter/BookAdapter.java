package com.example.android.bookship.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.bookship.Class.Books;
import com.example.android.bookship.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Books> {

    Context mcontext;
    public BookAdapter(Activity context, ArrayList<Books> books) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, books);

        mcontext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View bookListView = convertView;
        if(bookListView == null) {
            bookListView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Book} object located at this position in the list
        Books currentBook = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID book_name_text_view
        TextView booknameTextView = (TextView) bookListView.findViewById(R.id.book_name_text_view);
        // Get the book name from the current book object and
        // set this text on the name TextView
        booknameTextView.setText(currentBook.getbookname());

        // Find the TextView in the list_item.xml layout with the ID author_name_text_view
        TextView authornameTextView = (TextView) bookListView.findViewById(R.id.author_name_text_view);

        if(currentBook.getAuthornames()==null)
            authornameTextView.setText("No Authors provided.");
        else {
            // Get the author name from the current book object and
            // set this text on the number TextView
            StringBuilder stringBuilder = new StringBuilder();
            for (String names : currentBook.getAuthornames()) {
                stringBuilder.append(names + ",");
            }
            // Ti convert the stringBuilder to string
            String str = stringBuilder.toString();

            // To remove the last ','
            str = str.substring(0, str.length() - 1);

            authornameTextView.setText(str);
        }
        // Find the TextView in the list_item.xml layout with the ID desccription
        TextView descriptionTextView = (TextView) bookListView.findViewById(R.id.desccription);

        if(currentBook.getbookDescription() == null)
            descriptionTextView.setText("No description provided.");
        else {
            // Get the author name from the current book object and
            // set this text on the number TextView
            descriptionTextView.setText(currentBook.getbookDescription());
        }

        // Image view for book
        ImageView bookImage;
        if(currentBook.getbookImageRequestURL() != null) {
            bookImage = (ImageView) bookListView.findViewById(R.id.image);
            Picasso.get().load(currentBook.getbookImageRequestURL()).into(bookImage);
        }

        // Find the Rating Bar in the list_item.xml layout with the ID rating
        RatingBar ratingBar = (RatingBar) bookListView.findViewById(R.id.rating);
        // set the rating of the book by explicitly casting it to float value
        ratingBar.setRating((float)currentBook.getRating());

        return bookListView;
    }
}
