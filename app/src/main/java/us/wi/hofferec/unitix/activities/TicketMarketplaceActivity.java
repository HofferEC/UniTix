package us.wi.hofferec.unitix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.adapters.TicketAdapter;
import us.wi.hofferec.unitix.data.Ticket;

public class TicketMarketplaceActivity extends AppCompatActivity {

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference ticketsRef;
    private TicketAdapter adapter;
    private ArrayList<Ticket> ticketsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_marketplace);

        // Request the focus of the recycler view because android....
        findViewById(R.id.rv_find_ticket_details).requestFocus();

        setProfileImage();

        ticketsRef = database.collection("tickets");
        ticketsList = new ArrayList<Ticket>();

        // Setup Textviews for filtering the tickets list
        findViewById(R.id.tv_marketplace_date).setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 // TODO: sort by date
             }
         });
        findViewById(R.id.tv_marketplace_event).setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 // TODO: sort by event
             }
         });
        findViewById(R.id.tv_marketplace_price).setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 // TODO: sort by price
             }
         });
        findViewById(R.id.tv_marketplace_teams).setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 // TODO: sort by teams
             }
         });

        // Setup the search box for filtering the tickets list
        final EditText et_search = findViewById(R.id.et_find_ticket_search);
        et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

              // When focus is lost check that the text field has valid values.
              if (!hasFocus) {
                 String filterText = et_search.getText().toString();
                 if (!filterText.isEmpty()) {
                     Log.i("TicketMarketplace", "Searching tickets for text: " + filterText);
                     List<Ticket> searchedTickets = searchTicketsForText(filterText);
                     fillRecyclerView(searchedTickets);
                 }
              }
            }
         });

        setupRecyclerView();
    }

    // Searches the tickets list and solves
    public List<Ticket> searchTicketsForText(final String searchTerm){
        ArrayList<Ticket> filteredTicketsList = new ArrayList<>();
        for (Ticket t : ticketsList){
            if (t.getDate().contains(searchTerm)
                || t.getEvent().contains(searchTerm)
                || t.getPrice().contains(searchTerm)
                || t.getHomeTeam().contains(searchTerm)
                || t.getAwayTeam().contains(searchTerm)) {
                filteredTicketsList.add(t);
            }
        }
        return filteredTicketsList;
    }

    // Initial setup for recyclerview.
    private void setupRecyclerView() {
        Query query = ticketsRef.whereEqualTo("available", true);

        // retrieve  query results asynchronously using query.get()
        query.get().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TicketMarketplace", e.getMessage());
                    }
                }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshot) {
                        Log.i("TicketMarketplace", "Successful database read. Items: " + snapshot.size());
                        List<DocumentSnapshot> documentsList = snapshot.getDocuments();
                        for (DocumentSnapshot d : documentsList){
                            if (LoginActivity.user.getTicketsSelling().contains(d.getReference())){
                                continue;
                            }
                            ticketsList.add(d.toObject(Ticket.class));
                        }
                        fillRecyclerView(ticketsList);
                    }
                });
    }

    /**
     *  Populates the RecyclerView with a list of tickets.
     *
     *  Note that this method is written heavily favoring simplicity over efficiency.
     *  A more efficient solution would be to use recyclerview add and remove methods.
     *  We will update this if the efficiency gains prove necessary.
     */
    public void fillRecyclerView(List<Ticket> ticketsListToShow) {
        // Instantiates a TicketAdapter(intermediate class between RecyclerView and our data
        adapter = new TicketAdapter(ticketsListToShow);

        // Get the recyclerView
        RecyclerView recyclerView = findViewById(R.id.rv_find_ticket_details);

         // Don't allow recyclerView to resize
        recyclerView.setHasFixedSize(true);

        // Set Layout type
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Add dividers between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public void goToHome(View view){
        finish();
    }

    public void goToProfile(View view){
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }

    /**
     * Sets the users profile picture
     */
    public void setProfileImage(){
        if (LoginActivity.user.getProfileImageUri() != null) {
            // ImageView in your Activity
            ImageView profileImage = findViewById(R.id.iv_profile_ticket_marketplace);

            // Download directly from StorageReference using Glide
            // (See MyAppGlideModule for Loader registration)
            Glide.with(this).load(LoginActivity.user.getProfileImageUri()).apply(RequestOptions.circleCropTransform()).into(profileImage);        }
    }
}
