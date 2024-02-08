package com.ai.courses.coursesbank;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

        // Set OnClickListener for the checkResultsButton
        checkResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkResults(v);
            }
        });

        return view;
    }

    private void retrieveQuestions() {
        DatabaseReference questionsRef = FirebaseDatabase.getInstance().getReference().child("questions");
        questionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionList.clear();
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot questionSnapshot : categorySnapshot.getChildren()) {
                        String questionText = questionSnapshot.child("questionText").getValue(String.class);
                        String correctChoice = questionSnapshot.child("correctChoice").getValue(String.class);
                        List<String> choices = new ArrayList<>();
                        // Fetch choices from the "choices" node
                        DataSnapshot choicesSnapshot = questionSnapshot.child("choices");
                        for (DataSnapshot choiceSnapshot : choicesSnapshot.getChildren()) {
                            String choice = choiceSnapshot.getValue(String.class);
                            choices.add(choice);
                        }
                        // Create a Question object with retrieved data
                        Question question = new Question(questionText, choices, correctChoice);
                        questionList.add(question);
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

    // Method to check results when the button is clicked
    public void checkResults(View view) {
        // Implementation to check results...
    }
}

