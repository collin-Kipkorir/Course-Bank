package com.ai.courses.coursesbank;

import java.util.List;

public class Question {
    private String text;
    private List<String> choices;
    private String correctAnswer;

    public Question() {
        // Default constructor required for Firebase
    }

    public Question(String text, List<String> choices, String correctAnswer) {
        this.text = text;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
    }

    public String getText() {
        return text;
    }

    public List<String> getChoices() {
        return choices;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
