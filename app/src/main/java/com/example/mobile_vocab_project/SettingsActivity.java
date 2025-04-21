package com.example.mobile_vocab_project;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "VocabPrefs";
    private static final String KEY_STUDY_HOUR = "studyHour";
    private static final String KEY_STUDY_MINUTE = "studyMinute";
    private static final int NOTIFICATION_PERMISSION_CODE = 100;
    private static final int EXACT_ALARM_PERMISSION_CODE = 101;
    private static final String TAG = "SettingsActivity";

    private TextView tvStudyTime;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.settings_screen);

        tvStudyTime = findViewById(R.id.tvStudyTime);
        Button btnSetStudyTime = findViewById(R.id.btnSetStudyTime);
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load saved study time
        loadStudyTime();

        // Suggest disabling battery optimization
        Toast.makeText(this, "Để thông báo đúng giờ, hãy tắt tối ưu hóa pin cho ứng dụng trong Cài đặt", Toast.LENGTH_LONG).show();

        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE);
            }
        }

        // Request exact alarm permission for Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(this, "Vui lòng cấp quyền đặt báo thức chính xác", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivityForResult(intent, EXACT_ALARM_PERMISSION_CODE);
            }
        }

        // Set study time
        btnSetStudyTime.setOnClickListener(v -> showTimePickerDialog());
    }

    private void showTimePickerDialog() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minuteOfHour) -> {
                    // Save selected time
                    saveStudyTime(hourOfDay, minuteOfHour);
                    // Update UI
                    updateStudyTimeDisplay(hourOfDay, minuteOfHour);
                    // Schedule notification
                    scheduleNotification(hourOfDay, minuteOfHour);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void saveStudyTime(int hour, int minute) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_STUDY_HOUR, hour);
        editor.putInt(KEY_STUDY_MINUTE, minute);
        editor.apply();
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

        // If the time is in the past, schedule for the next day
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Log scheduled time
        Log.d(TAG, "Scheduling alarm for: " + calendar.getTime().toString());

        // Schedule exact alarm
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), pendingIntent);

        // Schedule next alarm for the following day
        Calendar nextDayCalendar = (Calendar) calendar.clone();
        nextDayCalendar.add(Calendar.DAY_OF_YEAR, 1);
        PendingIntent nextDayPendingIntent = PendingIntent.getBroadcast(this, 1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                nextDayCalendar.getTimeInMillis(), nextDayPendingIntent);

        Toast.makeText(this, "Thông báo được đặt cho " + String.format("%02d:%02d", hour, minute),
                Toast.LENGTH_SHORT).show();
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