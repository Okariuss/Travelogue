package com.okada.travelogue.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.okada.travelogue.Adapters.RecyclerAdapterSeat;
import com.okada.travelogue.HelperClasses.HelperClass;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {
    private String ticketId, imageUrl;
    private int price, hourTakeOff, minutesTakeOff, hourLanding, minutesLanding,
            hourTotal, minutesTotal, passengerCount;
    private ImageView logo;
    private TextView bookingStartTime, bookingEndTime, bookingTotalTime, ticketPrice, bookingDetail,
            bookingHeader, details, seat, seatPrice, autoSeat, passengerInfo, totalPrice;
    private ProgressBar progressBar;
    private FirebaseFirestore fireStore;
    private HashMap<Integer, HashMap<String, String>> passengersMap;
    private Button selectSeatButton, addPassengerInfo, pay;
    private Resources resources;
    private String[] selectedSeats;
    private String busySeats, flightId;
    private int seatPriceInt, totalPriceInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        getExtras();
        initialization();
        fillViews();
        getDataFromFirebase();
        checkLanguage();
    }

    private void checkLanguage() {
        bookingHeader.setText(resources.getString(R.string.booking_summary));
        details.setText(resources.getString(R.string.details));
        seat.setText(resources.getString(R.string.seat));
        selectSeatButton.setText(resources.getString(R.string.select_seat));
        seatPrice.setText(resources.getString(R.string.seat_price) + ": " + seatPriceInt + "$");
        autoSeat.setText(resources.getString(R.string.auto_seat));
        passengerInfo.setText(resources.getString(R.string.passenger_info));
        addPassengerInfo.setText(resources.getString(R.string.add));
        totalPriceInt = price * passengerCount + seatPriceInt;
        totalPrice.setText(resources.getString(R.string.total_price) + ": " + totalPriceInt + "$");
        pay.setText(resources.getString(R.string.pay));
    }


    private void getDataFromFirebase() {
        CollectionReference collectionReference = fireStore.collection("Flights");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(BookingActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } else if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> map = snapshot.getData();
                        flightId = (String) map.get("flightId");
                        String detailFS = (String) map.get("detail");
                        String detailLast = "";
                        if (ticketId.equalsIgnoreCase(flightId)) {
                            String[] detailSplit = detailFS.split("#");
                            busySeats = (String) map.get("busySeats");
                            for (int i = 0; i < detailSplit.length; i++) {
                                if (i != detailSplit.length - 1)
                                    detailLast += detailSplit[i] + "\n\n";
                                else detailLast += detailSplit[i];
                            }
                            bookingDetail.setText(detailLast);
                        }
                    }

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedSeats = HelperClass.getSelectedSeats(passengerCount);
        seatPriceInt=0;
        if (selectedSeats[0] != null) {
            for (int i = 0; i < selectedSeats.length; i++) {
                int a = Integer.parseInt(selectedSeats[i].substring(1)) - 1;
                if (a % 5 == 0) {
                    seatPriceInt += 15;
                }
            }
        }
        checkLanguage();
    }

    private void initialization() {
        logo = findViewById(R.id.booking_logo);
        bookingStartTime = findViewById(R.id.booking_start_time);
        bookingEndTime = findViewById(R.id.booking_end_time);
        bookingTotalTime = findViewById(R.id.booking_total_time);
        ticketPrice = findViewById(R.id.ticket_price);
        progressBar = findViewById(R.id.booking_progress);
        fireStore = FirebaseFirestore.getInstance();
        bookingDetail = findViewById(R.id.booking_detail);
        bookingHeader = findViewById(R.id.settings_header);
        resources = LanguageResourceClass.getResource(this);
        details = findViewById(R.id.details);
        seat = findViewById(R.id.seat);
        selectSeatButton = findViewById(R.id.select_seat_button);
        seatPrice = findViewById(R.id.seat_price);
        autoSeat = findViewById(R.id.auto_seat);
        passengerInfo = findViewById(R.id.boarding_passenger_info_text);
        addPassengerInfo = findViewById(R.id.add_passenger_info);
        totalPrice = findViewById(R.id.total_price);
        pay = findViewById(R.id.pay);
        selectedSeats = new String[passengerCount];
        seatPriceInt = 0;
    }

    private void getExtras() {
        Intent intent = getIntent();
        ticketId = intent.getStringExtra("ticketId");
        imageUrl = intent.getStringExtra("imageUrl");
        price = intent.getIntExtra("price", -1);
        hourTakeOff = intent.getIntExtra("hourTakeOff", -1);
        minutesTakeOff = intent.getIntExtra("minutesTakeOff", -1);
        hourLanding = intent.getIntExtra("hourLanding", -1);
        minutesLanding = intent.getIntExtra("minutesLanding", -1);
        hourTotal = intent.getIntExtra("hourTotal", -1);
        minutesTotal = intent.getIntExtra("minutesTotal", -1);
        passengerCount = intent.getIntExtra("passengerCount", -1);
    }


    @SuppressLint("SetTextI18n")
    private void fillViews() {
        bookingStartTime.setText(hourTakeOff + ":" + minutesTakeOff);
        bookingEndTime.setText(hourLanding + ":" + minutesLanding);
        if (minutesTotal != 0) {
            bookingTotalTime.setText(hourTotal + resources.getString(R.string.h) + " " + minutesTotal + resources.getString(R.string.min));
        } else {
            bookingTotalTime.setText(hourTotal + resources.getString(R.string.h));
        }
        ticketPrice.setText(resources.getString(R.string.price) + ": " + price + "$");
        progressBar.setVisibility(View.VISIBLE);
        Picasso.get().load(imageUrl).into(logo, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }


    public void pay(View view) {
        selectedSeats = HelperClass.getSelectedSeats(passengerCount);
        passengersMap = HelperClass.getMap();
        if (passengersMap == null) {
            Toast.makeText(this, resources.getString(R.string.passenger_info_form), Toast.LENGTH_SHORT).show();
        } else {

            Intent intent=new Intent(this,PayActivity.class);
            intent.putExtra("flightId",ticketId);
            intent.putExtra("passengerCount",passengerCount);
            intent.putExtra("totalPrice",totalPriceInt);
            startActivity(intent);
        }

    }

    public void selectSeat(View view) {
        selectedSeats = HelperClass.getSelectedSeats(passengerCount);
        HelperClass.setIsTrue(false);
        if (selectedSeats[0] != null) {
            ArrayList<String> arrayList = new ArrayList(Arrays.asList(busySeats.split(",")));
            for (int i = 0; i < selectedSeats.length; i++) {
                arrayList.remove(selectedSeats[i]);
            }
            busySeats = "";
            for (int i = 0; i < arrayList.size(); i++) {
                if (i != arrayList.size() - 1) {
                    busySeats += arrayList.get(i) + ",";
                } else busySeats += arrayList.get(i);
            }
            fireStore.collection("Flights").document(ticketId).update("busySeats", busySeats).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Intent intent = new Intent(BookingActivity.this, SeatActivity.class);
                    intent.putExtra("passengerCount", passengerCount);
                    intent.putExtra("ticketId", ticketId);
                    intent.putExtra("selectedSeatsArr", selectedSeats);
                    startActivity(intent);
                }
            });
        } else {
            Intent intent = new Intent(this, SeatActivity.class);
            intent.putExtra("passengerCount", passengerCount);
            intent.putExtra("ticketId", ticketId);
            intent.putExtra("selectedSeatsArr", selectedSeats);
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        selectedSeats = HelperClass.getSelectedSeats(passengerCount);

        if (selectedSeats[0] != null) {
            ArrayList<String> arrayList = new ArrayList(Arrays.asList(busySeats.split(",")));
            for (int i = 0; i < selectedSeats.length; i++) {
                arrayList.remove(selectedSeats[i]);
            }
            busySeats = "";
            for (int i = 0; i < arrayList.size(); i++) {
                if (i != arrayList.size() - 1) {
                    busySeats += arrayList.get(i) + ",";
                } else busySeats += arrayList.get(i);
            }
            fireStore.collection("Flights").document(ticketId).update("busySeats", busySeats);
            HelperClass.setSelectedSeats(new String[passengerCount]);

        }
        super.onBackPressed();

    }


    public void goBack(View view) {
        onBackPressed();
    }

    public void addInfo(View view) {
        Intent intent = new Intent(this, PassengerInfoActivity.class);
        intent.putExtra("passengerCount", passengerCount);
        startActivity(intent);
    }
}