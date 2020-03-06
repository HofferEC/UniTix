package us.wi.hofferec.unitix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TicketPostedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_posted);
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