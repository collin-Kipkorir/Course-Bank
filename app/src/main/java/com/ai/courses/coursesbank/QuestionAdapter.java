package com.ai.courses.coursesbank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    private Context context;
    private List<Question> questionList;

    public QuestionAdapter(Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.questionTextView.setText(question.getQuestionText());
        List<String> choices = question.getChoices();
        // Set choices for RadioButton elements
        holder.choice1RadioButton.setText(choices.get(0));
        holder.choice2RadioButton.setText(choices.get(1));
        holder.choice3RadioButton.setText(choices.get(2));
        holder.choice4RadioButton.setText(choices.get(3));

        // Update the selected state of RadioButtons based on selectedChoiceIndex
        int selectedChoiceIndex = question.getSelectedChoiceIndex();
        holder.choice1RadioButton.setChecked(selectedChoiceIndex == 0);
        holder.choice2RadioButton.setChecked(selectedChoiceIndex == 1);
        holder.choice3RadioButton.setChecked(selectedChoiceIndex == 2);
        holder.choice4RadioButton.setChecked(selectedChoiceIndex == 3);

        // Set onClickListener for RadioButtons
        holder.choice1RadioButton.setOnClickListener(v -> {
            question.setSelectedChoiceIndex(0);
            notifyDataSetChanged(); // Update UI
        });
        holder.choice2RadioButton.setOnClickListener(v -> {
            question.setSelectedChoiceIndex(1);
            notifyDataSetChanged(); // Update UI
        });
        holder.choice3RadioButton.setOnClickListener(v -> {
            question.setSelectedChoiceIndex(2);
            notifyDataSetChanged(); // Update UI
        });
        holder.choice4RadioButton.setOnClickListener(v -> {
            question.setSelectedChoiceIndex(3);
            notifyDataSetChanged(); // Update UI
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        RadioButton choice1RadioButton;
        RadioButton choice2RadioButton;
        RadioButton choice3RadioButton;
        RadioButton choice4RadioButton;

        QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            choice1RadioButton = itemView.findViewById(R.id.choice1RadioButton);
            choice2RadioButton = itemView.findViewById(R.id.choice2RadioButton);
            choice3RadioButton = itemView.findViewById(R.id.choice3RadioButton);
            choice4RadioButton = itemView.findViewById(R.id.choice4RadioButton);
        }
    }

    // Method to get the selected position for a given question
    public int getSelectedPosition(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < questionList.size()) {
            return questionList.get(questionIndex).getSelectedChoiceIndex();
        }
        return -1;
    }
}
