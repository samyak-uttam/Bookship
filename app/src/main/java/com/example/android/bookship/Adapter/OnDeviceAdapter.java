package com.example.android.bookship.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.bookship.Activity.OnDeviceDetailsActivity;
import com.example.android.bookship.Class.LocalBook;
import com.example.android.bookship.R;

import java.util.ArrayList;

public class OnDeviceAdapter extends RecyclerView.Adapter<OnDeviceAdapter.OnDeviceViewHolder> {

    private ArrayList<LocalBook> mLocalBooks;
    private Context mContext;

    public OnDeviceAdapter(ArrayList<LocalBook> mLocalBooks, Context mContext) {
        this.mLocalBooks = mLocalBooks;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public OnDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.on_device_item, parent, false);
        return new OnDeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnDeviceViewHolder holder, final int position) {

        final LocalBook book = mLocalBooks.get(position);

        holder.bookImage.setImageResource(book.getBookImage());
        holder.bookName.setText(book.getBookName());
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, OnDeviceDetailsActivity.class);
                intent.putExtra("index", position);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLocalBooks.size();
    }

    public static class OnDeviceViewHolder extends RecyclerView.ViewHolder {

        ImageView bookImage;
        TextView bookName;
        View rootView;

        public OnDeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            bookImage = itemView.findViewById(R.id.book_image);
            bookName = itemView.findViewById(R.id.book_name);
        }
    }
}
