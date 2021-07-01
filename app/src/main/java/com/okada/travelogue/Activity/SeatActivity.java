package com.okada.travelogue.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class SeatActivity extends AppCompatActivity {
    private RecyclerAdapterSeat adapterSeat;
    private RecyclerView recyclerView;
    private int passengerCount;
    private FirebaseFirestore firestore;
    private String currentTicketId;
    private String busySeats;
    private ArrayList<String > busySeatsArraylist;
    private TextView seatSelectPassengerText, busySeat, largeSeat, emptySeat, seatHeader, addTotalPrice;
    private Resources resources;
    private Button selectButton;
    private String selectedSeats[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);
        initialization();
        getDataFromFirebase();
        checkLanguage();
    }

    private void checkLanguage() {
        seatSelectPassengerText.setText(resources.getString(R.string.select_seat_for) + " " +passengerCount);
        busySeat.setText(resources.getString(R.string.busy_seat));
        largeSeat.setText(resources.getString(R.string.large_seat));
        emptySeat.setText(resources.getString(R.string.empty_seat));
        selectButton.setText(resources.getString(R.string.select));
        seatHeader.setText(resources.getString(R.string.seat_header));
        addTotalPrice.setText(resources.getString(R.string.add_total));

    }

    private void initialization() {
        passengerCount = getIntent().getIntExtra("passengerCount", -1);
        seatSelectPassengerText =findViewById(R.id.seat_select_passenger_text);
        currentTicketId = getIntent().getStringExtra("ticketId");
        busySeatsArraylist=new ArrayList<>();
        recyclerView = findViewById(R.id.seats_recycler_view);
        firestore = FirebaseFirestore.getInstance();
        busySeat = findViewById(R.id.seat_busy_seat);
        largeSeat = findViewById(R.id.large_area_seat);
        emptySeat = findViewById(R.id.empty_seat);
        selectButton = findViewById(R.id.select_button);
        seatHeader = findViewById(R.id.seat_header);
        addTotalPrice = findViewById(R.id.add_total_price);
        resources = LanguageResourceClass.getResource(this);
        selectedSeats=getIntent().getStringArrayExtra("selectedSeatsArr");
    }

    private void getDataFromFirebase() {


        CollectionReference collectionReference = firestore.collection("Flights");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(SeatActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } else if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> map = snapshot.getData();
                        String flightId = (String) map.get("flightId");
                        if (flightId.equals(currentTicketId)) {
                            busySeats = (String) map.get("busySeats");
                        }
                    }
                    String[] busySeatsArr=busySeats.split(",");
                    busySeatsArraylist=new ArrayList<>(Arrays.asList(busySeatsArr));
                    selectedSeats= HelperClass.getSelectedSeats(passengerCount);
                    adapterSeat = new RecyclerAdapterSeat(passengerCount,busySeatsArraylist,selectButton,selectedSeats,currentTicketId,SeatActivity.this);
                    recyclerConfiguration();
                }
            }
        });


    }


    private void recyclerConfiguration() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterSeat);
    }


    public void goBack(View view) {
        onBackPressed();
    }
}