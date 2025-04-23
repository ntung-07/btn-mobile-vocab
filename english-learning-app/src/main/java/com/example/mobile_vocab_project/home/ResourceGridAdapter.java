package com.example.mobile_vocab_project.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_vocab_project.R;

import java.util.List;

// Inside your ResourceGridAdapter.java
public class ResourceGridAdapter extends RecyclerView.Adapter<ResourceGridAdapter.ResourceViewHolder> {

    public interface OnResourceClickListener {
        void onClick(ResourceItem item);
    }

    private final Context context;
    private final List<ResourceItem> resourceList;
    private final OnResourceClickListener listener;

    public ResourceGridAdapter(Context context, List<ResourceItem> resourceList, OnResourceClickListener listener) {
        this.context = context;
        this.resourceList = resourceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ResourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_resource, parent, false);
        return new ResourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResourceViewHolder holder, int position) {
        ResourceItem item = resourceList.get(position);
        holder.icon.setImageResource(item.getIconResId());
        holder.label.setText(item.getLabel());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resourceList.size();
    }

    static class ResourceViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView label;

        public ResourceViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.resourceIcon);
            label = itemView.findViewById(R.id.resourceLabel);
        }
    }
}

