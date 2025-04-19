package com.example.mobile_vocab_project;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class VocabPagerAdapter extends FragmentStateAdapter {


    private List<VocabEntity> vocabs;


    public VocabPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<VocabEntity> vocabs) {
        super(fragmentActivity);
        this.vocabs = vocabs;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new VocabFragment(vocabs.get(position));
    }


    @Override
    public int getItemCount() {
        return vocabs.size();
    }
}

