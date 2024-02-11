package com.ai.courses.coursesbank;

import java.util.List;

public class Question {
    private String category;
    private boolean isCategoryHeader;
    private String questionText;
    private List<String> choices;
    private int correctChoiceIndex;
    private int selectedChoiceIndex = -1; // Default value indicating no selection
    // Method to check if the selected choice matches the correct choice index
    public boolean isCorrectlyAnswered() {
        return selectedChoiceIndex == correctChoiceIndex;
    }

    // Required empty constructor for Firebase
    public Question() {
    }

    public Question(String questionText, List<String> choices, int correctChoiceIndex) {
        this.questionText = questionText;
        this.choices = choices;
        this.correctChoiceIndex = correctChoiceIndex;
        this.isCategoryHeader = false;
    }
    // New getter and setter for category
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    // Getter and setter for correctChoiceIndex
    public int getCorrectChoiceIndex() {
        return correctChoiceIndex;
    }

    public void setCorrectChoiceIndex(int correctChoiceIndex) {
        this.correctChoiceIndex = correctChoiceIndex;
    }

    // Getter and setter for selectedChoiceIndex
    public int getSelectedChoiceIndex() {
        return selectedChoiceIndex;
    }

    public void setSelectedChoiceIndex(int selectedChoiceIndex) {
        this.selectedChoiceIndex = selectedChoiceIndex;
    }
    public Question(String category) {
        this.category = category;
        this.isCategoryHeader = true;
    }

    public boolean isCategoryHeader() {
        return isCategoryHeader;
    }
}
