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
        startActivity(intent);
        finish();
    }

    public void goToProfile(View view){
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    public void postTicket(View view){

        Bundle bundle = new Bundle();

        EditText dateEditText = findViewById(R.id.et_sell_ticket_date);
        EditText eventEditText = findViewById(R.id.et_sell_ticket_event);
        EditText homeTeamEditText = findViewById(R.id.et_sell_ticket_home_team);
        EditText awayTeamEditText = findViewById(R.id.et_sell_ticket_away_Team);
        EditText priceEditText = findViewById(R.id.et_sell_ticket_price);

        bundle.putString("date", dateEditText.getText().toString());
        bundle.putString("event", eventEditText.getText().toString());
        bundle.putString("homeTeam", homeTeamEditText.getText().toString());
        bundle.putString("awayTeam", awayTeamEditText.getText().toString());
        bundle.putString("price", priceEditText.getText().toString());
        bundle.putBoolean("available", true);

        Intent intent = new Intent(getApplicationContext(), TicketPostedActivity.class);

        intent.putExtras(bundle);

        startActivity(intent);
        finish();
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
