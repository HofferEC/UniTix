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
import us.wi.hofferec.unitix.data.Factory;
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

        // Add the ticket to the tickets database as well as associate it with the user
        Factory.addTicketToDatabaseAndUser("TicketPostedActivity", ticket);
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
