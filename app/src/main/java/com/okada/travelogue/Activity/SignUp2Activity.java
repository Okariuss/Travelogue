package com.okada.travelogue.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.HelperClasses.User;
import com.okada.travelogue.R;

public class SignUp2Activity extends AppCompatActivity {

    private TextInputEditText signUpPasswordText, signUpConfirmPasswordText;
    private TextInputLayout signUpPasswordLayout, signUpConfirmPasswordLayout;
    private String name, surname, email;
    private ImageView imageView2;
    private TextView welcomeTextView, signUpToContinue, alreadyHaveAccTextView,
            alreadyHaveAccSignInTextView, dateOfBirthTextView;
    private Button createAccButton;
    private LinearLayout linearLayout;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private DatabaseReference reference;
    private FirebaseUser user ;
    private DatePicker datePicker;
    private Boolean isEnglish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_2);
        initialization();
        checkLanguage();
        getIntentExtras();
        firebaseAuth = FirebaseAuth.getInstance();
        setTouchListeners();
        datePicker.setMinDate(System.currentTimeMillis()-Long.parseLong("5049112320000"));
        datePicker.setMaxDate(System.currentTimeMillis());
         user = firebaseAuth.getCurrentUser();
        firebaseAuth.signOut();
        if (user != null) {
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                }
            });
        }
    }



    public void initialization(){
        signUpPasswordText = findViewById(R.id.sign_up_password_edit_text);
        signUpConfirmPasswordText = findViewById(R.id.sign_up_confirm_password_edit_text);
        signUpPasswordLayout = findViewById(R.id.sign_up_password_layout);
        signUpConfirmPasswordLayout = findViewById(R.id.sign_up_confirm_password_layout);
        imageView2 = findViewById(R.id.imageView2);
        welcomeTextView = findViewById(R.id.signInStartTextView);
        signUpToContinue = findViewById(R.id.signInStartTextView2);
        progressBar=findViewById(R.id.sign_up2_progress_bar);
        createAccButton = findViewById(R.id.create_account_button);
        linearLayout = findViewById(R.id.linearLayout);
        datePicker=findViewById(R.id.sign_up_date_picker);
        alreadyHaveAccTextView=findViewById(R.id.already_have_acc_text_view);
        alreadyHaveAccSignInTextView=findViewById(R.id.already_have_acc_sign_in_text_view);
        dateOfBirthTextView=findViewById(R.id.date_of_birth_text_view);
        isEnglish=LanguageResourceClass.isEnglish(this);
    }

    private void checkLanguage() {
        Resources resources= LanguageResourceClass.getResource(this);
        welcomeTextView.setText(resources.getString(R.string.welcome));
        signUpToContinue.setText(resources.getString(R.string.sign_up_to_countinue));
        dateOfBirthTextView.setText(resources.getString(R.string.date_of_birthday));
        signUpPasswordLayout.setHint(resources.getString(R.string.password));
        signUpConfirmPasswordLayout.setHint(resources.getString(R.string.confirm_password));
        createAccButton.setText(resources.getString(R.string.create_account));
        alreadyHaveAccTextView.setText(resources.getString(R.string.already_have_account));
        alreadyHaveAccSignInTextView.setText(resources.getString(R.string.sign_in_underline));
        alreadyHaveAccSignInTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
    }
    @SuppressLint("ClickableViewAccessibility")
    public void setTouchListeners(){
        signUpPasswordText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                signUpPasswordLayout.setErrorEnabled(false);
                signUpConfirmPasswordLayout.setErrorEnabled(false);
                return false;
            }
        });
        signUpPasswordLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                signUpPasswordLayout.setErrorEnabled(false);
                signUpConfirmPasswordLayout.setErrorEnabled(false);
                return false;
            }
        });
        signUpConfirmPasswordText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                signUpConfirmPasswordLayout.setErrorEnabled(false);
                signUpPasswordLayout.setErrorEnabled(false);
                return false;
            }
        });
    }
    public void getIntentExtras(){
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        surname = intent.getStringExtra("surname");
        email = intent.getStringExtra("email");
    }

    public void createAccount(View view) {
        if (!isConnected(SignUp2Activity.this)){
            showDialog(SignUp2Activity.this);
        }else {
            progressBar.setVisibility(View.VISIBLE);
            String password = signUpPasswordText.getText().toString();
            String confirmPassword = signUpConfirmPasswordText.getText().toString();

            if (TextUtils.isEmpty(password)) {
                progressBar.setVisibility(View.INVISIBLE);
                if (isEnglish)
                signUpPasswordLayout.setError("Password can't be empty");
                else
                    signUpPasswordLayout.setError("Şifre boş olamaz");

            }
            if (TextUtils.isEmpty(confirmPassword)) {
                progressBar.setVisibility(View.INVISIBLE);
                if (isEnglish)
                    signUpConfirmPasswordLayout.setError("Confirm password can't be empty");
                else
                    signUpConfirmPasswordLayout.setError("Şifre tekrarı boş olamaz");
            }
            if (!TextUtils.isEmpty(password) & !TextUtils.isEmpty(confirmPassword)) {
                if (!confirmPassword.equals(password)) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (isEnglish){
                        signUpPasswordLayout.setError("Passwords are not same");
                        signUpConfirmPasswordLayout.setError("Passwords are not same");
                    }
                    else{
                        signUpPasswordLayout.setError("Şifreler aynı değil");
                        signUpConfirmPasswordLayout.setError("Şifreler aynı değil");
                    }

                } else if (password.contains(" ")) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (isEnglish){
                        signUpPasswordLayout.setError("Password can't contain white space");
                        signUpConfirmPasswordLayout.setError("Password can't contain white space");
                    }
                    else{
                        signUpPasswordLayout.setError("Şifre boşluk içeremez");
                        signUpConfirmPasswordLayout.setError("Şifre boşluk içeremez");
                    }

                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);

                            if (e instanceof FirebaseAuthException) {
                                if (((FirebaseAuthException) e).getErrorCode().equals("ERROR_WEAK_PASSWORD")) {
                                    if (isEnglish){
                                        signUpConfirmPasswordLayout.setError("Weak password");
                                        signUpPasswordLayout.setError("Weak password");
                                    }else {
                                        signUpConfirmPasswordLayout.setError("Zayıf şifre");
                                        signUpPasswordLayout.setError("Zayıf şifre");
                                    }

                                } else {
                                    Toast.makeText(SignUp2Activity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            reference=FirebaseDatabase.getInstance().getReference();
                            User user=new User(name,surname,email,password,datePicker.getYear(),datePicker.getMonth()+1,datePicker.getDayOfMonth(),"null");
                            reference.child("users").child(authResult.getUser().getUid()).setValue(user);

                            Intent toHomeActivity = new Intent(SignUp2Activity.this, HomeActivity.class);
                            if (isEnglish){
                                Toast.makeText(SignUp2Activity.this,"User successfully created.",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(SignUp2Activity.this,"Kullanıcı başarıyla oluşturuldu.",Toast.LENGTH_SHORT).show();
                            }
                            startActivity(toHomeActivity);
                            Animatoo.animateSwipeRight(SignUp2Activity.this);
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        }
    }
    public boolean isConnected(Context context){
        ConnectivityManager manager =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi=manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile=manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi!=null&&wifi.isConnected())||(mobile!=null&&mobile.isConnected())){
            return true;
        }else {
            return false;
        }
    }
    public void showDialog(Context context){
        String meesage,connect,cancel;
        if (LanguageResourceClass.isEnglish(SignUp2Activity.this)){
            meesage="We can't find internet connection. Please connect to internet";
            connect="Connect";
            cancel="Cancel";
        }else {
            meesage="İnternet bağlantısı bulamıyoruz. Lütfen internete bağlanın.";
            connect="Bağlan";
            cancel="Iptal";
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage(meesage)
                .setPositiveButton(connect, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }).setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(context,HomeActivity.class));
                        Animatoo.animateSlideRight(context);
                        finish();
                    }
                });
        builder.show();
    }
    public void toSignIn(View view) {
        Intent intent = new Intent(SignUp2Activity.this, SignInActivity.class);
        Pair[] pairs = new Pair[5];
        pairs[0] = new Pair<View, String>(imageView2, "sign_in_start_logo_image");
        pairs[1] = new Pair<View, String>(welcomeTextView, "sign_in_start_text_view");
        pairs[2] = new Pair<View, String>(signUpToContinue, "sign_up_to_continue");
        pairs[3] = new Pair<View, String>(createAccButton, "sign_up_btn");
        pairs[4] = new Pair<View, String>(linearLayout, "sign_up_already_have_layout");
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SignUp2Activity.this, pairs);
        startActivity(intent, activityOptions.toBundle());
        finish();
    }

}