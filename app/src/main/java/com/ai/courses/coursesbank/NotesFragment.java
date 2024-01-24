package com.ai.courses.coursesbank;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NotesFragment extends Fragment {

    private WebView webView;

    public NotesFragment() {
        // Required empty public constructor
    }

    // Factory method to create a new instance of NotesFragment
    public static NotesFragment newInstance(String courseDescription) {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putString("course_description", courseDescription);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        // Initialize WebView
        webView = view.findViewById(R.id.notesWebView);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String courseDescription = bundle.getString("course_description", "");
            displayCourseContent(courseDescription);
        }
    }

    // Public method to set and display course content
    public void displayCourseContent(String content) {
        if (webView != null) {
            // Load course content into WebView
            webView.loadData(content, "text/html", "utf-8");
        } else {
            Log.e("NotesFragment", "WebView is null");
        }
    }
}
