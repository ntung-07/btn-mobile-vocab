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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Vocab> data;
    private TextToSpeech tts;

    public MyAdapter(Context context, ArrayList<Vocab> data) {
        this.context = context;
        this.data = data;

        // Khởi tạo TextToSpeech
        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US); // Chọn giọng UK, có thể đổi sang Locale.US
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
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        Vocab vocab = data.get(position);
        String displayText = vocab.term + " - " + vocab.def + " " + vocab.ipa;
        holder.textView.setText(displayText);

        // Nút chia sẻ
        holder.btnShare.setOnClickListener(v -> {
            String content = vocab.term + " - " + vocab.def + " " + vocab.ipa;

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
                        shareText(content, null);
                        break;
                    case 1:
                        shareText(content, "com.facebook.katana");
                        break;
                    case 2:
                        shareText(content, "com.instagram.android");
                        break;
                }
            });
            builder.show();
        });

        // Nút phát âm
        holder.btnSpeak.setOnClickListener(v -> {
            String wordToSpeak = vocab.term;
            tts.speak(wordToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
        });

        // Click vào từ để xem chi tiết
        holder.textView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, VocabDetailActivity.class);
                intent.putExtra("vocab", data.get(currentPosition));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageButton btnShare, btnSpeak;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
            btnShare = itemView.findViewById(R.id.btn_share);
            btnSpeak = itemView.findViewById(R.id.btn_speak);
        }
    }

    // Hàm chia sẻ với tùy chọn package
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

    // Dọn bộ nhớ khi không dùng nữa (gọi từ Activity khi onDestroy nếu cần)
    public void shutdownTTS() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}

// Lớp Vocab
class Vocab implements Serializable {
    String term;
    String def;
    String ipa;

    public Vocab(String term, String def, String ipa) {
        this.term = term;
        this.def = def;
        this.ipa = ipa;
    }
}
