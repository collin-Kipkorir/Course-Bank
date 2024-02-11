package com.ai.courses.coursesbank;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {

    private RecyclerView recyclerView;
    private QuestionAdapter adapter;
    private List<Question> questionList;
    private Button checkResultsButton;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        checkResultsButton = view.findViewById(R.id.checkResultsButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        questionList = new ArrayList<>();
        adapter = new QuestionAdapter(getContext(), questionList); // Pass context here
        recyclerView.setAdapter(adapter);
        retrieveQuestions();
        checkResultsButton.setVisibility(View.GONE);
        // Set OnClickListener for the checkResultsButton
        checkResultsButton.setOnClickListener(v -> checkResults());
// Clear cache before retrieving questions
        clearCache();
        return view;
    }
    private void clearCache() {
        // Get the Firebase database instance
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // Clear the persistence cache
        try {
            firebaseDatabase.setPersistenceEnabled(false);
            firebaseDatabase.setPersistenceEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void retrieveQuestions() {
// Clear cache before retrieving questions
        clearCache();
        DatabaseReference questionsRef = FirebaseDatabase.getInstance().getReference().child("questions");
        questionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionList.clear();
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot questionSnapshot : categorySnapshot.getChildren()) {
                        String questionText = questionSnapshot.child("questionText").getValue(String.class);
                        Integer correctChoiceIndexObject = questionSnapshot.child("correctChoiceIndex").getValue(Integer.class);
                        int correctChoiceIndex = correctChoiceIndexObject != null ? correctChoiceIndexObject : -1; // Default value if null
                        List<String> choices = new ArrayList<>();
                        // Fetch choices from the "choices" node
                        DataSnapshot choicesSnapshot = questionSnapshot.child("choices");
                        for (DataSnapshot choiceSnapshot : choicesSnapshot.getChildren()) {
                            String choice = choiceSnapshot.getValue(String.class);
                            choices.add(choice);
                        }
                        // Create a Question object with retrieved data
                        Question question = new Question(questionText, choices, correctChoiceIndex);
                        questionList.add(question);
                        checkResultsButton.setVisibility(View.VISIBLE);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
    public void checkResults() {
        DatabaseReference questionsRef = FirebaseDatabase.getInstance().getReference().child("questions");
        questionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Retrieve correct choices from Firebase
                List<Integer> correctChoices = new ArrayList<>();
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot questionSnapshot : categorySnapshot.getChildren()) {
                        Integer correctChoiceIndexObject = questionSnapshot.child("correctChoiceIndex").getValue(Integer.class);
                        if (correctChoiceIndexObject != null) {
                            correctChoices.add(correctChoiceIndexObject);
                        } else {
                            correctChoices.add(-1); // Add a default value if correct choice index is not available
                        }
                    }
                }

                int totalQuestions = questionList.size();
                int correctAnswers = 0;
                boolean allQuestionsAnswered = true; // Flag to track if all questions have at least one option selected

                // Reset error marking for all questions
                for (int i = 0; i < totalQuestions; i++) {
                    resetRadioButtonColor(i);
                }

                for (int i = 0; i < totalQuestions; i++) {
                   // Question question = questionList.get(i);
                    int selectedPosition = adapter.getSelectedPosition(i);
                    int correctChoiceIndex = correctChoices.get(i);
                    if (selectedPosition != RecyclerView.NO_POSITION) {
                        // Compare the selected position with the correct choice index
                        if (correctChoiceIndex != -1 && selectedPosition == correctChoiceIndex) { // Check if correctChoiceIndex is valid
                            correctAnswers++;
                        }
                    } else {
                        // No option selected for this question
                        allQuestionsAnswered = false;

                        // Mark the RadioButton as an error
                        markRadioButtonAsError(i);
                    }
                }

                // Debug logging
                Log.d("CheckResults", "Correct Choices: " + correctChoices);
                Log.d("CheckResults", "Correct Answers: " + correctAnswers);

                if (allQuestionsAnswered) {
                    // Calculate percentage
                    int percentage = (correctAnswers * 100) / totalQuestions;

                    // Display a success message with the score
                    String message = "Congratulations! You scored " + percentage + "%";
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    // Show a Toast indicating that all questions need to have at least one option selected
                    Toast.makeText(getContext(), "Please select an option for all questions.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    private void markRadioButtonAsError(int position) {
        // Get the ViewHolder for the specified position
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder instanceof QuestionAdapter.QuestionViewHolder) {
            QuestionAdapter.QuestionViewHolder questionViewHolder = (QuestionAdapter.QuestionViewHolder) viewHolder;

            // Set error state for the RadioButtons (change their text color to red)
            questionViewHolder.choice1RadioButton.setTextColor(Color.RED);
            questionViewHolder.choice2RadioButton.setTextColor(Color.RED);
            questionViewHolder.choice3RadioButton.setTextColor(Color.RED);
            questionViewHolder.choice4RadioButton.setTextColor(Color.RED);
        }
    }

    private void resetRadioButtonColor(int position) {
        // Get the ViewHolder for the specified position
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder instanceof QuestionAdapter.QuestionViewHolder) {
            QuestionAdapter.QuestionViewHolder questionViewHolder = (QuestionAdapter.QuestionViewHolder) viewHolder;

            // Reset text color for RadioButtons
            questionViewHolder.choice1RadioButton.setTextColor(Color.BLACK);
            questionViewHolder.choice2RadioButton.setTextColor(Color.BLACK);
            questionViewHolder.choice3RadioButton.setTextColor(Color.BLACK);
            questionViewHolder.choice4RadioButton.setTextColor(Color.BLACK);
        }
    }
}
