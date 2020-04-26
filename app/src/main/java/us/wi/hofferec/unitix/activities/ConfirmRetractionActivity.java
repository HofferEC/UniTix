package us.wi.hofferec.unitix.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.internal.Util;
import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.data.Ticket;
import us.wi.hofferec.unitix.data.Utility;
import us.wi.hofferec.unitix.databinding.ConfirmPurchaseBinding;
import us.wi.hofferec.unitix.helpers.PaymentsUtil;

public class ConfirmRetractionActivity extends AppCompatActivity {

    Ticket ticket;

    /**
     * Initialize the Google Pay API on creation of the activity
     *
     * @see Activity#onCreate(Bundle)
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        ticket = (Ticket) intent.getSerializableExtra("ticket");

        setContentView(R.layout.activity_confirm_retraction);

        TextView ticketInfo = (TextView) findViewById(R.id.ticketInfoTextView);

        ticketInfo.append("Event: " + ticket.getEvent() + "\n\n");
        ticketInfo.append("Teams: " + ticket.getAwayTeam() + " @ " + ticket.getHomeTeam() + "\n\n");
        ticketInfo.append("Date: " + ticket.getDate() + "\n\n");

        if (LoginActivity.user.getSettings().get("currency").equals("USD")){
            ticketInfo.append("$" + ticket.getPrice());
        }
        else if (LoginActivity.user.getSettings().get("currency").equals("EUR")) {
            ticketInfo.append("€" + Utility.convert(Double.parseDouble(ticket.getPrice()), "EUR"));
        }
        else if (LoginActivity.user.getSettings().get("currency").equals("GBP")) {
            ticketInfo.append("£" + Utility.convert(Double.parseDouble(ticket.getPrice()), "GBP"));
        }

    }

    public void retractTicket(View view){
        ticket.setAvailable(false);
        ticket.setRetracted(true);
        Utility.updateTicketOnDatabase("ProfileTicketsSellingAdapter", ticket);
        Toast.makeText(this, "Ticket retracted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), ProfileSellingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void goToHome(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void cancel(View view){
        finish();
    }
}
