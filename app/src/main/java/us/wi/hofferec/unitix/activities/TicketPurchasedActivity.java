package us.wi.hofferec.unitix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import androidx.appcompat.app.AppCompatActivity;
import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.data.Ticket;

public class TicketPurchasedActivity extends AppCompatActivity {

    Ticket ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_purchased);

        setProfileImage();

        Intent intent = getIntent();
        ticket = (Ticket) intent.getSerializableExtra("ticket");

        TextView ticketInfo = (TextView) findViewById(R.id.ticketInfoTextView);

        ticketInfo.setText("Event: " + ticket.getEvent() + "\n\n" +
                "Teams: " + ticket.getAwayTeam() + " @ " + ticket.getHomeTeam() + "\n\n" +
                "Date: " + ticket.getDate() + "\n\n" +
                "Price: $" + ticket.getPrice());
    }

    public void goToHome(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToProfile(View view){
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Sets the users profile picture
     */
    public void setProfileImage(){
        if (LoginActivity.user.getProfileImageUri() != null) {
            // ImageView in your Activity
            ImageView profileImage = findViewById(R.id.iv_profile_ticket_purchased);

            // Download directly from StorageReference using Glide
            // (See MyAppGlideModule for Loader registration)
            Glide.with(this).load(LoginActivity.user.getProfileImageUri()).apply(RequestOptions.circleCropTransform()).into(profileImage);        }
    }
}
