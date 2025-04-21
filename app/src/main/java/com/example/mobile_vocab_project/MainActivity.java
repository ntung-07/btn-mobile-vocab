package com.example.mobile_vocab_project;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

//import thêm 2 dòng này
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.mobile_vocab_project.vocab.GuideActivity;
import com.example.mobile_vocab_project.vocab.MyAdapter;
import com.example.mobile_vocab_project.vocab.TopicListActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
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

        recyclerView = findViewById(R.id.recycler_view);
        guideButton = findViewById(R.id.btnGuide);
        topicButton = findViewById(R.id.btnTopic);
        toggleButton = findViewById(R.id.btnToggleTheme);
        quizButton = findViewById(R.id.quizButton);
        ProgressBar progressBar = findViewById(R.id.syncProgressBar);
        Button syncButton = findViewById(R.id.syncButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        guideButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GuideActivity.class));
        });

        topicButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, TopicListActivity.class));
        });

        toggleButton.setOnClickListener(v -> toggleTheme());

        VocabDatabase db = VocabDatabase.getInstance(this);
        VocabDao dao = db.vocabDao();
        listVocab = dao.getAll();

        adapter = new MyAdapter(this, listVocab);
        recyclerView.setAdapter(adapter);

        SyncUtils.syncRoomWithFireStore(this);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest syncRequest = new OneTimeWorkRequest.Builder(SyncWorker.class)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).enqueue(syncRequest);

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
                        adapter.setData(updatedList);
                        adapter.notifyDataSetChanged();
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

        quizButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("vocabList", new ArrayList<>(listVocab));
            startActivity(intent);
        });
    }
    //  Thêm 2 phương thức Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleTheme() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        setAppTheme(currentNightMode == Configuration.UI_MODE_NIGHT_YES ?
                AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
    }

    private void setAppTheme(int mode) {
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) adapter.shutdownTTS();
    }
}