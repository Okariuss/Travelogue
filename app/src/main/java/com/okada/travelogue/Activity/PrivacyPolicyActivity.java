package com.okada.travelogue.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.R;

public class PrivacyPolicyActivity extends AppCompatActivity {
    private TextView settingsHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        settingsHeader=findViewById(R.id.settings_header);
        Resources resources= LanguageResourceClass.getResource(this);
        settingsHeader.setText(resources.getString(R.string.privacy_policy));
    }

    public void toBackActivity(View view) {
        onBackPressed();
    }
}