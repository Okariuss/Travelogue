package com.okada.travelogue.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.R;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class FindHotelActivity extends AppCompatActivity {
    private ArrayList<String> cities, rooms;
    private PowerSpinnerView searchableSpinnerCity, searchableSpinnerRoom;
    private final Calendar myCalendar = Calendar.getInstance();
    private LinearLayout entryDateLayout, leavingDateLayout;
    private TextView entryText, leavingText, findYourHotel, findHotelTravel;
    private Resources resources;
    private Button searchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_hotel);
        initialization();
        addItemToSpinner();
        getCitiesFromFirebase();
        addListener();
        checkLanguage();

    }

    private void checkLanguage() {
        findYourHotel.setText(resources.getString(R.string.find_your_hotel));
        searchableSpinnerCity.setHint(resources.getString(R.string.city));
        entryText.setHint(resources.getString(R.string.entry_date));
        leavingText.setHint(resources.getString(R.string.leaving_date));
        searchableSpinnerRoom.setHint(resources.getString(R.string.room));
        searchButton.setText(resources.getString(R.string.search));
        findHotelTravel.setText(resources.getString(R.string.travel_with_us));
    }

    private void initialization() {
        resources = LanguageResourceClass.getResource(this);
        cities = new ArrayList<>();
        rooms = new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.rooms)));
        searchableSpinnerCity = findViewById(R.id.SearchableSpinnerCity);
        searchableSpinnerRoom = findViewById(R.id.SearchableSpinnerRoom);
        entryDateLayout = findViewById(R.id.find_hotel_entry_date_layout);
        leavingDateLayout = findViewById(R.id.find_hotel_leaving_date_layout);
        entryText = findViewById(R.id.find_hotel_entry_date_text);
        leavingText = findViewById(R.id.find_hotel_leaving_date_text);
        findYourHotel = findViewById(R.id.hotel_header);
        searchButton = findViewById(R.id.search_button_hotel);
        findHotelTravel = findViewById(R.id.find_hotel_travel_with_us);

    }

    private ArrayList<String> getCitiesFromFirebase() {

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Cities");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                cities.clear();
                if (error != null) {
                    Toast.makeText(FindHotelActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } else if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> map = snapshot.getData();
                        String name = (String) map.get("Name");
                        cities.add(name);
                    }
                    searchableSpinnerCity.setItems(cities);

                }
            }
        });

        return cities;
    }

    private void addListener() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        entryDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(FindHotelActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
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
                updateLabel1();
            }
        };
        leavingDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(FindHotelActivity.this, date1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                pickerDialog.show();
            }
        });
        searchableSpinnerRoom.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                searchableSpinnerRoom.dismiss();
            }
        });
        searchableSpinnerCity.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                searchableSpinnerCity.dismiss();
            }
        });
    }

    private void addItemToSpinner() {
        searchableSpinnerRoom.setItems(rooms);
        searchableSpinnerCity.setItems(cities);
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        entryText.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel1() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        leavingText.setText(sdf.format(myCalendar.getTime()));
    }

    public void search(View view) {
        boolean hasError = false;
        String error = "";
        int a = 0;

        if (searchableSpinnerCity.getSelectedIndex() == -1) {
            hasError = true;
            a++;
            error += resources.getString(R.string.city);
        }
        if (entryText.getText().toString().trim().equals("")) {
            if (a != 0) error += ", ";
            a++;
            hasError = true;
            error += resources.getString(R.string.entry_date);
        }
        if (leavingText.getText().toString().trim().equals("")) {
            if (a != 0) error += ", ";
            hasError = true;
            error += resources.getString(R.string.leaving_date);
            a++;
        }
        if (searchableSpinnerRoom.getSelectedIndex() == -1) {
            if (a != 0) error += ", ";
            hasError = true;
            error += resources.getString(R.string.room);
        }

        if (hasError) {
            Toast.makeText(FindHotelActivity.this, error + " " + resources.getString(R.string.cannot_empty), Toast.LENGTH_LONG).show();
        } else {
            String[] cityArr = new String[cities.size()];
            for (int i = 0; i < cities.size(); i++) {
                cityArr[i] = cities.get(i);
            }
            Intent intent = new Intent(this, HotelsActivity.class);
            intent.putExtra("roomCount", searchableSpinnerRoom.getSelectedIndex() + 1);
            intent.putExtra("city", searchableSpinnerCity.getSelectedIndex());
            intent.putExtra("cityArr", cityArr);
            intent.putExtra("entryDate", entryText.getText().toString());
            intent.putExtra("leavingDate", leavingText.getText().toString());
            startActivity(intent);
            Animatoo.animateZoom(this);
        }
    }

    public void goBack(View view) {
        onBackPressed();
    }
}