package us.wi.hofferec.unitix.activities;

import androidx.appcompat.app.AppCompatActivity;
import us.wi.hofferec.unitix.R;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PDFViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_viewer);

        String filepath = getIntent().getStringExtra("filepath");
        File f = new File(filepath);


        // Using barteksc's PDF viewer from https://github.com/barteksc/AndroidPdfViewer.
        PDFView pdfView = findViewById(R.id.pdfView);
        pdfView.fromFile(f).load();
    }
}
