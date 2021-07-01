package com.okada.travelogue.Adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.okada.travelogue.Activity.HotelBookingActivity;
import com.okada.travelogue.HelperClasses.HotelClass;
import com.okada.travelogue.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapterHotel extends RecyclerView.Adapter<RecyclerAdapterHotel.HotelHolder> {
    private ArrayList<HotelClass> hotels;
    private Activity activity;
    public RecyclerAdapterHotel(ArrayList<HotelClass> hotels, Activity activity) {
        this.hotels = hotels;
        this.activity=activity;

    }

    @NonNull
    @Override
    public HotelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_hotel_item, parent, false);
        return new HotelHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelHolder holder, int position) {
        holder.progressBar.setVisibility(View.VISIBLE);
        Picasso.get().load(hotels.get(position).getImageUrl()).into(holder.hotelLogo, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        holder.hotelAddress.setText(hotels.get(position).getAddress());
        holder.hotelName.setText(hotels.get(position).getName());
        holder.hotelRate.setRating((float) hotels.get(position).getRate());
        holder.roomCount.setText(hotels.get(position).getRoomCount());
        holder.roomPrice.setText(hotels.get(position).getRoomPrice()+"$");
        holder.containerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.hotelLogo.getContext(), HotelBookingActivity.class);
                intent.putExtra("imageUrl",hotels.get(position).getImageUrl());
                intent.putExtra("name",hotels.get(position).getName());
                intent.putExtra("rate",hotels.get(position).getRate());
                intent.putExtra("description",hotels.get(position).getDescription());
                intent.putExtra("address",hotels.get(position).getAddress());
                intent.putExtra("roomCount",hotels.get(position).getRoomCount());
                intent.putExtra("roomPrice",hotels.get(position).getRoomPrice());
                intent.putExtra("entryMillis",hotels.get(position).getEntryMillis());
                intent.putExtra("leavingMillis",hotels.get(position).getLeavingMillis());
                intent.putExtra("roomName",hotels.get(position).getRoomName());

                Pair[] pairs = new Pair[6];
                pairs[0] = new Pair<View, String>(holder.containerLayout, "containerLayout");
                pairs[1] = new Pair<View, String>(holder.hotelLogo, "logoImage");
                pairs[2] = new Pair<View, String>(holder.hotelName, "hotelName");
                pairs[3] = new Pair<View, String>(holder.hotelRate, "hotelRate");
                pairs[4] = new Pair<View, String>(holder.hotelAddress, "hotelAddress");
                pairs[5] = new Pair<View, String>(holder.roomCountLayout, "roomCount");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, pairs);
                holder.containerLayout.getContext().startActivity(intent, activityOptions.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }

    class HotelHolder extends RecyclerView.ViewHolder {
        ImageView hotelLogo;
        TextView roomPrice, roomCount, hotelName, hotelAddress;
        RatingBar hotelRate;
        ProgressBar progressBar;
        RelativeLayout containerLayout;
        LinearLayout roomCountLayout;
        HotelHolder(@NonNull View v) {
            super(v);
            containerLayout=v.findViewById(R.id.hotel_item_container_layout);
            progressBar = v.findViewById(R.id.hotel_item_progress);
            hotelLogo = v.findViewById(R.id.hotel_item_logo);
            roomPrice = v.findViewById(R.id.hotel_item_room_price);
            roomCount = v.findViewById(R.id.hotel_item_room_count);
            hotelName = v.findViewById(R.id.hotel_name_text);
            hotelAddress = v.findViewById(R.id.hotel_address_text);
            hotelRate = v.findViewById(R.id.hotel_item_rating_bar);
            roomCountLayout = v.findViewById(R.id.room_count_layout);
        }
    }
}
