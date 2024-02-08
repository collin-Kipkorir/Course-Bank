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
        // Set choices for RadioButton elements
        holder.choice1RadioButton.setText(question.getChoices().get(0));
        holder.choice2RadioButton.setText(question.getChoices().get(1));
        holder.choice3RadioButton.setText(question.getChoices().get(2));
        holder.choice4RadioButton.setText(question.getChoices().get(3));
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

}
