package com.example.myapplication;

public class Question {
    private String questionText;
    private String[] options;
    private int correctAnswerIndex;
    private int imageResId;

    public Question(String questionText, String[] options, int correctAnswerIndex, int imageResId) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.imageResId = imageResId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public int getImageResId() {
        return imageResId;
    }
}
