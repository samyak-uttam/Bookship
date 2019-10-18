package com.example.android.bookship.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.android.bookship.Fragment.OnDeviceFragment;
import com.example.android.bookship.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;

public class OnDeviceDetailsActivity extends AppCompatActivity {

    private PDFView pdfView;
    private TextView bookNameTV;
    private File curBookFile;
    private int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_device_details);

        pdfView = findViewById(R.id.pdf_view);

        curBookFile = OnDeviceFragment.bookFiles.
                get(getIntent().getIntExtra("index", -1));

        bookNameTV = findViewById(R.id.book_name);
        bookNameTV.setText(curBookFile.getName().split(".pdf")[0]);

        pdfView.fromFile(curBookFile)
                .enableSwipe(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();

        if(ContextCompat.checkSelfPermission(OnDeviceDetailsActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OnDeviceDetailsActivity.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.on_device_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                ActivityCompat.requestPermissions((Activity) OnDeviceDetailsActivity.this,
                        new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            case R.id.nightMode:
                pdfView.setNightMode(!item.isChecked());
                pdfView.loadPages();
                if(item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the book.

                boolean isDeleted = curBookFile.delete();
                if(isDeleted) {
                    Toast.makeText(OnDeviceDetailsActivity.this, getString(R.string.delete_book_successful),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OnDeviceDetailsActivity.this, getString(R.string.delete_book_failed),
                            Toast.LENGTH_SHORT).show();
                }

                // CLose the activity
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue reading the book.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
