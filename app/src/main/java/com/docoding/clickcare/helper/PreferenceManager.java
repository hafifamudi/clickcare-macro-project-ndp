package com.docoding.clickcare.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static SharedPreferences sharedPreferences;

    public PreferenceManager(Context context){
        sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static void putBoolean(String key, Boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key, false);
    }

    public static void putString(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String key){
        return sharedPreferences.getString(key, null);
    }

    public static void clear(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
