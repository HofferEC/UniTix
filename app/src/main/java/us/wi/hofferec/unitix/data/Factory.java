package us.wi.hofferec.unitix.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import us.wi.hofferec.unitix.activities.LoginActivity;

/**
 * This class is used to obtain and use different instances for this user
 */
public class Factory {

    // Represents the current user logged in
    private static User user;

    // Collection that stores our user information
    private static final String USERS_COLLECTION = "users";

    // Collection that stores our ticket information
    private static final String TICKETS_COLLECTION = "tickets";

    /**
     * Used to update the current authenticated user in the users database using the current user
     * object variables.
     *
     * @param TAG class associated with the update
     */
    public static void updateUserDatabase(final String TAG) {

        // Database context
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Rewrite the user back to the database
        database.collection(USERS_COLLECTION).document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully updated database information for user: " + user.getEmail());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating database information for user: " + user.getEmail(), e);
                    }
                });
    }

    /**
     * Used to update a ticket in the tickets database.
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

                        // Add UID to ticket and update on database again TODO how do we only do this once?
                        ticket.setUid(documentReference.getId());
                        updateTicketOnDatabase("Factory", ticket);

                        // Add the ticket to the current users profile
                        user.addTicket(documentReference);

                        // Call the method to update the user data on the database
                        Factory.updateUserDatabase("TicketPostedActivity");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding ticket " + ticket.getUid() + " to database", e);
                    }
                });
    }

    /**
     * Retrieves an updated copy of the users information. If this is the first time we are
     * retrieving a users information, it will create the user in the database.
     */
    public static void getUserInformation(){

        // Database context
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database.collection(USERS_COLLECTION).document(userUID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {

                        // Map the data from the document to the user object
                        user = document.toObject(User.class);

                        Log.d("LoginActivity", "Retrieved data for user: " + userUID + ": " + document.getData());
                    }
                    else {
                        Log.d("LoginActivity", "Unable to find document for user: " + userUID + ", creating document now");

                        // Get the current authenticated users email
                        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                        // Create document for this user
                        user = new User(email);

                        final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        database.collection(USERS_COLLECTION).document(userUID).set(user);
                    }


                    // Apply custom user settings
                    if(user.getSettings() != null)
                        applyCustomUserSettings();

                } else {
                    Log.d("LoginActivity", "accessing database failed with ", task.getException());
                }
            }
        });
    }

    private static void applyCustomUserSettings(){

        // Apply custom user settings
        if (user.getSettings().get("darkMode") != null) {
            if (((Boolean) user.getSettings().get("darkMode")))
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /**
     * Getter for user.
     *
     * @return current user logged in
     */
    public static User getUser(){
        if (user == null) {
            setUser(new User());
        }
        return user;
    }

    /**
     * Set the current user logged in
     *
     * @param user user currently logged in
     */
    public static void setUser(User user) {
        Factory.user = user;
    }
}
