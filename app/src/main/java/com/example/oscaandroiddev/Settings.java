package com.example.oscaandroiddev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Settings extends AppCompatActivity {

    TextView btnBack2;
    Spinner spinLanguage;
    List<String> languageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadLocale();
        setContentView(R.layout.activity_settings);
        btnBack2 = findViewById(R.id.btnBack2);
        spinLanguage = findViewById(R.id.spinLanguage);
        languageList.add("Default-es");
        languageList.add("English");
        languageList.add("Filipino");

        spinLanguage.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, languageList));

        spinLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLanguage = spinLanguage.getItemAtPosition(spinLanguage.getSelectedItemPosition()).toString();
                if (selectedLanguage.equals("Default-es")) {
                    setLocale("en");
                } else if (selectedLanguage.equals("English")) {
                    setLocale("en");
                } else if (selectedLanguage.equals("Filipino")) {
                    setLocale("fil");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", language);
        editor.apply();
    }

    private void loadLocale() {
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("My_Lang", "");
        setLocale(language);
    }
}