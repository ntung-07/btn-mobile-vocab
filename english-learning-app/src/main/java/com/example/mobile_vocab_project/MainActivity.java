package com.example.mobile_vocab_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.mobile_vocab_project.vocab.GuideActivity;
import com.example.mobile_vocab_project.vocab.MyAdapter;
import com.example.mobile_vocab_project.vocab.TopicListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private Button topicButton, quizButton;
    private List<VocabEntity> listVocab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SVEnglish");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.black));

        // Initialize UI
        recyclerView = findViewById(R.id.recycler_view);
        topicButton = findViewById(R.id.btnTopic);
        quizButton = findViewById(R.id.quizButton);
        ProgressBar progressBar = findViewById(R.id.syncProgressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Init vocab list from DB
        VocabDatabase db = VocabDatabase.getInstance(this);
        VocabDao dao = db.vocabDao();
        listVocab = dao.getAll();

        adapter = new MyAdapter(this, listVocab);
        recyclerView.setAdapter(adapter);

        topicButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, TopicListActivity.class));
        });

        // Sync with Firestore
        SyncUtils.syncRoomWithFireStore(this);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest syncRequest = new OneTimeWorkRequest.Builder(SyncWorker.class)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).enqueue(syncRequest);

        quizButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("vocabList", new ArrayList<>(listVocab));
            startActivity(intent);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_vocab); // default

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        return true;
                    case R.id.nav_vocab:
                        Fragment existingFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        if (existingFragment != null) {
                            getSupportFragmentManager().beginTransaction().remove(existingFragment).commit();
                        }
                        toggleMainLayoutVisibility(true);
                        return true;
                    case R.id.nav_account:
                        toggleMainLayoutVisibility(false);
                        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        if (!(currentFragment instanceof AccountFragment)) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container, new AccountFragment())
                                    .commit();
                        }
                        return true;
                }
                return false;
            }
        });
    }

    private void setAppTheme(int mode) {
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) adapter.shutdownTTS();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_info) {
            startActivity(new Intent(MainActivity.this, GuideActivity.class));
            return true;
        } else if (id == R.id.action_sync) {
            if (!SyncUtils.isOnline(this)) {
                Toast.makeText(this, "You're offline, syncing not available", Toast.LENGTH_SHORT).show();
                return true;
            }

            ProgressBar progressBar = findViewById(R.id.syncProgressBar);
            progressBar.setVisibility(View.GONE);

            new Thread(() -> {
                try {
                    SyncUtils.syncRoomWithFireStore(this);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Sync successful", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                        List<VocabEntity> updatedList = VocabDatabase.getInstance(this).vocabDao().getAll();
                        adapter.setData(updatedList);
                        adapter.notifyDataSetChanged();
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Sync failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    });
                }
            }).start();

            return true;
        } else if (id == R.id.action_share) {
            shareVocabulary();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleMainLayoutVisibility(boolean showMain) {
        findViewById(R.id.btnTopic).setVisibility(showMain ? View.VISIBLE : View.GONE);
        findViewById(R.id.quizButton).setVisibility(showMain ? View.VISIBLE : View.GONE);
        findViewById(R.id.recycler_view).setVisibility(showMain ? View.VISIBLE : View.GONE);
        findViewById(R.id.syncProgressBar).setVisibility(showMain ? View.GONE : View.GONE);
    }

    private void shareVocabulary() {
        new Thread(() -> {
            try {
                StringBuilder shareText = new StringBuilder("Danh sách từ vựng của tôi:\n\n");
                int maxItems = Math.min(listVocab.size(), 5);
                for (int i = 0; i < maxItems; i++) {
                    VocabEntity vocab = listVocab.get(i);
                    shareText.append(String.format("- %s [%s]: %s\n",
                            vocab.term, vocab.ipa, vocab.def));
                }
                shareText.append("\n").append(getString(R.string.share_footer));

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());

                runOnUiThread(() -> {
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_title)));
                    Toast.makeText(MainActivity.this, getString(R.string.share_success), Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this,
                        String.format(getString(R.string.share_failed), e.getMessage()),
                        Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}
