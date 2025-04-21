package com.example.mobile_vocab_project;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_vocab_project.vocab.VocabDetailActivity;

import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    private List<VocabEntity> data;
    private TextToSpeech tts;

    public MyAdapter(Context context, List<VocabEntity> data) {
        this.context = context;
        this.data = data;

        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
            } else {
                Toast.makeText(context, "TextToSpeech init failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VocabEntity vocab = data.get(position);

        holder.textView.setText(vocab.term);

        holder.textView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VocabDetailActivity.class);
            intent.putExtra("vocabList", new java.util.ArrayList<>(data));
            intent.putExtra("position", position);
            context.startActivity(intent);
        });

        holder.btnSpeak.setOnClickListener(v -> {
            tts.speak(vocab.term, TextToSpeech.QUEUE_FLUSH, null, null);
        });

        holder.btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, vocab.term + " - " + vocab.def + " " + vocab.ipa);
            context.startActivity(Intent.createChooser(shareIntent, "Chia sẻ từ vựng"));
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<VocabEntity> newData) {
        this.data = newData;
    }

    public void shutdownTTS() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageButton btnSpeak, btnShare;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
            btnSpeak = itemView.findViewById(R.id.btn_speak);
            btnShare = itemView.findViewById(R.id.btn_share);
        }
    }
}
