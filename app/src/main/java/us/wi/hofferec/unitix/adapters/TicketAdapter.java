package us.wi.hofferec.unitix.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.activities.ConfirmPurchaseActivity;
import us.wi.hofferec.unitix.data.Ticket;

public class TicketAdapter extends FirestoreRecyclerAdapter<Ticket, TicketAdapter.TicketHolder> {

    /**
     * Provides a direct reference to each of the views within a data item and caches the views
     * within the item layout for fast access.
     */
    class TicketHolder extends RecyclerView.ViewHolder {

        public TextView eventTextView;
        public TextView infoTextView;
        public TextView dateTextView;
        public TextView priceTextView;
        public Button buyButton;

        /**
         * Constructor that accepts the entire item row and does the view lookups to find each
         * subview.
         *
         * @param itemView item row
         */
        public TicketHolder(View itemView) {
            super(itemView);
            eventTextView = itemView.findViewById(R.id.tv_details_event);
            infoTextView = itemView.findViewById(R.id.tv_details_info);
            dateTextView = itemView.findViewById(R.id.tv_details_date);
            priceTextView = itemView.findViewById(R.id.tv_details_price);
            buyButton = itemView.findViewById(R.id.buy_button);
        }
    }

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options adapter settings
     */
    public TicketAdapter(FirestoreRecyclerOptions<Ticket> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(TicketHolder ticketHolder, int position, final Ticket ticket) {

        ticketHolder.eventTextView.setText(ticket.getEvent());

        StringBuilder info = new StringBuilder(ticket.getAwayTeam() + " @ " + ticket.getHomeTeam());
        ticketHolder.infoTextView.setText(info);

        ticketHolder.dateTextView.setText(ticket.getDate());

        StringBuilder price = new StringBuilder("$" + ticket.getPrice());
        ticketHolder.priceTextView.setText(price);

        ticketHolder.buyButton.setText(ticket.isAvailable() ? "Buy" : "Unavailable");
        ticketHolder.buyButton.setEnabled(ticket.isAvailable());

        ticketHolder.buyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ConfirmPurchaseActivity.class);
                intent.putExtra("ticket", ticket);
                view.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketHolder(view);
    }

}
