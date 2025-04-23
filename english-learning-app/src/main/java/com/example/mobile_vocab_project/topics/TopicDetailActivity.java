package com.example.mobile_vocab_project.topics;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_vocab_project.LocaleHelper;
import com.example.mobile_vocab_project.MyAdapter;
import com.example.mobile_vocab_project.R;
import com.example.mobile_vocab_project.vocab.VocabEntity;

import java.util.ArrayList;
import java.util.List;

public class TopicDetailActivity extends AppCompatActivity {

    private Topic topic;
    private RecyclerView recyclerView;
    private TextView topicTitle;
    private Button btnTopic;

    @Override
    protected void attachBaseContext(android.content.Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase)); // Ensure locale is applied
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);

        recyclerView = findViewById(R.id.recyclerTopicDetail);
        topicTitle = findViewById(R.id.topicTitle);
        btnTopic = findViewById(R.id.btnTopic);
        btnTopic.setVisibility(View.GONE); // Hide button by default

        topic = (Topic) getIntent().getSerializableExtra("topic");

        if (topic != null) {
            // Use string resource for topic title format
            String titleText = getString(R.string.topic_label_format, topic.name);
            topicTitle.setText(titleText);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            List<VocabEntity> convertedList = new ArrayList<>();
            for (VocabEntity v : topic.vocabList) {
                convertedList.add(new VocabEntity(v.term, v.def, v.ipa));
            }
            recyclerView.setAdapter(new MyAdapter(this, convertedList));
        }
    }
}
