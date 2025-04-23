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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_vocab_project.R;
import com.example.mobile_vocab_project.vocab.VocabDetailActivity;
import com.example.mobile_vocab_project.vocab.VocabEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private final Context context;
    private List<VocabEntity> data;
    private TextToSpeech tts;

    public MyAdapter(Context context, List<VocabEntity> data) {
        this.context = context;
        this.data = data;

        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
            } else {
                Toast.makeText(context, "Không thể khởi tạo TextToSpeech", Toast.LENGTH_SHORT).show();
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
        String displayText = vocab.term + " - " + vocab.def + " " + vocab.ipa;
        holder.textView.setText(displayText);

        // Phát âm
        holder.btnSpeak.setOnClickListener(v -> tts.speak(vocab.term, TextToSpeech.QUEUE_FLUSH, null, null));

        // Chia sẻ
        holder.btnShare.setOnClickListener(v -> {
            String[] options = {
                    "Chia sẻ qua ứng dụng khác",
                    "Chia sẻ qua Facebook",
                    "Chia sẻ qua Instagram"
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Chọn cách chia sẻ");
            builder.setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                        shareText(displayText, null);
                        break;
                    case 1:
                        shareText(displayText, "com.facebook.katana");
                        break;
                    case 2:
                        shareText(displayText, "com.instagram.android");
                        break;
                }

            });
            builder.show();
        });

        // Mở chi tiết
        holder.textView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, VocabDetailActivity.class);
                intent.putExtra("vocab", vocab);
                intent.putExtra("vocabList", new ArrayList<>(data));
                intent.putExtra("position", currentPosition);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<VocabEntity> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    public void shutdownTTS() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    private void shareText(String content, String packageName) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");

        if (packageName != null) {
            sendIntent.setPackage(packageName);
            if (sendIntent.resolveActivity(context.getPackageManager()) == null) {
                String appName = packageName.contains("facebook") ? "Facebook" : "Instagram";
                Toast.makeText(context, appName + " chưa được cài đặt", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        context.startActivity(Intent.createChooser(sendIntent, "Chia sẻ từ vựng"));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageButton btnShare, btnSpeak;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
            btnShare = itemView.findViewById(R.id.btn_share);
            btnSpeak = itemView.findViewById(R.id.btn_speak);
        }
    }
}
