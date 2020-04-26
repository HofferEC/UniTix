package us.wi.hofferec.unitix.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.data.Ticket;
import us.wi.hofferec.unitix.data.Utility;

public class TicketPurchasedActivity extends AppCompatActivity {

    Ticket ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_purchased);

        Intent intent = getIntent();
        ticket = (Ticket) intent.getSerializableExtra("ticket");

        ticket.setAvailable(false);

        Utility.updateTicketOnDatabase("TicketPurchasedActivity", ticket);

        Utility.addTicketToDatabaseAndUser("TicketPurchasedActivity", ticket, "buying");

        TextView ticketInfo = (TextView) findViewById(R.id.ticketInfoTextView);

        ticketInfo.setText("Event: " + ticket.getEvent() + "\n\n" +
                "Teams: " + ticket.getAwayTeam() + " @ " + ticket.getHomeTeam() + "\n\n" +
                "Date: " + ticket.getDate() + "\n\n" +
                "Price: $" + ticket.getPrice());

        checkPermsAndDownloadTicket("TicketPurchasedActivity");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v("TicketPurchasedActivity","Permission: "+permissions[0]+ "was "+grantResults[0]);
        }
    }


    // Ensures that we have permissions to write to the Android filesystem and DL's ticket .pdf
    private void checkPermsAndDownloadTicket(String TAG) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Write permission is granted");
            } else {

                Log.v(TAG,"Write permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Write permission is granted automatically due to SDK");
        }
        Utility.downloadTicket(this, TAG, ticket.getTicketPath());
    }

    // Opens the ticket .pdf
    public void openTicket(View view) {
        Utility.openTicket(this, "TicketPurchasedActivity", ticket.getTicketPath());
    }

    public void goToHome(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
