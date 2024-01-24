package com.ai.courses.coursesbank;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionsFragment extends Fragment {

    private static final String ARG_COURSE_ID = "courseId";

    public QuestionsFragment() {
        // Required empty public constructor
    }

    public static QuestionsFragment newInstance(String courseId) {
        QuestionsFragment fragment = new QuestionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COURSE_ID, courseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_questions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Display questions content based on the courseId
        if (getArguments() != null) {
            String courseId = getArguments().getString(ARG_COURSE_ID);
            displayQuestionsContent(courseId);
        }
    }

    public void displayQuestionsContent(String courseId) {
        if (courseId != null) {
            DatabaseReference questionsRef = FirebaseDatabase.getInstance().getReference("course_questions").child(courseId);

            questionsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("QuestionsFragment", "DataSnapshot: " + dataSnapshot.toString());


                    StringBuilder questionsContent = new StringBuilder();

                    for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                        String questionText = questionSnapshot.child("text").getValue(String.class);
                        List<String> choices = new ArrayList<>();

                        for (DataSnapshot choiceSnapshot : questionSnapshot.child("choices").getChildren()) {
                            choices.add(choiceSnapshot.getValue(String.class));
                        }

                        String correctAnswer = questionSnapshot.child("correctAnswer").getValue(String.class);

                        // Append the question details to the content
                        questionsContent.append("Question: ").append(questionText).append("\n");
                        questionsContent.append("Choices: ").append(TextUtils.join(", ", choices)).append("\n");
                        questionsContent.append("Correct Answer: ").append(correctAnswer).append("\n\n");
                    }

                    updateQuestionsTextView(questionsContent.toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        } else {
            // Handle the case where courseId is null
        }
    }



    private void updateQuestionsTextView(String questionsContent) {
        View view = getView();
        if (view != null) {
            TextView questionsTextView = view.findViewById(R.id.questionsTextView);
            questionsTextView.setText(questionsContent);
        }
    }
}
