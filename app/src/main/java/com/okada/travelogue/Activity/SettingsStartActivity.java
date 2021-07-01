package com.okada.travelogue.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.R;

public class SettingsStartActivity extends AppCompatActivity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_start);
        textView=findViewById(R.id.logo_name_text_view);
        checkLanguage();
        setAnimations();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            Intent intent=new Intent(SettingsStartActivity.this,SettingsActivity.class);
            startActivity(intent);
            Animatoo.animateZoom(SettingsStartActivity.this);
                finish();
            }
        }, 2600);
    }

    private void setAnimations() {
        textView.setAlpha(0);
        textView.animate().alpha(1).setDuration(2000);
    }

    private void checkLanguage() {
        if (LanguageResourceClass.isEnglish(this)){
            textView.setText("Settings");
        }else textView.setText("Ayarlar");
    }
}