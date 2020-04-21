package us.wi.hofferec.unitix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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

    private String sortField;
    private boolean ascending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_marketplace);

        // Request the focus of the recycler view because android....
        findViewById(R.id.rv_find_ticket_details).requestFocus();

        setProfileImage();

        ticketsRef = database.collection("tickets");

        // Setup Textviews for filtering the tickets list
        findViewById(R.id.tv_marketplace_date).setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 sortRecyclerView("date");
             }
         });
        findViewById(R.id.tv_marketplace_event).setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 sortRecyclerView("event");
             }
         });
        findViewById(R.id.tv_marketplace_price).setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 sortRecyclerView("price");
             }
         });
        findViewById(R.id.tv_marketplace_teams).setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 sortRecyclerView("homeTeam");
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
                 if (!filterText.isEmpty())
                    filterRecyclerView(filterText);
              }
            }
         });

        sortField = "date";
        ascending = true;
        setupRecyclerView();
    }

    // Initial setup for recyclerview. Defaults to sort by date.
    private void setupRecyclerView() {
        Query query = ticketsRef.whereEqualTo("available", true).orderBy("date", Query.Direction.ASCENDING);

        // Recycler Options (How we get the query into the recycler adapter)
        FirestoreRecyclerOptions<Ticket> options = new FirestoreRecyclerOptions.Builder<Ticket>()
                .setQuery(query, Ticket.class)
                .build();

        adapter = new TicketAdapter(options);

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

    // Sorts the recyclerview, flips ascending or descending
    private void sortRecyclerView(String newSortField) {
        if (newSortField.equals(sortField)) {
            ascending = !ascending;
        } else {
            ascending = false;
        }
        sortField = newSortField;
        Log.i("Marketplace", "Sorting marketplace by " + sortField + ", " + (ascending ? "ascending" : "descending"));
        Query query = ticketsRef.orderBy(newSortField, ascending ? Query.Direction.ASCENDING : Query.Direction.DESCENDING);

        // Recycler Options (How we get the query into the recycler adapter)
        FirestoreRecyclerOptions<Ticket> options = new FirestoreRecyclerOptions.Builder<Ticket>()
                .setQuery(query, Ticket.class)
                .build();

        adapter.updateOptions(options);
        adapter.notifyDataSetChanged();
    }

    // Filters the recyclerview by the defined text. Keeps the same sorting.
    private void filterRecyclerView(String filterText) {
        Log.i("Marketplace", "Filtering marketplace for " + filterText);
        Query query = ticketsRef.whereEqualTo(sortField, filterText).orderBy(sortField, ascending ? Query.Direction.ASCENDING : Query.Direction.DESCENDING);

        // Recycler Options (How we get the query into the recycler adapter)
        FirestoreRecyclerOptions<Ticket> options = new FirestoreRecyclerOptions.Builder<Ticket>()
                .setQuery(query, Ticket.class)
                .build();

        adapter.updateOptions(options);
        adapter.notifyDataSetChanged();
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
