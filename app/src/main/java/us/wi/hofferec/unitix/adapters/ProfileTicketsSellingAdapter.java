package us.wi.hofferec.unitix.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.data.Ticket;

public class ProfileTicketsSellingAdapter extends RecyclerView.Adapter<ProfileTicketsSellingAdapter.TicketHolder> {

    private List<Ticket> tickets;

    /**
     * Provides a direct reference to each of the views within a data item and caches the views
     * within the item layout for fast access.
     */
    class TicketHolder extends RecyclerView.ViewHolder {

        public TextView eventTextView;
        public TextView infoTextView;
        public TextView dateTextView;
        public TextView priceTextView;
        public TextView status;

        /**
         * Constructor that accepts the entire item row and does the view lookups to find each
         * subview.
         *
         * @param itemView item row
         */
        public TicketHolder(View itemView) {
            super(itemView);
            eventTextView = itemView.findViewById(R.id.tv_details_profile_event);
            infoTextView = itemView.findViewById(R.id.tv_details_profile_info);
            dateTextView = itemView.findViewById(R.id.tv_details_profile_date);
            priceTextView = itemView.findViewById(R.id.tv_details_profile_price);
            status = itemView.findViewById(R.id.tv_details_profile_status);
        }
    }

    public ProfileTicketsSellingAdapter(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @NonNull
    @Override
    public ProfileTicketsSellingAdapter.TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_tickets_selling, parent, false);
        return new ProfileTicketsSellingAdapter.TicketHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TicketHolder ticketHolder, int position) {

        ticketHolder.eventTextView.setText(tickets.get(position).getEvent());

        StringBuilder info = new StringBuilder(tickets.get(position).getAwayTeam() + " @ " + tickets.get(position).getHomeTeam());
        ticketHolder.infoTextView.setText(info);

        ticketHolder.dateTextView.setText(tickets.get(position).getDate());

        StringBuilder price = new StringBuilder("$" + tickets.get(position).getPrice());
        ticketHolder.priceTextView.setText(price);

        ticketHolder.status.setText(tickets.get(position).isAvailable() ? "Selling" : "Sold");
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }
}