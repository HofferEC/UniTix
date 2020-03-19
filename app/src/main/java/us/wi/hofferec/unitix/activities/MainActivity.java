package us.wi.hofferec.unitix.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.data.User;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    private String COLLECTION = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (LoginActivity.user == null)
            getUserInformation();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void getUserInformation(){

        // Set the location of where the users information is stored
        final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        documentReference = database.collection(COLLECTION).document(userUID);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Pull the data from the database
                        LoginActivity.user = document.toObject(User.class);

                        Log.d("MainActivity", "Retrieved data for user, " + userUID + ": " + document.getData());
                    }
                    else {
                        Log.d("MainActivity", "Unable to find document for user: " + userUID + ", creating document now");

                        // Create document for this user
                        LoginActivity.user = new User(getIntent().getStringExtra("email"));

                        final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        database.collection(COLLECTION).document(userUID).set(LoginActivity.user);
                    }
                } else {
                    Log.d("MainActivity", "accessing database failed with ", task.getException());
                }
            }
        });
    }

    public void openMarketplace(View view) {
        Intent intent = new Intent(getApplicationContext(), TicketMarketplaceActivity.class);
        startActivity(intent);
        finish();
    }

    public void sellTicket(View view) {
        Intent intent = new Intent(getApplicationContext(), SellTicketActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToProfile(View view){
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
        finish();
    }
}
