package com.example.mobile_vocab_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

public class AccountFragment extends Fragment {
    public AccountFragment() {
        super(R.layout.fragment_account);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View facebookBtn = view.findViewById(R.id.btnFacebook);
        View rateBtn = view.findViewById(R.id.btnRate);
        View feedbackBtn = view.findViewById(R.id.btnFeedback);
        View shareBtn = view.findViewById(R.id.btnShare);

        setupItem(facebookBtn, getString(R.string.facebook_page), R.drawable.facebook_img);
        setupItem(rateBtn, getString(R.string.rate_us), R.drawable.ratings_icon);
        setupItem(feedbackBtn, getString(R.string.feedback), R.drawable.feedback_icon);
        setupItem(shareBtn, getString(R.string.share_with_friends), R.drawable.share_icon);

        if (facebookBtn != null) {
            facebookBtn.setOnClickListener(v -> {
                String url = "https://www.facebook.com/ktqdNEU";
                Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url));
                startActivity(intent);
            });
        }
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
