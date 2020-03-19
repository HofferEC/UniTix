package us.wi.hofferec.unitix.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.xml.validation.Validator;

import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.data.Ticket;

public class TicketPostedActivity extends AppCompatActivity {

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final String TICKETS_COLLECTION = "tickets";
    private final String USERS_COLLECTION = "users";
    private final String TAG = "TicketPostedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addTicketToTicketDatabase();

        setContentView(R.layout.activity_ticket_posted);
    }

    private void addTicketToTicketDatabase() {

        // Create the ticket
        Ticket ticket = new Ticket();

        //Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        assert bundle != null;
        ticket.setDate(bundle.getString("date"));
        ticket.setHomeTeam(bundle.getString("homeTeam"));
        ticket.setAwayTeam(bundle.getString("awayTeam"));
        ticket.setEvent(bundle.getString("event"));
        ticket.setPrice(bundle.getString("price"));
        ticket.setAvailable(bundle.getBoolean("available"));

        database.collection(TICKETS_COLLECTION)
                .add(ticket)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());

                        // Add the new ticket number to the users database
                        addTicketToUserDatabase(documentReference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void addTicketToUserDatabase(DocumentReference documentReference) {

        // Add the ticket to the users profile
        LoginActivity.user.addTicket(documentReference);

        // Rewrite the user back to the database
        database.collection(USERS_COLLECTION).document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(LoginActivity.user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
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
