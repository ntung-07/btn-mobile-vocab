package com.example.mobile_vocab_project.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_vocab_project.R;
import com.example.mobile_vocab_project.vocab.VocabEntity;

public class WordDetailActivity extends AppCompatActivity {

    public static final String EXTRA_VOCAB = "extra_vocab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);

        VocabEntity vocab = (VocabEntity) getIntent().getSerializableExtra(EXTRA_VOCAB);

        TextView tvTerm = findViewById(R.id.tvTerm);
        TextView tvIPA = findViewById(R.id.tvIPA);
        TextView tvDefinition = findViewById(R.id.tvDefinition);

        if (vocab != null) {
            tvTerm.setText(vocab.term);
            tvIPA.setText(vocab.ipa != null ? vocab.ipa : "");
            tvDefinition.setText(vocab.def);
        }
    }

    // Static method to launch from anywhere
    public static void start(Context context, VocabEntity vocab) {
        Intent intent = new Intent(context, WordDetailActivity.class);
        intent.putExtra(EXTRA_VOCAB, vocab);
        context.startActivity(intent);
    }
}
