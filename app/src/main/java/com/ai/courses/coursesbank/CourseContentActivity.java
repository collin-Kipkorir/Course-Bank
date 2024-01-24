package com.ai.courses.coursesbank;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

public class CourseContentActivity extends AppCompatActivity {

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

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        // Set up ViewPager with the adapter
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // Connect TabLayout with ViewPager
        tabLayout.setupWithViewPager(viewPager);

        // Receive data from the clicked item
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

            // Set up ViewPager with the adapter
            setCourseContent(viewPager, clickedCourse);

            // Set up TabLayout with ViewPager
            tabLayout.setupWithViewPager(viewPager);

            // Set the default selected tab (Notes)
            tabLayout.getTabAt(0).select();

            // Load the content of the default selected tab (Notes)
            loadFragmentContent(0, clickedCourse);
        }
    }

    private void setCourseContent(ViewPager viewPager, Course clickedCourse) {
        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager());

        // Pass the course description to NotesFragment and courseId to QuestionsFragment
        NotesFragment notesFragment = NotesFragment.newInstance(clickedCourse.getCourseDescription());
        QuestionsFragment questionsFragment = QuestionsFragment.newInstance(clickedCourse.getCourseId());

        adapter.addFragment(notesFragment, "Notes");
        adapter.addFragment(questionsFragment, "Questions");
        viewPager.setAdapter(adapter);
    }

    private void loadFragmentContent(int position, Course clickedCourse) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + position);

        if (fragment != null) {
            if (position == 0 && fragment instanceof NotesFragment) {
                ((NotesFragment) fragment).displayCourseContent(clickedCourse.getCourseDescription());
            } else if (position == 1 && fragment instanceof QuestionsFragment) {
                ((QuestionsFragment) fragment).displayQuestionsContent(clickedCourse.getCourseId());
            }
        }
    }
}
