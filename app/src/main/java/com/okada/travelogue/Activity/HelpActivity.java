package com.okada.travelogue.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.R;

public class HelpActivity extends AppCompatActivity {

    private TextView settingsHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        settingsHeader = findViewById(R.id.settings_header);
        checkLanguage();
    }

    private void checkLanguage() {
        if (LanguageResourceClass.isEnglish(this))
            settingsHeader.setText("Help");
        else
            settingsHeader.setText("Yardım");
    }

    public void toBackActivity(View view) {
        onBackPressed();
    }
}