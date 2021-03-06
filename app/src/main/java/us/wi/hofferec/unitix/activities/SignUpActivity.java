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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.data.User;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText usernameEditText;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final String COLLECTION = "users";
    private CollectionReference collectionReference;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Get sharedPreferences
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        emailEditText = findViewById(R.id.et_signup_email);
        passwordEditText = findViewById(R.id.et_signup_password);
        confirmPasswordEditText = findViewById(R.id.et_signup_confirm_password);
        usernameEditText = findViewById(R.id.et_signup_name);

        mAuth = FirebaseAuth.getInstance();
    }

    public void cancel(View view){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void signUp(View view) {
        // TODO: Authenticate user AND add to users database
        //  just logs in for now
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String username = usernameEditText.getText().toString();

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords are not the same");
            confirmPasswordEditText.requestFocus();
        } if (email.isEmpty()) {
            emailEditText.setError("Please enter an email");
            emailEditText.requestFocus();
        } else if (password.isEmpty()) {
            passwordEditText.setError("Please enter a password");
            passwordEditText.requestFocus();
        } else if (username.length() < 3 || username.length() > 16) {
            usernameEditText.setError("Username must have [3-16] characters");
            usernameEditText.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("SignUpActivity.signUp()", "Unable create user given exception: " + task.getException());
                            } else {

                                // Add new user to users database
                                addUserToDatabase(username);
                            }
                        }
                    });
        }
    }

    /**
     * Retrieve the user information from the database before going to the main screen.
     */
    public void addUserToDatabase(String username) {

        // Database context
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database.collection("users").document(userUID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            Log.i("SignUpActivity", "Creating document for user: " + userUID);

                            // Get the current authenticated users email
                            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                            // Settings template
                            HashMap<String, Object> settings = new HashMap<>();
                            settings.put("darkMode", false);
                            settings.put("notifications", false);
                            settings.put("currency", "USD");

                            // Create document for this user
                            LoginActivity.user = new User(email, settings, username);

                            final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            database.collection("users").document(userUID).set(LoginActivity.user);

                            // Apply custom user settings
                            if(LoginActivity.user.getSettings() != null)
                                applyCustomUserSettings();

                            // Go to home screen, since all the information is loaded
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            // This will completely clear the activity stack so when the user clicks back, it will not go back to the login
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else {
                            Log.d("LoginActivity", "accessing database failed with ", task.getException());
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
        if (LoginActivity.user.getSettings().get("darkMode") != null) {
            if (((Boolean) LoginActivity.user.getSettings().get("darkMode"))) {
                sharedPreferences.edit().putBoolean("darkModeEnabled", true).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            else {
                sharedPreferences.edit().putBoolean("darkModeEnabled", false).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }

        // Apply notifications settings
        if (LoginActivity.user.getSettings().get("notifications") != null) {
            if ((Boolean) LoginActivity.user.getSettings().get("notifications")) {
                sharedPreferences.edit().putBoolean("notificationsKey", true).apply();
            }
            else {
                sharedPreferences.edit().putBoolean("notificationsKey", false).apply();
            }
        }

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
    }
}
