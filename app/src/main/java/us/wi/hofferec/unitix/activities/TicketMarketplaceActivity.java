package us.wi.hofferec.unitix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.adapters.TicketAdapter;
import us.wi.hofferec.unitix.data.Ticket;

public class TicketMarketplaceActivity extends AppCompatActivity {

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference ticketsRef;
    private TicketAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_marketplace);

        ticketsRef = database.collection("tickets");

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        Query query = ticketsRef.orderBy("date", Query.Direction.DESCENDING);

        // Recycler Options (How we get the query into the recycler adapter)
        FirestoreRecyclerOptions<Ticket> options = new FirestoreRecyclerOptions.Builder<Ticket>()
                .setQuery(query, Ticket.class)
                .build();

        adapter = new TicketAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.rv_find_ticket_details);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
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
}
