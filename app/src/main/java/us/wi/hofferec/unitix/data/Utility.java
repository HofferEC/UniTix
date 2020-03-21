package us.wi.hofferec.unitix.data;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

import androidx.annotation.NonNull;
import us.wi.hofferec.unitix.activities.LoginActivity;

/**
 * This class is used to obtain and use different instances for this user by abstracting away a lot
 * of the confusing firebase logic.
 */
public class Utility {

    // Collection that stores our user information
    private static final String USERS_COLLECTION = "users";

    // Collection that stores our ticket information
    private static final String TICKETS_COLLECTION = "tickets";

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
                        Log.d(TAG, "Successfully updated database information for user: " + LoginActivity.user.getEmail());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating database information for user: " + LoginActivity.user.getEmail(), e);
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
                        Log.d(TAG, "Successfully updated database information for ticket: " + ticket.getUid());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating database information for ticket: " + ticket.getUid(), e);
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
    public static void addTicketToDatabaseAndUser(final String TAG, final Ticket ticket) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection(TICKETS_COLLECTION)
                .add(ticket)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Successfully added ticket " + ticket.getUid() + " to database");

                        // Add UID to ticket and update on database again
                        updateTicketOnDatabase("Factory", ticket);

                        // Add the ticket to the current users profile
                        LoginActivity.user.addTicket(documentReference);

                        // Call the method to update the user data on the database
                        Utility.updateUserDatabase("TicketPostedActivity");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding ticket " + ticket.getUid() + " to database", e);
                    }
                });
    }
}
