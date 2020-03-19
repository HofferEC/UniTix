package us.wi.hofferec.unitix.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.data.User;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static User user;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    private String COLLECTION = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.et_signup_email);
        passwordEditText = findViewById(R.id.et_signup_password);

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFireBaseUser = mAuth.getCurrentUser();
                if (mFireBaseUser != null) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("email", mFireBaseUser.getEmail());
                    startActivity(intent);
                    finish();
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
                            } else {
                                // User has been validated, so we can retrieve their information
                                getUserInformation();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
        }
    }

    private void getUserInformation(){

        final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Set the location of where all users information is stored
        documentReference = database.collection(COLLECTION).document(userUID);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {

                        // Map the data from the document to the user object
                        user = document.toObject(User.class);

                        Log.d("LoginActivity", "Retrieved data for user, " + userUID + ": " + document.getData());
                    }
                    else {
                        Log.d("LoginActivity", "Unable to find document for user: " + userUID + ", creating document now");

                        // Create document for this user
                        user = new User(emailEditText.getText().toString());

                        final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        database.collection(COLLECTION).document(userUID).set(user);
                    }
                } else {
                    Log.d("LoginActivity", "accessing database failed with ", task.getException());
                }
            }
        });
    }
}
