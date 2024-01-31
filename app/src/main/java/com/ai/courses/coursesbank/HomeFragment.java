package com.ai.courses.coursesbank;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment implements CourseAdapter.OnItemClickListener {
    private SliderView sliderView;
    private ImageSliderAdapter imageSliderAdapter;
    private List<SliderImage> sliderImageList;
    private DatabaseReference databaseReference;

    private LinearLayout dynamicCategoriesLayout;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize dynamicCategoriesLayout
        dynamicCategoriesLayout = view.findViewById(R.id.dynamicCategoriesLayout);

        // Initialize overall RecyclerView and Adapter
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        List<Course> courseList = new ArrayList<>();
        CourseAdapter adapter = new CourseAdapter(courseList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        // Fetch categories and dynamically create RecyclerViews
        fetchCategoriesAndCreateRecyclerViews();


        sliderView = view.findViewById(R.id.sliderView);
        sliderImageList = new ArrayList<>();
        imageSliderAdapter = new ImageSliderAdapter(sliderImageList);
        sliderView.setSliderAdapter(imageSliderAdapter);

// Enable auto-cycling
        sliderView.setAutoCycle(true);

// Set a delay for auto-cycling (in milliseconds)
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setScrollTimeInSec(5);
        sliderView.startAutoCycle();


        // Fetch slider images from Firebase
        fetchSliderImages();


        return view;
    }

    private void fetchCategoriesAndCreateRecyclerViews() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference categoriesReference = firebaseDatabase.getReference("courses");

        categoriesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dynamicCategoriesLayout.removeAllViews(); // Clear existing views

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String categoryName = categorySnapshot.getKey();
                    if (categoryName != null) {
                        // Create TextView for category
                        TextView categoryTextView = new TextView(getContext());
                        categoryTextView.setText(categoryName);
                        categoryTextView.setTypeface(null, Typeface.BOLD);
                        categoryTextView.setTextColor(R.color.black);
                        dynamicCategoriesLayout.addView(categoryTextView);

                        // Create RecyclerView for category
                        RecyclerView categoryRecyclerView = new RecyclerView(getContext());
                        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                        dynamicCategoriesLayout.addView(categoryRecyclerView);

                        // Create list and adapter for category
                        List<Course> categoryCourseList = new ArrayList<>();
                        CourseAdapter categoryAdapter = new CourseAdapter(categoryCourseList);
                        categoryAdapter.setOnItemClickListener(HomeFragment.this);
                        categoryRecyclerView.setAdapter(categoryAdapter);

                        // Fetch courses for the category
                        DatabaseReference categoryDatabaseReference = firebaseDatabase.getReference("courses").child(categoryName);
                        categoryDatabaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                categoryCourseList.clear();
                                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                                    Course course = courseSnapshot.getValue(Course.class);
                                    if (course != null) {
                                        categoryCourseList.add(course);
                                    }
                                }
                                categoryAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle errors
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
    private void fetchSliderImages() {
        DatabaseReference sliderDatabaseReference = FirebaseDatabase.getInstance().getReference("slider_images");
        sliderDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sliderImageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SliderImage sliderImage = snapshot.getValue(SliderImage.class);
                    if (sliderImage != null) {
                        sliderImageList.add(sliderImage);
                    }
                }

                // Update the data directly in the adapter
                imageSliderAdapter.sliderImageList = sliderImageList;
                imageSliderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    @Override
    public void onItemClick(Course course) {
        Intent intent = new Intent(getActivity(), CourseContentActivity.class);
        intent.putExtra("clicked_course", course);
        startActivity(intent);
    }

}