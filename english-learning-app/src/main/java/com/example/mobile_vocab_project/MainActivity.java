package com.example.mobile_vocab_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

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

        ArrayList<Vocab> listVocab = new ArrayList();
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

        // Khởi tạo Adapter và đặt Adapter cho RecyclerView
        MyAdapter adapter = new MyAdapter(this, listVocab);
        recyclerView.setAdapter(adapter);

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