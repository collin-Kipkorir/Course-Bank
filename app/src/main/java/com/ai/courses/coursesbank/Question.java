package com.ai.courses.coursesbank;

import java.util.List;

public class Question {
    private String questionText;
    private List<String> choices;
    private String correctChoice;

    // Required empty constructor for Firebase
    public Question() {
    }

    public Question(String questionText, List<String> choices, String correctChoice) {
        this.questionText = questionText;
        this.choices = choices;
        this.correctChoice = correctChoice;
    }

    // Getter and setter for questionText
    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    // Getter and setter for choices
    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    // Getter and setter for correctChoice
    public String getCorrectChoice() {
        return correctChoice;
    }

    public void setCorrectChoice(String correctChoice) {
        this.correctChoice = correctChoice;
    }
}

