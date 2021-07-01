package com.okada.travelogue.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    private TextInputLayout forgotPasswordEmailLayout;
    private TextInputEditText forgotPasswordEmailText;
    private FirebaseAuth firebaseAuth;
    private TextView forgotPasswordStartTextView, dontWorry;
    private Button resetPasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initialization();
        checkLanguage();
        firebaseAuth = FirebaseAuth.getInstance();
        setTouchListeners();

    }

    private void checkLanguage() {
        Resources resources = LanguageResourceClass.getResource(this);
        forgotPasswordStartTextView.setText(resources.getString(R.string.forgot_password));
        dontWorry.setText(resources.getString(R.string.dont_worry));
        forgotPasswordEmailLayout.setHint(resources.getString(R.string.e_mail));
        resetPasswordBtn.setText(resources.getString(R.string.reset_password));
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setTouchListeners() {
        forgotPasswordEmailText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                forgotPasswordEmailLayout.setErrorEnabled(false);
                return false;
            }
        });
    }

    public void initialization() {
        forgotPasswordEmailText = findViewById(R.id.forgot_password_email_text);
        forgotPasswordEmailLayout = findViewById(R.id.forgot_password_email_layout);
        forgotPasswordStartTextView = findViewById(R.id.forgot_password_start_text_view);
        dontWorry = findViewById(R.id.dont_worry);
        resetPasswordBtn = findViewById(R.id.reset_password_btn);
    }

    public void resetPassword(View view) {

        String email = forgotPasswordEmailText.getText().toString();

        if (!TextUtils.isEmpty(email)) {
            firebaseAuth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult() != null && task.getResult().getSignInMethods() != null) {
                                    if (task.getResult().getSignInMethods().isEmpty()) {
                                        if (LanguageResourceClass.isEnglish(ForgotPasswordActivity.this))
                                            forgotPasswordEmailLayout.setError("Can't find e-mail");
                                        else
                                            forgotPasswordEmailLayout.setError("E-posta bulunamadı");
                                    } else {
                                        if (LanguageResourceClass.isEnglish(ForgotPasswordActivity.this))
                                            Toast.makeText(ForgotPasswordActivity.this, "We sent a link to " + email, Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(ForgotPasswordActivity.this, email + " adresine bir bağlantı gönderdik.", Toast.LENGTH_SHORT).show();

                                        firebaseAuth.sendPasswordResetEmail(email);
                                        Intent toHome = new Intent(ForgotPasswordActivity.this, HomeActivity.class);
                                        finish();
                                        startActivity(toHome);
                                    }
                                }

                            } else if (task.getException() != null) {
                                Exception e = task.getException();
                                if (e instanceof FirebaseAuthException) {
                                    if (((FirebaseAuthException) e).getErrorCode().equals("ERROR_INVALID_EMAIL")) {
                                        if (LanguageResourceClass.isEnglish(ForgotPasswordActivity.this))
                                            forgotPasswordEmailLayout.setError("Invalid email");
                                        else
                                            forgotPasswordEmailLayout.setError("Geçersiz e-posta");

                                    } else {
                                        Toast.makeText(ForgotPasswordActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        }
                    });

        } else{
            if (LanguageResourceClass.isEnglish(ForgotPasswordActivity.this))
                forgotPasswordEmailLayout.setError("E-mail can't be empty.");
            else
                forgotPasswordEmailLayout.setError("E-posta boş olamaz.");

        }
    }
}