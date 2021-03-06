package my.first.bookapp.bookship.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import my.first.bookapp.bookship.Fragment.OnDeviceFragment;
import com.first.bookapp.bookship.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;

public class FullScreenActivity extends AppCompatActivity {

    private File curBookFile;
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        pdfView = findViewById(R.id.full_screen_pdf_view);

        curBookFile = OnDeviceFragment.books
                .get(getIntent().getIntExtra("index", -1));

        boolean isNightMode = getIntent().getBooleanExtra("isNightMode", false);

        pdfView.fromFile(curBookFile)
                .enableSwipe(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .nightMode(isNightMode)
                .load();
    }
}
