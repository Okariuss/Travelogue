package com.okada.travelogue.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.okada.travelogue.R;

public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    public SlideAdapter(Context context) {
        this.context = context;
    }

    int[]images={
            R.drawable.travel_boarding_page,
            R.drawable.hotel_boarding_page,
            R.drawable.need_help_boarding_page,
            R.drawable.have_good_time_boarding_page
    };
    String [] titles={
            "Want Travel?",
            "Need Hotel?",
            "Need Help",
            "Have a Good Time"
    };



    String[] descriptions={
            "Decide where do you want to go, find your ticket and travel.",
            "You can sort all the hotels in your destination according to price, star and comfort and you can choose the most beautiful hotel.",
            "You can contact us by mail, phone number or live chat. We will assist you in everything.",
            "Now sit back and enjoy. We will handle everything for you."
    };


    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(ConstraintLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_item_layout,container,false);
        ImageView imageView=view.findViewById(R.id.imageView);
        TextView textTitle=view.findViewById(R.id.textView);
        TextView textDesc=view.findViewById(R.id.textView2);

        imageView.setImageResource(images[position]);
        textTitle.setText(titles[position]);
        textDesc.setText(descriptions[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
