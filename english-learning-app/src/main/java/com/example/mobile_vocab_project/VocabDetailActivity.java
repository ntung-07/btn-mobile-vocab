package com.example.mobile_vocab_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

public class VocabDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_detail);


        Intent intent = getIntent();
        VocabEntity vocab = (VocabEntity) getIntent().getSerializableExtra("vocab");


        VocabFragment vocabFragment = new VocabFragment(vocab);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, vocabFragment);
        fragmentTransaction.commit();
    }
}


