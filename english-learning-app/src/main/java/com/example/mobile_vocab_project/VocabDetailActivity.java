package com.example.mobile_vocab_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class VocabDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_detail);

        Intent intent = getIntent();
        ArrayList<Vocab> vocabList = (ArrayList<Vocab>) intent.getSerializableExtra("vocabList");
        int position = intent.getIntExtra("position", 0);

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        VocabPagerAdapter adapter = new VocabPagerAdapter(this, vocabList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position1) -> {}).attach();
    }
}