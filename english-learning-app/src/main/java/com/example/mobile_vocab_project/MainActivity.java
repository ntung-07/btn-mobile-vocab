package com.example.mobile_vocab_project;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        VocabDatabase db = VocabDatabase.getInstance(this);
        VocabDao dao = db.vocabDao();
        ArrayList<Vocab> listVocab = new ArrayList<>();
        listVocab.add(new Vocab("Cat", "Mèo", "[kæt]"));
        listVocab.add(new Vocab("Tiger", "Hổ", "[ˈtaɪɡər]"));
        listVocab.add(new Vocab("Fish", "Cá", "[fɪʃ]"));
        listVocab.add(new Vocab("Bird", "Chim", "[bɜrd]"));
        listVocab.add(new Vocab("Elephant", "Voi", "[ˈɛlɪfənt]"));
        listVocab.add(new Vocab("Snake", "Rắn", "[sneɪk]"));
        listVocab.add(new Vocab("Monkey", "Khỉ", "[ˈmʌŋki]"));
        listVocab.add(new Vocab("Bear", "Gấu", "[bɛr]"));
        listVocab.add(new Vocab("Cow", "Bò", "[kaʊ]"));
        listVocab.add(new Vocab("Horse", "Ngựa", "[hɔrs]"));
        listVocab.add(new Vocab("Duck", "Vịt", "[dʌk]"));
        listVocab.add(new Vocab("Chicken", "Gà", "[ˈtʃɪkɪn]"));
        listVocab.add(new Vocab("Sheep", "Cừu", "[ʃiːp]"));
        listVocab.add(new Vocab("Goat", "Dê", "[ɡoʊt]"));
        listVocab.add(new Vocab("Wolf", "Sói", "[wʊlf]"));
        listVocab.add(new Vocab("Fox", "Cáo", "[fɑks]"));
        listVocab.add(new Vocab("Rabbit", "Thỏ", "[ˈræbɪt]"));
        listVocab.add(new Vocab("Frog", "Ếch", "[frɔɡ]"));
        listVocab.add(new Vocab("Ant", "Kiến", "[ænt]"));
        listVocab.add(new Vocab("Bee", "Ong", "[bi]"));

        // Load from database
        List<VocabEntity> listVocab = dao.getAll();
        MyAdapter adapter = new MyAdapter(this, listVocab);  // you’ll update MyAdapter next
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Giải phóng TextToSpeech khi thoát activity
        if (adapter != null) {
            adapter.shutdownTTS();
        }
    }
}
