package me.sebi.clipedit;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("clearShortcut")) {
            boolean clearShortcut = sharedPreferences.getBoolean(key, true);

            int newState = PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
            if (!clearShortcut) newState = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;

            ComponentName cn = new ComponentName(this, ClearAction.class);
            getPackageManager().setComponentEnabledSetting(cn, newState, PackageManager.DONT_KILL_APP);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}