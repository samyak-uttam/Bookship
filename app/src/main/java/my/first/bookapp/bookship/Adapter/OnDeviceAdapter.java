package my.first.bookapp.bookship.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import my.first.bookapp.bookship.Activity.OnDeviceDetailsActivity;
import com.first.bookapp.bookship.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OnDeviceAdapter extends RecyclerView.Adapter<OnDeviceAdapter.OnDeviceViewHolder> {

    private ArrayList<File> mLocalBooks;
    private Context mContext;

    public OnDeviceAdapter(ArrayList<File> mLocalBooks, Context mContext) {
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
    public void onBindViewHolder(@NonNull final OnDeviceViewHolder holder, final int position) {

        final File book = mLocalBooks.get(position);
        Date lastModifiedDate = new Date(book.lastModified());
        holder.modified.setText(formatDate(lastModifiedDate) + ", " + formatTime(lastModifiedDate));

        holder.bookName.setText(book.getName().split(".pdf")[0]);
        holder.fileSize.setText(String.format("%.2f", book.length() / (1024.0 * 1024.0)) + " mb");
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

        TextView modified;
        TextView bookName;
        TextView fileSize;
        View rootView;

        public OnDeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            modified = itemView.findViewById(R.id.file_path);
            bookName = itemView.findViewById(R.id.book_name);
            fileSize = itemView.findViewById(R.id.file_size);
        }
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
