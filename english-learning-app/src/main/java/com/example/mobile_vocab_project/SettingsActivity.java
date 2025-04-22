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
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
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

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "VocabPrefs";
    private static final String KEY_STUDY_HOUR = "studyHour";
    private static final String KEY_STUDY_MINUTE = "studyMinute";
    private static final String KEY_REMINDER_ENABLED = "reminderEnabled";
    private static final String KEY_NIGHT_MODE = "nightMode";
    private static final int NOTIFICATION_PERMISSION_CODE = 100;
    private static final int EXACT_ALARM_PERMISSION_CODE = 101;
    private static final String TAG = "SettingsActivity";

    private SharedPreferences prefs;
    private TextView tvStudyTime;
    private LinearLayout layoutSetTime;
    private SwitchCompat switchReminder;
    private SwitchCompat switchNightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.settings_screen);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        tvStudyTime = findViewById(R.id.tvStudyTime);
        layoutSetTime = findViewById(R.id.layoutSetTime);
        switchReminder = findViewById(R.id.switchReminder);
        switchNightMode = findViewById(R.id.switchNightMode);

        // Load reminder toggle
        boolean isReminderOn = prefs.getBoolean(KEY_REMINDER_ENABLED, false);
        switchReminder.setChecked(isReminderOn);
        layoutSetTime.setVisibility(isReminderOn ? View.VISIBLE : View.GONE);

        // Load and show study time
        loadStudyTime();

        // Load night mode toggle
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switchNightMode.setChecked(currentNightMode == Configuration.UI_MODE_NIGHT_YES);

        // Request notification permission (T+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE);
            }
        }

        // Request exact alarm permission (S+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(this, "Vui lòng cấp quyền đặt báo thức chính xác", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivityForResult(intent, EXACT_ALARM_PERMISSION_CODE);
            }
        }

        // Toggle Reminder
        switchReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_REMINDER_ENABLED, isChecked).apply();
            layoutSetTime.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        // Tap on Set Time
        layoutSetTime.setOnClickListener(v -> {
            if (switchReminder.isChecked()) {
                showTimePickerDialog();
            }
        });

        // Toggle Theme
        switchNightMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setAppTheme(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });
    }

    private void showTimePickerDialog() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minuteOfHour) -> {
                    saveStudyTime(hourOfDay, minuteOfHour);
                    updateStudyTimeDisplay(hourOfDay, minuteOfHour);
                    scheduleNotification(hourOfDay, minuteOfHour);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void saveStudyTime(int hour, int minute) {
        prefs.edit().putInt(KEY_STUDY_HOUR, hour).putInt(KEY_STUDY_MINUTE, minute).apply();
    }

    private void loadStudyTime() {
        int hour = prefs.getInt(KEY_STUDY_HOUR, -1);
        int minute = prefs.getInt(KEY_STUDY_MINUTE, -1);
        if (hour != -1 && minute != -1) {
            updateStudyTimeDisplay(hour, minute);
        }
    }

    private void updateStudyTimeDisplay(int hour, int minute) {
        String time = String.format("%02d:%02d", hour, minute);
        tvStudyTime.setText(time);
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
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), pendingIntent);

        Calendar nextDayCalendar = (Calendar) calendar.clone();
        nextDayCalendar.add(Calendar.DAY_OF_YEAR, 1);
        PendingIntent nextDayPendingIntent = PendingIntent.getBroadcast(this, 1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                nextDayCalendar.getTimeInMillis(), nextDayPendingIntent);

        Toast.makeText(this, "Thông báo được đặt cho " + String.format("%02d:%02d", hour, minute),
                Toast.LENGTH_SHORT).show();
    }

    private void setAppTheme(int mode) {
        AppCompatDelegate.setDefaultNightMode(mode);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Quyền thông báo đã được cấp", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Cần quyền thông báo để gửi nhắc nhở", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
