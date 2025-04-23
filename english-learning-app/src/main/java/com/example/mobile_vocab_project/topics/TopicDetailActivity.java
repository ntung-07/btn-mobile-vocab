package com.example.mobile_vocab_project.topics;
import android.os.Bundle;
import android.view.View;  // Đảm bảo đã import View
import android.widget.TextView;
import android.widget.Button;  // Đảm bảo đã import Button
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_vocab_project.MyAdapter;
import com.example.mobile_vocab_project.R;
import com.example.mobile_vocab_project.vocab.VocabEntity;

import java.util.ArrayList;
import java.util.List;

public class TopicDetailActivity extends AppCompatActivity {

    private Topic topic;
    private RecyclerView recyclerView;
    private TextView topicTitle;
    private Button btnTopic;  // Khai báo nút "Luyện theo chủ đề"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);

        // Ánh xạ các view
        recyclerView = findViewById(R.id.recyclerTopicDetail);
        topicTitle = findViewById(R.id.topicTitle);
        btnTopic = findViewById(R.id.btnTopic);  // Ánh xạ nút "Luyện theo chủ đề"

        // Ẩn nút "Luyện theo chủ đề" khi vào trang chi tiết
        btnTopic.setVisibility(View.GONE);  // Sử dụng View.GONE để ẩn nút

        // Nhận chủ đề từ intent
        topic = (Topic) getIntent().getSerializableExtra("topic");

        // Nếu có dữ liệu chủ đề, hiển thị nó
        if (topic != null) {
            topicTitle.setText("Chủ đề: " + topic.name);  // Hiển thị tên chủ đề trong TextView

            // Thiết lập RecyclerView để hiển thị danh sách từ vựng
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            // Convert Vocab to VocabEntity
            List<VocabEntity> convertedList = new ArrayList<>();
            for (VocabEntity v : topic.vocabList) {
                convertedList.add(new VocabEntity(v.term, v.def, v.ipa));  // Make sure VocabEntity has this constructor
            }
            recyclerView.setAdapter(new MyAdapter(this, convertedList));

        }
    }
}
