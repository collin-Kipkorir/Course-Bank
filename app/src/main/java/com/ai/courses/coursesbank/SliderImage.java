package com.ai.courses.coursesbank;

// SliderImage.java
public class SliderImage {
    private String imageUrl;

    public SliderImage() {
        // Default constructor required for Firebase
    }

    public SliderImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

