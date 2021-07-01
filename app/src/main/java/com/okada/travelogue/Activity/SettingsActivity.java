package com.okada.travelogue.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.okada.travelogue.HelperClasses.LocaleHelper;
import com.okada.travelogue.R;

public class SettingsActivity extends AppCompatActivity {
    private String currentCountry, currentCurrency, currentLanguage;
    private TextView currentCountryText, currentCurrencyText, currentLanguageText;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPref;
    private int checkedItemCountry = 0, checkedItemCurrency = 0, checkedItemLanguage = 0;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        currentCountryText = findViewById(R.id.current_country_name);
        currentCurrencyText = findViewById(R.id.current_currency_name);
        currentLanguageText = findViewById(R.id.current_language_name);
        builder = new AlertDialog.Builder(SettingsActivity.this);
        sharedPref = getSharedPreferences("com.okada.travelogue.Activity", Context.MODE_PRIVATE);
        currentCurrency = sharedPref.getString("CURRENCY", "DOLLAR");
        currentCountry = sharedPref.getString("COUNTRY", "USA");
        currentLanguage = sharedPref.getString("LANGUAGE", "ENGLISH");
        currentCountryText.setText(currentCountry);
        currentCurrencyText.setText(currentCurrency);
        currentLanguageText.setText(currentLanguage);
        checkLanguage();
    }

    public void changeLanguage(View view) {
        if (currentLanguage.equals("ENGLISH") || currentLanguage.equals("İNGİLİZCE")) {
            String[] languages = {"ENGLISH", "TURKISH"};
            builder.setCancelable(false);
            checkedItemLanguage = 0;
            builder.setTitle("Select a language")
                    .setSingleChoiceItems(languages, checkedItemLanguage, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkedItemLanguage = which;

                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentLanguage = languages[checkedItemLanguage];
                            currentLanguageText.setText(currentLanguage);
                            editor = sharedPref.edit();
                            editor.putString("LANGUAGE", currentLanguage);
                            editor.apply();
                            checkLanguage();
                            dialog.dismiss();
                        }
                    });
        } else {
            String[] languages = {"İNGİLİZCE", "TÜRKÇE"};
            builder.setCancelable(false);
            checkedItemLanguage = 1;
            builder.setTitle("Dil Seçiniz")
                    .setSingleChoiceItems(languages, checkedItemLanguage, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkedItemLanguage = which;

                        }
                    })
                    .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentLanguage = languages[checkedItemLanguage];
                            currentLanguageText.setText(currentLanguage);
                            editor = sharedPref.edit();
                            editor.putString("LANGUAGE", currentLanguage);
                            editor.apply();
                            checkLanguage();
                            dialog.dismiss();
                        }
                    });
        }
        builder.create().show();


    }

    public void checkLanguage() {
        if (currentLanguage.equals("ENGLISH") || currentLanguage.equals("İNGİLİZCE")) {
            checkedItemLanguage = 0;
        } else {
            checkedItemLanguage = 1;
        }
        Context context = null;
        Resources resources = null;
        if (checkedItemLanguage == 0) {
            context = LocaleHelper.setLocale(SettingsActivity.this, "en");
            resources = context.getResources();
            ((TextView) findViewById(R.id.current_language_name)).setText(resources.getString(R.string.english));
        } else {
            context = LocaleHelper.setLocale(SettingsActivity.this, "tr");
            resources = context.getResources();
            ((TextView) findViewById(R.id.current_language_name)).setText(resources.getString(R.string.turkish));
        }
        ((TextView) findViewById(R.id.settings_header)).setText(resources.getString(R.string.settings));
        ((TextView) findViewById(R.id.settings_language_text)).setText(resources.getString(R.string.language));
        ((TextView) findViewById(R.id.settings_country_text)).setText(resources.getString(R.string.country));
        ((TextView) findViewById(R.id.settings_currency_text)).setText(resources.getString(R.string.currency));
        ((TextView) findViewById(R.id.settings_terms_text)).setText(resources.getString(R.string.terms_conditions));
        ((TextView) findViewById(R.id.settings_privacy_text)).setText(resources.getString(R.string.privacy_policy));
        switch (currentCountry) {
            case "USA":
            case "ABD":
                ((TextView) findViewById(R.id.current_country_name)).setText(resources.getString(R.string.usa));
                break;
            case "TURKEY":
            case "TÜRKİYE":
                ((TextView) findViewById(R.id.current_country_name)).setText(resources.getString(R.string.turkey));
                break;
            case "RUSSIA":
            case "RUSYA":
                ((TextView) findViewById(R.id.current_country_name)).setText(resources.getString(R.string.russia));
                break;

        }
        switch (currentCurrency) {
            case "DOLLAR":
            case "DOLAR":
                ((TextView) findViewById(R.id.current_currency_name)).setText(resources.getString(R.string.dollar));
                break;
            case "TL":
                ((TextView) findViewById(R.id.current_currency_name)).setText(resources.getString(R.string.tl));
                break;
            case "RUBLE":
                ((TextView) findViewById(R.id.current_currency_name)).setText(resources.getString(R.string.ruble));
                break;
        }
    }

    public void changeCountry(View view) {
        if (currentLanguage.equals("ENGLISH") || currentLanguage.equals("İNGİLİZCE")) {

            String[] countries = {"USA", "TURKEY", "RUSSIA"};
            builder.setCancelable(false);
            if (currentCountry.equals("USA")) {
                checkedItemCountry = 0;
            } else if (currentCountry.equals("TURKEY")) {
                checkedItemCountry = 1;
            } else if (currentCountry.equals("RUSSIA")) {
                checkedItemCountry = 2;
            }

            builder.setTitle("Select a country")
                    .setSingleChoiceItems(countries, checkedItemCountry, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkedItemCountry = which;
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentCountry = countries[checkedItemCountry];
                            currentCountryText.setText(currentCountry);
                            editor = sharedPref.edit();
                            editor.putString("COUNTRY", currentCountry);
                            editor.apply();
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        } else {

            String[] countries = {"ABD", "TÜRKİYE", "RUSYA"};
            builder.setCancelable(false);
            if (currentCountry.equals("ABD")) {
                checkedItemCountry = 0;
            } else if (currentCountry.equals("TÜRKİYE")) {
                checkedItemCountry = 1;
            } else if (currentCountry.equals("RUSYA")) {
                checkedItemCountry = 2;
            }

            builder.setTitle("Ülke Seçiniz")
                    .setSingleChoiceItems(countries, checkedItemCountry, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkedItemCountry = which;
                        }
                    })
                    .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentCountry = countries[checkedItemCountry];
                            currentCountryText.setText(currentCountry);
                            editor = sharedPref.edit();
                            editor.putString("COUNTRY", currentCountry);
                            editor.apply();
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }
    }

    public void changeCurrency(View view) {
        if (currentLanguage.equals("ENGLISH") || currentLanguage.equals("İNGİLİZCE")) {
            String[] currencies = {"DOLLAR", "TL", "RUBLE"};
            builder.setCancelable(false);
            if (currentCurrency.equals("DOLLAR")) {
                checkedItemCurrency = 0;
            } else if (currentCurrency.equals("TL")) {
                checkedItemCurrency = 1;
            } else if (currentCurrency.equals("RUBLE")) {
                checkedItemCurrency = 2;
            }

            builder.setTitle("Select a currency")
                    .setSingleChoiceItems(currencies, checkedItemCurrency, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkedItemCurrency = which;
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentCurrency = currencies[checkedItemCurrency];
                            currentCurrencyText.setText(currentCurrency);
                            editor = sharedPref.edit();
                            editor.putString("CURRENCY", currentCurrency);
                            editor.apply();
                            dialog.dismiss();
                        }
                    });
        } else {
            String[] currencies = {"DOLAR", "TL", "RUBLE"};
            builder.setCancelable(false);
            if (currentCurrency.equals("DOLAR")) {
                checkedItemCurrency = 0;
            } else if (currentCurrency.equals("TL")) {
                checkedItemCurrency = 1;
            } else if (currentCurrency.equals("RUBLE")) {
                checkedItemCurrency = 2;
            }

            builder.setTitle("Para Birimi Seçiniz")
                    .setSingleChoiceItems(currencies, checkedItemCurrency, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkedItemCurrency = which;
                        }
                    })
                    .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentCurrency = currencies[checkedItemCurrency];
                            currentCurrencyText.setText(currentCurrency);
                            editor = sharedPref.edit();
                            editor.putString("CURRENCY", currentCurrency);
                            editor.apply();
                            dialog.dismiss();
                        }
                    });
        }
        builder.create().show();

    }

    public void to_privacy_policy(View view) {
        Intent toPrivacyPolicy = new Intent(this,PrivacyPolicyActivity.class);
        startActivity(toPrivacyPolicy);
    }

    public void to_terms_conditions(View view) {
        Intent toTermsConditions = new Intent(this,ConditionsActivity.class);
        startActivity(toTermsConditions);
    }

    public void toBackActivity(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent(SettingsActivity.this,HomeActivity.class);
        startActivity(intent);
        Animatoo.animateZoom(SettingsActivity.this);
        finish();
    }
}