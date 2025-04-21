package com.example.mobile_vocab_project;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import com.example.mobile_vocab_project.vocab.GuideActivity;
import com.example.mobile_vocab_project.vocab.TopicListActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button guideButton, topicButton, toggleButton, quizButton;
    private List<VocabEntity> listVocab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ứng Dụng Từ Vựng");

        // Initialize buttons
        guideButton = findViewById(R.id.btnGuide);
        topicButton = findViewById(R.id.btnTopic);
        toggleButton = findViewById(R.id.btnToggleTheme);
        quizButton = findViewById(R.id.quizButton);
        ProgressBar progressBar = findViewById(R.id.syncProgressBar);
        Button syncButton = findViewById(R.id.syncButton);

        // Handle guide button click
        guideButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GuideActivity.class));
        });

        // Handle topic button click
        topicButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, TopicListActivity.class));
        });

        // Handle theme toggle button click
        toggleButton.setOnClickListener(v -> toggleTheme());

        // Initialize vocab list from database
        VocabDatabase db = VocabDatabase.getInstance(this);
        VocabDao dao = db.vocabDao();
        listVocab = dao.getAll();

        // Sync data with Firestore
        SyncUtils.syncRoomWithFireStore(this);

        // Configure network constraints for syncing
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // Create and enqueue sync work request
        OneTimeWorkRequest syncRequest = new OneTimeWorkRequest.Builder(SyncWorker.class)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(this).enqueue(syncRequest);

        // Handle sync button click
        syncButton.setOnClickListener(v -> {
            if (!SyncUtils.isOnline(MainActivity.this)) {
                Toast.makeText(this, "You're offline, syncing not available", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            syncButton.setEnabled(false);

            new Thread(() -> {
                try {
                    SyncUtils.syncRoomWithFireStore(this);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Sync successful", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        syncButton.setEnabled(true);

                        List<VocabEntity> updatedList = VocabDatabase.getInstance(this).vocabDao().getAll();
                        // Handle updated list if needed (can be used in another UI element)
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Sync failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        syncButton.setEnabled(true);
                    });
                }
            }).start();
        });

        // Handle quiz button click
        quizButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("vocabList", new ArrayList<>(listVocab));
            startActivity(intent);
        });
    }

    // Toggle between light and dark themes
    private void toggleTheme() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        setAppTheme(currentNightMode == Configuration.UI_MODE_NIGHT_YES ?
                AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
    }

    // Set the theme mode
    private void setAppTheme(int mode) {
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ensure any resources are cleaned up
    }
}
