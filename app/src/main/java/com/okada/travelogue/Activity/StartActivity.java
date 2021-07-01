package com.okada.travelogue.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.HelperClasses.LocaleHelper;
import com.okada.travelogue.R;

public class StartActivity extends AppCompatActivity {
    private Animation fromTop, fromBottom;
    private ImageView logoImageView;
    private TextView logoNameText, logoTextTextView;
    private ConstraintLayout constraintLayout;
    private SharedPreferences sharedPreferences;
    private LottieAnimationView lottieAnimationView, lottieAnimationView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findingViews();
        checkLanguage();
        setAnimations();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
                boolean firstTime = sharedPreferences.getBoolean("firstTime", true);
                if (firstTime) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("firstTime", false);
                    editor.apply();
                    Intent intent = new Intent(StartActivity.this, BoardingPageActivity.class);
                    startActivity(intent);
                    Animatoo.animateDiagonal(StartActivity.this);
                } else {
                    Intent intent = new Intent(StartActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Animatoo.animateFade(StartActivity.this);
                }
                finish();
            }
        }, 4500);
    }
    public void checkLanguage(){
        Resources resources=LanguageResourceClass.getResource(this);
        logoTextTextView.setText(resources.getString(R.string.choose_and_travel));
    }
    public void findingViews() {
        logoImageView = findViewById(R.id.logoImageView);
        logoNameText = findViewById(R.id.logo_name_text_view);
        logoTextTextView = findViewById(R.id.logo_text_text_view);
        constraintLayout = findViewById(R.id.start_activity_constraint);
        lottieAnimationView = findViewById(R.id.lottie_plane);
        lottieAnimationView1 = findViewById(R.id.lottie_bus);
    }

    public void setAnimations() {
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.start_anim_bottom);
        fromTop = AnimationUtils.loadAnimation(this, R.anim.start_anim_top);
        logoImageView.setAnimation(fromTop);
        logoTextTextView.setTranslationX(-250);
        logoNameText.setTranslationX(250);
        logoTextTextView.animate().translationX(0).setDuration(2000);
        logoNameText.animate().translationX(0).setDuration(2000);
        lottieAnimationView.setTranslationX(-440);
        lottieAnimationView.animate().translationX(1800).setDuration(4300);
        lottieAnimationView1.animate().alpha(0).setDuration(800).setStartDelay(2900);
    }
}