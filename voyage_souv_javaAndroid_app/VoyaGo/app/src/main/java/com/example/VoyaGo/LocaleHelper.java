package com.example.VoyaGo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LocaleHelper {

    public static void setLocal(Activity activity, String lanCode){
        Locale locale = new Locale(lanCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(config, dm);
    }



}

