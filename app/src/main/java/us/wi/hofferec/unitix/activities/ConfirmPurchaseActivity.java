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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void cancel(View view){
        finish();
    }

    public void buyTicket(View view){
        Intent intent = new Intent(getApplicationContext(), TicketPurchasedActivity.class);
        intent.putExtra("ticket", ticket);
        startActivity(intent);
    }
}
