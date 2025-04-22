package com.example.mobile_vocab_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class AccountFragment extends Fragment {
    public AccountFragment() {
        super(R.layout.fragment_account); // your account layout file
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupItem(view.findViewById(R.id.btnFacebook), "Facebook page", R.drawable.ic_facebook);
        setupItem(view.findViewById(R.id.btnRate), "Rate us", R.drawable.ic_thumb_up);
        setupItem(view.findViewById(R.id.btnFeedback), "Feedback", R.drawable.ic_feedback);
        setupItem(view.findViewById(R.id.btnShare), "Share with friends", R.drawable.ic_share);
    }

    private void setupItem(View item, String label, int iconResId) {
        if (item != null) {
            View title = item.findViewById(R.id.title);
            View icon = item.findViewById(R.id.icon);

            if (title instanceof android.widget.TextView) {
                ((android.widget.TextView) title).setText(label);
            }
            if (icon instanceof android.widget.ImageView) {
                ((android.widget.ImageView) icon).setImageResource(iconResId);
            }
        }
    }
}


