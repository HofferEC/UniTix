package us.wi.hofferec.unitix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.adapters.TicketsAdapter;
import us.wi.hofferec.unitix.data.Ticket;

public class TicketMarketplaceActivity extends AppCompatActivity {

    private static ArrayList<Ticket> tickets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_marketplace);

        // Lookup the recyclerview in activity layout
        RecyclerView rvTickets = findViewById(R.id.rv_find_ticket_details);

        // change focus from search (Really annoying... WHY ANDROID?)
        rvTickets.requestFocus();

        // Initialize example tickets TODO replace with Firebase
        tickets = Ticket.getTickets(20);

        // Create adapter passing in the sample data
        TicketsAdapter adapter = new TicketsAdapter(tickets);

        // Attach the adapter to the recyclerview to populate items
        rvTickets.setAdapter(adapter);

        // Set layout manager to position the items
        rvTickets.setLayoutManager(new LinearLayoutManager(this));
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

    public void confirmPurchase(View view) {
        setContentView(R.layout.confirm_purchase);
    }
}
