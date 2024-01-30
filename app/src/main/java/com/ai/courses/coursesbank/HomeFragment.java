package com.ai.courses.coursesbank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private View view;
    private SliderView sliderView;
    private ImageSliderAdapter imageSliderAdapter;
    private List<SliderImage> sliderImageList;
    private DatabaseReference databaseReference;

    private RecyclerView recyclerView;
    private CourseAdapter adapter;
    private List<Course> courseList;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize overall RecyclerView and Adapter
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        courseList = new ArrayList<>();
        adapter = new CourseAdapter(courseList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase for overall courses
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("courses");

        // Attach a listener to retrieve data for overall courses
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseList.clear();

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot courseSnapshot : categorySnapshot.getChildren()) {
                        Course course = courseSnapshot.getValue(Course.class);
                        courseList.add(course);
                    }
                }

                adapter.notifyDataSetChanged();
                Log.d("Firebase", "Number of courses: " + courseList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });

        // Initialize RecyclerView and Adapter for the first category
        RecyclerView recyclerViewCategory1 = view.findViewById(R.id.recyclerView2);
        recyclerViewCategory1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        List<Course> courseListCategory1 = new ArrayList<>();
        CourseAdapter adapterCategory1 = new CourseAdapter(courseListCategory1);
        adapterCategory1.setOnItemClickListener(this);
        recyclerViewCategory1.setAdapter(adapterCategory1);

        // Initialize Firebase for the first category
        FirebaseDatabase firebaseDatabaseCategory1 = FirebaseDatabase.getInstance();
        DatabaseReference databaseReferenceCategory1 = firebaseDatabaseCategory1.getReference("courses").child("category1");

        // Attach a listener to retrieve data for the first category
        databaseReferenceCategory1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseListCategory1.clear();

                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    Course course = courseSnapshot.getValue(Course.class);
                    courseListCategory1.add(course);
                }

                adapterCategory1.notifyDataSetChanged();
                Log.d("Firebase", "Number of courses in category1: " + courseListCategory1.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });

        // Repeat the above code for the second category
        RecyclerView recyclerViewCategory2 = view.findViewById(R.id.recyclerView3);
        recyclerViewCategory2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        List<Course> courseListCategory2 = new ArrayList<>();
        CourseAdapter adapterCategory2 = new CourseAdapter(courseListCategory2);
        adapterCategory2.setOnItemClickListener(this);
        recyclerViewCategory2.setAdapter(adapterCategory2);

        FirebaseDatabase firebaseDatabaseCategory2 = FirebaseDatabase.getInstance();
        DatabaseReference databaseReferenceCategory2 = firebaseDatabaseCategory2.getReference("courses").child("category3");

        databaseReferenceCategory2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseListCategory2.clear();

                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    Course course = courseSnapshot.getValue(Course.class);
                    courseListCategory2.add(course);
                }

                adapterCategory2.notifyDataSetChanged();
                Log.d("Firebase", "Number of courses in category2: " + courseListCategory2.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });


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
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("textsviews");

// Assuming you have a TextView with ID "course_category" in your layout
        TextView categoryTextView1 = view.findViewById(R.id.course_category1);
        TextView categoryTextView2 = view.findViewById(R.id.course_category2);
        TextView categoryTextView3 = view.findViewById(R.id.course_category3);

        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear existing text
                categoryTextView1.setText("");
                categoryTextView2.setText("");
                categoryTextView3.setText("");

                // Loop through the children of "textsviews"
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String categoryKey = categorySnapshot.getKey();
                    String categoryText = categorySnapshot.child("categories").getValue(String.class);

                    if (categoryKey != null && categoryText != null) {
                        switch (categoryKey) {
                            case "category1":
                                categoryTextView1.setText(categoryText);
                                break;
                            case "category2":
                                categoryTextView2.setText(categoryText);
                                break;
                            case "category3":
                                categoryTextView3.setText(categoryText);
                                break;
                            // Add more cases for additional categories as needed
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });

        return view;
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
