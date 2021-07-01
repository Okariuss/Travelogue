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
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.HelperClasses.User;
import com.okada.travelogue.R;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ImageView imageView;
    private TextView welcomeTextView, signInToCont,signInForgotPassword, orTextView, dontYouHaveAccTextView, dontYouHaveAccSignUpTextView;
    private Button button, googleSignInButton;
    private LinearLayout linearLayout;
    private TextInputLayout passwordTextInputLayout, emailTextInputLayout;
    private TextInputEditText passwordEditText,usernameEditText;
    private ProgressBar progressBar;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN=123;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        firebaseAuth=FirebaseAuth.getInstance();
        createRequest();
        initialization();
        checkLanguage();
        setTouchListeners();

    }

    private void checkLanguage() {
        Resources resources= LanguageResourceClass.getResource(this);
        welcomeTextView.setText(resources.getString(R.string.welcome_back));
        signInToCont.setText(resources.getString(R.string.sign_in_to_continue));
        emailTextInputLayout.setHint(resources.getString(R.string.e_mail));
        passwordTextInputLayout.setHint(resources.getString(R.string.password));
        signInForgotPassword.setText(resources.getString(R.string.forgot_password_underline));
        signInForgotPassword.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        button.setText(resources.getString(R.string.sign_in));
        orTextView.setText(resources.getString(R.string.or));
        googleSignInButton.setText(resources.getString(R.string.google_sign_in));
        dontYouHaveAccTextView.setText(resources.getString(R.string.dont_you_have_account));
        dontYouHaveAccSignUpTextView.setText(resources.getString(R.string.sign_up_underline));
        dontYouHaveAccSignUpTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);


    }

    public void initialization(){
        imageView=findViewById(R.id.imageView2);
        orTextView=findViewById(R.id.or_text_view);
        dontYouHaveAccTextView =findViewById(R.id.dontYouHaveAccTextView);
        dontYouHaveAccSignUpTextView =findViewById(R.id.already_have_acc_sign_in_text_view);
        welcomeTextView =findViewById(R.id.signInStartTextView);
        signInToCont =findViewById(R.id.sign_in_to_continue);
        googleSignInButton=findViewById(R.id.google_sign_in_button);
        signInForgotPassword=findViewById(R.id.signInForgotPassword);
        button=findViewById(R.id.nextButton);
        linearLayout=findViewById(R.id.sign_in_lineer_layout);
        passwordEditText=findViewById(R.id.sign_in_password);
        usernameEditText=findViewById(R.id.sign_in_email_edit_text);
        passwordTextInputLayout =findViewById(R.id.sign_in_password_layout);
        progressBar=findViewById(R.id.sign_in_progress_bar);
        emailTextInputLayout =findViewById(R.id.sign_in_email_layout);
    }


    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    public void signInWithGoogle(View view) {
        if (!isConnected(SignInActivity.this)){
            showDialog(SignInActivity.this);
        }else {
            progressBar.setVisibility(View.VISIBLE);
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(SignInActivity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);

            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(SignInActivity.this);
                            if (acct != null) {
                                String personGivenName = acct.getGivenName();
                                String personFamilyName = acct.getFamilyName();
                                String personEmail = acct.getEmail();
                                Uri personPhoto = acct.getPhotoUrl();
                                reference= FirebaseDatabase.getInstance().getReference();
                                User user=new User(personGivenName,personFamilyName,personEmail,"null",0,0,0,personPhoto.toString());
                                reference.child("users").child(task.getResult().getUser().getUid()).setValue(user);
                            }
                            startActivity(new Intent(SignInActivity.this,HomeActivity.class));
                            Animatoo.animateSwipeRight(SignInActivity.this);
                            progressBar.setVisibility(View.INVISIBLE);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    }
                });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setTouchListeners(){
        usernameEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                emailTextInputLayout.setErrorEnabled(false);

                return false;
            }
        });
        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                passwordTextInputLayout.setErrorEnabled(false);
                return false;
            }
        });
    }

    public void signIn(View view){
        if (!isConnected(SignInActivity.this)){
            showDialog(SignInActivity.this);
        }else {
            progressBar.setVisibility(View.VISIBLE);
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (password.matches("")) {
                progressBar.setVisibility(View.INVISIBLE);
                if (LanguageResourceClass.isEnglish(SignInActivity.this))
                passwordTextInputLayout.setError("Password can't be empty");
                else
                    passwordTextInputLayout.setError("Şifre boş olamaz");

            }
            if (!TextUtils.isEmpty(username)) {
                if (!password.matches("")) {
                    firebaseAuth.signInWithEmailAndPassword(username, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Intent toHomeActivity = new Intent(SignInActivity.this, HomeActivity.class);
                                    if (LanguageResourceClass.isEnglish(SignInActivity.this))
                                        Toast.makeText(SignInActivity.this,"Logged in",Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(SignInActivity.this,"Giriş yapıldı",Toast.LENGTH_SHORT).show();
                                    startActivity(toHomeActivity);
                                    Animatoo.animateSwipeRight(SignInActivity.this);
                                    finish();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    if (e instanceof FirebaseAuthException) {
                                        if (((FirebaseAuthException) e).getErrorCode().equals("ERROR_WRONG_PASSWORD")) {
                                            if (LanguageResourceClass.isEnglish(SignInActivity.this))
                                            passwordTextInputLayout.setError("Wrong password");
                                            else
                                                passwordTextInputLayout.setError("Yanlış şifre");
                                        } else if (((FirebaseAuthException) e).getErrorCode().equals("ERROR_USER_NOT_FOUND")) {
                                            if (LanguageResourceClass.isEnglish(SignInActivity.this))
                                                emailTextInputLayout.setError("Wrong e-mail");
                                            else
                                                emailTextInputLayout.setError("Yanlış e-posta");
                                        } else if (((FirebaseAuthException) e).getErrorCode().equals("ERROR_INVALID_EMAIL")) {
                                            if (LanguageResourceClass.isEnglish(SignInActivity.this))
                                                emailTextInputLayout.setError("Invalid e-mail");
                                            else
                                                emailTextInputLayout.setError("Geçersiz e-posta");
                                        } else {
                                            Toast.makeText(SignInActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                            });
                }

            } else {
                progressBar.setVisibility(View.INVISIBLE);
                if (LanguageResourceClass.isEnglish(SignInActivity.this))
                    emailTextInputLayout.setError("E-mail can't be empty");
                else
                    emailTextInputLayout.setError("E-posta boş olamaz");

            }
        }
    }

    public void toSignUp(View view) {
        Intent toSignUpAct = new Intent(SignInActivity.this, SignUp1Activity.class);
        Pair[]pairs=new Pair[7];
        pairs[0]=new Pair<View,String >(imageView,"sign_in_start_logo_image");
        pairs[1]=new Pair<View,String >(welcomeTextView,"sign_in_start_text_view");
        pairs[2]=new Pair<View,String >(signInToCont,"sign_up_to_continue");
        pairs[3]=new Pair<View,String >(emailTextInputLayout,"sign_up_email_layout");
        pairs[4]=new Pair<View,String >(passwordTextInputLayout,"sign_up_password_layout");
        pairs[5]=new Pair<View,String >(button,"sign_up_btn");
        pairs[6]=new Pair<View,String >(linearLayout,"sign_up_already_have_layout");
        ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(SignInActivity.this,pairs);
        startActivity(toSignUpAct,activityOptions.toBundle());
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
        if (LanguageResourceClass.isEnglish(SignInActivity.this)){
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
    public void toForgotPassword(View view) {
        Intent toForgotPasswordAct = new Intent(SignInActivity.this,ForgotPasswordActivity.class);
        Pair[]pairs1=new Pair[5];
        pairs1[0]=new Pair<View,String >(imageView,"sign_in_start_logo_image");
        pairs1[1]=new Pair<View,String >(welcomeTextView,"sign_in_start_text_view");
        pairs1[2]=new Pair<View,String >(signInToCont,"sign_up_to_continue");
        pairs1[3]=new Pair<View,String >(emailTextInputLayout,"sign_up_email_layout");
        pairs1[4]=new Pair<View,String >(button,"sign_up_btn");
        ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(SignInActivity.this,pairs1);
        startActivity(toForgotPasswordAct,activityOptions.toBundle());
    }

}