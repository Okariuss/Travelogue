package com.okada.travelogue.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.okada.travelogue.HelperClasses.HelperClass;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.HelperClasses.ReservationClass;
import com.okada.travelogue.HelperClasses.TicketClass;
import com.okada.travelogue.HelperClasses.User;
import com.okada.travelogue.R;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class PayActivity extends AppCompatActivity {
    private TextInputLayout cardNum, cardCvv, cardHolder;
    private PowerSpinnerView spinnerMonth, spinnerYear;
    private Resources resources;
    private int passengerCount, totalPrice;
    private String flightId, idFlight, busySeats,startTime,endTime,hotelName,roomName,roomCount;
    private FirebaseFirestore fireStore;
    private TextInputEditText payCardNumText, payContactText;
    private TextView paymentHeader, totalPriceText, payCardNum, payValidUntil, payCardHolder;
    private ArrayList<String> busySeatsList;
    private FirebaseAuth firebaseAuth;
    private Button payButton;
    private boolean isHotelPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        getExtras();
        initialization();
        if (!isHotelPay)
        getBusySeats();
        fillSpinners();
        checkBlanks();
        addListener();
        checkLanguage();


    }

    private void checkLanguage() {
        paymentHeader.setText(resources.getString(R.string.payment_header));
        totalPriceText.setText(resources.getString(R.string.total_price) + ": " + totalPrice + "$");
        payCardNum.setText(resources.getString(R.string.pay_card_num));
        cardNum.setHint(resources.getString(R.string.pay_card_num_text));
        payValidUntil.setText(resources.getString(R.string.pay_valid_until));
        spinnerMonth.setHint(resources.getString(R.string.month));
        spinnerYear.setHint(resources.getString(R.string.year));
        cardCvv.setHint(resources.getString(R.string.three_digit));
        payCardHolder.setText(resources.getString(R.string.card_holder));
        cardHolder.setHint(resources.getString(R.string.card_holder_name));
        payButton.setText(resources.getString(R.string.pay));
    }


    private void initialization() {
        cardNum = findViewById(R.id.pay_card_num_layout);
        cardCvv = findViewById(R.id.pay_card_cvv_layout);
        cardHolder = findViewById(R.id.pay_card_holder_layout);
        spinnerMonth = findViewById(R.id.SearchableSpinnerMonth);
        spinnerYear = findViewById(R.id.SearchableSpinnerYear);
        resources = LanguageResourceClass.getResource(this);
        fireStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        payCardNumText = findViewById(R.id.pay_card_num_text);
        totalPriceText = findViewById(R.id.pay_total_price_text);
        payCardNum = findViewById(R.id.pay_card_num);
        payValidUntil = findViewById(R.id.pay_valid_until);
        payCardHolder = findViewById(R.id.pay_card_holder);
        paymentHeader = findViewById(R.id.payment_header);
        payButton = findViewById(R.id.pay_button);
        payContactText = findViewById(R.id.pay_contact_text);
    }

    private void getBusySeats() {
        CollectionReference collectionReference = fireStore.collection("Flights");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(PayActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } else if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> map = snapshot.getData();
                        idFlight = (String) map.get("flightId");
                        if (idFlight.equals(flightId)) {
                            busySeats = (String) map.get("busySeats");
                        }
                    }
                    busySeatsList = new ArrayList<>(Arrays.asList(busySeats.split(",")));


                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addListener() {

        spinnerMonth.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                spinnerMonth.dismiss();
            }
        });
        spinnerYear.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                spinnerYear.dismiss();

            }
        });
        payCardNumText.addTextChangedListener(new TextWatcher() {
            char lastChar = '9';

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                if (charSequence.length() != 0)
                    lastChar = charSequence.charAt(charSequence.length() - 1);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.length() == 4 || s.length() == 9 || s.length() == 14) && lastChar != '-') {
                    payCardNumText.setText(payCardNumText.getText() + "-");
                }
                payCardNumText.setSelection(payCardNumText.getText().toString().length());

            }
        });

    }


    private void fillSpinners() {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(System.currentTimeMillis());
        int year = cal.get(Calendar.YEAR);
        ArrayList<String> years = new ArrayList<>();
        ArrayList<String> months = new ArrayList<>();
        for (int i = year; i < year + 7; i++) {
            years.add(i + "");
        }
        for (int i = 1; i < 13; i++) {
            months.add(i + "");
        }
        spinnerYear.setItems(years);
        spinnerMonth.setItems(months);

    }


    private void getExtras() {
        flightId = getIntent().getStringExtra("flightId");
        passengerCount = getIntent().getIntExtra("passengerCount", -1);
        totalPrice = getIntent().getIntExtra("totalPrice", -1);
        isHotelPay = getIntent().getBooleanExtra("isHotel", false);
        startTime= getIntent().getStringExtra("startTime");
        endTime= getIntent().getStringExtra("endTime");
        hotelName= getIntent().getStringExtra("hotelName");
        roomName=getIntent().getStringExtra("roomName");
        roomCount=getIntent().getStringExtra("roomCount");
    }

    private boolean checkBlanks() {
        boolean hasEmptyBlank = false;
        if (cardNum.getEditText().getText().toString().trim().equals("")) {
            hasEmptyBlank = true;
        }
        if (cardCvv.getEditText().getText().toString().trim().equals("")) {
            hasEmptyBlank = true;
        }
        if (cardHolder.getEditText().getText().toString().trim().equals("")) {
            hasEmptyBlank = true;
        }
        if (payContactText.getText().toString().equals("")) {
            hasEmptyBlank = true;
        }
        if (spinnerMonth.getSelectedIndex() == -1) {
            hasEmptyBlank = true;
        }
        if (spinnerYear.getSelectedIndex() == -1) {
            hasEmptyBlank = true;
        }
        return hasEmptyBlank;
    }


    public void pay(View view) {
        if (!checkBlanks()) {
            boolean hasProblem = false;
            String str = payCardNumText.getText().toString();
            for (int i = 0; i < 3; i++) {
                if (str.contains("-")) {
                    str = str.replaceFirst("-", "");
                } else {
                    hasProblem = true;
                    break;
                }
            }
            if (!hasProblem) {
                if (!isHotelPay)
                    payTicket();
                else payHotel();

                Toast.makeText(PayActivity.this, resources.getString(R.string.success_paid), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PayActivity.this, HomeActivity.class);
                startActivity(intent);


            } else {
                Toast.makeText(this, resources.getString(R.string.correct_card), Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(this, resources.getString(R.string.must_filled), Toast.LENGTH_SHORT).show();

        }
    }

    private void payHotel() {
        String customerContact = payContactText.getText().toString();
        ReservationClass reservation = new ReservationClass(customerContact,startTime
                ,endTime,hotelName,totalPrice);
        fireStore.collection("Reservations")
                .add(reservation)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.getException() != null) {
                            task.getException().printStackTrace();
                        }
                    }
                });
        FirebaseDatabase.getInstance().getReference().child("hotels")
                .child(hotelName)
                .child("hotelRooms")
                .child("roomNum"+roomCount)
                .child(roomName)
                .child("fullDate")
                .child(startTime)
                .setValue(endTime);
    }

    private void payTicket() {
        String[] selectedSeats = HelperClass.getSelectedSeats(passengerCount);
        HashMap<Integer, HashMap<String, String>> passengersMap = HelperClass.getMap();
        for (int i = 0; i < passengerCount; i++) {
            String currentSeat = "";

            if (selectedSeats[0] == null) {
                boolean isSelected = true;
                while (isSelected) {
                    Random rnd = new Random();
                    int count = rnd.nextInt(16);
                    int count2 = rnd.nextInt(6);
                    switch (count2) {
                        case 0:
                            currentSeat = "A";
                            break;
                        case 1:
                            currentSeat = "B";
                            break;
                        case 2:
                            currentSeat = "C";
                            break;
                        case 3:
                            currentSeat = "D";
                            break;
                        case 4:
                            currentSeat = "E";
                            break;
                        case 5:
                            currentSeat = "F";
                            break;
                    }
                    currentSeat += count + 1;
                    if (!busySeatsList.contains(currentSeat)) {
                        isSelected = false;
                        busySeats += "," + currentSeat;
                        fireStore.collection("Flights").document(flightId).update("busySeats", busySeats).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        });
                    }
                }
            } else
                currentSeat = selectedSeats[i];
            HashMap<String, String> currentPass = passengersMap.get(i);
            String documentNo = currentPass.get("documentNo");
            String gender = currentPass.get("gender");
            String surname = currentPass.get("surname");
            String name = currentPass.get("name");
            String dateOfBirth = currentPass.get("dateOfBirth");
            String documentCountry = currentPass.get("documentCountry");
            String ticketPurchaseTime = System.currentTimeMillis() + "";
            TicketClass ticket = new TicketClass(flightId, dateOfBirth, documentCountry, documentNo
                    , gender, name, surname, currentSeat, ticketPurchaseTime);
            fireStore.collection("Tickets")
                    .add(ticket)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.getException() != null) {
                                task.getException().printStackTrace();
                            }
                        }
                    });
            if (firebaseAuth.getCurrentUser() != null) {
                FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getCurrentUser().getUid() + "").child("tickets").child(ticketPurchaseTime).setValue(flightId);
            }
        }
        HelperClass.setSelectedSeats(new String[passengerCount]);
    }


    public void goBack(View view) {
        onBackPressed();
    }
}