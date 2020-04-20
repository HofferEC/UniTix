package us.wi.hofferec.unitix.activities;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.adapters.ProfileTicketsSellingAdapter;
import us.wi.hofferec.unitix.data.Ticket;

public class ProfileBuyingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProfileTicketsSellingAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_buying);

        setupRecyclerView();
    }

    // Initial setup for recyclerview.
    private void setupRecyclerView() {

        // Get the current user
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Save the data
        final List<Ticket> data = new ArrayList<>();

        recyclerView = findViewById(R.id.rv_profile_buying);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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
                            for (DocumentReference documentReference : list) {
                                Task<DocumentSnapshot> documentSnapshotTask = documentReference.get();
                                tasks.add(documentSnapshotTask);
                            }
                            Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                                @Override
                                public void onSuccess(List<Object> list) {

                                    // This list now contains all the tickets as references
                                    for (Object object : list) {
                                        Ticket ticket = ((DocumentSnapshot) object).toObject(Ticket.class);
                                        data.add(ticket);
                                    }
                                    Log.i("ProfileBuyingActivity", "Successfully loaded " + list.size() + " ticket for user: " + firebaseUser.getUid());

                                    adapter = new ProfileTicketsSellingAdapter(data);
                                    recyclerView.setAdapter(adapter);

                                }
                            });
                        }
                    }
                }
            });
        }
    }

    public void goBack(View view){
        finish();
    }
}
