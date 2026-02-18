package com.example.cookingrecipes;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Context context;
    Resources resources;
    Button btnRussian;
    Button btnEnglish;
    Button btnSpanish;
    Button btnStart;
    TextView appName;
    TextView languageTip;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnRussian = findViewById(R.id.btnRussian);
        btnEnglish = findViewById(R.id.btnEnglish);
        btnSpanish = findViewById(R.id.btnSpanish);
        btnStart = findViewById(R.id.btnStart);
        appName = findViewById(R.id.appName);
        languageTip = findViewById(R.id.selectLanguage);

        btnRussian.setOnClickListener(v -> {
            context = LocaleHelper.setLocale(MainActivity.this, "ru");
            resources = context.getResources();

            changeLanguageRender();
        });

        btnEnglish.setOnClickListener(v -> {
            context = LocaleHelper.setLocale(MainActivity.this, "en");
            resources = context.getResources();

            changeLanguageRender();
        });

        btnSpanish.setOnClickListener(v -> {
            context = LocaleHelper.setLocale(MainActivity.this, "es");
            resources = context.getResources();

            changeLanguageRender();
        });

        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecipesActivity.class);
            startActivity(intent);
        });

    }

    protected void changeLanguageRender() {
        appName.setText(resources.getString(R.string.app_name));
        languageTip.setText(resources.getString(R.string.select_language));
        btnRussian.setText(resources.getString(R.string.russian));
        btnEnglish.setText(resources.getString(R.string.english));
        btnSpanish.setText(resources.getString(R.string.spanish));
        btnStart.setText(resources.getString(R.string.start));
    }


}