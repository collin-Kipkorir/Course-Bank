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

public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_CATEGORY = 0;
    private static final int VIEW_TYPE_QUESTION = 1;

    private Context context;
    private List<Question> questionList;

    public QuestionAdapter(Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_CATEGORY) {
            View itemView = inflater.inflate(R.layout.item_category_header, parent, false);
            return new CategoryViewHolder(itemView);
        } else {
            View itemView = inflater.inflate(R.layout.item_question, parent, false);
            return new QuestionViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Question question = questionList.get(position);
        if (holder instanceof CategoryViewHolder) {
            // Bind category header data
            CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
            categoryViewHolder.categoryTextView.setText(question.getCategory());
        } else if (holder instanceof QuestionViewHolder) {
            // Bind question data
            QuestionViewHolder questionViewHolder = (QuestionViewHolder) holder;
            // Populate the question data as before
            questionViewHolder.questionTextView.setText(question.getQuestionText());
            List<String> choices = question.getChoices();
            // Set choices for RadioButton elements
            questionViewHolder.choice1RadioButton.setText(choices.get(0));
            questionViewHolder.choice2RadioButton.setText(choices.get(1));
            questionViewHolder.choice3RadioButton.setText(choices.get(2));
            questionViewHolder.choice4RadioButton.setText(choices.get(3));
            // Update the selected state of RadioButtons based on selectedChoiceIndex
            int selectedChoiceIndex = question.getSelectedChoiceIndex();
            questionViewHolder.choice1RadioButton.setChecked(selectedChoiceIndex == 0);
            questionViewHolder.choice2RadioButton.setChecked(selectedChoiceIndex == 1);
            questionViewHolder.choice3RadioButton.setChecked(selectedChoiceIndex == 2);
            questionViewHolder.choice4RadioButton.setChecked(selectedChoiceIndex == 3);
            // Set onClickListener for RadioButtons
            questionViewHolder.choice1RadioButton.setOnClickListener(v -> {
                question.setSelectedChoiceIndex(0);
                notifyDataSetChanged(); // Update UI
            });
            questionViewHolder.choice2RadioButton.setOnClickListener(v -> {
                question.setSelectedChoiceIndex(1);
                notifyDataSetChanged(); // Update UI
            });
            questionViewHolder.choice3RadioButton.setOnClickListener(v -> {
                question.setSelectedChoiceIndex(2);
                notifyDataSetChanged(); // Update UI
            });
            questionViewHolder.choice4RadioButton.setOnClickListener(v -> {
                question.setSelectedChoiceIndex(3);
                notifyDataSetChanged(); // Update UI
            });
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return questionList.get(position).isCategoryHeader() ? VIEW_TYPE_CATEGORY : VIEW_TYPE_QUESTION;
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTextView;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
        }
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
