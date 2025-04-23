package com.example.mobile_vocab_project.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_vocab_project.R;
import com.example.mobile_vocab_project.vocab.VocabEntity;

import java.util.List;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.SuggestionViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(VocabEntity vocab);
    }

    private List<VocabEntity> vocabList;
    private OnItemClickListener listener;

    public SuggestionAdapter(List<VocabEntity> vocabList, OnItemClickListener listener) {
        this.vocabList = vocabList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggestion, parent, false);
        return new SuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {
        VocabEntity vocab = vocabList.get(position);
        holder.wordText.setText(vocab.term);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(vocab));
    }

    @Override
    public int getItemCount() {
        return vocabList.size();
    }

    static class SuggestionViewHolder extends RecyclerView.ViewHolder {
        TextView wordText;

        public SuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            wordText = itemView.findViewById(R.id.textWord);
        }
    }
}
