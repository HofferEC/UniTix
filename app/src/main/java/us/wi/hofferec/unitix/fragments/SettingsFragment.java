package us.wi.hofferec.unitix.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.activities.LoginActivity;
import us.wi.hofferec.unitix.data.Factory;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // Get sharedPreferences
        if (getActivity() != null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "notificationsKey":
                boolean on = sharedPreferences.getBoolean("notificationsKey", false);
                if (on) {
                    Toast.makeText(getActivity(), "Notifications enabled", Toast.LENGTH_SHORT).show();
                    Factory.getUser().addSetting("notifications", true);
                }
                else {
                    Toast.makeText(getActivity(), "Notifications disabled", Toast.LENGTH_SHORT).show();
                    Factory.getUser().addSetting("notifications", false);
                }
                break;
            case "darkModeEnabled":
                on = sharedPreferences.getBoolean("darkModeEnabled", false);
                if(on) {
                    Toast.makeText(getActivity(), "Dark Theme enabled", Toast.LENGTH_SHORT).show();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Factory.getUser().addSetting("darkMode", true);
                }
                else {
                    Toast.makeText(getActivity(), "Dark Theme disabled", Toast.LENGTH_SHORT).show();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Factory.getUser().addSetting("darkMode", false);
                }
                break;
        }

        Factory.updateUserDatabase("SettingsFragment");
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
