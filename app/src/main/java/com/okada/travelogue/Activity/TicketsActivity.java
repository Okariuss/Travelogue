package com.okada.travelogue.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.okada.travelogue.Adapters.RecyclerAdapterTicket;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.HelperClasses.FlightClass;
import com.okada.travelogue.R;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TicketsActivity extends AppCompatActivity {
    private LinearLayout layout;
    private TextView textView, header;
    private final Calendar myCalendar = Calendar.getInstance();
    private PowerSpinnerView searchableSpinnerFrom, searchableSpinnerTo, searchableSpinnerPrice,
            searchableSpinnerAlphabet,searchableSpinnerStartHour,searchableSpinnerEndHour;
    private ImageView fromImageView, toImageView;
    private FirebaseFirestore fireStore;
    private ArrayList<String> cities,hour,favouritesList;
    private ArrayList<FlightClass> tickets;
    private String currentVehicle, date;
    private int fromIndex, toIndex,passengerCount;
    private RecyclerView ticketsRecycler;
    private RecyclerAdapterTicket adapterTicket;
    private Intent intent;
    private Resources resources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);
        initialization();
        addItemToSpinner();
        getIntentFromActivity();
        addListeners();
        configureRecycler();
        getTicketsFromFirebase();
        checkLanguage();
    }

    private void checkLanguage() {
        searchableSpinnerPrice.setHint(resources.getString(R.string.price));
        searchableSpinnerAlphabet.setHint(resources.getString(R.string.alphabet));
        searchableSpinnerStartHour.setHint(resources.getString(R.string.start));
        searchableSpinnerEndHour.setHint(resources.getString(R.string.finish));
        header.setText(resources.getString(R.string.tickets));
    }


    private void getIntentFromActivity() {
        currentVehicle = intent.getStringExtra("vehicle");
        fromIndex = intent.getIntExtra("from", -1);
        toIndex = intent.getIntExtra("to", -1);
        date = intent.getStringExtra("date");
        textView.setText(date);
        if (currentVehicle.equals("bus")) {
            fromImageView.setImageResource(R.drawable.ic_baseline_directions_bus_24);
            toImageView.setImageResource(R.drawable.ic_baseline_directions_bus_24);
        } else if (currentVehicle.equals("ship")) {
            fromImageView.setImageResource(R.drawable.ic_baseline_directions_boat_filled_24);
            toImageView.setImageResource(R.drawable.ic_baseline_directions_boat_filled_24);
        } else if (currentVehicle.equals("plane")) {
            fromImageView.setImageResource(R.drawable.take_off);
            toImageView.setImageResource(R.drawable.boarding);
        }
    }

    private void initialization() {
        layout = findViewById(R.id.tickets_date_layout);
        textView = findViewById(R.id.tickets_date_text);
        searchableSpinnerFrom = findViewById(R.id.SearchableSpinnerFrom);
        searchableSpinnerTo = findViewById(R.id.SearchableSpinnerTo);
        searchableSpinnerPrice = findViewById(R.id.SearchableSpinnerPrice);
        searchableSpinnerAlphabet = findViewById(R.id.SearchableSpinnerAlphabet);
        searchableSpinnerStartHour = findViewById(R.id.SearchableSpinnerStartHour);
        searchableSpinnerEndHour = findViewById(R.id.SearchableSpinnerEndHour);
        fromImageView = findViewById(R.id.tickets_from_image);
        toImageView = findViewById(R.id.tickets_to_image);
        header = findViewById(R.id.settings_header);
        ticketsRecycler = findViewById(R.id.tickets_recycler_view);
        intent=getIntent();
        tickets = new ArrayList<>();
        favouritesList=new ArrayList<>();
        passengerCount=intent.getIntExtra("passengerCount",-1);
        adapterTicket = new RecyclerAdapterTicket(tickets,passengerCount,favouritesList,TicketsActivity.this);
        fillFavouritesList();
        fireStore = FirebaseFirestore.getInstance();
        resources = LanguageResourceClass.getResource(this);
    }

    private void fillFavouritesList() {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid() + "")
                    .child("favourites");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favouritesList.clear();
                HashMap<String,String> map= (HashMap<String, String>) snapshot.getValue();
                if (map!=null)
                for (String s : map.keySet()) {
                    favouritesList.add(s);
                }
                adapterTicket.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        }

    }

    private void configureRecycler() {
        ticketsRecycler.setLayoutManager(new LinearLayoutManager(this));
        ticketsRecycler.setAdapter(adapterTicket);
    }

    private void addListeners() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                fillFavouritesList();
                getTicketsFromFirebase();
            }
        };

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] dateSplit=date.split("/");

                DatePickerDialog pickerDialog = new DatePickerDialog(TicketsActivity.this,
                        onDateSetListener, Integer.parseInt(dateSplit[2]),
                        Integer.parseInt(dateSplit[0])-1, Integer.parseInt(dateSplit[1]));
                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                pickerDialog.show();
            }
        });

        searchableSpinnerTo.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                searchableSpinnerTo.dismiss();
            }
        });
        searchableSpinnerFrom.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                searchableSpinnerFrom.dismiss();
            }
        });
        searchableSpinnerFrom.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @org.jetbrains.annotations.Nullable Object o, int i1, Object t1) {
                fromIndex = searchableSpinnerFrom.getSelectedIndex();
                fillFavouritesList();
                getTicketsFromFirebase();

            }
        });
        searchableSpinnerTo.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @org.jetbrains.annotations.Nullable Object o, int i1, Object t1) {
                toIndex = searchableSpinnerTo.getSelectedIndex();
                fillFavouritesList();
                getTicketsFromFirebase();
            }
        });

        searchableSpinnerPrice.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                searchableSpinnerPrice.dismiss();
            }
        });
        searchableSpinnerAlphabet.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                searchableSpinnerAlphabet.dismiss();
            }
        });
        searchableSpinnerStartHour.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                searchableSpinnerStartHour.dismiss();
            }
        });
        searchableSpinnerEndHour.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                searchableSpinnerEndHour.dismiss();
            }
        });
        searchableSpinnerStartHour.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @org.jetbrains.annotations.Nullable Object o, int i1, Object t1) {
                getTicketsFromFirebase();
            }
        });
        searchableSpinnerEndHour.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @org.jetbrains.annotations.Nullable Object o, int i1, Object t1) {
                getTicketsFromFirebase();
            }
        });
        searchableSpinnerPrice.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @org.jetbrains.annotations.Nullable Object o, int i1, Object t1) {
                getTicketsFromFirebase();
            }
        });
        searchableSpinnerAlphabet.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @org.jetbrains.annotations.Nullable Object o, int i1, Object t1) {
                getTicketsFromFirebase();

            }
        });



    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textView.setText(sdf.format(myCalendar.getTime()));
    }

    private void addItemToSpinner() {
        getCitiesFromFirebase();
        ArrayList<String> price = new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.price)));
        ArrayList<String> alphabet = new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.alphabet)));
         hour=new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hour.add(i+":"+"00");
            hour.add(i+":"+"30");
        }
        searchableSpinnerFrom.setItems(cities);
        searchableSpinnerTo.setItems(cities);
        searchableSpinnerPrice.setItems(price);
        searchableSpinnerAlphabet.setItems(alphabet);
        searchableSpinnerEndHour.setItems(hour);
        searchableSpinnerStartHour.setItems(hour);
    }

    private ArrayList<String> getCitiesFromFirebase() {
        cities = new ArrayList<>();
        CollectionReference collectionReference = fireStore.collection("Cities");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                cities.clear();
                if (error != null) {
                    Toast.makeText(TicketsActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } else if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> map = snapshot.getData();
                        String name = (String) map.get("Name");
                        cities.add(name);
                    }
                    searchableSpinnerFrom.setItems(cities);
                    searchableSpinnerTo.setItems(cities);
                    searchableSpinnerFrom.selectItemByIndex(fromIndex);
                    searchableSpinnerTo.selectItemByIndex(toIndex);
                }
            }
        });

        return cities;
    }

    private void getTicketsFromFirebase() {
        date = textView.getText().toString();
        CollectionReference collectionReference = fireStore.collection("Flights");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                tickets.clear();
                if (error != null) {
                    Toast.makeText(TicketsActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } else if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> map = snapshot.getData();
                        Timestamp timeTakeOff = (Timestamp) map.get("timetakeoff");
                        Timestamp timeLanding = (Timestamp) map.get("timelanding");
                        String imageUrl = (String) map.get("image");
                        int price = Integer.parseInt(map.get("price") + "");
                        String from = (String) map.get("from");
                        String to = (String) map.get("to");
                        String vehicle = (String) map.get("vehicle");
                        String companyName = (String) map.get("companyName");
                        String detail = (String) map.get("detail");
                        String ticketId = (String) map.get("flightId");

                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(timeTakeOff.getSeconds() * 1000);
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH) + 1;
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        int hourTakeOff = cal.get(Calendar.HOUR_OF_DAY)+4;
                        int minutesTakeOff = cal.get(Calendar.MINUTE);
                        boolean a=true;
                        if (searchableSpinnerStartHour.getSelectedIndex()!=-1){
                            String[] strTime=hour.get(searchableSpinnerStartHour.getSelectedIndex()).split(":");
                            if (hourTakeOff<Integer.parseInt(strTime[0])){
                                a=false;
                            }else if (hourTakeOff==Integer.parseInt(strTime[0])&&minutesTakeOff<Integer.parseInt(strTime[1])){
                                a=false;
                            }
                        }
                        boolean b=true;
                        if (searchableSpinnerEndHour.getSelectedIndex()!=-1){
                            String[] strTime=hour.get(searchableSpinnerEndHour.getSelectedIndex()).split(":");
                            if (hourTakeOff>Integer.parseInt(strTime[0])){
                                b=false;
                            }else if (hourTakeOff==Integer.parseInt(strTime[0])&&minutesTakeOff>Integer.parseInt(strTime[1])){
                                b=false;
                            }
                        }
                        String[] dateSub = date.split("/");
                        if (from.equals(cities.get(fromIndex)) && to.equals(cities.get(toIndex)) &&
                                Integer.parseInt(dateSub[0]) == month &&
                                Integer.parseInt(dateSub[1]) == day &&
                                Integer.parseInt(dateSub[2]) == year &&
                                currentVehicle.equals(vehicle) && a && b)
                            tickets.add(new FlightClass(imageUrl, from, to, vehicle, timeTakeOff, timeLanding, price,companyName,detail,ticketId));
                    }
                    if (searchableSpinnerPrice.getSelectedIndex()==0){
                        Collections.sort(tickets, new Comparator<FlightClass>() {
                            @Override
                            public int compare(FlightClass o1, FlightClass o2) {

                                return o1.getPrice()-o2.getPrice();
                            }
                        });
                    }else if (searchableSpinnerPrice.getSelectedIndex()==1){
                        Collections.sort(tickets, new Comparator<FlightClass>() {
                            @Override
                            public int compare(FlightClass o1, FlightClass o2) {

                                return o2.getPrice()-o1.getPrice();
                            }
                        });
                    }
                    if (searchableSpinnerAlphabet.getSelectedIndex()==0){
                        Collections.sort(tickets, new Comparator<FlightClass>() {
                            @Override
                            public int compare(FlightClass o1, FlightClass o2) {

                                return o1.getCompanyName().compareTo(o2.getCompanyName());
                            }
                        });
                    }else if (searchableSpinnerAlphabet.getSelectedIndex()==1){
                        Collections.sort(tickets, new Comparator<FlightClass>() {
                            @Override
                            public int compare(FlightClass o1, FlightClass o2) {

                                return o2.getCompanyName().compareTo(o1.getCompanyName());
                            }
                        });
                    }

                    adapterTicket.notifyDataSetChanged();
                }
            }
        });
    }
    public void refreshFilter(View view){
        searchableSpinnerAlphabet.clearSelectedItem();
        searchableSpinnerPrice.clearSelectedItem();
        searchableSpinnerStartHour.clearSelectedItem();
        searchableSpinnerEndHour.clearSelectedItem();
        getTicketsFromFirebase();
    }
    public  void goBack(View view){
        onBackPressed();
    }
}