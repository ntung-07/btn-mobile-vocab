package com.example.vocab;

import com.example.vocab.Vocab;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Vocab> data;

    public MyAdapter(Context context, ArrayList<Vocab> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        String item = data.get(position).term;
        holder.textView.setText(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);

            textView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        Intent intent = new Intent(context, VocabDetailActivity.class);
                        intent.putExtra("vocabList", data);
                        intent.putExtra("position", position);
                        context.startActivity(intent);
                    } else {
                        VocabFragment vocabFragment = VocabFragment.newInstance(data.get(position));
                        vocabFragment.show(((MainActivity) context).getSupportFragmentManager(), "vocab_dialog");
                    }
                }
            });
        }
    }
}
