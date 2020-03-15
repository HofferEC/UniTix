package us.wi.hofferec.unitix.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import us.wi.hofferec.unitix.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
