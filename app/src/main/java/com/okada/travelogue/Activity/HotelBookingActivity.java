package com.okada.travelogue.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

public class HotelBookingActivity extends AppCompatActivity {
    private ImageView hotelLogo;
    private ProgressBar progressBar;
    private TextView hotelNameText, hotelAddressText, roomCountText, descriptionText
            , totalPriceText, hotelBookingHeader, descriptionTxt, hotelBookingNightText;
    private RatingBar ratingBar;
    private String imageUrl, name, description, address, roomCount, roomPrice,roomName;
    private double rate;
    private Resources resources;
    private Button payBtn;
    private long entryMillis, leavingMillis;
    private int night,totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_booking);
        getExtras();
        initialization();
        getData();
        checkLanguages();
    }



    private void checkLanguages() {
        roomCountText.setText(resources.getString(R.string.room_count) + ": " + roomCount);
        hotelBookingHeader.setText(resources.getString(R.string.booking_summary));
        descriptionTxt.setText(resources.getString(R.string.description) + ":");
        payBtn.setText(resources.getString(R.string.pay));
        night =Integer.parseInt( (leavingMillis-entryMillis)/86400000+"");
        totalPrice=Integer.parseInt(roomPrice)*night;
        totalPriceText.setText(resources.getString(R.string.total_price) + ": " + totalPrice + "$");
        hotelBookingNightText.setText("For "+night+" night");

    }

    private void initialization() {
        hotelLogo = findViewById(R.id.hotel_booking_logo);
        progressBar = findViewById(R.id.hotel_booking_progress);
        hotelNameText = findViewById(R.id.hotel_booking_name_text);
        ratingBar = findViewById(R.id.hotel_booking_rating_bar);
        hotelAddressText = findViewById(R.id.hotel_address_text);
        roomCountText = findViewById(R.id.hotel_booking_room_count);
        descriptionText = findViewById(R.id.hotel_booking_desc);
        totalPriceText = findViewById(R.id.hotel_booking_total_price);
        resources = LanguageResourceClass.getResource(this);
        hotelBookingHeader = findViewById(R.id.hotel_booking_header);
        descriptionTxt = findViewById(R.id.description);
        payBtn = findViewById(R.id.pay);
        hotelBookingNightText=findViewById(R.id.hotel_booking_night_text);
    }

    private void getData() {
        progressBar.setVisibility(View.VISIBLE);
        Picasso.get().load(imageUrl).into(hotelLogo, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        hotelNameText.setText(name);
        hotelAddressText.setText(address);
        roomCountText.setText("Room Count: " + roomCount);
        description = description.replace("#", "\n");
        descriptionText.setText(description);
        totalPriceText.setText("Total Price: " + roomPrice + "$");
        ratingBar.setRating((float) rate);
    }

    private void getExtras() {
        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("imageUrl");
        name = intent.getStringExtra("name");
        description = intent.getStringExtra("description");
        address = intent.getStringExtra("address");
        roomCount = intent.getStringExtra("roomCount");
        roomPrice = intent.getStringExtra("roomPrice");
        rate = intent.getDoubleExtra("rate", -1);
        entryMillis = intent.getLongExtra("entryMillis", -1);
        roomName=intent.getStringExtra("roomName");
        leavingMillis = intent.getLongExtra("leavingMillis", -1);
    }

    public void goBack(View view) {
        onBackPressed();
    }

    public void pay(View view) {
        Intent intent=new Intent(this,PayActivity.class);
        intent.putExtra("isHotel",true);
        intent.putExtra("startTime",entryMillis+"");
        intent.putExtra("endTime",leavingMillis+"");
        intent.putExtra("hotelName",name);
        intent.putExtra("totalPrice",totalPrice);
        intent.putExtra("roomName",roomName);
        intent.putExtra("roomCount",roomCount);
        startActivity(intent);
        Animatoo.animateZoom(this);
    }
}