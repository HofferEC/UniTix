package us.wi.hofferec.unitix.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.adapters.ProfileTicketsBuyingAdapter;
import us.wi.hofferec.unitix.data.Ticket;

public class ProfileBalanceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProfileTicketsBuyingAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_balance);

        checkPerms("ProfileBuyingActivity");
        setupRecyclerView();
    }

    // Initial setup for recyclerview.
    private void setupRecyclerView() {

        // Get the current user
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get the recyclerView
        recyclerView = findViewById(R.id.rv_profile_buying);

        // Don't allow recyclerView to resize
        recyclerView.setHasFixedSize(true);

        // Set Layout type
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Add dividers between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Save the data
        final List<Ticket> data = new ArrayList<>();

        final Context context = getApplicationContext();

        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            database.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<DocumentReference> list = (List<DocumentReference>) document.get("ticketsBuying");
                            List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                            if (list != null) {
                                for (DocumentReference documentReference : list) {
                                    Task<DocumentSnapshot> documentSnapshotTask = documentReference.get();
                                    tasks.add(documentSnapshotTask);
                                }
                            }

                            // Only move to the next step when all data is loaded
                            Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                                @Override
                                public void onSuccess(List<Object> list) {

                                    // This list now contains all the tickets as snapshots
                                    // Convert each snapshot to its object type
                                    for (Object object : list) {
                                        Ticket ticket = ((DocumentSnapshot) object).toObject(Ticket.class);
                                        data.add(ticket);
                                    }
                                    Log.i("ProfileBuyingActivity", "Successfully loaded " + list.size() + " ticket for user: " + firebaseUser.getUid());

                                    adapter = new ProfileTicketsBuyingAdapter(data, context);
                                    recyclerView.setAdapter(adapter);

                                }
                            });
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.i("ProfileBuyingActivity","Permission: "+permissions[0]+ " was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    private void checkPerms(String TAG) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG,"Write permission is granted");
            } else {

                Log.e(TAG,"Write permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.i(TAG,"Write permission is granted automatically due to SDK");
        }
    }

    public void openTicket(String ticket){

    }

    public void goBack(View view){
        finish();
    }
}
