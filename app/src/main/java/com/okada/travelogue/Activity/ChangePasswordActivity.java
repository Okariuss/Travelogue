package com.okada.travelogue.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.HelperClasses.User;
import com.okada.travelogue.R;

import java.util.HashMap;

public class ChangePasswordActivity extends AppCompatActivity {
    private TextInputLayout changePasswordOldLayout, changePasswordNewLayout, changePasswordNewConfirmLayout;
    private TextInputEditText changePasswordOldText, changePasswordNewText, changePasswordNewConfirmText;
    private TextView settingsHeader, changePasswordForgot;
    private Button changeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initialization();
        checkLanguage();
        setOnTouchListeners();

    }

    private void checkLanguage() {
        Resources resources= LanguageResourceClass.getResource(this);
        settingsHeader.setText(resources.getString(R.string.change_password));
        changeButton.setText(resources.getString(R.string.change_password));
        changePasswordOldLayout.setHint(resources.getString(R.string.old_password));
        changePasswordNewLayout.setHint(resources.getString(R.string.new_password));
        changePasswordNewConfirmLayout.setHint(resources.getString(R.string.confirm_new_password));
        changePasswordForgot.setText(resources.getString(R.string.forgot_password_underline));
        changePasswordForgot.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    public void changePassword(View view) {
        changePasswordOldLayout.setErrorEnabled(false);
        changePasswordNewLayout.setErrorEnabled(false);
        changePasswordNewConfirmLayout.setErrorEnabled(false);

        int a = 1;
        if (changePasswordOldText.getText().toString().trim().equals("")) {
            changePasswordOldLayout.setErrorEnabled(true);
            if (LanguageResourceClass.isEnglish(this))
            changePasswordOldLayout.setError("Can't be empty");
            else changePasswordOldLayout.setError("Boş olamaz");

            a = 2;
        }
        if (changePasswordNewText.getText().toString().trim().equals("")) {
            changePasswordNewLayout.setErrorEnabled(true);
            if (LanguageResourceClass.isEnglish(this))
                changePasswordNewLayout.setError("Can't be empty");
            else changePasswordNewLayout.setError("Boş olamaz");
            a = 2;
        }
        if (changePasswordNewConfirmText.getText().toString().trim().equals("")) {
            changePasswordNewConfirmLayout.setErrorEnabled(true);
            if (LanguageResourceClass.isEnglish(this))
                changePasswordNewConfirmLayout.setError("Can't be empty");
            else changePasswordNewConfirmLayout.setError("Boş olamaz");
            a = 2;
        }
        if (changePasswordNewConfirmText.getText().toString().trim().equals(changePasswordNewText.getText().toString().trim())&& a==1){
            FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds : snapshot.child("users").getChildren()) {
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(ds.getKey())){
                            HashMap hashMap=(HashMap)ds.getValue();

                            if (changePasswordOldText.getText().toString().trim().equals(((String)hashMap.get("password")).trim())){
                                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                                String newPassword = changePasswordNewText.getText().toString();
                                user1.updatePassword(newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    User user=new User((String) hashMap.get("name"),
                                                            (String)hashMap.get("surname"),
                                                            (String)hashMap.get("email"),
                                                            newPassword,
                                                            Integer.parseInt((Long)hashMap.get("birthYear")+""),
                                                            Integer.parseInt((Long)hashMap.get("birthMonth")+""),
                                                            Integer.parseInt((Long)hashMap.get("birthDay")+""),
                                                            (String)hashMap.get("photoUrl"));
                                                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"").setValue(user);
                                                    if (LanguageResourceClass.isEnglish(ChangePasswordActivity.this)) {
                                                        Toast.makeText(ChangePasswordActivity.this,"The password has been changed",Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
                                                        Toast.makeText(ChangePasswordActivity.this,"Şifre değiştirildi",Toast.LENGTH_SHORT).show();
                                                    }
                                                    finish();
                                                }else {
                                                    if (task.getException()!=null)
                                                    Toast.makeText(ChangePasswordActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }else {
                                changePasswordOldLayout.setErrorEnabled(true);
                                if (LanguageResourceClass.isEnglish(ChangePasswordActivity.this))
                                    changePasswordOldLayout.setError("Old password is wrong");
                                else changePasswordOldLayout.setError("Eski şifre yanlış");
                            }

                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ChangePasswordActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else if (a==1) {

            changePasswordNewConfirmLayout.setErrorEnabled(true);
            changePasswordNewLayout.setErrorEnabled(true);
            if (LanguageResourceClass.isEnglish(ChangePasswordActivity.this)) {
                changePasswordNewConfirmLayout.setError("Passwords are not same");
                changePasswordNewLayout.setError("Passwords are not same");
            }
            else {
                changePasswordNewConfirmLayout.setError("Şifreler aynı değil");
                changePasswordNewLayout.setError("Şifreler aynı değil");
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnTouchListeners() {
        changePasswordOldText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changePasswordOldLayout.setErrorEnabled(false);
                return false;
            }
        });
        changePasswordNewText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changePasswordNewLayout.setErrorEnabled(false);
                return false;
            }
        });
        changePasswordNewConfirmText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changePasswordNewConfirmLayout.setErrorEnabled(false);
                return false;
            }
        });
    }

    public void initialization() {
        changePasswordOldLayout = findViewById(R.id.change_password_old_layout);
        changePasswordNewLayout = findViewById(R.id.change_password_new_layout);
        changePasswordNewConfirmLayout = findViewById(R.id.change_password_new_confirm_layout);
        changePasswordOldText = findViewById(R.id.change_password_old_text);
        changePasswordNewText = findViewById(R.id.change_password_new_text);
        changePasswordNewConfirmText = findViewById(R.id.change_password_new_confirm_text);
        settingsHeader=findViewById(R.id.settings_header);
        changeButton=findViewById(R.id.create_account_button);
        changePasswordForgot=findViewById(R.id.change_password_forgot);
    }

    public void toBackActivity(View view) {
        onBackPressed();
    }

    public void toForgotPassword(View view) {
        Intent toForgotPasswordAct = new Intent(ChangePasswordActivity.this,ForgotPasswordActivity.class);
        startActivity(toForgotPasswordAct);
        Animatoo.animateSwipeLeft(this);

    }
}