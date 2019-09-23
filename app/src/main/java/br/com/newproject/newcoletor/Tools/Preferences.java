package br.com.newproject.newcoletor.Tools;

import android.content.SharedPreferences;

public class Preferences {

    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;

    public Preferences(SharedPreferences preferences){
        this.preferences = preferences;
        this.editor = this.preferences.edit();
    }
}
