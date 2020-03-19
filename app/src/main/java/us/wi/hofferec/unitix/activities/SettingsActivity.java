package us.wi.hofferec.unitix.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    public static final String NOTIFICATION_SWITCH = "notificationsKey";
    public static final String CURRENCY = "currency";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Start the settings fragment
        SettingsFragment settingsFragment = new SettingsFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_settings, settingsFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
