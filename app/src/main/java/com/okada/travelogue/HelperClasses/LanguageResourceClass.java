package com.okada.travelogue.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

public class LanguageResourceClass {

    public static Resources getResource(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("com.okada.travelogue.Activity", Context.MODE_PRIVATE);
        String currentLanguage = sharedPref.getString("LANGUAGE", "ENGLISH");
        if (currentLanguage.equals("ENGLISH") ||currentLanguage.equals("İNGİLİZCE")) {
            return LocaleHelper.setLocale(context, "en").getResources();
        } else return LocaleHelper.setLocale(context, "tr").getResources();
    }
    public static boolean isEnglish(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("com.okada.travelogue.Activity", Context.MODE_PRIVATE);
        String currentLanguage = sharedPref.getString("LANGUAGE", "ENGLISH");
        if (currentLanguage.equals("ENGLISH")||currentLanguage.equals("İNGİLİZCE")) {
            return true;
        } else return false;
    }
}
