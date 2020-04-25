package us.wi.hofferec.unitix.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.helpers.Configs;
import us.wi.hofferec.unitix.helpers.Validation;

public class SellTicketActivity extends AppCompatActivity {

    private EditText dateEditText;
    private Calendar calendar;

    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;

    // request code
    private final int PICK_FILE_REQUEST = 100;

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

        // get the Firebase storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

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

    public void selectTicketFile(View v){

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Ticket File from here..."),
                PICK_FILE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // checking request code and result code
        // if request code is PICK_TICKET_REQUEST and
        // resultCode is RESULT_OK
        // then set the filepath
        if (requestCode == PICK_FILE_REQUEST
            && resultCode == RESULT_OK
            && data != null
            && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();

            TextView et_ticket_URI = findViewById(R.id.tv_ticket_URI);
            et_ticket_URI.setText(filePath.toString());
        }
    }

    // Uploads the ticket file to Google Cloud.
    // Note that ticket posting only proceeds if this upload is successful.
    private void uploadTicket(final Bundle bundle) throws Exception {

      if(filePath != null)
      {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference ref = storageReference.child("tickets/"+ UUID.randomUUID().toString());
        StorageTask<UploadTask.TaskSnapshot> task = ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                  @Override
                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(SellTicketActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    continuePosting(bundle, taskSnapshot.getStorage().getPath());
                  }
                })
                .addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(SellTicketActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                  }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                  @Override
                  public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                  }
                });
      }
      throw new InvalidParameterException("Filepath cannot be null");
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
     * checks whether input is valid, then uploads ticket.
     * @param view
     */
    public void initiateTicketPost(View view) {

        EditText dateEditText = findViewById(R.id.et_sell_ticket_date);
        Spinner eventSpinner = findViewById(R.id.spinner_sell_ticket_event);
        EditText homeTeamEditText = findViewById(R.id.et_sell_ticket_home_team);
        EditText awayTeamEditText = findViewById(R.id.et_sell_ticket_away_Team);
        EditText priceEditText = findViewById(R.id.et_sell_ticket_price);
        TextView ticket_URI_tv = findViewById(R.id.tv_ticket_URI);

        String date = dateEditText.getText().toString();
        String event = eventSpinner.getSelectedItem().toString();
        String homeTeam = homeTeamEditText.getText().toString();
        String awayTeam = awayTeamEditText.getText().toString();
        String price = priceEditText.getText().toString();
        String file = ticket_URI_tv.getText().toString();

        // Validate the input of the ticket fields
        if (!Validation.validateTicket(getApplicationContext(),
                date, event, homeTeam, awayTeam, price, file))
            return;

        Bundle bundle = new Bundle();

        bundle.putString("date", date);
        bundle.putString("event", event);
        bundle.putString("homeTeam", homeTeam);
        bundle.putString("awayTeam", awayTeam);
        bundle.putString("price", price);
        bundle.putBoolean("available", true);

        // Upload the ticket file
        try {
            uploadTicket(bundle);
        } catch (Exception e) {
            Log.e("SellTicketActivity", "Error uploading ticket:" + e.getMessage());
        }
    }

    // Finishes the ticket posting on SellTicketActivity side. Only called after ticket .pdf is
    // successfully uploaded.
    private void continuePosting(Bundle bundle, String ticketPath){
        bundle.putString("ticketPath", ticketPath);

        // Note: Ticket is posted within onCreate of TicketPostedActivity
        Intent intent = new Intent(getApplicationContext(), TicketPostedActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
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
