package us.wi.hofferec.unitix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.data.Ticket;

public class ConfirmPurchaseActivity extends AppCompatActivity {

    Ticket ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_purchase);

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

    public void buyTicket(View view){
        Intent intent = new Intent(getApplicationContext(), TicketPurchasedActivity.class);
        intent.putExtra("ticket", ticket);
        startActivity(intent);
        finish();
    }

    public void returnToMarketplace(View view) {
        Intent intent = new Intent(getApplicationContext(), TicketMarketplaceActivity.class);
        startActivity(intent);
        finish();
    }
}
