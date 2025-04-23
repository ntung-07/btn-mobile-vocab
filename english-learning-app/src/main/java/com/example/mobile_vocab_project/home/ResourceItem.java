package com.example.mobile_vocab_project.home;

public class ResourceItem {
    public int iconResId;
    public String label;

    public ResourceItem(int iconResId, String label) {
        this.iconResId = iconResId;
        this.label = label;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getLabel() {
        return label;
    }
}
