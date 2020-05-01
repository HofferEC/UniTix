package us.wi.hofferec.unitix.data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import us.wi.hofferec.unitix.activities.LoginActivity;
import us.wi.hofferec.unitix.activities.MainActivity;
import us.wi.hofferec.unitix.activities.PDFViewerActivity;
import us.wi.hofferec.unitix.helpers.Notifications;
import us.wi.hofferec.unitix.interfaces.CurrencyInterface;

/**
 * This class is used to obtain and use different instances for this user by abstracting away a lot
 * of the confusing firebase logic.
 */
public class Utility {

    // Collection that stores our user information
    private static final String USERS_COLLECTION = "users";

    // Collection that stores our ticket information
    private static final String TICKETS_COLLECTION = "tickets";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    private static Map<String, Double> currencies;

    /**
     * Updates the current authenticated user in the users database using the current user
     * object variables.
     *
     * @param TAG class associated with the update
     */
    public static void updateUserDatabase(final String TAG) {

        // Database context
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Rewrite the user back to the database
        database.collection(USERS_COLLECTION).document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(LoginActivity.user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Successfully updated database information for user: " + LoginActivity.user.getEmail());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating database information for user: " + LoginActivity.user.getEmail(), e);
                    }
                });
    }

    /**
     * Updates a ticket in the tickets database.
     *
     * @param TAG class associated with the update
     * @param ticket ticket to be updated
     */
    public static void updateTicketOnDatabase(final String TAG, final Ticket ticket) {

        // Database context
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Rewrite the ticket back to the database
        database.collection(TICKETS_COLLECTION).document(ticket.getUid())
                .set(ticket)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Successfully updated database information for ticket: " + ticket.getUid());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating database information for ticket: " + ticket.getUid(), e);
                    }
                });
    }

    /**
     * Adds the ticket to the tickets database as well as associates the ticket with the current
     * authenticated user.
     *
     * @param TAG class associated with the update
     * @param ticket ticket to be added
     */
    public static void addTicketToDatabaseAndUser(final String TAG, final Ticket ticket, final String type) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        if (type.equals("selling")) {
            database.collection(TICKETS_COLLECTION)
                    .add(ticket)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.i(TAG, "Successfully added ticket " + documentReference.getId() + " to database");

                            // Assign UID to ticket
                            ticket.setUid(documentReference.getId());

                            // Update ticket on database with UID
                            updateTicketOnDatabase("Factory", ticket);

                            // Add the ticket to the current users profile (Selling)
                            LoginActivity.user.addTicketToSelling(documentReference);

                            // Call the method to update the user data on the database
                            Utility.updateUserDatabase("TicketPostedActivity");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Error adding ticket to database", e);
                        }
                    });
        }
        else if (type.equals("buying")) {
            LoginActivity.user.addTicketToBuying(database.collection(TICKETS_COLLECTION).document(ticket.getUid()));

            Utility.updateUserDatabase("TicketPostedActivity");
        }

    }

    /**
     * Updates the currency rates for the day
     */
    public static void updateCurrencyRates(){

        // Create instance of REST API with Retrofit
        Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(CurrencyInterface.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        CurrencyInterface currencyInterface = retrofit.create(CurrencyInterface.class);

        // We have to get currency rates converted from EUR because we are using the free version
        Call<String> call = currencyInterface.getCurrencyRatesFromEUR();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    Log.i("Utility/updateCurrency", "Successfully received currency response from " + CurrencyInterface.BASE_URL);
                    String jsonResponse = response.body();

                    writeJsonToCurrency(jsonResponse);
                }
                else {
                    Log.i("Utility/updateCurrency", "Returned empty response");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Utility/updateCurrency", t.getMessage());
            }
        });
    }

    /**
     * Takes the json that contains the currencies and pulls currencies out into data structure
     *
     * @param json json that contains currencies
     */
    private static void writeJsonToCurrency(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);

            // Data structure to hold all our currencies
            currencies = new HashMap<>();

            JSONObject allCurrencies = jsonObject.getJSONObject("rates");

            currencies.put("EUR", 1/Double.parseDouble(allCurrencies.get("USD").toString()));
            Log.i("Utility/readCurrency", "Successfully read EUR exchange rate");

            currencies.put("GBP", Double.parseDouble(allCurrencies.get("GBP").toString()));
            Log.i("Utility/readCurrency", "Successfully read GBP exchange rate");
        }
        catch (JSONException e) {
            Log.e("Utility/writeCurrency", "Error pulling currencies out of json");
        }
    }

    /**
     * Converts startingAmount in USD into the endingCurrency using the most recent exchange rates.
     *
     * @param startingAmount amount of money to be converted
     * @param endingCurrency the currency to be converted to
     * @return the new currency after conversion
     */
    public static String convert(double startingAmount, String endingCurrency) {

        double currencyConverted = 0;

        switch (endingCurrency) {
            case "EUR":
                currencyConverted = startingAmount * currencies.get("EUR");
                break;
            case "GBP":
                currencyConverted = startingAmount * currencies.get("EUR") * currencies.get("GBP");
                break;
        }

        DecimalFormat df = new DecimalFormat("#.##");

        return df.format(currencyConverted);
    }

    /**
     * Downloads a given ticket to the user device
     *
     * @param context the context of the calling activity
     * @param TAG class associated with the update
     * @param TAG ticketFilepath the ticket to download
     */
    public static void downloadTicket(final Context context, final String TAG, final String ticketFilepath) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ticketReference = storage.getReference(ticketFilepath);
        Log.i(TAG, "Downloading ticket: " + ticketFilepath);

        File downloadLocation;
        Log.i(TAG, "Ticket details: " + ticketReference.getBucket() + " " + ticketReference.getName() + " " + ticketReference.getPath());

        downloadLocation = new File(context.getExternalFilesDir(null), ticketReference.getName() + ".pdf");
        Log.i(TAG, "Download location is: " + downloadLocation.getAbsolutePath());

        ticketReference.getFile(downloadLocation)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.i(TAG, "Ticket download complete!");
                        Toast.makeText(context, "Downloaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Failed to download: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to download ticket: " + ticketFilepath);
                    Log.e(TAG, e.toString());
                    Log.e(TAG, this.toString());
                  }
                });
    }

    public static void openTicket(Context context, String TAG, String ticketFilepath) {
        String ticketFileUUID = ticketFilepath.replace("/tickets/","");
        Log.i(TAG, "Opening ticket :" + ticketFilepath);
        File f = new File(context.getExternalFilesDir(null), ticketFileUUID + ".pdf");
        if (!f.exists()){
            Log.i(TAG, "File does not exist! Downloading now.");
            Toast.makeText(context, "Downloading ticket data\nfrom server.", Toast.LENGTH_SHORT).show();
            downloadTicket(context, TAG, ticketFilepath);
            return;
        }

        Log.i(TAG, "TICKET exists :" + ticketFileUUID);
        Intent target = new Intent(context, PDFViewerActivity.class);
        target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        target.putExtra("filepath", f.getPath());
        context.startActivity(target);
    }

    /**
     * Updates the local user based on any changes made in firebase
     *
     * @param TAG tag
     */
    public static void updateUser(final String TAG) {

        // Database context
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database.collection("users").document(userUID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {

                            // Map the data from the document to the user object
                            LoginActivity.user = document.toObject(User.class);

                            Log.i(TAG, "Retrieved data for user: " + userUID + ": " + document.getData());
                        } else {
                            Log.w(TAG, "Unable to find document for user: " + userUID + ", creating document now");
                        }

                    } else {
                        Log.e(TAG, "accessing database failed with ", task.getException());
                    }
                });
    }

}
