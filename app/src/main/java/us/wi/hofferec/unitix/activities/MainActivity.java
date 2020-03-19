package us.wi.hofferec.unitix.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.data.Factory;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    private String COLLECTION = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // This means somehow the user had previously logged in and their information was not loaded
        Factory.getUserInformation();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
