package us.wi.hofferec.unitix.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import us.wi.hofferec.unitix.R;

public class SellTicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_ticket);
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

    public void postTicket(View view){

        Bundle bundle = new Bundle();

        EditText dateEditText = findViewById(R.id.et_sell_ticket_date);
        EditText eventEditText = findViewById(R.id.et_sell_ticket_event);
        EditText teamsEditText = findViewById(R.id.et_sell_ticket_teams);
        EditText priceEditText = findViewById(R.id.et_sell_ticket_price);

        bundle.putString("date", dateEditText.getText().toString());
        bundle.putString("event", eventEditText.getText().toString());
        bundle.putString("homeTeam", teamsEditText.getText().toString().substring(0, teamsEditText.getText().toString().indexOf(",")));
        bundle.putString("awayTeam", teamsEditText.getText().toString().substring(teamsEditText.getText().toString().indexOf(",") + 2));
        bundle.putString("price", priceEditText.getText().toString());
        bundle.putBoolean("available", true);

        Intent intent = new Intent(getApplicationContext(), TicketPostedActivity.class);

        intent.putExtras(bundle);

        startActivity(intent);
        finish();
    }
}
