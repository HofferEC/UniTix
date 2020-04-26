package us.wi.hofferec.unitix.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.activities.LoginActivity;
import us.wi.hofferec.unitix.data.Utility;

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
                    LoginActivity.user.addSetting("notifications", true);
                }
                else {
                    Toast.makeText(getActivity(), "Notifications disabled", Toast.LENGTH_SHORT).show();
                    LoginActivity.user.addSetting("notifications", false);
                }
                break;
            case "darkModeEnabled":
                on = sharedPreferences.getBoolean("darkModeEnabled", false);
                if(on) {
                    LoginActivity.user.addSetting("darkMode", true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Toast.makeText(getActivity(), "Dark Theme enabled", Toast.LENGTH_SHORT).show();
                }
                else {
                    LoginActivity.user.addSetting("darkMode", false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Toast.makeText(getActivity(), "Dark Theme disabled", Toast.LENGTH_SHORT).show();
                }
                break;
            case "currency":
                String currency = sharedPreferences.getString("currency", "");
                LoginActivity.user.addSetting("currency", currency);
                Toast.makeText(getActivity(), "Currency set to " + currency, Toast.LENGTH_SHORT).show();
        }

        // Update users settings in the database
        Utility.updateUserDatabase("SettingsFragment");
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
