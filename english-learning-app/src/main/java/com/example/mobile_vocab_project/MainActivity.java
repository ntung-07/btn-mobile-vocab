package com.example.mobile_vocab_project;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.example.mobile_vocab_project.vocab.GuideActivity;
import com.example.mobile_vocab_project.vocab.MyAdapter;
import com.example.mobile_vocab_project.vocab.TopicListActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile_vocab_project.R;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private Button guideButton, topicButton, toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ứng Dụng Từ Vựng");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo RecyclerView và các nút
        guideButton = findViewById(R.id.btnGuide);
        topicButton = findViewById(R.id.btnTopic);
        toggleButton = findViewById(R.id.btnToggleTheme);

        // Thiết lập LayoutManager cho RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Mở hướng dẫn sử dụng
        guideButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GuideActivity.class);
            startActivity(intent);
        });

        // Mở luyện theo chủ đề
        topicButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TopicListActivity.class);
            startActivity(intent);
        });

        // Cài đặt sự kiện cho nút Toggle Theme
        toggleButton.setOnClickListener(v -> {
            toggleTheme();  // Gọi phương thức để chuyển đổi giữa chế độ sáng và tối
        });

        VocabDatabase db = VocabDatabase.getInstance(this);
        VocabDao dao = db.vocabDao();

        // Load from database
        List<VocabEntity> listVocab = dao.getAll();
        adapter = new MyAdapter(this, listVocab);  // you’ll update MyAdapter next
        // Gắn Adapter vào RecyclerView
        adapter = new MyAdapter(this, listVocab);
        recyclerView.setAdapter(adapter);

        SyncUtils.syncRoomWithFireStore(this);

        //Automatically run sync when app starts and has internet
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest syncRequest =
                new OneTimeWorkRequest.Builder(SyncWorker.class)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(this).enqueue(syncRequest);

        //Periodic syncing every 12 hours.
//        PeriodicWorkRequest periodicWorkRequest =
//                new PeriodicWorkRequest.Builder(SyncWorker.class, 12, java.util.concurrent.TimeUnit.HOURS)
//                        .setConstraints(constraints)
//                        .build();
//        WorkManager.getInstance(this).enqueue(periodicWorkRequest);

        ProgressBar progressBar = findViewById(R.id.syncProgressBar);
        Button syncButton = findViewById(R.id.syncButton);

        //Click: Trigger sync manually
        syncButton.setOnClickListener(v -> {
            if (!SyncUtils.isOnline(MainActivity.this)) {
                Toast.makeText(MainActivity.this, "You're offline, syncing not available", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            syncButton.setEnabled(false);

            new Thread(() -> {
                try{
                    SyncUtils.syncRoomWithFireStore(MainActivity.this);
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Sync successful", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        syncButton.setEnabled(true);

                        List<VocabEntity> updatedList = VocabDatabase.getInstance(this).vocabDao().getAll();
                        adapter.setData(updatedList);
                        adapter.notifyDataSetChanged();
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Sync failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        syncButton.setEnabled(true);
                    });
                }
            }).start();
        });
    }

    // Hàm để chuyển đổi giữa chế độ sáng và tối
    private void toggleTheme() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        // Nếu đang ở chế độ tối, chuyển sang chế độ sáng
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            setAppTheme(AppCompatDelegate.MODE_NIGHT_NO);  // Chuyển sang chế độ sáng
        } else {
            // Nếu đang ở chế độ sáng, chuyển sang chế độ tối
            setAppTheme(AppCompatDelegate.MODE_NIGHT_YES);  // Chuyển sang chế độ tối
        }
    }

    // Hàm giúp đổi chế độ sáng/tối
    private void setAppTheme(int mode) {
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Giải phóng TextToSpeech khi thoát activity
        if (adapter != null) {
            adapter.shutdownTTS();
        }
    }
}
        // Thêm sự kiện nhấn nút để vào phần Quiz
        Button quizButton = findViewById(R.id.quizButton);
        if (quizButton != null) {
            quizButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                intent.putExtra("vocabList", listVocab);
                startActivity(intent);
            });
        } else {
            Toast.makeText(this, "Không tìm thấy quizButton", Toast.LENGTH_LONG).show();
        }
    }
}