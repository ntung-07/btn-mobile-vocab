package com.example.mobile_vocab_project;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    private TextView quizQuestion, scoreText;
    private Button option1, option2, option3, option4;
    private ArrayList<VocabEntity> vocabList;
    private ArrayList<VocabEntity> quizQuestions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private VocabEntity currentVocab;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quizQuestion = findViewById(R.id.quizQuestion);
        scoreText = findViewById(R.id.scoreText);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        // Lấy danh sách từ vựng từ intent
        vocabList = (ArrayList<VocabEntity>) getIntent().getSerializableExtra("vocabList");
        quizQuestions = new ArrayList<>(vocabList);
        Collections.shuffle(quizQuestions); // Xáo trộn câu hỏi

        scoreText.setText("Điểm: " + score);
        loadNextQuestion();

        // Thiết lập sự kiện nhấn cho các lựa chọn
        option1.setOnClickListener(v -> checkAnswer(option1.getText().toString()));
        option2.setOnClickListener(v -> checkAnswer(option2.getText().toString()));
        option3.setOnClickListener(v -> checkAnswer(option3.getText().toString()));
        option4.setOnClickListener(v -> checkAnswer(option4.getText().toString()));
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex < quizQuestions.size()) {
            currentVocab = quizQuestions.get(currentQuestionIndex);
            quizQuestion.setText("Nghĩa của từ \"" + currentVocab.term + "\" là gì?");

            // Tạo các lựa chọn
            ArrayList<String> options = new ArrayList<>();
            options.add(currentVocab.def); // Đáp án đúng
            while (options.size() < 4) {
                String randomDef = vocabList.get(random.nextInt(vocabList.size())).def;
                if (!options.contains(randomDef)) {
                    options.add(randomDef);
                }
            }
            Collections.shuffle(options);

            // Gán các lựa chọn vào nút
            option1.setText(options.get(0));
            option2.setText(options.get(1));
            option3.setText(options.get(2));
            option4.setText(options.get(3));
        } else {
            // Kết thúc quiz
            Toast.makeText(this, "Kết thúc Quiz! Điểm cuối: " + score, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void checkAnswer(String selectedAnswer) {
        if (selectedAnswer.equals(currentVocab.def)) {
            score++;
            Toast.makeText(this, "Đúng rồi!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sai! Đáp án đúng: " + currentVocab.def, Toast.LENGTH_SHORT).show();
        }
        scoreText.setText("Điểm: " + score);
        currentQuestionIndex++;
        loadNextQuestion();
    }
}