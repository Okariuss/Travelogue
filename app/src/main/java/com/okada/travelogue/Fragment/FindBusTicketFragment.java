package com.okada.travelogue.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
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
import com.okada.travelogue.Activity.TicketsActivity;
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


public class FindBusTicketFragment extends Fragment {
    private LinearLayout layout;
    private TextView textView,findPlaneTravelWithUs;
    private final Calendar myCalendar = Calendar.getInstance();
    private PowerSpinnerView searchableSpinnerFrom, searchableSpinnerTo, searchableSpinnerPassenger;
    private FirebaseFirestore firestore;
    private ImageButton button;
    private ArrayList<String> cities,passengers;
    private Button searchButton;
    private Resources resources;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_bus_ticket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialization(view);
        addListeners();
        checkLanguage();
        addItemToSpinner();
    }

    private void checkLanguage() {
        searchableSpinnerFrom.setHint(resources.getString(R.string.from));
        searchableSpinnerTo.setHint(resources.getString(R.string.to));
        textView.setHint(resources.getString(R.string.select_date));
        searchableSpinnerPassenger.setHint(resources.getString(R.string.passenger_num));
        findPlaneTravelWithUs.setText(resources.getString(R.string.travel_with_us));
        searchButton.setText(resources.getString(R.string.search));
        passengers = new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.passengers)));

    }

    private void initialization(View view) {
        layout = view.findViewById(R.id.find_bus_ticket_date_layout);
        textView = view.findViewById(R.id.find_bus_ticket_date_text);
        searchableSpinnerFrom = view.findViewById(R.id.SearchableSpinnerFrom);
        searchableSpinnerTo = view.findViewById(R.id.SearchableSpinnerTo);
        searchableSpinnerPassenger = view.findViewById(R.id.SearchableSpinnerPassenger);
        button = view.findViewById(R.id.changeCityPlaceButton);
        searchButton=view.findViewById(R.id.bus_search_button);
        findPlaneTravelWithUs=view.findViewById(R.id.find_plane_travel_with_us);
        firestore = FirebaseFirestore.getInstance();
        resources = LanguageResourceClass.getResource(getContext());
    }

    private void addItemToSpinner() {
        getCitiesFromFirebase();
            searchableSpinnerFrom.setItems(cities);
            searchableSpinnerTo.setItems(cities);
            searchableSpinnerPassenger.setItems(passengers);
    }

    private ArrayList<String> getCitiesFromFirebase() {
        cities = new ArrayList<>();

            CollectionReference collectionReference = firestore.collection("Cities");
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    cities.clear();
                    if (error != null) {
                        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    } else if (value != null) {
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            Map<String, Object> map = snapshot.getData();
                            String name = (String) map.get("Name");
                            cities.add(name);
                        }
                        searchableSpinnerFrom.setItems(cities);
                        searchableSpinnerTo.setItems(cities);

                    }
                }
            });

        return cities;
    }



    private void addListeners() {
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

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                pickerDialog.show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int from=searchableSpinnerFrom.getSelectedIndex();
                int to=searchableSpinnerTo.getSelectedIndex();
                searchableSpinnerFrom.selectItemByIndex(to);
                searchableSpinnerTo.selectItemByIndex(from);
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
        searchableSpinnerPassenger.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                searchableSpinnerPassenger.dismiss();
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasError=false;
                String error="";
                if (searchableSpinnerFrom.getSelectedIndex()==-1){
                    hasError=true;
                    error+=resources.getString(R.string.from) + ", ";
                }
                if (searchableSpinnerTo.getSelectedIndex()==-1){
                    hasError=true;
                    error+=resources.getString(R.string.to) + ", ";
                }
                if (textView.getText().toString().trim().equals("")){
                    hasError=true;
                    error+=resources.getString(R.string.date) + ", ";
                }
                if (searchableSpinnerPassenger.getSelectedIndex()==-1){
                    hasError=true;
                    error+=resources.getString(R.string.passenger);
                }
                if (hasError){
                    Toast.makeText(getActivity(),error+" "+resources.getString(R.string.cannot_empty),Toast.LENGTH_LONG).show();
                }else {
                    if (getActivity()!=null){
                    Intent intent =new Intent(getActivity(), TicketsActivity.class);
                        intent.putExtra("vehicle","bus");
                        intent.putExtra("passengerCount",searchableSpinnerPassenger.getSelectedIndex()+1);
                        intent.putExtra("from", searchableSpinnerFrom.getSelectedIndex());
                        intent.putExtra("to", searchableSpinnerTo.getSelectedIndex());
                        intent.putExtra("date",textView.getText().toString());
                        intent.putExtra("passenger", passengers.get(searchableSpinnerPassenger.getSelectedIndex()));
                        getActivity().startActivity(intent);
                    Animatoo.animateZoom(getActivity());}
                }

            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textView.setText(sdf.format(myCalendar.getTime()));
    }

}