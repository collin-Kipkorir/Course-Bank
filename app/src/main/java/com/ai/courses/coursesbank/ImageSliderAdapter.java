package com.ai.courses.coursesbank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class ImageSliderAdapter extends SliderViewAdapter<ImageSliderAdapter.SliderViewHolder> {

    public List<SliderImage> sliderImageList;

    public ImageSliderAdapter(List<SliderImage> sliderImageList) {
        this.sliderImageList = sliderImageList;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_slider, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {
        SliderImage sliderImage = sliderImageList.get(position);

        // Load the image using Glide
        Glide.with(viewHolder.imageView.getContext())
                .load(sliderImage.getImageUrl())
                .centerCrop()
                .into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return sliderImageList.size();
    }

    static class SliderViewHolder extends SliderViewAdapter.ViewHolder {
        ImageView imageView;

        public SliderViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sliderImage);
        }
    }
}
