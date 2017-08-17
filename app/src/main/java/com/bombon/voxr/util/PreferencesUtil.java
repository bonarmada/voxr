package com.bombon.voxr.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by Vaughn on 8/17/17.
 */

public class PreferencesUtil {
    private static SharedPreferences preferences;

    @Inject
    PreferencesUtil(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public static void showAll(){
        Map<String, ?> keys = preferences.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            Log.e("map values", entry.getKey() + ": " +
                    entry.getValue().toString());
        }
    }

    public static void set(String identifier, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(identifier, value);
        editor.commit();
    }

    public static String get(String identifier) {
        String restoredText = preferences.getString(identifier, null);
        return restoredText != null ? restoredText : null;
    }

    public static void removeSharedPref(Context mContext, String identifier) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(identifier);
        editor.apply();
    }
}
