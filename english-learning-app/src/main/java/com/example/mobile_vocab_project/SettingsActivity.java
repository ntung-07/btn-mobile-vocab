package com.example.mobile_vocab_project;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "VocabPrefs";
    private static final String KEY_STUDY_HOUR = "studyHour";
    private static final String KEY_STUDY_MINUTE = "studyMinute";
    private static final String KEY_REMINDER_ENABLED = "reminderEnabled";
    private static final int NOTIFICATION_PERMISSION_CODE = 100;
    private static final int EXACT_ALARM_PERMISSION_CODE = 101;

    private TextView tvStudyTime;
    private TextView tvCurrentLanguage;
    private LinearLayout layoutSetTime, layoutLanguage;
    private SwitchCompat switchReminder, switchNightMode;
    private SharedPreferences prefs;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.settings_screen);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        tvStudyTime = findViewById(R.id.tvStudyTime);
        layoutSetTime = findViewById(R.id.layoutSetTime);
        switchReminder = findViewById(R.id.switchReminder);
        switchNightMode = findViewById(R.id.switchNightMode);
        layoutLanguage = findViewById(R.id.layoutLanguage);
        tvCurrentLanguage = findViewById(R.id.tvCurrentLanguage); // Make sure to define this in XML

        boolean isReminderOn = prefs.getBoolean(KEY_REMINDER_ENABLED, false);
        switchReminder.setChecked(isReminderOn);
        layoutSetTime.setVisibility(isReminderOn ? View.VISIBLE : View.GONE);
        loadStudyTime();

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switchNightMode.setChecked(currentNightMode == Configuration.UI_MODE_NIGHT_YES);

        requestPermissionsIfNeeded();

        switchReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_REMINDER_ENABLED, isChecked).apply();
            layoutSetTime.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        layoutSetTime.setOnClickListener(v -> {
            if (switchReminder.isChecked()) showTimePickerDialog();
        });

        switchNightMode.setOnCheckedChangeListener((buttonView, isChecked) ->
                AppCompatDelegate.setDefaultNightMode(
                        isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
                ));

        layoutLanguage.setOnClickListener(v -> showLanguageDialog());

        // Display current language
        Locale current = getResources().getConfiguration().locale;
        tvCurrentLanguage.setText(current.getDisplayLanguage(current));
    }

    private void requestPermissionsIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(this, "Vui lòng cấp quyền đặt báo thức chính xác", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivityForResult(intent, EXACT_ALARM_PERMISSION_CODE);
            }
        }
    }

    private void showTimePickerDialog() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this, (view, hour, minute) -> {
            saveStudyTime(hour, minute);
            updateStudyTimeDisplay(hour, minute);
            scheduleNotification(hour, minute);
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
        dialog.show();
    }

    private void saveStudyTime(int hour, int minute) {
        prefs.edit().putInt(KEY_STUDY_HOUR, hour).putInt(KEY_STUDY_MINUTE, minute).apply();
    }

    private void loadStudyTime() {
        int hour = prefs.getInt(KEY_STUDY_HOUR, -1);
        int minute = prefs.getInt(KEY_STUDY_MINUTE, -1);
        if (hour != -1 && minute != -1) updateStudyTimeDisplay(hour, minute);
    }

    private void updateStudyTimeDisplay(int hour, int minute) {
        tvStudyTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
    }

    private void scheduleNotification(int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "Thông báo được đặt cho " +
                String.format(Locale.getDefault(), "%02d:%02d", hour, minute), Toast.LENGTH_SHORT).show();
    }

    private void showLanguageDialog() {
        String[] languages = {"English", "Tiếng Việt", "Français"};
        String[] codes = {"en", "vi", "fr"};

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(R.string.choose_language)
                .setItems(languages, (dialog, which) -> {
                    saveAndApplyLanguage(codes[which]);
                }).show();
    }

    private void saveAndApplyLanguage(String langCode) {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        prefs.edit().putString("lang", langCode).apply();

        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            String msg = (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    ? "Quyền thông báo đã được cấp"
                    : "Cần quyền thông báo để gửi nhắc nhở";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EXACT_ALARM_PERMISSION_CODE) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(this, "Quyền báo thức chính xác đã được cấp", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Cần quyền báo thức chính xác để gửi thông báo đúng giờ", Toast.LENGTH_LONG).show();
            }
        }
    }
}
