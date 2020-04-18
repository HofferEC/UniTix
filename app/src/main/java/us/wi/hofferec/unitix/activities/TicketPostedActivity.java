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
import us.wi.hofferec.unitix.data.Ticket;

public class TicketPostedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_posted);

        addTicketToTicketDatabase();

        setProfileImage();
    }

    private void addTicketToTicketDatabase() {

        // Create the ticket
        Ticket ticket = new Ticket();

        //Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        assert bundle != null;
        ticket.setDate(bundle.getString("date"));
        ticket.setHomeTeam(bundle.getString("homeTeam"));
        ticket.setAwayTeam(bundle.getString("awayTeam"));
        ticket.setEvent(bundle.getString("event"));
        ticket.setPrice(bundle.getString("price"));
        ticket.setAvailable(bundle.getBoolean("available"));

        // Add the ticket to the tickets database as well as associate it with the user
        Utility.addTicketToDatabaseAndUser("TicketPostedActivity", ticket);
    }

    public void goToHome(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }

    /**
     * Sets the users profile picture
     */
    public void setProfileImage() {
        if (LoginActivity.user.getProfileImageUri() != null) {
            // ImageView in your Activity
            ImageView profileImage = findViewById(R.id.iv_profile_ticket_posted);

            // Download directly from StorageReference using Glide
            // (See MyAppGlideModule for Loader registration)
            Glide.with(this).load(LoginActivity.user.getProfileImageUri()).apply(RequestOptions.circleCropTransform()).into(profileImage);        }
    }
}
