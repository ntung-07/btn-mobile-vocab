package com.example.mobile_vocab_project.vocab;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.mobile_vocab_project.VocabEntity;  // Import đúng lớp VocabEntity
import java.util.List;

public class VocabPagerAdapter extends FragmentStateAdapter {

    private List<VocabEntity> vocabs;  // Sử dụng VocabEntity thay vì Vocab

    public VocabPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<VocabEntity> vocabs) {
        super(fragmentActivity);
        this.vocabs = vocabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return VocabFragment.newInstance(vocabs.get(position));  // Truyền VocabEntity vào VocabFragment
    }

    @Override
    public int getItemCount() {
        return vocabs.size();  // Số lượng phần tử trong danh sách
    }
}
