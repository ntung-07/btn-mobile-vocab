package com.example.mobile_vocab_project.vocab;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.example.mobile_vocab_project.R;
import com.example.mobile_vocab_project.VocabEntity;
import com.example.mobile_vocab_project.vocab.VocabPagerAdapter;
import java.util.ArrayList;

public class VocabDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_detail);
        Intent intent = getIntent();
        VocabEntity vocab = (VocabEntity) getIntent().getSerializableExtra("vocab");
        VocabFragment vocabFragment = VocabFragment.newInstance(vocab);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, vocabFragment);
        fragmentTransaction.commit();
        ArrayList<VocabEntity> vocabList = (ArrayList<VocabEntity>) intent.getSerializableExtra("vocabList");
        int position = intent.getIntExtra("position", 0); // Lấy vị trí từ Intent, mặc định là 0 nếu không có giá trị được truyền

        if (vocabList != null && !vocabList.isEmpty()) {
            ViewPager2 viewPager = findViewById(R.id.viewPager);
            VocabPagerAdapter adapter = new VocabPagerAdapter(this, vocabList);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(position);
        } else {
            // Fallback (optional): don't show pager or show error
            Toast.makeText(this, "No vocab list passed to activity!", Toast.LENGTH_SHORT).show();
        }

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        VocabPagerAdapter adapter = new VocabPagerAdapter(this, vocabList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }
}