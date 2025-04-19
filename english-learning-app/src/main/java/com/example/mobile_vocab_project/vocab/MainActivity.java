package com.example.vocab;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button guideButton, topicButton, toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo RecyclerView và các nút
        recyclerView = findViewById(R.id.recycler_view);
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
}
