package us.wi.hofferec.unitix.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import androidx.appcompat.app.AppCompatDelegate;
import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.data.Utility;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView emailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /*
        We need to add this if() because for some reason this onCreate method is called after
         executing logout(). In logout(), we are clearing the activity stack and starting fresh with
         LoginActivity, so I don't see how this onCreate method is being called. If you
         */
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            emailTV = findViewById(R.id.tv_profile_email);
            emailTV.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            profileImage = findViewById(R.id.iv_profile_profile);
            TextView tv_username = (findViewById(R.id.tv_profile_username));
            tv_username.setText(LoginActivity.user.getUsername());

            // If user clicks profile image, they can change it
            profileImage.setOnClickListener(view -> {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                int PICK_IMAGE = 1;
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            });

            // Apply the users profile picture
            setProfileImage();
        }
    }

    /**
     * Go to Main activity.
     *
     * @param view current view
     */
    public void goToHome(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * Logout the current user and go to Login activity.
     *
     * @param view current view
     */
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        // This will completely clear activity stack and start from scratch at Login activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Go to ProfileBalacne activity.
     *
     * @param view current view
     */
    public void openBalance(View view){
        Intent intent = new Intent(getApplicationContext(), ProfileBalanceActivity.class);
        startActivity(intent);
    }

    /**
     * Go to ProfileSelling activity.
     *
     * @param view current view
     */
    public void openSelling(View view){
        Intent intent = new Intent(getApplicationContext(), ProfileSellingActivity.class);
        startActivity(intent);
    }

    /**
     * Go to ProfileBuying activity.
     *
     * @param view current view
     */
    public void openBuying(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfileBuyingActivity.class);
        startActivity(intent);
    }

    /**
     * Go to Settings activity.
     *
     * @param view current view
     */
    public void openSettingsFragment(View view) {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Sets the users profile picture
     */
    public void setProfileImage(){
        if (LoginActivity.user.getProfileImageUri() != null) {
            // ImageView in your Activity
            ImageView profileImage = findViewById(R.id.iv_profile_profile);

            // Download directly from StorageReference using Glide
            // (See MyAppGlideModule for Loader registration)
            Glide.with(this).load(LoginActivity.user.getProfileImageUri()).apply(RequestOptions.circleCropTransform()).into(profileImage);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if(resultCode == Activity.RESULT_OK) {
            assert imageReturnedIntent != null;
            uploadImageToDatabase("ProfileActivity (Upload Profile Image)", imageReturnedIntent);
        }
    }

    /**
     * Uploads an image to the Profile Images cloud storage.
     *
     * @param imageReturnedIntent intent of the image being saved
     */
    public void uploadImageToDatabase(final String TAG, Intent imageReturnedIntent) {

        final FirebaseStorage storage = FirebaseStorage.getInstance();

        // Uri to the image
        Uri selectedImage = imageReturnedIntent.getData();

        final UUID imageUUID = UUID.randomUUID();

        final StorageReference storageReference = storage.getReference().child("Profile Images/" + imageUUID);

        final UploadTask uploadTask = storageReference.putFile(selectedImage);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "Error adding image: " + imageUUID + " to cloud storage");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i(TAG, "Successfully added image: " + imageUUID + " to cloud storage");

                storage.getReference().child("Profile Images/" + imageUUID).getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            LoginActivity.user.setProfileImageUri(uri.toString());
                            Utility.updateUserDatabase(TAG);

                            // Restart this activity again with the updated picture
                            finish();
                            startActivity(getIntent());
                        })
                        .addOnFailureListener(exception -> {
                            Log.e(TAG, "Error retrieving uri for image: " + imageUUID + " in cloud storage, " + exception.getMessage());
                        });
            }
        });
    }
}
