package com.example.mobile_vocab_project;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class VocabApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        applySavedLocale();
    }

    private void applySavedLocale() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", null);

        if (lang == null) {
            lang = Locale.getDefault().getLanguage(); // auto-detect
            prefs.edit().putString("lang", lang).apply();
        }

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }
}
