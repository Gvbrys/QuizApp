package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView questionTextView;
    private ImageView questionImageView;
    private RadioGroup answersRadioGroup;
    private Button submitButton;
    private TextView timerTextView;

    private CountDownTimer countDownTimer;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private static final long TIME_LIMIT = 20000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = findViewById(R.id.questionTextView);
        questionImageView = findViewById(R.id.questionImageView);
        answersRadioGroup = findViewById(R.id.answersRadioGroup);
        submitButton = findViewById(R.id.submitButton);
        timerTextView = findViewById(R.id.timerTextView);

        questions = getSampleQuestions();

        loadQuestion(currentQuestionIndex);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                checkAnswer();
            }
        });
    }

    private List<Question> getSampleQuestions() {
        List<Question> questionList = new ArrayList<>();
        questionList.add(new Question(
                "Który z tych języków programowania nie jest językiem kompilowanym?",
                new String[]{"C++", "C", "JavaScript", "Rust"},
                2,
                R.drawable.q1));

        questionList.add(new Question(
                "Co oznacza skrót IDE?",
                new String[]{"Integrated Design Editor", "Integrated Development Environment", "Internet Development Editor", "Independent Debugging Environment"},
                1,
                R.drawable.q2));

        questionList.add(new Question(
                "Jaką funkcję pełni git commit?",
                new String[]{"Kopiuje pliki do innego repozytorium.", "Zapisuje zmiany w historii projektu.", "Przywraca pliki do poprzedniej wersji.", "Usuwa zmiany w plikach."},
                1,
                R.drawable.q3));

        return questionList;
    }

    private void loadQuestion(int questionIndex) {
        Question question = questions.get(questionIndex);
        questionTextView.setText(question.getQuestionText());
        questionImageView.setImageResource(question.getImageResId());

        answersRadioGroup.removeAllViews();

        for (int i = 0; i < question.getOptions().length; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(question.getOptions()[i]);
            radioButton.setId(i);
            answersRadioGroup.addView(radioButton);
        }

        startTimer();
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(TIME_LIMIT, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                timerTextView.setText("Pozostały czas: " + secondsRemaining + "s");
            }

            @Override
            public void onFinish() {
                timerTextView.setText("Czas minął!");
                Toast.makeText(MainActivity.this, "Czas na to pytanie minął!", Toast.LENGTH_SHORT).show();
                checkAnswerAutomatically();
            }
        }.start();
    }

    private void checkAnswerAutomatically() {

        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            loadQuestion(currentQuestionIndex);
        } else {
            Toast.makeText(this, "Koniec quizu! Twój wynik: " + score, Toast.LENGTH_LONG).show();
            submitButton.setEnabled(false);
        }
    }

    private void checkAnswer() {
        int selectedId = answersRadioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Wybierz odpowiedź!", Toast.LENGTH_SHORT).show();
            return;
        }

        Question currentQuestion = questions.get(currentQuestionIndex);
        if (selectedId == currentQuestion.getCorrectAnswerIndex()) {
            score++;
            Toast.makeText(this, "Poprawna odpowiedź!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Błędna odpowiedź!", Toast.LENGTH_SHORT).show();
        }

        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            loadQuestion(currentQuestionIndex);
        } else {
            Intent intent = new Intent(MainActivity.this, EndActivity.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
            finish();
        }
    }
}
