package us.wi.hofferec.unitix.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.helpers.Configs;
import us.wi.hofferec.unitix.helpers.Validation;

public class SellTicketActivity extends AppCompatActivity {

    private EditText dateEditText;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_ticket);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item, Configs.eventTypes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner eventSpinner = findViewById(R.id.spinner_sell_ticket_event);
        eventSpinner.setAdapter(adapter);

        setProfileImage();

        // Setup date picker
        calendar = Calendar.getInstance();
        dateEditText = findViewById(R.id.et_sell_ticket_date);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        // Date picker listener
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SellTicketActivity.this, date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
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
        Spinner eventSpinner = findViewById(R.id.spinner_sell_ticket_event);
        EditText homeTeamEditText = findViewById(R.id.et_sell_ticket_home_team);
        EditText awayTeamEditText = findViewById(R.id.et_sell_ticket_away_Team);
        EditText priceEditText = findViewById(R.id.et_sell_ticket_price);

        String date = dateEditText.getText().toString();
        String event = eventSpinner.getSelectedItem().toString();
        String homeTeam = homeTeamEditText.getText().toString();
        String awayTeam = awayTeamEditText.getText().toString();
        String price = priceEditText.getText().toString();

        // Validate the input of the ticket fields, then post ticket.
        if (Validation.validateTicket(getApplicationContext(),
                date, event, homeTeam, awayTeam, price)) {
            bundle.putString("date", date);
            bundle.putString("event", event);
            bundle.putString("homeTeam", homeTeam);
            bundle.putString("awayTeam", awayTeam);
            bundle.putString("price", price);
            bundle.putBoolean("available", true);

            // Note: Ticket is posted within onCreate of TicketPostedActivity
            Intent intent = new Intent(getApplicationContext(), TicketPostedActivity.class);

            intent.putExtras(bundle);

            startActivity(intent);

            finish();
        }
    }

    /**
     * Updates date label with the date selected
     */
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat date = new SimpleDateFormat(myFormat, Locale.US);
        dateEditText.setText(date.format(calendar.getTime()));
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
