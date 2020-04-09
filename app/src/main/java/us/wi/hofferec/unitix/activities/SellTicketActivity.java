package us.wi.hofferec.unitix.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import us.wi.hofferec.unitix.R;

public class SellTicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_ticket);

        setProfileImage();
    }

    public void goToHome(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void goToProfile(View view){
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }

    /**
     * checks whether input is valid and then starts a new intent which contains all the
     * data and passes that to TicketPostedActivity
     * @param view
     */
    public void postTicket(View view){

        Bundle bundle = new Bundle();

        EditText dateEditText = findViewById(R.id.et_sell_ticket_date);
        EditText eventEditText = findViewById(R.id.et_sell_ticket_event);
        EditText homeTeamEditText = findViewById(R.id.et_sell_ticket_home_team);
        EditText awayTeamEditText = findViewById(R.id.et_sell_ticket_away_Team);
        EditText priceEditText = findViewById(R.id.et_sell_ticket_price);

        String date = dateEditText.getText().toString();
        String event = eventEditText.getText().toString();
        String homeTeam = homeTeamEditText.getText().toString();
        String awayTeam = awayTeamEditText.getText().toString();
        String price = priceEditText.getText().toString();

        // check if all edit texts have input
        if (date.isEmpty()) {
            dateEditText.setError("Enter a date");
            dateEditText.requestFocus();
        } else if (event.isEmpty()) {
            eventEditText.setError("Enter an event");
            eventEditText.requestFocus();
        } else if (homeTeam.isEmpty()) {
            homeTeamEditText.setError("Enter a home team");
            homeTeamEditText.requestFocus();
        } else if (awayTeam.isEmpty()) {
            awayTeamEditText.setError("Enter an away team");
            awayTeamEditText.requestFocus();
        } else if (price.isEmpty()) {
            priceEditText.setError("Enter a price");
            priceEditText.requestFocus();
        } else {
            bundle.putString("date", date);
            bundle.putString("event", event);
            bundle.putString("homeTeam", homeTeam);
            bundle.putString("awayTeam", awayTeam);
            bundle.putString("price", price);
            bundle.putBoolean("available", true);

            Intent intent = new Intent(getApplicationContext(), TicketPostedActivity.class);

            intent.putExtras(bundle);

            startActivity(intent);

            finish();
        }
    }

    /**
     * Sets the users profile picture
     */
    public void setProfileImage(){
        if (LoginActivity.user.getProfileImageUri() != null) {
            // ImageView in your Activity
            ImageView profileImage = findViewById(R.id.iv_profile_sell_ticket);

            // Download directly from StorageReference using Glide
            // (See MyAppGlideModule for Loader registration)
            Glide.with(this).load(LoginActivity.user.getProfileImageUri()).apply(RequestOptions.circleCropTransform()).into(profileImage);
        }
    }
}
