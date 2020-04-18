package us.wi.hofferec.unitix.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.data.Utility;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Apply the users profile picture
        setProfileImage();

        // Check if the user's tickets have been sold. If so, sends notification.
        if ((boolean) LoginActivity.user.getSettings().get("notifications"))
            Utility.checkForSoldTickets(getApplicationContext(), "MainActivity", LoginActivity.user);
    }

    public void openMarketplace(View view) {
        Intent intent = new Intent(getApplicationContext(), TicketMarketplaceActivity.class);
        startActivity(intent);
    }

    public void sellTicket(View view) {
        Intent intent = new Intent(getApplicationContext(), SellTicketActivity.class);
        startActivity(intent);
    }

    public void goToProfile(View view){
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }

    /**
     * Sets the users profile picture
     */
    public void setProfileImage(){
        if (LoginActivity.user.getProfileImageUri() != null) {
            // ImageView in your Activity
            ImageView profileImage = findViewById(R.id.iv_profile_main);

            // Download directly from StorageReference using Glide
            // (See MyAppGlideModule for Loader registration)
            Glide.with(this).load(LoginActivity.user.getProfileImageUri()).apply(RequestOptions.circleCropTransform()).into(profileImage);
        }
    }
}
