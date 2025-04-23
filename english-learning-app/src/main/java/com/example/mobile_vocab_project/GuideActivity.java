package com.example.mobile_vocab_project;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        TextView guideText = findViewById(R.id.guideTextView);
        guideText.setText(getString(R.string.guide_text)); // 👉 Lấy từ strings.xml
    }
}
