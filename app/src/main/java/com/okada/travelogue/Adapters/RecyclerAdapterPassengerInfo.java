package com.okada.travelogue.Adapters;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.R;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class RecyclerAdapterPassengerInfo extends RecyclerView.Adapter<RecyclerAdapterPassengerInfo.InfoHolder> {
    int passengerCount;
    private Resources resources;

    public RecyclerAdapterPassengerInfo(int passengerCount) {
        this.passengerCount = passengerCount;
    }

    @NonNull
    @Override
    public InfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.recycler_passenger_info_item,parent,false);
        return new InfoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoHolder holder, int position) {

        holder.infoPassengerNoText.setText(resources.getString(R.string.passenger)+" "+(position+1));
        holder.nameLayout.setHint(resources.getString(R.string.name));
        holder.surnameLayout.setHint(resources.getString(R.string.surname));
        holder.searchableSpinnerGender.setHint(resources.getString(R.string.gender));
        holder.infoDateBirthDateText.setHint(resources.getString(R.string.date_of_birthday));
        holder.documentLayout.setHint(resources.getString(R.string.document_no));
        holder.searchableSpinnerCountryDocument.setHint(resources.getString(R.string.country_document));
    }

    @Override
    public int getItemCount() {
        return passengerCount;
    }

    public class  InfoHolder extends RecyclerView.ViewHolder{
        public TextView infoPassengerNoText, infoDateBirthDateText;
        public TextInputLayout nameLayout, surnameLayout, documentLayout;
        public TextInputEditText infoNameEditText, infoSurnameEditText, infoDocumentNoText;
        public PowerSpinnerView searchableSpinnerGender,searchableSpinnerCountryDocument;
        public LinearLayout infoDateBirthDateLayout;
        private final Calendar myCalendar = Calendar.getInstance();

        public InfoHolder(@NonNull View itemView) {
            super(itemView);
            infoPassengerNoText=itemView.findViewById(R.id.info_passenger_no_text);
            infoNameEditText =itemView.findViewById(R.id.info_name_edit_text);
            infoSurnameEditText =itemView.findViewById(R.id.info_surname_edit_text);
            infoDocumentNoText =itemView.findViewById(R.id.info_document_no_text);
            infoDateBirthDateText =itemView.findViewById(R.id.info_date_birth_date_text);
            searchableSpinnerGender=itemView.findViewById(R.id.SearchableSpinnerGender);
            searchableSpinnerCountryDocument=itemView.findViewById(R.id.SearchableSpinnerCountryDocument);
            infoDateBirthDateLayout =itemView.findViewById(R.id.info_date_birth_date_layout);
            nameLayout = itemView.findViewById(R.id.info_name_layout);
            surnameLayout = itemView.findViewById(R.id.info_surname_layout);
            documentLayout = itemView.findViewById(R.id.info_document_no_layout);
            resources=LanguageResourceClass.getResource(itemView.getContext());
            addItemsToSpinner(itemView);
            addListener(itemView);
        }

        private void addListener(View view) {
            searchableSpinnerGender.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
                @Override
                public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                    searchableSpinnerGender.dismiss();
                }
            });
            searchableSpinnerCountryDocument.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
                @Override
                public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                    searchableSpinnerCountryDocument.dismiss();
                }
            });
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
            infoDateBirthDateLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog pickerDialog = new DatePickerDialog(view.getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-Long.parseLong("5049112320000"));
                    pickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    pickerDialog.show();
                }
            });
        }
        private void updateLabel() {
            String myFormat = "MM/dd/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            infoDateBirthDateText.setText(sdf.format(myCalendar.getTime()));
        }

        private void addItemsToSpinner(View view) {
            searchableSpinnerGender.setItems(new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.genders))));
            searchableSpinnerCountryDocument.setItems(new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.countries_array))));
        }
    }
}
