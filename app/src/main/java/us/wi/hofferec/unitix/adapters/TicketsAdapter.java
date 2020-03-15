package us.wi.hofferec.unitix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.data.Ticket;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.ViewHolder> {

    /**
     * Provides a direct reference to each of the views within a data item and caches the views
     * within the item layout for fast access.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

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
        public ViewHolder(View itemView) {
            super(itemView);
            eventTextView = itemView.findViewById(R.id.tv_details_event);
            infoTextView = itemView.findViewById(R.id.tv_details_info);
            dateTextView = itemView.findViewById(R.id.tv_details_date);
            priceTextView = itemView.findViewById(R.id.tv_details_price);
            buyButton = itemView.findViewById(R.id.buy_button);
        }
    }

    // Store a member variable for the tickets
    private List<Ticket> mTickets;

    public TicketsAdapter(List<Ticket> tickets) {
        mTickets = tickets;
    }

    /**
     * Inflate a layout from XML and return the holder.
     *
     * @param parent parent view
     * @param viewType type of view
     * @return the holder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View ticketView = inflater.inflate(R.layout.item_ticket, parent, false);

        // Return the new holder instance
        return new ViewHolder(ticketView);
    }

    /**
     *  Populate data into the item through holder
     *
     * @param holder current holder
     * @param position position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        Ticket ticket = mTickets.get(position);

        // Set item views based on our views and data model
        TextView eventTextView;
        TextView infoTextView;
        TextView dateTextView;
        TextView priceTextView;
        Button button;

        eventTextView = holder.eventTextView;
        eventTextView.setText(ticket.getEvent());

        infoTextView = holder.infoTextView;
        StringBuilder info = new StringBuilder(ticket.getAwayTeam() + " @ " + ticket.getHomeTeam());
        infoTextView.setText(info);

        dateTextView = holder.dateTextView;
        dateTextView.setText(ticket.getDate());

        priceTextView = holder.priceTextView;
        StringBuilder price = new StringBuilder("$" + ticket.getPrice());
        priceTextView.setText(price);

        button = holder.buyButton;
        button.setText(ticket.isAvailable() ? "Buy" : "Unavailable");
        button.setEnabled(ticket.isAvailable());
    }

    /**
     * Returns the total count of items in the list.
     *
     * @return the total count of items in the list
     */
    @Override
    public int getItemCount() {
        return mTickets.size();
    }
}
