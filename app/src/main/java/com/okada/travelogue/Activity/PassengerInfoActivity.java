package com.okada.travelogue.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.okada.travelogue.Adapters.RecyclerAdapterPassengerInfo;
import com.okada.travelogue.HelperClasses.HelperClass;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.R;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PassengerInfoActivity extends AppCompatActivity {
    private int passengerCount,currentPosition;
    private RecyclerView passengerInfoRecycler;
    private RecyclerAdapterPassengerInfo adapterPassengerInfo;
    private TextView passengerInfo;
    private Button infoNextSaveButton;
    public HashMap<Integer, HashMap<String, String>> passengersInfoMap;
    private HashMap<String, String> passengerInfoMap;
    private Resources resources;
    private String name, surname, gender, documentCountry, documentNo, dateOfBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_info);
        getExtras();
        initialization();
        configureRecycler();
        accessProcedures();
        checkLanguage();

    }

    private void checkLanguage() {
        passengerInfo.setText(resources.getString(R.string.passenger_info));

    }

    private void initialization() {
        passengerInfoRecycler = findViewById(R.id.passenger_info_recycler);
        adapterPassengerInfo = new RecyclerAdapterPassengerInfo(passengerCount);
        infoNextSaveButton = findViewById(R.id.info_next_save_button);
        passengersInfoMap = new HashMap<>();
        passengerInfoMap = new HashMap<>();
        currentPosition = 0;
        passengerInfo = findViewById(R.id.settings_header);
        resources = LanguageResourceClass.getResource(this);

    }

    private void accessProcedures() {

        if (currentPosition==passengerCount-1){
            infoNextSaveButton.setText(resources.getString(R.string.save));
        }else {
            infoNextSaveButton.setText(resources.getString(R.string.next).toUpperCase());
        }

    }

    private void configureRecycler() {
        passengerInfoRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        passengerInfoRecycler.setAdapter(adapterPassengerInfo);
        passengerInfoRecycler.scrollToPosition(currentPosition);
        passengerInfoRecycler.suppressLayout(true);

    }

    private void getExtras() {
        Intent intent = getIntent();
        passengerCount = intent.getIntExtra("passengerCount", -1);
    }


    public void goBack(View view) {
        onBackPressed();
        finish();
    }



    public boolean getDataFromView() {
        RecyclerAdapterPassengerInfo.InfoHolder vh = (RecyclerAdapterPassengerInfo.InfoHolder) passengerInfoRecycler.findViewHolderForAdapterPosition(currentPosition);
        boolean hasError = false;
        if (vh != null) {
            if (vh.infoNameEditText.getText().toString().trim().equals("")) {
                hasError = true;
            }
            if (vh.infoSurnameEditText.getText().toString().trim().equals("")) {
                hasError = true;
            }
            if (vh.infoDocumentNoText.getText().toString().trim().equals("")) {
                hasError = true;
            }
            if (vh.infoDateBirthDateText.getText().toString().trim().equals("")) {
                hasError = true;
            }
            if (vh.searchableSpinnerGender.getSelectedIndex() == -1) {
                hasError = true;
            }
            if (vh.searchableSpinnerCountryDocument.getSelectedIndex() == -1) {
                hasError = true;
            }
            if (hasError) {
                Toast.makeText(this, resources.getString(R.string.must_filled), Toast.LENGTH_SHORT).show();
            } else {
                name = vh.infoNameEditText.getText().toString();
                surname = vh.infoSurnameEditText.getText().toString();
                List<String> genders = Arrays.asList(resources.getStringArray(R.array.genders));
                gender = genders.get(vh.searchableSpinnerGender.getSelectedIndex());
                dateOfBirth = vh.infoDateBirthDateText.getText().toString();
                documentNo = vh.infoDocumentNoText.getText().toString();
                List<String> lines = Arrays.asList(resources.getStringArray(R.array.countries_array));
                documentCountry = lines.get(vh.searchableSpinnerCountryDocument.getSelectedIndex());
                passengerInfoMap=new HashMap<>();
                passengerInfoMap.put("name",name);
                passengerInfoMap.put("surname",surname);
                passengerInfoMap.put("gender",gender);
                passengerInfoMap.put("dateOfBirth",dateOfBirth);
                passengerInfoMap.put("documentNo",documentNo);
                passengerInfoMap.put("documentCountry",documentCountry);

            }
        }
        return hasError;
    }





    public void next(View view) {
        if (!getDataFromView()){
            if (currentPosition == (passengerCount - 1)) {
                passengersInfoMap.put(currentPosition,passengerInfoMap);
                HelperClass.setMap(passengersInfoMap);
                onBackPressed();
            } else {
                passengersInfoMap.put(currentPosition,passengerInfoMap);
                currentPosition++;
                passengerInfoRecycler.suppressLayout(false);
                passengerInfoRecycler.scrollToPosition(currentPosition);
                passengerInfoRecycler.suppressLayout(true);
                accessProcedures();
            }
        }

    }
}