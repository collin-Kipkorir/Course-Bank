package com.ai.courses.coursesbank;

import android.os.Parcel;
import android.os.Parcelable;

public class Course implements Parcelable {
    private String courseId;
    private String courseName;
    private String courseDescription;
    private String courseImageUrl;

    // Empty constructor (required for Firebase)
    public Course() {
    }

    // Constructor
    public Course(String courseId, String courseName, String courseDescription, String courseImageUrl) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.courseImageUrl = courseImageUrl;
    }

    // Parcelable implementation
    protected Course(Parcel in) {
        courseId = in.readString();
        courseName = in.readString();
        courseDescription = in.readString();
        courseImageUrl = in.readString();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    // Getters and Setters
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseImageUrl() {
        return courseImageUrl;
    }

    public void setCourseImageUrl(String courseImageUrl) {
        this.courseImageUrl = courseImageUrl;
    }

    // Parcelable methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseId);
        dest.writeString(courseName);
        dest.writeString(courseDescription);
        dest.writeString(courseImageUrl);
    }
}
