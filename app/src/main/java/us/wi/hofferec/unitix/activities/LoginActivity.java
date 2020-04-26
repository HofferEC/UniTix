package us.wi.hofferec.unitix.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.data.User;
import us.wi.hofferec.unitix.data.Utility;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText passwordEditText;
    private EditText emailEditText;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static User user;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get sharedPreferences
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        // Need to update the currency rates at least daily
        Utility.updateCurrencyRates();

        // Login with uid if user did not logout last time
        String storedUid = sharedPreferences.getString("USER", null);
        if (storedUid != null)
            getUserFromDatabaseWithUid(storedUid);
        else {
            user = new User();
            passwordEditText = findViewById(R.id.et_signup_password);
            emailEditText = findViewById(R.id.et_signup_email);
        }

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFireBaseUser = mAuth.getCurrentUser();
                // This means the user has been authorized and can login
                if (mFireBaseUser != null) {
                    getUserFromDatabase();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    public void openSignUp(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Authenticate user and retrieve user information.
     *
     * @param view current view
     */
    public void login(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty()) {
            emailEditText.setError("Please enter an email");
            emailEditText.requestFocus();
        } else if (password.isEmpty()) {
            passwordEditText.setError("Please enter a password");
            passwordEditText.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                getUserFromDatabase();
                            }
                        }
                    });
        }
    }

    /**
     * Retrieve the user information from the database before going to the main screen.
     */
    public void getUserFromDatabase() {

        // Database context
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database.collection("users").document(userUID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {

                                // Map the data from the document to the user object
                                user = document.toObject(User.class);

                                Log.i("LoginActivity", "Retrieved data for user: " + userUID + ": " + document.getData());
                            }
                            else {
                                Log.w("LoginActivity", "Unable to find document for user: " + userUID + ", creating document now");

                                // Get the current authenticated users email and name
                                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                                // Settings template
                                HashMap<String, Object> settings = new HashMap<>();
                                settings.put("darkMode", false);
                                settings.put("notifications", false);
                                settings.put("currency", "USD");

                                // Create document for this user
                                user = new User(email, settings, username);

                                final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                database.collection("users").document(userUID).set(user);
                            }

                            // Apply custom user settings
                            if(user.getSettings() != null)
                                applyCustomUserSettings();

                            // add user to SharedPreferences to persist login
                            sharedPreferences.edit().putString("USER", userUID).apply();

                            // Go to home screen, since all the information is loaded
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        } else {
                            Log.e("LoginActivity", "accessing database failed with ", task.getException());
                        }
                    }
                });
    }

    /**
     * Retrieve the user information from the database before going to the main screen.
     */
    public void getUserFromDatabaseWithUid(String uid) {

        // Database context
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        final String userUID = uid;

        database.collection("users").document(userUID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {

                                // Map the data from the document to the user object
                                user = document.toObject(User.class);

                                Log.i("LoginActivity", "Retrieved data for user: " + userUID + ": " + document.getData());
                            }
                            else {
                                Log.w("LoginActivity", "Unable to find document for user: " + userUID + ", creating document now");

                                // Get the current authenticated users email and name
                                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                                // Settings template
                                HashMap<String, Object> settings = new HashMap<>();
                                settings.put("darkMode", false);
                                settings.put("notifications", false);
                                settings.put("currency", "USD");

                                // Create document for this user
                                user = new User(email, settings, username);

                                final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                database.collection("users").document(userUID).set(user);
                            }

                            // Apply custom user settings
                            if(user.getSettings() != null)
                                applyCustomUserSettings();

                            // Go to home screen, since all the information is loaded
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            Log.e("LoginActivity", "accessing database failed with ", task.getException());
                        }
                    }
                });
    }

    /**
     * Helper method used to set user specific settings
     */
    private void applyCustomUserSettings(){

        // Have to switch shared preferences to the one that holds our settings
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Apply dark mode
        if (user.getSettings().get("darkMode") != null) {
            if (((Boolean) user.getSettings().get("darkMode"))) {
                sharedPreferences.edit().putBoolean("darkModeEnabled", true).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            else {
                sharedPreferences.edit().putBoolean("darkModeEnabled", false).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }

        // Apply notifications settings
        if (user.getSettings().get("notifications") != null) {
            if ((Boolean) user.getSettings().get("notifications")) {
                sharedPreferences.edit().putBoolean("notificationsKey", true).apply();
            }
            else {
                sharedPreferences.edit().putBoolean("notificationsKey", false).apply();
            }
        }

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
    }
}
