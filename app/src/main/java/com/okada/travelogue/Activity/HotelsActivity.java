package com.okada.travelogue.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.okada.travelogue.Adapters.RecyclerAdapterHotel;
import com.okada.travelogue.HelperClasses.HotelClass;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.R;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class HotelsActivity extends AppCompatActivity {
    private int roomCount, cityIndex, entryYear, entryMonth, entryDay, leaveYear, leaveMonth, leaveDay;
    private String city, entryDate, leavingDate;
    private String[] cityArr;
    private final Calendar myCalendar = Calendar.getInstance();
    private long entryMillis, leavingMillis;
    private ArrayList<HotelClass> hotels;
    private PowerSpinnerView searchableSpinnerCity;
    private TextView entryDateText, leavingDateText, hotelHeader;
    private LinearLayout entryDateLayout, leavingDateLayout;
    private RecyclerView recyclerView;
    private RecyclerAdapterHotel adapterHotel;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels);
        getExtras();
        initialization();
        addListener();
        getData();
        checkLanguages();
    }

    private void checkLanguages() {
        hotelHeader.setText(resources.getString(R.string.hotels));
        searchableSpinnerCity.setHint(resources.getString(R.string.city));
        entryDateText.setHint(resources.getString(R.string.entry_date));
        leavingDateText.setHint(resources.getString(R.string.leaving_date));

    }

    private void initialization() {
        searchableSpinnerCity = findViewById(R.id.SearchableSpinnerCity);
        searchableSpinnerCity.setItems(Arrays.asList(cityArr));
        searchableSpinnerCity.selectItemByIndex(cityIndex);
        entryDateText = findViewById(R.id.hotels_entry_date_text);
        leavingDateText = findViewById(R.id.hotels_leaving_date_text);
        entryDateLayout = findViewById(R.id.hotels_entry_date_layout);
        leavingDateLayout = findViewById(R.id.hotels_leaving_date_layout);
        recyclerView = findViewById(R.id.hotel_recycler_view);
        entryDateText.setText(entryDate);
        leavingDateText.setText(leavingDate);
        entryYear = Integer.parseInt(entryDate.substring(6));
        entryMonth = Integer.parseInt(entryDate.substring(0, 2));
        entryDay = Integer.parseInt(entryDate.substring(3, 5));
        leaveYear = Integer.parseInt(leavingDate.substring(6));
        leaveMonth = Integer.parseInt(leavingDate.substring(0, 2));
        leaveDay = Integer.parseInt(leavingDate.substring(3, 5));
        calculateMillis();
        hotels = new ArrayList<>();
        adapterHotel = new RecyclerAdapterHotel(hotels,HotelsActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterHotel);
        resources = LanguageResourceClass.getResource(this);
        hotelHeader = findViewById(R.id.hotel_header);
    }

    private void calculateMillis() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(entryYear, entryMonth, entryDay);
        entryMillis = cal.getTimeInMillis();
        cal.clear();
        cal.set(leaveYear, leaveMonth, leaveDay);
        leavingMillis = cal.getTimeInMillis();
    }

    private void addListener() {
        searchableSpinnerCity.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                searchableSpinnerCity.dismiss();
            }
        });
        searchableSpinnerCity.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {

                cityIndex = i1;
                getData();
            }
        });
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                entryDay = dayOfMonth;
                entryMonth = monthOfYear + 1;
                entryYear = year;
                calculateMillis();
                updateLabel();
                getData();
            }
        };
        entryDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(HotelsActivity.this, date, entryYear, entryMonth - 1,
                        entryDay);
                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                pickerDialog.show();
            }
        });
        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                leaveDay = dayOfMonth;
                leaveMonth = monthOfYear + 1;
                leaveYear = year;
                calculateMillis();
                updateLabel1();
                getData();
            }
        };

        leavingDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(HotelsActivity.this, date1, leaveYear, leaveMonth - 1,
                        leaveDay);
                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                pickerDialog.show();
            }
        });

    }

    private void getExtras() {
        Intent intent = getIntent();
        roomCount = intent.getIntExtra("roomCount", -1);
        cityIndex = intent.getIntExtra("city", -1);
        cityArr = intent.getStringArrayExtra("cityArr");
        entryDate = intent.getStringExtra("entryDate");
        leavingDate = intent.getStringExtra("leavingDate");

    }

    private void getData() {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hotels.clear();
                for (DataSnapshot ds : snapshot.child("hotels").getChildren()) {
                    if (ds.child("hotelCity").getValue().toString().equals(cityArr[cityIndex])) {
                        for (DataSnapshot hotelRooms : ds.child("hotelRooms").child("roomNum" + roomCount).getChildren()) {
                            boolean isEmpty = true;
                            if (entryMillis < leavingMillis) {
                                for (DataSnapshot fullDate : hotelRooms.child("fullDate").getChildren()) {

                                    if (Long.parseLong(fullDate.getKey()) <= entryMillis && Long.parseLong(fullDate.getValue() + "") > entryMillis) {
                                        isEmpty = false;
                                    } else if (Long.parseLong(fullDate.getKey()) < leavingMillis && Long.parseLong(fullDate.getValue() + "") >= leavingMillis) {
                                        isEmpty = false;
                                    } else if (Long.parseLong(fullDate.getKey()) > entryMillis && Long.parseLong(fullDate.getValue() + "") < leavingMillis) {
                                        isEmpty = false;
                                    }
                                }
                            } else {
                                isEmpty = false;
                                Toast.makeText(HotelsActivity.this, resources.getString(R.string.please_fill_date), Toast.LENGTH_SHORT).show();
                            }
                            if (isEmpty) {
                                hotels.add(new HotelClass(ds.getKey().toString()
                                        , ds.child("hotelImage").getValue().toString()
                                        , ds.child("hotelCity").getValue().toString()
                                        , ds.child("hotelAddress").getValue().toString()
                                        , hotelRooms.child("price").getValue().toString()
                                        , Double.parseDouble(ds.child("hotelStar").getValue().toString())
                                        , roomCount + ""
                                        , hotelRooms.child("description").getValue().toString()
                                        , entryMillis
                                        , leavingMillis
                                        ,hotelRooms.getKey().toString()
                                ));
                            }
                        }
                        adapterHotel.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HotelsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        entryDateText.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel1() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        leavingDateText.setText(sdf.format(myCalendar.getTime()));
    }

    public void goBack(View view) {
        onBackPressed();
    }
}