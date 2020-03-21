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

import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.data.User;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
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

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords are not the same");
            confirmPasswordEditText.requestFocus();
        } if (email.isEmpty()) {
            emailEditText.setError("Please enter an email");
            emailEditText.requestFocus();
        } else if (password.isEmpty()) {
            passwordEditText.setError("Please enter a password");
            passwordEditText.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("SignUpActivity.signUp()", "Unable create user given exception: " + task.getException());
                            } else {

                                // Add new user to users database
                                addUserToDatabase();
                            }
                        }
                    });
        }
    }

    /**
     * Retrieve the user information from the database before going to the main screen.
     */
    public void addUserToDatabase() {

        // Database context
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database.collection("users").document(userUID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            Log.d("SignUpActivity", "Creating document for user: " + userUID);

                            // Get the current authenticated users email
                            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                            // Settings template
                            HashMap<String, Object> settings = new HashMap<>();
                            settings.put("darkMode", false);
                            settings.put("notifications", false);
                            settings.put("currency", "USD");

                            // Create document for this user
                            LoginActivity.user = new User(email, settings);

                            final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            database.collection("users").document(userUID).set(LoginActivity.user);

                            // add user to SharedPreferences to persist login
                            sharedPreferences.edit().putString("USER", userUID).apply();

                            // Go to home screen, since all the information is loaded
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                        else {
                            Log.d("LoginActivity", "accessing database failed with ", task.getException());
                        }
                    }
                });
    }
}
