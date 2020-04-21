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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    private ArrayList<Ticket> ticketsList;

    private enum SortField { EVENT, EVENT_REVERSE, DATE, DATE_REVERSE,
        PRICE, PRICE_REVERSE, TEAM, TEAM_REVERSE }

    private SortField lastSorted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_marketplace);

        // Request the focus of the recycler view because android....
        findViewById(R.id.rv_find_ticket_details).requestFocus();

        setProfileImage();

        ticketsRef = database.collection("tickets");
        ticketsList = new ArrayList<>();

        // Setup Textviews for filtering the tickets list
        findViewById(R.id.tv_marketplace_date).setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 sortByDate();
             }
         });
        findViewById(R.id.tv_marketplace_event).setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 sortByEvent();
             }
         });
        findViewById(R.id.tv_marketplace_price).setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 sortByPrice();
             }
         });
        findViewById(R.id.tv_marketplace_teams).setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 sortByTeam();
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

        lastSorted = SortField.DATE;
        setupRecyclerView();
    }

    // Initial setup for recyclerview.
    private void setupRecyclerView() {

        // Add dividers between items
        RecyclerView recyclerView = findViewById(R.id.rv_find_ticket_details);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        // Query Firebase for tickets
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
                        sortByDate();
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
        TicketAdapter adapter = new TicketAdapter(ticketsListToShow);

        // Get the recyclerView
        RecyclerView recyclerView = findViewById(R.id.rv_find_ticket_details);

         // Don't allow recyclerView to resize
        recyclerView.setHasFixedSize(true);

        // Set Layout type
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    // Sorts the tickets list by date
    private void sortByDate(){
        // If sorted by date last, alternate the sorting by clicking again
        if (lastSorted == SortField.DATE)
            lastSorted = SortField.DATE_REVERSE;
        else
            lastSorted = SortField.DATE;
        final int reverseMod = (lastSorted == SortField.DATE_REVERSE ? -1 : 1);

        // Sort the list
        Collections.sort(ticketsList, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket a, Ticket b) {
                // Date format "MM/DD/YYYY"
                String[] aDateArgs = a.getDate().split("/");
                String[] bDateArgs = b.getDate().split("/");

                // Puts the date strings into YYYY/MM/DD format so that string comparison works
                String aDateConcat = aDateArgs[2] + aDateArgs[0] + aDateArgs[1];
                String bDateConcat = bDateArgs[2] + bDateArgs[0] + bDateArgs[1];
                return reverseMod * aDateConcat.compareTo(bDateConcat);
            }
        });
        fillRecyclerView(ticketsList);
    }

    // Sorts the tickets list by price
    private void sortByPrice(){
        // If sorted by price last, alternate the sorting by clicking again
        if (lastSorted == SortField.PRICE)
            lastSorted = SortField.PRICE_REVERSE;
        else
            lastSorted = SortField.PRICE;
        final int reverseMod = (lastSorted == SortField.PRICE_REVERSE ? -1 : 1);

        // Sort the list
        Collections.sort(ticketsList, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket a, Ticket b) {
                float aPrice = Float.parseFloat(a.getPrice());
                float bPrice = Float.parseFloat(b.getPrice());
                return reverseMod * Float.compare(aPrice, bPrice);
            }
        });
        fillRecyclerView(ticketsList);
    }

    // Sorts the tickets list by Away Team
    // NOTE: Chosen to compare by Away team instead of Home team because the app is focused towards
    // events where UW-Madison will be the home team.
    private void sortByTeam(){
        // If sorted by team last, alternate the sorting by clicking again
        if (lastSorted == SortField.TEAM)
            lastSorted = SortField.TEAM_REVERSE;
        else
            lastSorted = SortField.TEAM;
        final int reverseMod = (lastSorted == SortField.TEAM_REVERSE ? -1 : 1);

        // Sort the list
        Collections.sort(ticketsList, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket a, Ticket b) {
                return reverseMod * (a.getAwayTeam().compareToIgnoreCase(b.getAwayTeam()));
            }
        });
        fillRecyclerView(ticketsList);
    }

    // Sorts the tickets list by Event
    private void sortByEvent(){
        // If sorted by team last, alternate the sorting by clicking again
        if (lastSorted == SortField.EVENT)
            lastSorted = SortField.EVENT_REVERSE;
        else
            lastSorted = SortField.EVENT;
        final int reverseMod = (lastSorted == SortField.EVENT_REVERSE ? -1 : 1);

        // Sort the list
        Collections.sort(ticketsList, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket a, Ticket b) {
                return reverseMod * (a.getEvent().compareToIgnoreCase(b.getEvent()));
            }
        });
        fillRecyclerView(ticketsList);
    }

    // Searches the tickets list and returns a list of tickets with at least one field
    // containing the given search term.
    public List<Ticket> searchTicketsForText(final String searchTerm){
        if (searchTerm == null || searchTerm.isEmpty())
            return ticketsList;
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
