package us.wi.hofferec.unitix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class TicketMarketplaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_marketplace);

        // use listview view to display notes
        ArrayList<String> displayTickets = new ArrayList<>();
        displayTickets.add("test1");
        displayTickets.add("test2");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, displayTickets);
        ListView listView = (ListView) findViewById(R.id.ticketsListView);
        listView.setAdapter(adapter);

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
        Intent intent = new Intent(getApplicationContext(), TicketPostedActivity.class);
        startActivity(intent);
        finish();
    }


}
