package com.example.oscaandroiddev;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;

public class PDF extends AppCompatActivity {

    PDFView pdfView;
    TextView btnBack3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pdf);
        pdfView = findViewById(R.id.pdfView);
        btnBack3 = findViewById(R.id.btnBack3);
        pdfView.fromAsset("IRR of the Expanded  Senior Citizens Act.pdf").load();

        btnBack3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}