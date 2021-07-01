package com.okada.travelogue.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.HelperClasses.User;
import com.okada.travelogue.R;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    private TextInputLayout nametextInputLayout,surnametextInputLayout;
    private TextInputEditText nametextInputText,surnametextInputText;
    private TextView settingsHeader, editProfileDateBirth;
    private DatePicker datePicker;
    private Button saveChangesButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initilaization();
        checkLanguage();
        setDatePicker();
        setOnTouchListeners();
        getProfileData();

    }
    private void checkLanguage() {
        Resources resources= LanguageResourceClass.getResource(this);
        settingsHeader.setText(resources.getString(R.string.edit_profile));
        nametextInputLayout.setHint(resources.getString(R.string.name));
        surnametextInputLayout.setHint(resources.getString(R.string.surname));
        editProfileDateBirth.setText(resources.getString(R.string.date_of_birthday));
        saveChangesButton.setText(resources.getString(R.string.save_changes));
    }


    private void getProfileData() {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.child("users").getChildren()) {
                    if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(ds.getKey())){
                        HashMap hashMap=(HashMap)ds.getValue();
                        nametextInputText.setText((String)hashMap.get("name"));
                        surnametextInputText.setText((String)hashMap.get("surname"));
                        if ((Long)hashMap.get("birthYear")!=0){
                            datePicker.init(Integer.parseInt((Long)hashMap.get("birthYear")+""),
                                    Integer.parseInt((Long)hashMap.get("birthMonth")+""),
                                    Integer.parseInt((Long)hashMap.get("birthDay")+""),
                                    null);
                        }else {
                            datePicker.init(2000,1,1,null);
                            Toast.makeText(EditProfileActivity.this,"Date of birth auto-generated because no birth date is entered",Toast.LENGTH_LONG).show();
                        }

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnTouchListeners() {
        nametextInputText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                nametextInputLayout.setErrorEnabled(false);
                return false;
            }
        });
        surnametextInputText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                surnametextInputLayout.setErrorEnabled(false);
                return false;
            }
        });
    }

    private void setDatePicker() {
        datePicker.setMinDate(System.currentTimeMillis()-Long.parseLong("5049112320000"));
        datePicker.setMaxDate(System.currentTimeMillis());
    }


    private void initilaization() {
        nametextInputLayout=findViewById(R.id.edit_profile_name_text_layout);
        surnametextInputLayout=findViewById(R.id.edit_profile_surname_text_layout);
        nametextInputText=findViewById(R.id.edit_profile_name_text);
        surnametextInputText=findViewById(R.id.edit_profile_surname_text);
        settingsHeader=findViewById(R.id.settings_header);
        editProfileDateBirth =findViewById(R.id.edit_profile_date_birth);
        datePicker=findViewById(R.id.edit_profile_date_picker);
        saveChangesButton=findViewById(R.id.save_changes_button);
    }

    public void save(View view){
        int a=1;
        if (nametextInputLayout.getEditText().getText().toString().trim().equals("")){
            nametextInputLayout.setErrorEnabled(true);
            if (LanguageResourceClass.isEnglish(EditProfileActivity.this))
                nametextInputLayout.setError("Can't be empty");
            else nametextInputLayout.setError("Boş olamaz");

            a=2;
        }
        if (surnametextInputLayout.getEditText().getText().toString().trim().equals("")){
            surnametextInputLayout.setErrorEnabled(true);
            if (LanguageResourceClass.isEnglish(EditProfileActivity.this))
                surnametextInputLayout.setError("Can't be empty");
            else surnametextInputLayout.setError("Boş olamaz");
            a=2;
        }
        if (a==1){
            FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds : snapshot.child("users").getChildren()) {
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(ds.getKey())){
                            HashMap hashMap=(HashMap)ds.getValue();
                            User user=new User(
                                    nametextInputText.getText().toString(),
                                    surnametextInputText.getText().toString(),
                                    (String)hashMap.get("email"),
                                    (String)hashMap.get("password"),
                                    datePicker.getYear(),
                                    datePicker.getMonth(),
                                    datePicker.getDayOfMonth(),
                                    (String)hashMap.get("photoUrl"));
                            FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"").setValue(user);
                            finish();
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(EditProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
    public void toBackActivity(View view){
        onBackPressed();
    }
}