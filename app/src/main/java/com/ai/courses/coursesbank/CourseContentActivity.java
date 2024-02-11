package com.ai.courses.coursesbank;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

public class CourseContentActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_content);

        // Change the color of the status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary));
        }

        // Initialize WebView
        webView = findViewById(R.id.notesWebView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript

        // Retrieve clicked course from Intent
        Course clickedCourse = getIntent().getParcelableExtra("clicked_course");

        if (clickedCourse != null) {
            setTitle(clickedCourse.getCourseName());

            ImageView imageView = findViewById(R.id.courseContentImageView);
            TextView nameTextView = findViewById(R.id.courseContentNameTextView);

            // Load course image using Glide (or any other image loading library)
            Glide.with(this)
                    .load(clickedCourse.getCourseImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .into(imageView);

            nameTextView.setText(clickedCourse.getCourseName());

            // Display course content in WebView
            displayCourseContent(clickedCourse.getCourseDescription());
        } else {
            Toast.makeText(this, "No course data found", Toast.LENGTH_SHORT).show();
        }
    }

    // Public method to set and display course content
    public void displayCourseContent(String content) {
        if (webView != null) {
            // Load course content into WebView
            webView.loadData(content, "text/html", "utf-8");
        } else {
            Toast.makeText(this, "WebView is null", Toast.LENGTH_SHORT).show();
        }
    }
}
