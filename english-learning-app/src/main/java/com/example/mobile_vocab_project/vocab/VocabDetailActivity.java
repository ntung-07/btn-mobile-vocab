package com.example.mobile_vocab_project.vocab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.mobile_vocab_project.R;
import com.example.mobile_vocab_project.VocabEntity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;

public class VocabDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_detail);

        Intent intent = getIntent();

        // Lấy danh sách vocab và vị trí của item đang được chọn
        ArrayList<VocabEntity> vocabList = (ArrayList<VocabEntity>) intent.getSerializableExtra("vocabList");
        int position = intent.getIntExtra("position", 0);

        // Đảm bảo rằng vocabList không rỗng
        if (vocabList != null && !vocabList.isEmpty()) {
            ViewPager2 viewPager = findViewById(R.id.viewPager);
            VocabPagerAdapter adapter = new VocabPagerAdapter(this, vocabList);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(position, false);

            TabLayout tabLayout = findViewById(R.id.tabLayout);
            new TabLayoutMediator(tabLayout, viewPager, (tab, pos) -> {
                tab.setText(String.valueOf(pos + 1));  // Đặt số cho mỗi tab
            }).attach();
        } else {
            Toast.makeText(this, "No vocab list passed to activity!", Toast.LENGTH_SHORT).show();
        }
    }
}
