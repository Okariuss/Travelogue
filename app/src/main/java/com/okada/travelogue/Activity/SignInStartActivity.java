package com.okada.travelogue.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.R;

public class SignInStartActivity extends AppCompatActivity {
    private ImageView logoImageView;
    private TextView textView;
    private Animation animTop,animBottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_start);
        findingViews();
        checkLanguage();
        setAnimations();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SignInStartActivity.this,SignInActivity.class);
                Pair[]pairs=new Pair[2];
                pairs[0]=new Pair<View,String >(logoImageView,"sign_in_start_logo_image");
                pairs[1]=new Pair<View,String >(textView,"sign_in_start_text_view");
                ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(SignInStartActivity.this,pairs);
                startActivity(intent,activityOptions.toBundle());
                finish();
            }
        },3000);

    }

    private void checkLanguage() {
        Resources resources= LanguageResourceClass.getResource(this);
        textView.setText(resources.getString(R.string.welcome_back));
    }

    public void findingViews(){
        logoImageView=findViewById(R.id.signInStartLogoImage);
        textView=findViewById(R.id.signInStartTextView);
    }
    public void setAnimations(){
        animTop= AnimationUtils.loadAnimation(this,R.anim.start_anim_top);
        animBottom=AnimationUtils.loadAnimation(this,R.anim.start_anim_bottom);
        logoImageView.setAnimation(animTop);
        textView.setAnimation(animBottom);
    }

}